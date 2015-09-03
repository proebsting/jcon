//  wCanvas.java -- an Icon window canvas, including attributes

package jcon;

import java.awt.*;
import java.util.*;



public final class wCanvas extends Canvas {

    Frame f;			// enclosing Frame object
    Image i;			// backing image for refreshing visible image

    vList evq;			// event queue
    wTTY tty;			// file I/O stuff

    Vector<vWindow> wlist;	// list of associated vWindows

    int width, height;		// last known width and height
    boolean have_set_width;	// was width set explicitly?
    boolean have_set_height;	// was height set explicitly?

    long evtime;		// timestamp of last event
    int xloc, yloc;		// last known mouse position

    String visibility;		// value of "canvas" (visibility) attribute
    String pointer;		// value of "pointer" (mouse cursor) attribute

    vString imgfile;		// value of "image" attribute, if set
    boolean defer_image;	// defer image loading?
    Image deferred_image;	// deferred image

    private static final long serialVersionUID = 1;	// prevent warning



//  new wCanvas(win, label, w, h) -- create new canvas
//
//  (canvas is created but not made visible)

wCanvas(vWindow win, String label, int w, int h) {

    wlist = new Vector<vWindow>();
    wlist.addElement(win);

    this.setSize(width = w, height = h);

    f = new Frame(label);
    f.add(this, "Center");
    f.pack();
    f.setResizable(false);

    // make an Image most compatible with the screen for faster drawImage()
    // n.b. TRANSLUCENT is measurably faster than OPAQUE on Mac "Pierre".
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice device = env.getDefaultScreenDevice();
    GraphicsConfiguration config = device.getDefaultConfiguration();
    i = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

    evq = vList.New(0, null);			// create event queue
    tty = new wTTY();				// create TTY instance

    wEvent.register(this);			// register event handlers

    Pointer(win, "arrow");
    Label(win, label);
}



//  dispose() -- mark as closed

void dispose() {
    f.dispose();	// dispose frame
    i.flush();		// dispose backing image
    f = null;
    i = null;
}



//  config(win, multiplier, x, y, w, h) -- reconfigure geometry
//
//  arguments are *String* values a la WAttrib()
//  any particular argument can be null
//
//  returns failure (false) or success (true).

boolean config(vWindow win, int m, String x, String y, String w, String h) {
    Rectangle r = this.getBounds();

    try {
	if (x != null) { r.x = m * wAttrib.parseInt(x); }
	if (y != null) { r.y = m * wAttrib.parseInt(y); }
	if (w != null) { r.width = m * wAttrib.parseInt(w); }
	if (h != null) { r.height = m * wAttrib.parseInt(h); }
	// do the following only after we know both values parsed okay
	if (w != null) { have_set_width = true; }
	if (h != null) { have_set_height = true; }
    } catch (Exception e) {
	return false;
    }
    if (x != null || y != null) {
    	f.setLocation(r.x, r.y);
    }
    resize(win, r.width, r.height);
    return true;
}



//  defconfig -- set default configuration for window
//
//  If not explicitly set otherwise, the default window size is
//  12 rows x 80 columns.  This cannot be done until the font and
//  leading are known.

void defconfig(vWindow win) {
    if (have_set_width && have_set_height) {
	return;
    }
    Rectangle r = this.getBounds();
    if (! have_set_width) {
	r.width = 80 * win.Fwidth();
    }
    if (! have_set_height) {
	r.height = 12 * win.Leading();
    }
    resize(win, r.width, r.height);
}



//  resize(win, w, h) -- resize canvas
//
//  note that win can be null if called from unattributed event code

public void resize(vWindow win, int w, int h) {

    if (width != w || height != h) {
	boolean b = f.isResizable();
	f.setResizable(true);
	this.setSize(width = w, height = h);	// alter size
	f.pack();				// alter size of enclosing frame
	Toolkit.getDefaultToolkit().sync();	// sync
	Thread.yield();
	f.setResizable(b);
    }

    int iw = i.getWidth(null);
    int ih = i.getHeight(null);

    if (iw >= w && ih >= h) {
	return;					// image is large enough
    }

    if (iw < w) { iw = w; }
    if (ih < h) { ih = h; }

    Image inew = this.createImage(iw, ih);
    Graphics2D g = (Graphics2D) inew.getGraphics();

    // install new graphics context in each associated window
    for (int j = 0; j < wlist.size(); j++) {
	wlist.elementAt(j).newgcb(g);
    }

    // g has been cloned for use with each window
    // we can now use it without affecting anything else

    // clear the new image, then copy in the old portion
    if (win == null) {
	// we don't know which Icon graphics context to use, because the
	// window was resized via the mouse;  so, just use the first
	win = wlist.elementAt(0);	
    }
    g.setColor(win.getBg());
    g.fillRect(0, 0, iw, ih);
    g.drawImage(i, 0, 0, null);

    // out with the old, in with the new
    g.dispose();
    i.flush();
    i = inew;
}



//  paint(g) -- refresh the canvas from the backing image
//
//  g is a zero-origin graphics context with no clipping

public void paint(Graphics g) {
    g.drawImage(i,0,0,null);
}



//  interval(newtime) -- return interval in milliseconds since last event

public long interval(long newtime) {
    long elapsed;
    if (evtime == 0) {
	elapsed = 0;
    } else {
	elapsed = newtime - evtime;
    }
    evtime = newtime;
    return elapsed;
}



//  enqueue(a, b, c) -- enqueue three event values (synchronized)

public void enqueue(vValue a, vValue b, vValue c) {
    synchronized(evq) {
	evq.Put(a);
	evq.Put(b);
	evq.Put(c);
    }
}



//  Canvas(win, s) -- set "canvas" (visibility) attribute
//
//  Accepts only "hidden" and "normal", rejects "maximal" and "iconic"

public vString Canvas(vWindow win, String s) {
    if (s != null) {
	if (s.equals("hidden")) {
	    f.setVisible(false);
	} else if (s.equals("normal")) {
	    f.setVisible(true);
	} else {
	    return null; /*FAIL*/
	}
	visibility = s;
    }
    if (visibility == null) {
	return null;
    } else {
	return vString.New(visibility);
    }
}



//  Resize(win, s) -- set "resize" attribute

public vString Resize(vWindow win, String s) {
    if (s != null) {
	if (s.equals("off")) {
	    f.setResizable(false);
	} else if (s.equals("on")) {
	    f.setResizable(true);
	} else {
	    return null; /*FAIL*/
	}
    }
    return vString.New(f.isResizable() ? "on" : "off");
}



//  Label(win, s) -- set "label" attribute

public vString Label(vWindow win, String s) {
    if (s != null) {
	if (s.equals("")) {
	    f.setTitle(" ");
	} else {
	    f.setTitle(s);
	}
    }
    return vString.New(f.getTitle());
}



//  Pointer(win, s) -- set "pointer" attribute
//
//  indented values are provided for compatibility but are not preferred.

static Hashtable<String,vInteger> plist = new Hashtable<String,vInteger>();
static {
    plist.put("arrow",			vInteger.New(Cursor.DEFAULT_CURSOR));
	plist.put("left ptr",		vInteger.New(Cursor.DEFAULT_CURSOR));
    plist.put("cross",			vInteger.New(Cursor.CROSSHAIR_CURSOR));
	plist.put("crosshair",		vInteger.New(Cursor.CROSSHAIR_CURSOR));
    plist.put("hand",			vInteger.New(Cursor.HAND_CURSOR));
    plist.put("move",			vInteger.New(Cursor.MOVE_CURSOR));
	plist.put("fleur",		vInteger.New(Cursor.MOVE_CURSOR));
    plist.put("text",			vInteger.New(Cursor.TEXT_CURSOR));
	plist.put("ibeam",		vInteger.New(Cursor.TEXT_CURSOR));
	plist.put("xterm",		vInteger.New(Cursor.TEXT_CURSOR));
    plist.put("wait",			vInteger.New(Cursor.WAIT_CURSOR));
	plist.put("watch",		vInteger.New(Cursor.WAIT_CURSOR));
    plist.put("top side",		vInteger.New(Cursor.N_RESIZE_CURSOR));
    plist.put("top right corner",	vInteger.New(Cursor.NE_RESIZE_CURSOR));
    plist.put("right side",		vInteger.New(Cursor.E_RESIZE_CURSOR));
    plist.put("bottom right corner",	vInteger.New(Cursor.SE_RESIZE_CURSOR));
    plist.put("bottom side",		vInteger.New(Cursor.S_RESIZE_CURSOR));
    plist.put("bottom left corner",	vInteger.New(Cursor.SW_RESIZE_CURSOR));
    plist.put("left side",		vInteger.New(Cursor.W_RESIZE_CURSOR));
    plist.put("top left corner",	vInteger.New(Cursor.NW_RESIZE_CURSOR));
}

public vString Pointer(vWindow win, String s) {
    if (s != null) {
	vInteger i = plist.get(s);
	if (i == null) {
	    return null; /*FAIL*/
	}
	setCursor(Cursor.getPredefinedCursor((int)i.value));
	pointer = s;
    }
    return vString.New(pointer);
}



} // wCanvas

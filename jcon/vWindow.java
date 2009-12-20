//  vWindow.java -- graphics window Type
//
//  note that vWindow extends vFile to support read(), write(), etc.

package jcon;

import java.io.*;
import java.util.*;
import java.awt.*;



public final class vWindow extends vFile {

    // when a window has closed, a/b/c are null

    private wCanvas c;		// underlying Icon canvas
    private Graphics a, b;	// graphics context for canvas and backing image

    private int wnum;		// window serial number
    private int gnum;		// graphics context serial number

    // graphics context attributes
    // when adding new ones, be sure to update the cloning code below

    private boolean clipping;	// is clipping enabled?
    int dx, dy;			// graphics origin

    double gamma;		// gamma correction factor
    wColor bg;			// background color
    wColor fg;			// foreground color
    private boolean xormode;	// in "drawop=reverse" (XOR) mode?
    private vString revatt;	// value of "reverse=" attribute

    private wFont font;		// text font
    private int leading;	// leading value



// constant vstrings
private static vString vSoff = vString.New("off");
private static vString vSon = vString.New("on");
private static vString vScopy = vString.New("copy");
private static vString vSreverse = vString.New("reverse");


public boolean iswin() {
    if (c == null) {
	iRuntime.error(142, this);
    }
    return true;
}

public wCanvas getCanvas() {
    if (c == null) {
	iRuntime.error(142, this);
    }
    return c;
}
public wTTY getTTY()			{ return getCanvas().tty; }

public int getWidth()			{ return c.width; }
public int getHeight()			{ return c.height; }

public wColor getFg()			{ return fg; }
public wColor getBg()			{ return bg; }

public wFont getFont()			{ return font; }
public FontMetrics getFontMetrics()	{ return c.getFontMetrics(font); }



private static Toolkit toolkit = Toolkit.getDefaultToolkit();

private static int wcount = 0;	// count of windows allocated
private static int gcount = 0;	// count of graphics contexts allocated



private static vString typestring = vString.New("window");
public vString Type()	{ return typestring; }

public vString image()	{
    return vString.New("window_" + wnum + ":" + gnum + "(" + title() + ")");
}
String title()		{ return c == null ? "<closed>" : c.f.getTitle(); }

int rank()		{ return 50; }		// windows sort after csets
int compareTo(vValue v)	{ return title().compareTo(((vWindow)v).title()); }



//  new vWindow(title, mode, args) -- normal constructor

vWindow(String title, String mode, vDescriptor args[]) throws IOException {

    int w = 480;	// default initial width (later reset depending on font)
    int h = 156;	// default initial height

    c = new wCanvas(this, title, w, h);
    wnum = ++wcount;

    a = c.getGraphics();
    b = c.i.getGraphics();
    gnum = ++gcount;

    // set the usual defaults
    clipping = false;
    dx = 0;
    dy = 0;

    gamma = iConfig.Gamma;
    fg = wColor.Black;
    bg = wColor.White;
    revatt = vSoff;
    Font(vString.New(iConfig.FontName));

    wAttrib alist[] = wAttrib.parseAtts(args, 2);  // verify parsing first

    c.defer_image = true;			// set flag to defer image load
    for (int i = 0; i < alist.length; i++) {	// apply attributes
	wAttrib a = alist[i];
	if (a.val != null) {
	    if (a.set(this) == null) {		// if couldn't set attribute
		this.close();
		throw new IOException();	// indicate failure
	    }
	}
    }

    c.defconfig(this);		// set window size, if not set by attributes
    c.tty.Row(this, "1");	// set text cursor position
    c.tty.Col(this, "1");

    // unless canvas attribute was specified, make visible now
    if (c.Canvas(this, null) == null) {
	c.Canvas(this, "normal");
    }

    // clear window and backing store, including out-of-bounds area, with new bg
    EraseArea(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);

    // now (after initializing background) load deferred image
    if (c.deferred_image != null) {
    	CopyImage(c.deferred_image, -dx, -dy);
	c.deferred_image.flush();
	c.deferred_image = null;
    }
    c.defer_image = false;

    this.flush();
    setCurrent(this);		// remember as "current" window
}



//  new vWindow(w) -- for implementing WClone()

private vWindow(vWindow w) {
    c = w.c;
    wnum = w.wnum;
    c.wlist.addElement(this);

    a = w.a.create();
    b = w.b.create();
    gnum = ++gcount;

    this.clipping = w.clipping;
    this.dx = w.dx;
    this.dy = w.dy;

    this.gamma = w.gamma;
    this.xormode = w.xormode;
    this.revatt = w.revatt;
    this.fg = w.fg;
    this.bg = w.bg;
    b.setColor(fg);
    a.setColor(fg);

    this.font = w.font;
    b.setFont(font);
    a.setFont(font);

    this.leading = w.leading;
}



//  newgcb(g) -- install new graphics context for backing store
//
//  (called by the Canvas code if the underlying image has changed)

void newgcb(Graphics g) {

    Graphics newb = g.create();

    newb.setColor(fg);
    if (xormode) {
	newb.setXORMode(bg);
    }

    newb.setFont(font);

    newb.translate(dx, dy);
    if (clipping) {
	Rectangle r = b.getClipBounds();
	newb.setClip(r.x, r.y, r.width, r.height);
    }

    b.dispose();
    b = newb;
}



//  "current window" maintenance  (not the same as &window)
//  set by open() and Event()
//  used by assignment to &x, &y, &row, &col

private static vWindow curwin;

public static void setCurrent(vWindow win) {
    curwin = win;
    vFile.winToSync = win;
}

public static vWindow getCurrent() {
    if (curwin != null && curwin.c != null) {	// if still open
    	return curwin;
    }
    return curwin = iKeyword.window.getWindow();  // no, fall back to &window
}



//  close() -- close window -- overrides vFile.close()

public vFile close() {
    if (a == null) {
	return this;		// already closed
    }
    a.dispose();		// dispose graphics contexts
    b.dispose();
    c.dispose();		// dispose frame and image

    Vector v = c.wlist;		// list of windows sharing the canvas
    for (int j = 0; j < v.size(); j++) {
	vWindow win = (vWindow) v.elementAt(j);
	win.a = null;		// mark each as closed
	win.b = null;
	win.c = null;
	openfiles.remove(win);
    }
    v.removeAllElements();
    return this;
}



//  uncouple() -- close this graphics context only

public vFile uncouple() {
    if (a == null) {
	return this;		// already closed
    }
    if (c.wlist.size() == 1) {
	return this.close();	// this is last binding
    }
    c.wlist.removeElement(this);
    a.dispose();
    b.dispose();
    a = null;
    b = null;
    c = null;
    return this;
}



//  static function that references the toolkit

public static void beep() {		// send a beep
    toolkit.beep();
}



//  static methods for argument processing and defaulting


//  argBase(args) -- get index of first non-window argument, 0 or 1

public static int argBase(vDescriptor args[]) {
    if (args.length > 0 && args[0].iswin()) {
	return 1;
    } else {
	return 0;
    }
}



//  winArg(args) -- get explicit window argument, or implicit value from &window

public static vWindow winArg(vDescriptor args[]) {
    vWindow win;

    if (argBase(args) == 1) {
	win = (vWindow) args[0].Deref();
    } else {
	win = iKeyword.window.getWindow();
    }

    if (win.a == null) {
	iRuntime.error(142, win);
    }
    return win;
}



//  tty-mode I/O

public vString read()		{ return getTTY().read(this); }
public vString reads(long n)	{ return getTTY().reads(this, n); }
public void writes(vString s)	{ getTTY().writes(this, s); }
public void newline()		{ getTTY().newline(this); }

public vFile flush() {		// flush() is all-purpose window synchronizer
    getTTY();			// ensure window is still open
    toolkit.sync();		// flush graphics output, sync toolkit
    Thread.yield();		// let event handlers run
    return this;
}



public vWindow Clone() {
    return new vWindow(this);
}

public vList Pending() {
    this.flush();
    return c.evq;
}

public vValue Event() {
    setCurrent(this);
    vValue e = wEvent.dequeue(c, dx, dy);	// look for event
    if (e == null) {				// if none available
	iKeyword.output.file().flush();		// flush stdout
	this.flush();				// flush & sync window
   	e = wEvent.dequeue(c, dx, dy);		// try again
    }
    while (e == null) {	
	try {
	    Thread.sleep(iConfig.PollDelay);
	} catch (InterruptedException x) {
	}
	if (c == null) {
	    iRuntime.error(142);	// window was closed while waiting
	}
	e = wEvent.dequeue(c, dx, dy);
    }
    return e;
}



private static int offset;		// round-robin starting point

public static vWindow Active() {	// Active() finds win w/ pending event
    Vector<vWindow> v = new Vector<vWindow>();
    for (Enumeration e = openfiles.elements(); e.hasMoreElements(); ) {
	Object o = e.nextElement();
	if (o instanceof vWindow && ((vWindow)o).c != null) {
	    v.addElement((vWindow)o);
	}
    }
    int n = v.size();
    if (n == 0) {
	return null; /*FAIL*/
    }
    ++offset;				// update starting point
    while (true) {
	for (int i = 0; i < n; i++) {
	    vWindow w = (vWindow) v.elementAt((i + offset) % n);
	    if (w.c != null && w.c.evq.Size().value > 0) {
		return w;
	    }
	}
	iKeyword.output.file().flush();		// flush stdout
	try {
	    Thread.sleep(iConfig.PollDelay);	// sleep a little
	} catch (InterruptedException x) {
	}
    }
}



public void Origin(int newdx, int newdy) {	// set origin
    b.translate(newdx - dx, newdy - dy);
    a.translate(newdx - dx, newdy - dy);
    dx = newdx;
    dy = newdy;
}



public Rectangle getClip() {			// inquire about clipping
    if (clipping) {
	return b.getClipBounds();
    } else {
	return null;
    }
}

public vWindow Clip() {				// disable clipping
    b.setClip(null);
    a.setClip(null);
    clipping = false;
    return this;
}

public vWindow Clip(int x, int y, int w, int h) {  // enable clipping (int vals)
    b.setClip(x, y, w, h);
    a.setClip(x, y, w, h);
    clipping = true;
    return this;
}

private static vValue clipArgs[] = new vValue[4];

public vWindow Clip(vString xs, vString ys, vString ws, vString hs){ // set atts
    try {
	Rectangle r;
	if (clipping) {
	    r = b.getClipBounds();
	} else {
	    r = c.getBounds();
	    r.x = -dx;
	    r.y = -dy;
	}
	if (xs != null) r.x = (int) xs.mkInteger().value;
	if (ys != null) r.y = (int) ys.mkInteger().value;
	if (ws != null) r.width = (int) ws.mkInteger().value;
	if (hs != null) r.height = (int) hs.mkInteger().value;
	return Clip(r.x, r.y, r.width, r.height);
    } catch (iError e) {
	return null;
    }
}



public vString Gamma(vString s) {
    if (s == null) {
	return vString.New(String.valueOf(gamma));
    }
    double d = s.mkReal().value;
    if (d <= 0.0) {
	return null; /*FAIL*/
    }
    gamma = d;			// save gamma value
    Fg(fg.spec);		// respecify fg and bg
    Bg(bg.spec);
    return vString.New(String.valueOf(gamma));
}

public vString Fg(vString s) {
    if (s == null) {
	return this.fg.spec;
    }
    wColor k = wColor.New(s, gamma);
    if (k == null) {
	return null;
    } else {
	b.setColor(k);
	a.setColor(k);
	fg = k;
	return k.spec;
    }
}

public vString Bg(vString s) {
    if (s == null) {
	return this.bg.spec;
    }
    wColor k = wColor.New(s, gamma);
    if (k == null) {
	return null;
    } else {
	bg = k;
	if (xormode) {
	    b.setXORMode(bg);
	    a.setXORMode(bg);
	}
	return k.spec;
    }
}

public vString Drawop(vString s) {
    if (s != null) {
        if (s.identical(vScopy)) {
	    if (xormode) {
		b.setPaintMode();
		a.setPaintMode();
	    	xormode = false;
	    }
	} else if (s.identical(vSreverse)) {
	    if (!xormode) {
		b.setXORMode(bg);
		a.setXORMode(bg);
	    	xormode = true;
	    }
	} else {
	    return null; /*FAIL*/
	}
    }
    return xormode ? vSreverse : vScopy; 
}

public vString Reverse(vString s) {			// reverse= attribute
    if (s != null && ! (revatt.identical(s))) {		// if changing value
	if (s.identical(vSoff) || s.identical(vSon)) {	// if legal new value
	    vString fgspec = fg.spec;			// swap fg/bg
	    Fg(bg.spec);
	    Bg(fgspec);	// set bg second in case of drawop=reversed mode
	    revatt = s;					// save new value
	} else {
	    return null; /*FAIL*/
	}
    }
    return revatt;
}



public vString Font(vString s) {
    if (s == null) {
	return this.font.spec;
    }
    wFont f = wFont.parse(s);
    if (f == null) {
	return null;
    } else {
	font = f;
	this.b.setFont(f);
	this.a.setFont(f);
	leading = c.getFontMetrics(font).getHeight(); // java calls it "height"
	return s;
    }
}

public int Leading()		{ return leading; }
public int Leading(int n)	{ return leading = n; }

public int Fwidth() {
    FontMetrics m = c.getFontMetrics(font);
    return m.charWidth('W');	// more reliable than getMaxAdvance
}



public vInteger TextWidth(String s) {
    return vInteger.New(c.getFontMetrics(font).stringWidth(s));
}



// drawing operations write backing store first in case of inopportune refresh



public void DrawRectangle(int x, int y, int w, int h) {
    b.drawRect(x, y, w, h);
    a.drawRect(x, y, w, h);
}

public void FillRectangle(int x, int y, int w, int h) {
    b.fillRect(x, y, w, h);
    a.fillRect(x, y, w, h);
}

public void EraseArea(int x, int y, int w, int h) {
    if (xormode) {
	b.setPaintMode();
	a.setPaintMode();
    }
    b.setColor(bg);
    a.setColor(bg);
    b.fillRect(x, y, w, h);
    a.fillRect(x, y, w, h);
    b.setColor(fg);
    a.setColor(fg);
    if (xormode) {
	b.setXORMode(bg);
	a.setXORMode(bg);
    }
}



public void DrawArc(int x, int y, int w, int h, double theta, double alpha) {
    int start = (int) Math.round(-180 * theta / Math.PI);
    int arc = ((int) Math.round(-180 * (theta + alpha) / Math.PI)) - start;
    b.drawArc(x, y, w, h, start, arc);
    a.drawArc(x, y, w, h, start, arc);
}

public void FillArc(int x, int y, int w, int h, double theta, double alpha) {
    int start = (int) Math.round(-180 * theta / Math.PI);
    int arc = ((int) Math.round(-180 * (theta + alpha) / Math.PI)) - start;
    b.fillArc(x, y, w, h, start, arc);
    a.fillArc(x, y, w, h, start, arc);
}



public void DrawLine(int x1, int y1, int x2, int y2) {
    b.drawLine(x1, y1, x2, y2);
    a.drawLine(x1, y1, x2, y2);
}

public void DrawLine(wCoords c) {
    b.drawPolyline(c.xPoints, c.yPoints, c.nPoints);
    a.drawPolyline(c.xPoints, c.yPoints, c.nPoints);
}



//  DrawCurve - draw a smooth curve through a set of points
//
//  The first and last points of the argument array are not drawn, but
//  they establish the curvature at the second and penultimate points.
//
//  The algorithm is from
//	Barry, Phillip J., and Goldman, Ronald N. (1988).
//	A Recursive Evaluation Algorithm for a class of Catmull-Rom Splines.
//	Computer Graphics 22(4), 199-204.

public void DrawCurve(int xpts[], int ypts[]) {
    int i, j, nsteps;
    double ax, ay, bx, by, stepsize, stepsize2, stepsize3;
    double x, dx, d2x, d3x, y, dy, d2y, d3y;

    for (i = 3; i < xpts.length; i++) {		// for each segment
	
	//  build the coefficients ax, ay, bx and by, using:
	//                              _              _   _    _
	//    i                 i    1 | -1   3  -3   1 | | Pi-3 |
	//   Q (t) = T * M   * G   = - |  2  -5   4  -1 | | Pi-2 |
	//                CR    Bs   2 | -1   0   1   0 | | Pi-1 |
	//                             |_ 0   2   0   0_| |_Pi  _|

	ax = xpts[i] - 3 * xpts[i-1] + 3 * xpts[i-2] - xpts[i-3];
	ay = ypts[i] - 3 * ypts[i-1] + 3 * ypts[i-2] - ypts[i-3];
	bx = 2 * xpts[i-3] - 5 * xpts[i-2] + 4 * xpts[i-1] - xpts[i];
	by = 2 * ypts[i-3] - 5 * ypts[i-2] + 4 * ypts[i-1] - ypts[i];

	//  compute step size
	dx = xpts[i-2] - xpts[i-1];
	dy = ypts[i-2] - ypts[i-1];
	nsteps = (int) Math.sqrt(dx * dx + dy * dy) + 4;
	stepsize = 1.0 / nsteps;
	stepsize2 = stepsize * stepsize;
	stepsize3 = stepsize * stepsize2;

	//  compute forward differences
	dx = (stepsize3*0.5) * ax + (stepsize2*0.5) * bx +
				(stepsize*0.5) * (xpts[i-1] - xpts[i-3]);
	dy = (stepsize3*0.5) * ay + (stepsize2*0.5) * by +
				(stepsize*0.5) * (ypts[i-1] - ypts[i-3]);
	d2x = (stepsize3*3) * ax + stepsize2 * bx;
	d2y = (stepsize3*3) * ay + stepsize2 * by;
	d3x = (stepsize3*3) * ax;
	d3y = (stepsize3*3) * ay;

	//  initialize point calculations
	int[] xdraw = new int[nsteps+1];
	int[] ydraw = new int[nsteps+1];
	x = xdraw[0] = xpts[i-2];
	y = ydraw[0] = ypts[i-2];

	//  compute points for drawing the curve segment
	for (j = 1; j <= nsteps; j++) {
	    x = x + dx;
	    y = y + dy;
	    dx = dx + d2x;
	    dy = dy + d2y;
	    d2x = d2x + d3x;
	    d2y = d2y + d3y;
	    xdraw[j] = (int) x;
	    ydraw[j] = (int) y;
	}

	//  draw the segment
	b.drawPolyline(xdraw, ydraw, nsteps + 1);
	a.drawPolyline(xdraw, ydraw, nsteps + 1);
    }
}



public void DrawPolygon(wCoords c) {
    b.drawPolygon(c.xPoints, c.yPoints, c.nPoints);
    a.drawPolygon(c.xPoints, c.yPoints, c.nPoints);
}

public void FillPolygon(wCoords c) {
    b.fillPolygon(c.xPoints, c.yPoints, c.nPoints);
    a.fillPolygon(c.xPoints, c.yPoints, c.nPoints);
}



public void DrawString(int x, int y, String s) {
    b.drawString(s, x, y);
    a.drawString(s, x, y);
}



public void CopyImage(Image im, int x, int y) {
    b.drawImage(im, x, y, null);
    a.drawImage(im, x, y, null);
}



public void CopyArea(vWindow src, int x1, int y1, int w, int h, int x2, int y2){

    // adjust negative width and height
    if (w < 0) {
	x1 -= (w = -w);
    }
    if (h < 0) {
	y1 -= (h = -h);
    }

    // check for source portions outside window bounds
    Dimension d = src.c.getSize();
    int lmar, rmar, tmar, bmar;
    lmar = -src.dx - x1;		// amount outside left edge
    rmar = -src.dx + x1 + w - d.width;	// amount outside right edge
    tmar = -src.dy - y1;		// amount outside top edge
    bmar = -src.dy + y1 + h - d.height;	// amount outside bottom edge

    // adjust bounds for any positive margins; truncate negative margins to 0
    if (lmar > 0) {
	x1 += lmar;
	x2 += lmar;
	w -= lmar;
    } else {
	lmar = 0;
    }
    if (rmar > 0) {
	w -= rmar;
    } else {
	rmar = 0;
    }
    if (tmar > 0) {
	y1 += tmar;
	y2 += tmar;
	h -= tmar;
    } else {
	tmar = 0;
    }
    if (bmar > 0) {
	h -= bmar;
    } else {
	bmar = 0;
    }

    // copy source region, if anything remains
    if (w > 0 && h > 0) {
	if (xormode) {
	    b.setPaintMode();
	    a.setPaintMode();
	}
	// remaining source area is within bounds of window
	if (src == this) {
	    b.copyArea(x1, y1, w, h, x2 - x1, y2 - y1);
	} else {
	    b.drawImage(src.c.i, x2, y2, x2+w, y2+h,
		x1 + src.dx, y1 + src.dy, x1 + src.dx + w, y1 + src.dy + h,
		null);
	}
	a.drawImage(c.i, x2, y2, x2 + w, y2 + h,
	    x2 + dx, y2 + dy, x2 + w + dx, y2 + h + dy, null);
    }

    // erase areas "copied" from outside window bounds
    if (lmar + rmar + tmar + bmar > 0) {
	wColor bgsave = bg;
	bg = src.bg;
	if (lmar > 0) {
	    EraseArea(x2 - lmar, y2 - tmar, lmar, tmar + h + bmar);
	}
	if (rmar > 0) {
	    EraseArea(x2 + w, y2 - tmar, rmar, tmar + h + bmar);
	}
	if (tmar > 0) {
	    EraseArea(x2, y2 - tmar, w, tmar);
	}
	if (bmar > 0) {
	    EraseArea(x2, y2 + h, w, bmar);
	}
	bg = bgsave;
    }

    if (xormode) {
	b.setXORMode(bg);
	a.setXORMode(bg);
    }
}



} // class vWindow

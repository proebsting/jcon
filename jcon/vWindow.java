//  vWindow.java -- graphics window Type
//
//  note that vWindow extends vFile to support read(), write(), etc.

package rts;

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

    private double gamma;	// gamma correction factor
    private wColor bg;		// background color
    private wColor fg;		// foreground color
    private boolean xormode;	// in "drawop=reverse" (XOR) mode?
    private vString revatt;	// value of "reverse=" attribute

    private wFont font;		// text font
    private int leading;	// leading value



// constant vstrings
private static vString vSoff = vString.New("off");
private static vString vSon = vString.New("on");
private static vString vScopy = vString.New("copy");
private static vString vSreverse = vString.New("reverse");


public boolean iswin()		{ return true; }

wCanvas getCanvas() {
    if (c == null) {
	iRuntime.error(142, this);
    }
    return c;
}
wTTY getTTY()			{ return getCanvas().tty; }

wColor getBg()			{ return bg; }
wFont getFont()			{ return font; }
FontMetrics getFontMetrics()	{ return c.getFontMetrics(font); }



private static Toolkit toolkit = Toolkit.getDefaultToolkit();

private static int wcount = 0;	// count of windows allocated
private static int gcount = 0;	// count of graphics contexts allocated



static vString typestring = vString.New("window");
public vString Type()	{ return typestring; }

public vString image()	{
    return vString.New("window_" + wnum + ":" + gnum + "(" + title() + ")");
}
String title()		{ return c == null ? "<closed>" : c.f.getTitle(); }

int rank()		{ return 50; }		// windows sort after csets
int compareTo(vValue v)	{ return title().compareTo(((vWindow)v).title()); }



//  new vWindow(title, mode, args) -- normal constructor

vWindow(String title, String mode, vDescriptor args[]) throws IOException {

    int w = 480;	// default width	// #%#% should depend on font
    int h = 156;	// default height	// #%#% should depend on font

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
    this.flush();

    setCurrent(this);		// remember as "current" window

    //#%#% might want to consider spawning a background thread
    //#%#% that would sync the canvas (handle exposure events) periodically
}



//  new vWindow(w) -- for implementing WClone()

vWindow(vWindow w) {
    //#%#% really clone it, to ensure it gets all attribs??
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
    b = g.create();
    b.setColor(fg);
    b.setFont(font);
}



//  "current window" maintenance  (not the same as &window)
//  set by open() and Event()
//  used by assignment to &x, &y, &row, &col
//  and for synching with reads from tty

private static vWindow curwin;

static void setCurrent(vWindow win) {
    curwin = win;
    vFile.winToSync = win;
}

static vWindow getCurrent() {
    if (curwin == null) {
	iRuntime.error(140);
    }
    return curwin;
}



//  close() -- close window -- overrides vFile.close()

vFile close() {
    if (a == null) {
	return this;		// already closed
    }
    c.f.dispose();		// dispose frame
    c.i.flush();		// dispose backing image
    a.dispose();		// dispose graphics contexts
    b.dispose();

    Vector v = c.wlist;		// list of windows sharing the canvas
    for (int j = 0; j < v.size(); j++) {
	vWindow win = (vWindow) v.elementAt(j);
	win.a = null;		// mark each as closed
	win.b = null;
	win.c = null;
    }
    v.removeAllElements();
    return this;
}



//  uncouple() -- close this graphics context only

vFile uncouple() {
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

static void beep() {		// send a beep
    toolkit.beep();
}




//  static methods for argument processing and defaulting


//  argBase(args) -- get index of first non-window argument, 0 or 1

static int argBase(vDescriptor args[]) {
    if (args.length > 0 && args[0].iswin()) {
	return 1;
    } else {
	return 0;
    }
}


//  winArg(args) -- get explicit window argument, or implicit value from &window

static vWindow winArg(vDescriptor args[]) {
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

vString read()			{ return getTTY().read(this); }
vString reads(long n)		{ return getTTY().reads(this, n); }
void writes(vString s)		{ getTTY().writes(this, s); }
void newline()			{ getTTY().newline(this); }

vFile flush()			{ toolkit.sync(); return this; }



vWindow Clone() {
    return new vWindow(this);
}

vList Pending() {
    return c.evq;
}

vValue Event() {
    if (c.evq.Size().value == 0) {	// if we're going to block
	iKeyword.output.file().flush();	// flush stdout first
    }
    vValue e = wEvent.dequeue(c.evq, dx, dy);
    setCurrent(this);
    return e;
}



void Origin(int newdx, int newdy) {		// set origin
    b.translate(newdx - dx, newdy - dy);
    a.translate(newdx - dx, newdy - dy);
    dx = newdx;
    dy = newdy;
}



Rectangle getClip() {				// inquire about clipping
    if (clipping) {
	return b.getClipBounds();
    } else {
	return null;
    }
}

vWindow Clip() {				// disable clipping
    b.setClip(-Integer.MIN_VALUE, -Integer.MIN_VALUE,
	Integer.MAX_VALUE, Integer.MAX_VALUE);
    a.setClip(-Integer.MIN_VALUE, -Integer.MIN_VALUE,
	Integer.MAX_VALUE, Integer.MAX_VALUE);
    clipping = false;
    return this;
}

vWindow Clip(int x, int y, int w, int h) {	// enable clipping (int vals)
    b.setClip(x, y, w, h);
    a.setClip(x, y, w, h);
    clipping = true;
    return this;
}

static vValue clipArgs[] = new vValue[4];

vWindow Clip(vString xs, vString ys, vString ws, vString hs) {	// set attribs
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



vString Gamma(vString s) {
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

vString Fg(vString s) {
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

vString Bg(vString s) {
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

vString Drawop(vString s) {
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

vString Reverse(vString s) {				// reverse= attribute
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



vString Font(vString s) {
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

int Leading()		{ return leading; }
int Leading(int n)	{ return leading = n; }

int Fwidth() {
    FontMetrics m = c.getFontMetrics(font);
    int fw = m.getMaxAdvance();
    if (fw > 0) {
	return fw;
    } else {
	return m.charWidth('W');
    }
}



vInteger TextWidth(String s) {
    return vInteger.New(c.getFontMetrics(font).stringWidth(s));
}



// drawing operations write backing store first in case of inopportune refresh



void DrawRectangle(int x, int y, int w, int h) {
    b.drawRect(x, y, w, h);
    a.drawRect(x, y, w, h);
}

void FillRectangle(int x, int y, int w, int h) {
    b.fillRect(x, y, w, h);
    a.fillRect(x, y, w, h);
}

void EraseArea(int x, int y, int w, int h) {
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



void CopyArea(vWindow src, int x1, int y1, int w, int h, int x2, int y2) {

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
	    b.drawImage(src.c.i, x2, y2, x2+w, y2+h, x1, y1, x1+w, y1+h, null);
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



void DrawArc(int x, int y, int w, int h, double theta, double alpha) {
    int start = (int) Math.round(-180 * theta / Math.PI);
    int arc = ((int) Math.round(-180 * (theta + alpha) / Math.PI)) - start;
    b.drawArc(x, y, w, h, start, arc);
    a.drawArc(x, y, w, h, start, arc);
}

void FillArc(int x, int y, int w, int h, double theta, double alpha) {
    int start = (int) Math.round(-180 * theta / Math.PI);
    int arc = ((int) Math.round(-180 * (theta + alpha) / Math.PI)) - start;
    b.fillArc(x, y, w, h, start, arc);
    a.fillArc(x, y, w, h, start, arc);
}



void DrawLine(int x1, int y1, int x2, int y2) {
    b.drawLine(x1, y1, x2, y2);
    a.drawLine(x1, y1, x2, y2);
}

void DrawLine(wCoords c) {
    b.drawPolyline(c.xPoints, c.yPoints, c.nPoints);
    a.drawPolyline(c.xPoints, c.yPoints, c.nPoints);
}

void DrawPolygon(wCoords c) {
    b.drawPolygon(c.xPoints, c.yPoints, c.nPoints);
    a.drawPolygon(c.xPoints, c.yPoints, c.nPoints);
}

void FillPolygon(wCoords c) {
    b.fillPolygon(c.xPoints, c.yPoints, c.nPoints);
    a.fillPolygon(c.xPoints, c.yPoints, c.nPoints);
}



void DrawString(int x, int y, String s) {
    b.drawString(s, x, y);
    a.drawString(s, x, y);
}



} // class vWindow

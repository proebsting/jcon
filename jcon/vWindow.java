//  vWindow.java -- graphics window type
//
//  note that vWindow extends vFile to support read(), write(), etc.

package rts;

import java.io.*;
import java.awt.*;



public class vWindow extends vFile {

    // NOTE: when adding instance variables, update cloning initializion below

    private wCanvas c;		// underlying Icon canvas
    private Graphics a, b;	// graphics context for canvas and backing image

    private int wnum;		// window serial number
    private int gnum;		// graphics context serial number

    // graphics context attributes

    private wColor bg;		// current background color
    private wColor fg;		// current foreground color
    private wFont font;		// current text font
    private int leading;	// current leading value



wCanvas getCanvas()		{ return c; }
wTTY getTTY()			{ return c.tty; }
wColor getBg()			{ return bg; }
wFont getFont()			{ return font; }
FontMetrics getFontMetrics()	{ return c.getFontMetrics(font); }



private static Toolkit toolkit = Toolkit.getDefaultToolkit();

private static int wcount = 0;	// count of windows allocated
private static int gcount = 0;	// count of graphics contexts allocated



String type()			{ return "window"; }
String image()			{ return "window_" + wnum + ":" + gnum 
				    + "(" + c.f.getTitle() + ")"; } 

int rank()			{ return 50; }	// windows sort after csets
int compareTo(vValue v)
	    { return c.f.getTitle().compareTo(((vWindow)v).c.f.getTitle()); }




//  window creation

static vWindow open(String name, String mode, vDescriptor args[]) {

    wAttrib alist[] = wAttrib.parseAtts(args, 2);  // verify parsing first

    vWindow win = new vWindow(name);		// open a default window

    for (int i = 0; i < alist.length; i++) {	// apply attributes
	wAttrib a = alist[i];
	if (a.val != null) {
	    if (a.set(win) == null) {		// if couldn't set attribute
		//#%#%#%#% win.close();
		return null;			// open failed
	    }
	}
    }

    // clear window and backing store, including out-of-bounds area, with new bg
    win.EraseArea(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);

    win.c.defconfig(win);	// set window size, if not set by attributes
    win.c.tty.Row(win, "1");	// set text cursor position
    win.c.tty.Col(win, "1");

    setCurrent(win);
    return win;
}

vWindow(String title) {				// new vWindow(s)
    
    int w = 480;	// default width	// #%#%#%#???
    int h = 156;	// default height	// #%#%#%#???

    c = new wCanvas(this, title, w, h);
    wnum = ++wcount;

    a = c.getGraphics();
    b = c.i.getGraphics();
    gnum = ++gcount;

    // set the usual defaults
    fg = wColor.Black;
    bg = wColor.White;
    Font(iNew.String(iConfig.FontName));
}

vWindow(vWindow w) {				// new vWindow(w)  [a Clone()]
			//#%#%#% really clone, to ensure it gets all attribs??
    c = w.c;
    wnum = w.wnum;

    a = w.a.create();
    b = w.b.create();
    gnum = ++gcount;

    this.fg = w.fg;
    b.setColor(fg);
    a.setColor(fg);

    this.bg = w.bg;

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

private static vWindow curwin;

static void setCurrent(vWindow win) {
    curwin = win;
}

static vWindow getCurrent() {
    if (curwin == null) {
	iRuntime.error(140);
    }
    return curwin;
}



//  static functions that reference the toolkit

static void sync() {		// sync graphics, if any, with tty input
    if (curwin != null) {
    	toolkit.sync();
    }
}

static void beep() {		// send a beep
    toolkit.beep();
}




//  static methods for argument processing and defaulting


//  argBase(args) -- get index of first non-window argument, 0 or 1

static int argBase(vDescriptor args[]) {
    if (args.length > 0 && args[0] instanceof vWindow) {
	return 1;
    } else {
	return 0;
    }
}


//  winArg(args) -- get explicit window argument, or implicit value from &window

static vWindow winArg(vDescriptor args[]) {
    if (argBase(args) == 1) {
	return (vWindow) args[0];
    } else {
	return k$window.getWindow();
    }
}



//  tty-mode I/O

vString read()			{ return c.tty.read(this); }
vString reads(long n)		{ return c.tty.reads(this, n); }
void writes(String s)		{ c.tty.writes(this, s); }
void newline()			{ c.tty.newline(this); }

vFile flush()			{ toolkit.sync(); return this; }

vFile close()			{ return this; } //#%#%#%#%#% TO BE WRITTEN




vWindow Clone() {
    return new vWindow(this);
}

vList Pending() {
    return c.evq;
}

vValue Event() {
    vValue e = wEvent.dequeue(c.evq);
    setCurrent(this);
    return e;
}



vString Fg(vString s) {
    if (s == null) {
	return this.fg.spec;
    }
    wColor k = wColor.parse(s);
    if (k == null) {
	return null;
    } else {
	a.setColor(k);
	b.setColor(k);
	fg = k;
	return k.spec;
    }
}

vString Bg(vString s) {
    if (s == null) {
	return this.bg.spec;
    }
    wColor k = wColor.parse(s);
    if (k == null) {
	return null;
    } else {
	bg = k;
	return k.spec;
    }
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
    return iNew.Integer(c.getFontMetrics(font).stringWidth(s));
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
    b.setColor(bg);
    a.setColor(bg);
    b.fillRect(x, y, w, h);
    a.fillRect(x, y, w, h);
    b.setColor(fg);
    a.setColor(fg);
}



void CopyArea(int x1, int y1, int w, int h, int x2, int y2) {

    // check for source portions outside window bounds
    Dimension d = c.getSize();
    int lmar, rmar, tmar, bmar;
    lmar = -x1;			// amount outside left edge
    rmar = x1 + w - d.width;	// amount outside right edge
    tmar = -y1;			// amount outside top edge
    bmar = y1 + h - d.height;	// amount outside bottom edge

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
	// remaining source area is within bounds of window
	// #%#%##%#% although it might be obscured -- need to handle that
	b.copyArea(x1, y1, w, h, x2 - x1, y2 - y1);
	a.copyArea(x1, y1, w, h, x2 - x1, y2 - y1);
    }

    // erase areas "copied" from outside window bounds
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

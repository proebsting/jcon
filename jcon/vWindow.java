//  vWindow.java -- graphics window type

package rts;

import java.io.*;
import java.awt.*;



public class vWindow extends vValue {

    // NOTE: when adding instance variables, update cloning initializion below

    private wCanvas c;		// underlying Icon canvas
    private Graphics a, b;	// graphics context for canvas and backing image

    private int wnum;		// window serial number
    private int gnum;		// graphics context serial number

    // graphics context attributes

    private wColor bg;		// current background color
    private wColor fg;		// current foreground color
    private wFont font;		// current text font



wCanvas getCanvas()		{ return c; }
wColor getBg()			{ return bg; }




private static Toolkit toolkit = Toolkit.getDefaultToolkit();

private static int wcount = 0;	// count of windows allocated
private static int gcount = 0;	// count of graphics contexts allocated



String type()			{ return "window"; }
String image()			{ return "window_" + wnum + ":" + gnum 
				    + "(" + c.f.getTitle() + ")"; } 

int rank()			{ return 50; }	// windows sort after csets
int compareTo(vValue v)
	    { return c.f.getTitle().compareTo(((vWindow)v).c.f.getTitle()); }

//#%#%#%   vDescriptor Bang(iClosure c)	{ return this.read(); }



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
    Font(iNew.String(wFont.DEFAULT_FONT));
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




//#%#%# seek, where:  fail
//#%#%  flush, close
//#%#%  read, reads
//#%#%  write, writes



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
	return s;
    }
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

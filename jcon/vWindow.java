//  vWindow.java -- Icon graphics window type

package rts;

import java.io.*;
import java.awt.*;



public class vWindow extends vValue {



private wCanvas c;		// underlying Icon canvas
private Graphics a, b;		// graphics context for canvas and backing image

private int wnum;		// window serial number
private int gnum;		// graphics context serial number


// graphics context attributes

private wColor fg;		// current foreground color



private static int wcount = 0;	// count of windows allocated
private static int gcount = 0;	// count of graphics contexts allocated



String type()			{ return "window"; }
String image()			{ return "window_" + wnum + "," + gnum 
				    + "(" + c.f.getTitle() + ")"; } 

int rank()			{ return 62; }	// windows sort after files
int compareTo(vValue v)
	    { return c.f.getTitle().compareTo(((vWindow)v).c.f.getTitle()); }

//#%#%#%   vDescriptor Bang(iClosure c)	{ return this.read(); }




//  window creation

static vWindow open(String name, String mode, vDescriptor args[]) {

	//#%#%#%##% need to process arg list etc.

    vWindow win;
    win = new vWindow(name, 500, 300);	//#%#%#% 
    setCurrent(win);
    return win;
}

vWindow(String title, int w, int h) {		// new vWindow(s, w, h)

    c = new wCanvas(title, w, h);
    wnum = ++wcount;

    a = c.getGraphics();
    b = c.i.getGraphics();
    gnum = ++gcount;

    Fg(wColor.White);
    FillRectangle(0, 0, w, h);
    Fg(wColor.Black);
}

vWindow(vWindow w) {				// new vWindow(w)  [a Clone()]
    c = w.c;
    wnum = w.wnum;

    a = w.a.create();
    b = w.b.create();
    gnum = ++gcount;
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




//  static methods for argument processing and defaulting


//  argBase(args) -- get index of first real (non-window) argument, 0 or 1

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




//#%#%#% rectArgs doesn't handle defaulted values yet

static int[] rectArgs(vDescriptor args[]) {	// get x,y,w,h sets as ints
    int i, j, n;
    int a[];

    if (args.length == 0) {
	iRuntime.error(146);
    }
    if (args[0] instanceof vWindow) {
	i = 1;
    } else {
	i = 0;
    }
    n = args.length - i;
    if ((n % 4) != 0) {
	iRuntime.error(146);
    }
    a = new int[n];
    for (j = 0; j < n; j++) {
	a[j] = (int) vInteger.argVal(args, i++);
    }
    for (j = 0; j < n; j+=4) {
	if (a[j+2] < 0) {
	    a[j] -= (a[j+2] = -a[j+2]);
	}
	if (a[j+3] < 0) {
	    a[j+1] -= (a[j+3] = -a[j+3]);
	}
    }
    return a;
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

vString Fg(wColor k) {
    if (k == null) {
    	return null;
    }
    this.b.setColor(k);
    this.a.setColor(k);
    fg = k;
    return k.spec;
}

vString Fg(vString s) {
    return Fg(wColor.parse(s));
}



// write backing store first in case of inopportune refresh

void DrawCircle(int x, int y, int r) {
    x -= r;
    y -= r;
    b.drawOval(x, y, r, r);
    a.drawOval(x, y, r, r);
}

void DrawLine(int x1, int y1, int x2, int y2) {
    b.drawLine(x1, y1, x2, y2);
    a.drawLine(x1, y1, x2, y2);
}

void DrawRectangle(int x, int y, int w, int h) {
    b.drawRect(x, y, w, h);
    a.drawRect(x, y, w, h);
}

void FillRectangle(int x, int y, int w, int h) {
    b.fillRect(x, y, w, h);
    a.fillRect(x, y, w, h);
}



} // class vWindow

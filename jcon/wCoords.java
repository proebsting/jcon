//  wCoords.java -- coordinate lists and such

//  wCoords encapsulates a list of points using two arrays
//  suitable for passing to java.awt.graphics.drawPolygon()
//  and similar methods.

package rts;

import java.awt.*;



public final class wCoords {

    int xPoints[];
    int yPoints[];
    int nPoints;



//  new wCoords(arglist) -- construct wCoords from descriptor list

wCoords(vDescriptor args[]) {

    int i = vWindow.argBase(args);
    int n = args.length - i;
    if ((n % 2) != 0) {
	iRuntime.error(146);
    }

    nPoints = n / 2;
    xPoints = new int[n];
    yPoints = new int[n];

    for (int j = 0; j < nPoints; j++) {
	xPoints[j] = (int) vInteger.argVal(args, i++);
	yPoints[j] = (int) vInteger.argVal(args, i++);
    }
}



//  rectargs(arglist, step) -- extract x,y,w,h sets into array of ints
//
//  step gives increment between arg sets (usually 4, but 6 for Draw/FillArc)

static int[] rectArgs(vDescriptor args[], int step) {	// get x,y,w,h int sets

    vWindow win = vWindow.winArg(args);
    Dimension d = win.getCanvas().getSize();

    int b = vWindow.argBase(args);
    int nsets = (args.length - b + step - 1) / step;
    if (nsets == 0) {
	nsets = 1;
    }

    int a[] = new int[4 * nsets];
    int j = 0;
    for (int i = 0; i < nsets * step; i += step) {
	int x, y;
	a[j++] = x = (int) vInteger.argVal(args, b + i, -win.dx);
	a[j++] = y = (int) vInteger.argVal(args, b + i + 1, -win.dy);
	a[j++] = (int) vInteger.argVal(args, b + i + 2, d.width - x);
	a[j++] = (int) vInteger.argVal(args, b + i + 3, d.height - y);
    }

    // put in canonical form: handle negative w/h values
    for (j = 0; j < a.length; j+=4) {
	if (a[j+2] < 0) {
	    a[j] -= (a[j+2] = -a[j+2]);
	}
	if (a[j+3] < 0) {
	    a[j+1] -= (a[j+3] = -a[j+3]);
	}
    }

    return a;
}



} // class wCoords

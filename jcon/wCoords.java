//  wCoords.java -- coordinate lists and such

//  wCoords encapsulates a list of points using two arrays
//  suitable for passing to java.awt.graphics.drawPolygon()
//  and similar methods.


package rts;


public class wCoords {

    int xPoints[];
    int yPoints[];
    int nPoints;



//  new wCoords(arglist) -- construct wCoords from descriptor list

wCoords(vDescriptor args[]) {

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
    if ((n % 2) != 0) {
	iRuntime.error(146);
    }

    nPoints = n / 2;
    xPoints = new int[n];
    yPoints = new int[n];

    for (j = 0; j < nPoints; j++) {
	xPoints[j] = (int) vInteger.argVal(args, i++);
	yPoints[j] = (int) vInteger.argVal(args, i++);
    }
}



//  rectargs(arglist) -- extract x,y,w,h sets into array of ints

//  #%#% doesn't handle defaulted values yet

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

    // put in canonical form: handle negative w/h values
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



} // class wCoords

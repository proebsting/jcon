//  fDrawing.java -- built-in drawing functions

package rts;

import java.awt.*;
import java.util.*;



final class fDrawing extends iInstantiate {
    public static fDrawing self = new fDrawing();
    public vProc instantiate(String name) {
        if (name.equals("f$DrawArc")) return new f$DrawArc();
        if (name.equals("f$DrawCircle")) return new f$DrawCircle();
        if (name.equals("f$DrawCurve")) return new f$DrawCurve();
        if (name.equals("f$DrawImage")) return new f$DrawImage();
        if (name.equals("f$DrawLine")) return new f$DrawLine();
        if (name.equals("f$DrawPoint")) return new f$DrawPoint();
        if (name.equals("f$DrawPolygon")) return new f$DrawPolygon();
        if (name.equals("f$DrawRectangle")) return new f$DrawRectangle();
        if (name.equals("f$DrawSegment")) return new f$DrawSegment();
        if (name.equals("f$DrawString")) return new f$DrawString();
        if (name.equals("f$EraseArea")) return new f$EraseArea();
        if (name.equals("f$FillArc")) return new f$FillArc();
        if (name.equals("f$FillCircle")) return new f$FillCircle();
        if (name.equals("f$FillPolygon")) return new f$FillPolygon();
        if (name.equals("f$FillRectangle")) return new f$FillRectangle();
        return null;
    } // vProc instantiate(String)
}




final class f$DrawPoint extends vProcV {	// DrawPoint(W,x,y,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	wCoords c = new wCoords(args);
	for (int i = 0; i < c.nPoints; i++) {
	    int x = c.xPoints[i];
	    int y = c.yPoints[i];
	    win.DrawLine(x, y, x, y);
	}
	return win;
    }
}

final class f$DrawLine extends vProcV {		// DrawLine(W,x,y,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	win.DrawLine(new wCoords(args));
	return win;
    }
}

final class f$DrawSegment extends vProcV {	// DrawSegment(W,x,y,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	wCoords c = new wCoords(args);
	if (c.nPoints % 2 != 0) {
	    iRuntime.error(146);
	}
	for (int i = 0; i < c.nPoints; i += 2) {
	    win.DrawLine(c.xPoints[i], c.yPoints[i],
		c.xPoints[i+1], c.yPoints[i+1]);
	}
	return win;
    }
}




final class f$DrawCurve extends vProcV {	// DrawCurve(W,x,y,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	wCoords c = new wCoords(args);
	int n = c.nPoints;
	if (n < 2) {
	    win.DrawLine(c);
	    return win;
	}

	int[] xpts = new int[n+2];
	int[] ypts = new int[n+2];
	System.arraycopy(c.xPoints, 0, xpts, 1, n);
	System.arraycopy(c.yPoints, 0, ypts, 1, n);

	int x0 = c.xPoints[0];
	int y0 = c.yPoints[0];
	int xN = c.xPoints[n-1];
	int yN = c.yPoints[n-1];

	if (x0 == xN && y0 == yN) {
	    // this is a closed curve -- copy first point to end & v.v.
	    xpts[0] = xN;
	    ypts[0] = yN;
	    xpts[n+1] = x0;
	    ypts[n+1] = y0;
	} else {
	    // this is an open curve -- duplicate first and last points
	    xpts[0] = x0;
	    ypts[0] = y0;
	    xpts[n+1] = xN;
	    ypts[n+1] = yN;
	}

	win.DrawCurve(xpts, ypts);
	return win;
    }
}



final class f$DrawPolygon extends vProcV {	// DrawPolygon(W,x,y,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	win.DrawPolygon(new wCoords(args));
	return win;
    }
}

final class f$FillPolygon extends vProcV {	// FillPolygon(W,x,y,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	win.FillPolygon(new wCoords(args));
	return win;
    }
}



final class f$DrawRectangle extends vProcV {	// DrawRectangle(W,x,y,w,h,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int[] a = wCoords.rectArgs(args, 4);
	for (int i = 0; i < a.length; i += 4) {
	    win.DrawRectangle(a[i], a[i+1], a[i+2], a[i+3]);
	}
	return win;
    }
}

final class f$EraseArea extends vProcV {	// EraseArea(W,x,y,w,h,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int[] a = wCoords.rectArgs(args, 4);
	for (int i = 0; i < a.length; i += 4) {
	    win.EraseArea(a[i], a[i+1], a[i+2], a[i+3]);
	}
	return win;
    }
}

final class f$FillRectangle extends vProcV {	// FillRectangle(W,x,y,w,h,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int[] a = wCoords.rectArgs(args, 4);
	for (int i = 0; i < a.length; i += 4) {
	    win.FillRectangle(a[i], a[i+1], a[i+2], a[i+3]);
	}
	return win;
    }
}



final class f$DrawArc extends vProcV {		// DrawArc(W,x,y,w,h,t,a,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int b = vWindow.argBase(args);

	int[] r = wCoords.rectArgs(args, 6);
	for (int i = 0; i < r.length; i += 4) {
	    double t = vReal.argVal(args, b + 4, 0.0);
	    double a = vReal.argVal(args, b + 5, 2.0 * Math.PI);
	    win.DrawArc(r[i], r[i+1], r[i+2], r[i+3], t, a);
	    b += 6;
	}
	return win;
    }
}

final class f$FillArc extends vProcV {		// FillArc(W,x,y,w,h,t,a,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int b = vWindow.argBase(args);

	int[] r = wCoords.rectArgs(args, 6);
	for (int i = 0; i < r.length; i += 4) {
	    double t = vReal.argVal(args, b + 4, 0.0);
	    double a = vReal.argVal(args, b + 5, 2.0 * Math.PI);
	    win.FillArc(r[i], r[i+1], r[i+2], r[i+3], t, a);
	    b += 6;
	}
	return win;
    }
}



final class f$DrawCircle extends vProcV {	// DrawCircle(W,x,y,r,t,a,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int b = vWindow.argBase(args);
	int ncir = (args.length - b + 4) / 5;
	if (ncir == 0) {
	    ncir = 1;
	}
	for (int i = 0; i < 5 * ncir; i += 5) {
	    double x = vReal.argVal(args, b + i);
	    double y = vReal.argVal(args, b + i + 1);
	    double r = vReal.argVal(args, b + i + 2);
	    double t = vReal.argVal(args, b + i + 3, 0.0);
	    double a = vReal.argVal(args, b + i + 4, 2.0 * Math.PI);
	    int ix = (int) (x - r);
	    int iy = (int) (y - r);
	    int w = (int) (2 * r);
	    win.DrawArc(ix, iy, w, w, t, a);
	}
	return win;
    }
}

final class f$FillCircle extends vProcV {	// FillCircle(W,x,y,r,t,a,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int b = vWindow.argBase(args);
	int ncir = (args.length - b + 4) / 5;
	if (ncir == 0) {
	    ncir = 1;
	}
	for (int i = 0; i < 5 * ncir; i += 5) {
	    double x = vReal.argVal(args, b + i);
	    double y = vReal.argVal(args, b + i + 1);
	    double r = vReal.argVal(args, b + i + 2);
	    double t = vReal.argVal(args, b + i + 3, 0.0);
	    double a = vReal.argVal(args, b + i + 4, 2.0 * Math.PI);
	    int ix = (int) (x - r);
	    int iy = (int) (y - r);
	    int w = (int) (2 * r);
	    win.FillArc(ix, iy, w, w, t, a);
	}
	return win;
    }
}




final class f$DrawString extends vProcV {	// DrawString(W,x,y,s,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);	// validate args
	int b = vWindow.argBase(args);
	if ((args.length - b) % 3 != 0) {
	    iRuntime.error(146);
	}
	for (int i = b; i < args.length; i += 3) {
	    int x = (int) vInteger.argVal(args, i);
	    int y = (int) vInteger.argVal(args, i + 1);
	    String s = vString.argDescr(args, i + 2).toString();
	    win.DrawString(x, y, s);
	}
	return null;				// fail
    }
}



final class f$DrawImage extends vProc4 {	// DrawImage(W,x,y,s)
    public vDescriptor Call(
	    vDescriptor a, vDescriptor b, vDescriptor c, vDescriptor d) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a, b, c);
	}
	vWindow win = (vWindow)(a.Deref());
	int x = b.isnull() ? -win.dx : ((int) b.mkInteger().value);
	int y = c.isnull() ? -win.dy : ((int) c.mkInteger().value);
	Image im = wImage.decode(win, d.mkString());
	if (im == null) {
	    return null; /*FAIL*/
	}
	win.CopyImage(im, x, y);
	im.flush();
	return vNull.New();	// note: returns null, not window
    }
}

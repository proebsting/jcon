//  wBuiltins.java -- built-in graphics functions

package rts;

import java.awt.*;



public final class wBuiltins extends iFile {



static void announce() {

    // graphics functions that are at least partially implemented:

    iEnv.declareBuiltin("Alert", 1);
    iEnv.declareBuiltin("Bg", 2);
    iEnv.declareBuiltin("Clip", 5);
    iEnv.declareBuiltin("Clone", -1);
    iEnv.declareBuiltin("Color", -1);		// always fails
    iEnv.declareBuiltin("ColorValue", 2);
    iEnv.declareBuiltin("CopyArea", 8);		//#%#% within same window only
    iEnv.declareBuiltin("DrawArc", -1);
    iEnv.declareBuiltin("DrawCircle", -1);
    iEnv.declareBuiltin("DrawLine", -1);
    iEnv.declareBuiltin("DrawPoint", -1);
    iEnv.declareBuiltin("DrawPolygon", -1);
    iEnv.declareBuiltin("DrawRectangle", -1);
    iEnv.declareBuiltin("DrawSegment", -1);
    iEnv.declareBuiltin("DrawString", -1);
    iEnv.declareBuiltin("EraseArea", -1);
    iEnv.declareBuiltin("Event", 1);
    iEnv.declareBuiltin("Fg", 2);
    iEnv.declareBuiltin("FillArc", -1);
    iEnv.declareBuiltin("FillCircle", -1);
    iEnv.declareBuiltin("FillRectangle", -1);
    iEnv.declareBuiltin("FillPolygon", -1);
    iEnv.declareBuiltin("Font", 2);
    iEnv.declareBuiltin("FreeColor", -1);
    iEnv.declareBuiltin("GotoRC", 3);
    iEnv.declareBuiltin("GotoXY", 3);
    iEnv.declareBuiltin("NewColor", 2);		// always fails
    iEnv.declareBuiltin("Pending", 1);
    iEnv.declareBuiltin("TextWidth", 2);
    iEnv.declareBuiltin("Uncouple", 1);		// calls close(), a no-op so far
    iEnv.declareBuiltin("WAttrib", -1);
    iEnv.declareBuiltin("WDefault", 3);		// always fails
    iEnv.declareBuiltin("WFlush", 1);
    iEnv.declareBuiltin("WSync", 1);

    // #%#% IMPLEMENTED AS NO-OPS:
    // close(win)	(see wTTY.java)

    // #%#% NOT YET IMPLEMENTED:
    //
    // iEnv.declareBuiltin("Pattern", 2);
    //
    // iEnv.declareBuiltin("ReadImage", 5);
    // iEnv.declareBuiltin("WriteImage", 6);
    // iEnv.declareBuiltin("Pixel", 5);
    //
    // iEnv.declareBuiltin("PaletteChars", 2);
    // iEnv.declareBuiltin("PaletteColor", 3);
    // iEnv.declareBuiltin("PaletteKey", 3);
    // iEnv.declareBuiltin("DrawImage", 4);
    //
    // iEnv.declareBuiltin("DrawCurve", -1);
    //
    // iEnv.declareBuiltin("Active", 0);
    //
    // iEnv.declareBuiltin("Lower", 1);
    // iEnv.declareBuiltin("Raise", 1);
    //
    // iEnv.declareBuiltin("Couple", 2);
}



} // class wBuiltins



//------------------------------------------  miscellaneous functions follow




final class f$WAttrib extends vProcV { 		// WAttrib(W,attribs,..)
    public vDescriptor Call(vDescriptor[] args) {

	final vWindow win = vWindow.winArg(args);
	int b = vWindow.argBase(args);

	for (int i = b; i < args.length; i++) {
	    args[i] = args[i].Deref();
	}
	final wAttrib alist[] = wAttrib.parseAtts(args, b);

	for (int i = 0; i < alist.length; i++) {
	    wAttrib a = alist[i];
	    if (a.val != null) {		// if name=val argument
		if (a.set(win) == null) {
		    alist[i] = null;		// set failed
		}
	    }
	}

	return new vClosure() {
	    int next = 0;
	    public vDescriptor Resume() {
		vDescriptor v = null;
		while (next < alist.length) {
		    wAttrib a = alist[next++];
		    if (a != null) {
			v = a.get(win);
			if (v != null) {
			    break;			// generate value
			}
		    }
		}
		if (v == null) {
		    return null;
		} else {
		    retval = v;
		    return this;
		}
	    }
	}.Resume();
    }
}



final class f$Clone extends vProcV {		// Clone(W,attribs...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int b = vWindow.argBase(args);
	wAttrib alist[] = wAttrib.parseAtts(args, b);

	win = win.Clone();			// clone window
	for (int i = 0; i < alist.length; i++) { // apply attribs
	    wAttrib a = alist[i];
	    if (a.val != null) {		// if val given
		if (a.set(win) == null) {	// if set fails
		    return null;		// clone failed
		}
	    }
	}
	return win;
    }
}



final class f$Pending extends vProc1 {		// Pending(W)
    public vDescriptor Call(vDescriptor a) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow());
	}
	return ((vWindow)a.Deref()).Pending();
    }
}

final class f$Event extends vProc1 {		// Event(W)
    public vDescriptor Call(vDescriptor a) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow());
	}
	return ((vWindow)a.Deref()).Event();
    }
}



final class f$Clip extends vProcV {		// Clip(W, x, y, w, h)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	if (vWindow.argBase(args) >= args.length) {
	    return win.Clip();			// disable clipping
	}
	int[] a = wCoords.rectArgs(args, 4);
	return win.Clip(a[0], a[1], a[2], a[3]); // set clipping
    }
}



final class f$Bg extends vProc2 {		// Bg(W, s)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a);
	}
	return ((vWindow)a.Deref()).Bg(b.isnull() ? null : b.mkString());
    }
}

final class f$Fg extends vProc2 {		// Fg(W, s)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a);
	}
	return ((vWindow)a.Deref()).Fg(b.isnull() ? null : b.mkString());
    }
}

final class f$ColorValue extends vProc2 {	// ColorValue(W, s)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.iswin()) {
	    b = a;			// W is not needed or used
	}
	vString s = b.mkString();
	wColor k = wColor.New(s, 1.0);	// parse string (gamma has no effect)
	if (k == null) {
	    return null; /*FAIL*/
	} else {
	    return vString.New((int)(65535 * k.r + 0.5) + "," + 
		(int)(65535 * k.g + 0.5) + "," + (int)(65535 * k.b + 0.5));
	}
    }
}




final class f$Font extends vProc2 {		// Font(W,s)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a);
	}
	return ((vWindow)a.Deref()).Font(b.isnull() ? null : b.mkString());
    }
}

final class f$TextWidth extends vProc2 {	// TextWidth(W,s)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a);
	}
	return ((vWindow)a.Deref()).TextWidth(b.mkString().toString());
    }
}

final class f$GotoXY extends vProc3 {		// GotoXY(W,x,y)
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a, b);
	}
	vWindow win = (vWindow)a.Deref();
	long x = b.mkInteger().value;
	long y = c.mkInteger().value;
	win.getTTY().X(x + "");
	win.getTTY().Y(y + "");
	return win;
    }
}

final class f$GotoRC extends vProc3 {		// GotoXY(W,r,c)
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a, b);
	}
	vWindow win = (vWindow)a.Deref();
	long row = b.mkInteger().value;
	long col = c.mkInteger().value;
	win.getTTY().Row(win, row + "");
	win.getTTY().Col(win, col + "");
	return win;
    }
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
	vWindow win = vWindow.winArg(args);	//#%#% ClassCastException?!
	int[] a = wCoords.rectArgs(args, 4);
	for (int i = 0; i < a.length; i += 4) {
	    win.FillRectangle(a[i], a[i+1], a[i+2], a[i+3]);
	}
	return win;
    }
}

//#%#%#% CopyArea only works with one window argument
final class f$CopyArea extends vProcV {		// CopyArea(W,x1,y1,w,h,x2,y2)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	int[] a = wCoords.rectArgs(args, 4);	// ignore w2, h2
	if (a.length > 4) {	// explicit x2
	    win.CopyArea(a[0], a[1], a[2], a[3], a[4], a[5]);
	} else {
	    win.CopyArea(a[0], a[1], a[2], a[3], -win.dx, -win.dy);
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



//  mutable colors are not available

final class f$NewColor extends vProc2 {		// NewColor(W,k) always fails
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a);
	}
	return null; /*FAIL*/
    }
}

final class f$Color extends vProcV {		// Color(W,i,k,...) always fails
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);	// validate args
	int i = vWindow.argBase(args);
	if ((args.length - i) % 2 != 0) {
	    iRuntime.error(146);
	}
	for (; i < args.length; i += 2) {
	    vInteger.argVal(args, i);
	    vString.argDescr(args, i + 1);
	}
	return null; /*FAIL*/
    }
}

final class f$FreeColor extends vProcV {	// FreeColor(W,k,...) is a no-op
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);	// validate args
	for (int i = vWindow.argBase(args); i < args.length; i++) {
	    vString.argDescr(args, i);
	}
	return win;				// succeed
    }
}



//  miscellaneous AWT functions

final class f$Alert extends vProc1 {		// Alert(W) sends a beep
    public vDescriptor Call(vDescriptor a) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow());
	}
	((vWindow)a.Deref()).beep();
	return a;
    }
}

final class f$Uncouple extends vProc1 {		// Uncouple(W) calls close(W)
    public vDescriptor Call(vDescriptor a) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow());
	}
	((vWindow)a.Deref()).close();
	return a;
    }
}

final class f$WDefault extends vProc3 {		// WDefault(W,s1,s2) just fails
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a, b);
	}
	return null; /*FAIL*/
    }
}

final class f$WFlush extends vProc1 {		// WFlush(W) syncs w/ toolkit
    public vDescriptor Call(vDescriptor a) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow());
	}
	((vWindow)a.Deref()).flush();
	return a;
    }
}

final class f$WSync extends vProc1 {		// WSync(W) syncs w/ toolkit
    public vDescriptor Call(vDescriptor a) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow());
	}
	((vWindow)a.Deref()).flush();
	return a;
    }
}

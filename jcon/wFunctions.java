//  wBuiltins.java -- built-in graphics functions

package rts;

import java.awt.*;

public final class wFunctions extends iInstantiate {
    public static wFunctions self = new wFunctions();
    public vProc instantiate(String name) {
        if (name.compareTo( "f$WAttrib" ) == 0) return new f$WAttrib();
        if (name.compareTo( "f$Clone" ) == 0) return new f$Clone();
        if (name.compareTo( "f$Pending" ) == 0) return new f$Pending();
        if (name.compareTo( "f$Event" ) == 0) return new f$Event();
        if (name.compareTo( "f$Clip" ) == 0) return new f$Clip();
        if (name.compareTo( "f$Bg" ) == 0) return new f$Bg();
        if (name.compareTo( "f$Fg" ) == 0) return new f$Fg();
        if (name.compareTo( "f$Pattern" ) == 0) return new f$Pattern();
        if (name.compareTo( "f$ColorValue" ) == 0) return new f$ColorValue();
        if (name.compareTo( "f$PaletteChars" ) == 0) return new f$PaletteChars();
        if (name.compareTo( "f$PaletteColor" ) == 0) return new f$PaletteColor();
        if (name.compareTo( "f$PaletteKey" ) == 0) return new f$PaletteKey();
        if (name.compareTo( "f$Font" ) == 0) return new f$Font();
        if (name.compareTo( "f$TextWidth" ) == 0) return new f$TextWidth();
        if (name.compareTo( "f$GotoXY" ) == 0) return new f$GotoXY();
        if (name.compareTo( "f$GotoRC" ) == 0) return new f$GotoRC();
        if (name.compareTo( "f$DrawPoint" ) == 0) return new f$DrawPoint();
        if (name.compareTo( "f$DrawLine" ) == 0) return new f$DrawLine();
        if (name.compareTo( "f$DrawCurve" ) == 0) return new f$DrawCurve();
        if (name.compareTo( "f$DrawSegment" ) == 0) return new f$DrawSegment();
        if (name.compareTo( "f$DrawPolygon" ) == 0) return new f$DrawPolygon();
        if (name.compareTo( "f$FillPolygon" ) == 0) return new f$FillPolygon();
        if (name.compareTo( "f$DrawRectangle" ) == 0) return new f$DrawRectangle();
        if (name.compareTo( "f$EraseArea" ) == 0) return new f$EraseArea();
        if (name.compareTo( "f$FillRectangle" ) == 0) return new f$FillRectangle();
        if (name.compareTo( "f$CopyArea" ) == 0) return new f$CopyArea();
        if (name.compareTo( "f$DrawArc" ) == 0) return new f$DrawArc();
        if (name.compareTo( "f$FillArc" ) == 0) return new f$FillArc();
        if (name.compareTo( "f$DrawCircle" ) == 0) return new f$DrawCircle();
        if (name.compareTo( "f$FillCircle" ) == 0) return new f$FillCircle();
        if (name.compareTo( "f$DrawString" ) == 0) return new f$DrawString();
        if (name.compareTo( "f$NewColor" ) == 0) return new f$NewColor();
        if (name.compareTo( "f$Color" ) == 0) return new f$Color();
        if (name.compareTo( "f$FreeColor" ) == 0) return new f$FreeColor();
        if (name.compareTo( "f$Alert" ) == 0) return new f$Alert();
        if (name.compareTo( "f$Uncouple" ) == 0) return new f$Uncouple();
        if (name.compareTo( "f$WDefault" ) == 0) return new f$WDefault();
        if (name.compareTo( "f$WFlush" ) == 0) return new f$WFlush();
        if (name.compareTo( "f$WSync" ) == 0) return new f$WSync();
        return null;
    } // vProc instantiate(String)
}


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

final class f$Pattern extends vProc2 {		// Pattern(W, s) always fails
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow(), a);
	}
	return null; /*FAIL*/
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



final class f$PaletteChars extends vProc2 {	// PaletteChars(W, s)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.iswin()) {
	    b = a;			// W is not needed or used
	}
	cPalette p = cPalette.New(b.mkString());
	if (p == null) {
	    return null; /*FAIL*/
	} else {
	    return p.chars;
	}
    }
}

final class f$PaletteColor extends vProc3 {	// PaletteColor(W, s, c)
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	if (!a.iswin()) {
	    c = b;			// W is not needed or used
	    b = a;
	}
	cPalette p = cPalette.New(b.mkString());
	if (p == null) {
	    return null; /*FAIL*/
	}
	vString cstr = c.mkString();
	if (cstr.length() != 1) {
	    iRuntime.error(205, c);
	}
	cEntry e = p.entry[cstr.charAt(0)];
	return vString.New(e.r + "," + e.g + "," + e.b);
    }
}

final class f$PaletteKey extends vProc3 {	// PaletteKey(W, s, k)
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	if (!a.iswin()) {
	    c = b;			// W is not needed or used
	    b = a;
	}
	cPalette p = cPalette.New(b.mkString());
	if (p == null) {
	    return null; /*FAIL*/
	}
	vString s = c.mkString();
	wColor k = wColor.New(s, 1.0);	// parse string (gamma has no effect)
	return p.Key(k);
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

final class f$DrawCurve extends vProcV {	// DrawCurve(W,x,y,...)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win = vWindow.winArg(args);
	win.DrawLine(new wCoords(args));	//#%#% poor substitute for curve
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
	vWindow win = vWindow.winArg(args);
	int[] a = wCoords.rectArgs(args, 4);
	for (int i = 0; i < a.length; i += 4) {
	    win.FillRectangle(a[i], a[i+1], a[i+2], a[i+3]);
	}
	return win;
    }
}

final class f$CopyArea extends vProcV {	    // CopyArea(W1,W2,x1,y1,w,h,x2,y2)
    public vDescriptor Call(vDescriptor[] args) {
	vWindow win1, win2;
	win1 = win2 = vWindow.winArg(args);
	int b = vWindow.argBase(args);
	if (b > 0 && args.length > b && args[b].iswin()) {
	    win2 = (vWindow) args[b++].Deref();
	}

	Dimension d = win1.getCanvas().getSize();
	int x1 = (int) vInteger.argVal(args, b + 0, -win1.dx);
	int y1 = (int) vInteger.argVal(args, b + 1, -win1.dy);
	int w  = (int) vInteger.argVal(args, b + 2, d.width - x1);
	int h  = (int) vInteger.argVal(args, b + 3, d.height - y1);
	int x2 = (int) vInteger.argVal(args, b + 4, -win2.dx);
	int y2 = (int) vInteger.argVal(args, b + 5, -win2.dy);

	win2.CopyArea(win1, x1, y1, w, h, x2, y2);
	return win1;
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

final class f$Uncouple extends vProc1 {		// Uncouple(W)
    public vDescriptor Call(vDescriptor a) {
	if (!a.iswin()) {
	    return Call(iKeyword.window.getWindow());
	}
	((vWindow)a.Deref()).uncouple();
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

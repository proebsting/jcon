//  wBuiltins.java -- built-in graphics functions

package rts;

import java.awt.*;



public class wBuiltins extends iFile {

static final String PREFIX = "rts.f$";	// classname prefix for built-in funcs



void announce() {

	// graphics functions that are at least partially implemented:

	iBuiltins.declare("Alert", 1);
	iBuiltins.declare("Bg", 2);
	iBuiltins.declare("Clone", -1);
	iBuiltins.declare("Color", -1);
	iBuiltins.declare("DrawArc", -1);
	iBuiltins.declare("DrawCircle", -1);
	iBuiltins.declare("DrawLine", -1);
	iBuiltins.declare("DrawPoint", -1);
	iBuiltins.declare("DrawPolygon", -1);
	iBuiltins.declare("DrawRectangle", -1);
	iBuiltins.declare("DrawSegment", -1);
	iBuiltins.declare("DrawString", -1);
	iBuiltins.declare("EraseArea", -1);
	iBuiltins.declare("Event", 1);
	iBuiltins.declare("Fg", 2);
	iBuiltins.declare("FillArc", -1);
	iBuiltins.declare("FillCircle", -1);
	iBuiltins.declare("FillRectangle", -1);
	iBuiltins.declare("FillPolygon", -1);
	iBuiltins.declare("Font", 2);
	iBuiltins.declare("FreeColor", -1);
	iBuiltins.declare("NewColor", 2);
	iBuiltins.declare("Pending", 1);
	iBuiltins.declare("WFlush", 1);
	iBuiltins.declare("WSync", 1);

	// #%#%#%#%#%# NOT YET IMPLEMENTED:
	//
	// iBuiltins.declare("WAttrib", -1);
	// iBuiltins.declare("Clip", 5);
	// iBuiltins.declare("Pattern", 2);
	//
	// iBuiltins.declare("TextWidth", 2);
	//
	// iBuiltins.declare("GotoRC", 3);
	// iBuiltins.declare("GotoXY", 3);
	// also read(W), write(W,s...), etc.
	//
	// iBuiltins.declare("CopyArea", 8);
	// iBuiltins.declare("ReadImage", 5);
	// iBuiltins.declare("WriteImage", 6);
	// iBuiltins.declare("Pixel", 5);
	//
	// iBuiltins.declare("ColorValue", 2);
	// iBuiltins.declare("PaletteChars", 2);
	// iBuiltins.declare("PaletteColor", 3);
	// iBuiltins.declare("PaletteKey", 3);
	// iBuiltins.declare("DrawImage", 4);
	//
	// iBuiltins.declare("DrawCurve", -1);
	//
	// iBuiltins.declare("Active", 0);
	//
	// iBuiltins.declare("Lower", 1);
	// iBuiltins.declare("Raise", 1);
	//
	// iBuiltins.declare("Couple", 2);
	// iBuiltins.declare("Uncouple", 1);
	//
	// iBuiltins.declare("WDefault", 3); (might check properties?)
}



} // class wBuiltins



//------------------------------------------  miscellaneous functions follow


class f$Clone extends iValueClosure {		// Clone(W,attribs...) 
						// #%#%#% no attribs yet
	vDescriptor function(vDescriptor[] args) {
		return vWindow.winArg(args).Clone();
	}
}



class f$Pending extends iValueClosure {		// Pending(W)
	vDescriptor function(vDescriptor[] args) {
		return vWindow.winArg(args).Pending();
	}
}

class f$Event extends iValueClosure {		// Event(W)
	vDescriptor function(vDescriptor[] args) {
		return vWindow.winArg(args).Event();
	}
}



class f$Bg extends iValueClosure {		// Bg(W, s) #%#% todo: Bg(&null)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
	    	vString s = vString.argDescr(args, vWindow.argBase(args));
		return win.Bg(s);
	}
}

class f$Fg extends iValueClosure {		// Fg(W, s) #%#% todo: Fg(&null)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
	    	vString s = vString.argDescr(args, vWindow.argBase(args));
		return win.Fg(s);
	}
}

class f$Font extends iValueClosure {		// Font(W,s) #%#% do:Font(&null)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
	    	vString s = vString.argDescr(args, vWindow.argBase(args));
		return win.Font(s);
	}
}



class f$DrawPoint extends iValueClosure {	// DrawPoint(W,x,y,...)
	vDescriptor function(vDescriptor[] args) {
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

class f$DrawLine extends iValueClosure {	// DrawLine(W,x,y,...)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
		win.DrawLine(new wCoords(args));
		return win;
	}
}

class f$DrawSegment extends iValueClosure {	// DrawSegment(W,x,y,...)
	vDescriptor function(vDescriptor[] args) {
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

class f$DrawPolygon extends iValueClosure {	// DrawPolygon(W,x,y,...)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
		win.DrawPolygon(new wCoords(args));
		return win;
	}
}

class f$FillPolygon extends iValueClosure {	// FillPolygon(W,x,y,...)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
		win.FillPolygon(new wCoords(args));
		return win;
	}
}



class f$DrawRectangle extends iValueClosure {	// DrawRectangle(W,x,y,w,h,...)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
		int[] a = wCoords.rectArgs(args, 4);
		for (int i = 0; i < a.length; i += 4) {
			win.DrawRectangle(a[i], a[i+1], a[i+2], a[i+3]);
		}
		return win;
	}
}

class f$EraseArea extends iValueClosure {	// EraseArea(W,x,y,w,h,...)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
		int[] a = wCoords.rectArgs(args, 4);
		for (int i = 0; i < a.length; i += 4) {
			win.EraseArea(a[i], a[i+1], a[i+2], a[i+3]);
		}
		return win;
	}
}

class f$FillRectangle extends iValueClosure {	// FillRectangle(W,x,y,w,h,...)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
		int[] a = wCoords.rectArgs(args, 4);
		for (int i = 0; i < a.length; i += 4) {
			win.FillRectangle(a[i], a[i+1], a[i+2], a[i+3]);
		}
		return win;
	}
}



class f$DrawArc extends iValueClosure {		// DrawArc(W,x,y,w,h,t,a,...)
	vDescriptor function(vDescriptor[] args) {
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

class f$FillArc extends iValueClosure {		// FillArc(W,x,y,w,h,t,a,...)
	vDescriptor function(vDescriptor[] args) {
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



class f$DrawCircle extends iValueClosure {	// DrawCircle(W,x,y,r,t,a,...)
	vDescriptor function(vDescriptor[] args) {
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

class f$FillCircle extends iValueClosure {	// FillCircle(W,x,y,r,t,a,...)
	vDescriptor function(vDescriptor[] args) {
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




class f$DrawString extends iValueClosure {	// DrawString(W,x,y,s,...)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);	// validate args
		int b = vWindow.argBase(args);
		if ((args.length - b) % 3 != 0) {
			iRuntime.error(146);
		}
		for (int i = b; i < args.length; i += 3) {
			int x = (int) vInteger.argVal(args, i);
			int y = (int) vInteger.argVal(args, i + 1);
			String s = vString.argVal(args, i + 2);
			win.DrawString(x, y, s);
		}
		return null;				// fail
	}
}



//  mutable colors are not available

class f$NewColor extends iValueClosure {	// NewColor(W,k) always fails
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);	// validate args
	    	vString.argDescr(args, vWindow.argBase(args));
		return null;				// fail
	}
}

class f$Color extends iValueClosure {		// Color(W,i,k,...) always fails
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);	// validate args
	    	int i = vWindow.argBase(args);
		if ((args.length - i) % 2 != 0) {
			iRuntime.error(146);
		}
		for (; i < args.length; i += 2) {
			vInteger.argVal(args, i);
			vString.argDescr(args, i + 1);
		}
		return null;				// fail
	}
}

class f$FreeColor extends iValueClosure {	// FreeColor(W,k,...) is a no-op
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);	// validate args
	    	for (int i = vWindow.argBase(args); i < args.length; i++) {
			vString.argDescr(args, i);
		}
		return win;				// succeed
	}
}



//  miscellaneous AWT functions

class f$Alert extends iValueClosure {		// Alert(W) sends a beep
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);	// validate arg
		vWindow.beep();
		return win;
	}
}

class f$WFlush extends iValueClosure {		// WFlush(W) syncs w/ toolkit
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);	// validate arg
		vWindow.sync();
		return win;
	}
}

class f$WSync extends iValueClosure {		// WSync(W) syncs w/ toolkit
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);	// validate arg
		vWindow.sync();
		return win;
	}
}

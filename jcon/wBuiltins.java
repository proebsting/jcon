//  wBuiltins.java -- built-in graphics functions

package rts;

import java.awt.*;



public class wBuiltins extends iFile {

static final String PREFIX = "rts.f$";	// classname prefix for built-in funcs



void announce() {
	iBuiltins.declare("Event", 1);
	iBuiltins.declare("Fg", 2);
	iBuiltins.declare("Pending", 1);
	iBuiltins.declare("DrawRectangle", 5);
	iBuiltins.declare("FillRectangle", 5);
}



} // class wBuiltins



//------------------------------------------  miscellaneous functions follow


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

class f$Fg extends iValueClosure {		// Fg(W, s)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
	    	vString s = vString.argDescr(args, vWindow.argBase(args));
		return win.Fg(s);
	}
}


class f$DrawRectangle extends iValueClosure {	// DrawRectangle(W,x,y,w,h)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
		int[] a = vWindow.rectArgs(args);
		for (int i = 0; i < a.length; i += 4) {
			win.DrawRectangle(a[i], a[i+1], a[i+2], a[i+3]);
		}
		return args[0];
	}
}

class f$FillRectangle extends iValueClosure {	// FillRectangle(W,x,y,w,h)
	vDescriptor function(vDescriptor[] args) {
		vWindow win = vWindow.winArg(args);
		int[] a = vWindow.rectArgs(args);
		for (int i = 0; i < a.length; i += 4) {
			win.FillRectangle(a[i], a[i+1], a[i+2], a[i+3]);
		}
		return args[0];
	}
}

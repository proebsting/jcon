abstract class iClosure {

int PC;
vDescriptor retvalue;
boolean initialized;
iClosure parent;
boolean returned;		// flag to indicate resumption unnecessary

iEnv env;
vDescriptor[] arguments;

Object o;			// arbitrary storage for RTS methods

int line;
int column;
String file;

iClosure() {			// constructor
	PC = 1;
}



final void resume() {
	try {
		if (k$trace.trace != 0) {
			k$trace.trace--;
			for (iClosure p=this.parent; p != null; p=p.parent) {
				System.err.print("| ");
			}
			System.err.println("Entering  : " + this.trace());
		}
		nextval();
		if (k$trace.trace != 0) {
			for (iClosure p=this.parent; p != null; p=p.parent) {
				System.err.print("| ");
			}
			String p;
			k$trace.trace--;
			if (retvalue == null) {
				p = "Failed";
			} else if (returned) {
				p = "Returned " + retvalue.report();
			} else {
				p = "Suspended " + retvalue.report();
			}
			System.err.println(p);
		}
	} catch (iError e) {
		//  e.printStackTrace();  //#%#%#% TEMP: enable for debugging
		//#%#%# check &error here and fail or:
		e.report(this);
	}
}

abstract void nextval();

// copy() is used to return a "refreshed" copy of the closure.
iClosure copy(int PC) { iRuntime.error(901); return null; }

void closure(iEnv e, vDescriptor[] a, iClosure parent) {
	env = e;
	arguments = a;
	this.parent = parent;
}



// tfmt() -- return trace message format
//
// (this definition is for functions and procedures;
// operators typically override)

String tfmt() {
    return "$0($*)";
}
   


// trace -- format this call for traceback purposes
//
// calls this.tfmt() to get the trace format string
// substitutes procedure/function name, derived from class name, for "$0"
// substitutes argument n-1 (in Java terms) for "$n" (1 <= n <= 9)
// substitutes entire argument list for "$*"

String trace() {

    String f = this.tfmt();			// trace format
    StringBuffer b = new StringBuffer();	// output buffer

    for (int i = 0; i < f.length(); i++) {	// scan format
	char c = f.charAt(i);
	if (c == '$') {				// if $x
	    c = f.charAt(++i);			// get x

	    if (c == '0') {			// $0: proc/func name
		String s = this.getClass().getName();
		if (s.charAt(1) == '$') {	// should be [pf]$xxxxx format
		    b.append(s.substring(2));
		} else {			// no, use full class name
		    b.append(s);
		}

	    } else if (c > '0' && c <= '9') {	// $n: one arg
		b.append(getarg((int)c - '1'));

	    } else if (c == '*') {		// $*: all ags, comma-separated
		if (arguments != null && arguments.length > 0) {
		    b.append(getarg(0));
		    for (int j = 1; j < arguments.length; j++) {
			b.append(',').append(getarg(j));
		    }
		}

	    } else {
		b.append(c);			// $<unknown>: just copy
	    }

	} else {
	    b.append(c);			// not $; use as is
	}
    }
    if (parent != null && parent.file != null) {
	b.append(" from line " + parent.line + " in " + parent.file);
    }
    return b.toString();
}


//  getarg(n) -- get the image of argument n for traceback purposes

String getarg(int n) {
    if (arguments[n] == null || arguments.length <= n) {
	return "???";		// shouldn't happen, but handle it
    } else {
	return arguments[n].report();
    }
}



} // class iClosure

abstract class iClosure {


int PC;
vDescriptor retvalue;
boolean initialized;
iClosure parent;

iEnv env;
vDescriptor[] arguments;

Object o;			// arbitrary storage for RTS methods



iClosure() {			// constructor
	PC = 1;
}



final void resume() {
	try {
		nextval();
	} catch (iError e) {
		//  e.printStackTrace();  //#%#%#% TEMP: enable for debugging
		//#%#%# check &error here and fail or:
		e.report(this);
	}
}

abstract void nextval();



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
   


// trace -- report this call for traceback purposes
//
// calls this.tfmt() to get the trace format string
// substitutes procedure/function name, derived from class name, for "$0"
// substitutes argument n-1 (in Java terms) for "$n" (1 <= n <= 9)
// substitutes entire argument list for "$*"

void trace() {

    String f = this.tfmt();			// trace format
    StringBuffer b = new StringBuffer();	// output buffer

    b.append("   ");				// start with three blanks
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
		if (arguments.length > 0) {
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
    System.err.println(b);
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

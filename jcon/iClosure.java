package rts;

public abstract class iClosure {

    public iClosure parent;		// enclosing closure
    public vDescriptor[] arguments;	// argument list

    public vProc vproc;			// "creator" object;

    public int PC;			// "program counter" (initially = 1)

    public Object o;			// arbitrary storage for RTS methods
    public int oint;			// arbitrary storage for RTS methods

    public String file;			// location in source file
    public int line;
    public int column;

    public String[] names;		// created by locals() for returning
    public vVariable[] variables;	//	contents of active closure.



public final void init() {
    PC = 1;
}

public iClosure() {		// constructor
}

public void locals()	{}	// initializes name/variable arrays.

String trace_coordinate() {
    String file = "";
    String line = "";
    if (parent != null && parent.file != null) {
	file = parent.file;
	line = parent.line +"";
    }
    while (file.length() < 12) {
	file += " ";
    }
    if (file.length() > 12) {
	file = file.substring(file.length()-12);
    }
    while (line.length() < 4) {
	line = " " + line;
    }
    if (line.length() > 4) {
	line = line.substring(line.length()-4);
    }
    return file + " : " + line + " ";
}

public vDescriptor resume() {
    vDescriptor ret;

    try {
	try {
	    if (k$trace.trace != 0) {
		k$trace.trace--;
		System.err.print(trace_coordinate());
		for (iClosure p=this.parent; p != null; p=p.parent) {
		    System.err.print("| ");
		}
		System.err.println(this.trace_prototype());
	    }
	    ret = nextval();
	    if (k$trace.trace != 0) {
		System.err.print(trace_coordinate());
		for (iClosure p=this.parent; p != null; p=p.parent) {
		    System.err.print("| ");
		}
		String p = this.trace_prototype().toString();
		k$trace.trace--;
		if (ret == null) {
		    p += " failed";
		} else if (PC==0) {
		    p += " returned " + ret.report();
		} else {
		    p += " suspended " + ret.report();
		}
		System.err.println(p);
	    }
	} catch (OutOfMemoryError e) {
	    iRuntime.error(307);	// #%#% really out of memory.
	    ret = null;
	}
    } catch (iError e) {
	e.report(this);  // returns only on error->failure conversion.
	ret = null;
    }
    if (ret == null) {
	Free();
    }
    return ret;
}

public void Free() {
    iNew.FreeArgs(arguments);
    arguments = null;
    if (vproc != null) {
	vproc.cachedclosure = this;
    }
}

public abstract vDescriptor nextval();

// copy() is used to return a "refreshed" copy of the closure.
public iClosure copy(int PC) { iRuntime.error(901); return null; } //#%#% NYI?

public void closure(vDescriptor[] a, iClosure parent) {
    init();
    arguments = a;
    this.parent = parent;
}

public void closure(
	vDescriptor arg0, vDescriptor arg1, vDescriptor arg2, iClosure parent) {
    init();
    vDescriptor[] args = { arg0, arg1, arg2 };
    closure(args, parent);
}

public void closure(vDescriptor arg0, vDescriptor arg1, iClosure parent) {
    init();
    vDescriptor[] args = { arg0, arg1 };
    closure(args, parent);
}

public void closure(vDescriptor arg0, iClosure parent) {
    init();
    vDescriptor[] args = { arg0 };
    closure(args, parent);
}



// tfmt() -- return trace message format
//
// (this definition is for functions and procedures;
// operators typically override)

String tfmt() {
    return "$0($*)";
}



// trace_prototype -- format this call for traceback purposes
//
// calls this.tfmt() to get the trace format string
// substitutes procedure/function name, derived from class name, for "$0"
// substitutes argument n-1 (in Java terms) for "$n" (1 <= n <= 9)
// substitutes entire argument list for "$*"

StringBuffer trace_prototype() {

    String f = this.tfmt();			// trace format
    StringBuffer b = new StringBuffer();	// output buffer

    for (int i = 0; i < f.length(); i++) {	// scan format
	char c = f.charAt(i);
	if (c == '$') {				// if $x
	    c = f.charAt(++i);			// get x

	    if (c == '0') {			// $0: proc/func name
		String s = this.getClass().getName();
		int j = s.lastIndexOf('$');
		if (j >= 0) {			// xxx$file$yyyyy format
		    b.append(s.substring(j+1));
		} else {			// no, use full class name
		    b.append(s);
		}

	    } else if (c > '0' && c <= '9') {	// $n: one arg
		b.append(getarg((int)c - '1').toString());

	    } else if (c == '*') {		// $*: all ags, comma-separated
		if (arguments != null && arguments.length > 0) {
		    b.append(getarg(0).toString());
		    for (int j = 1; j < arguments.length; j++) {
			b.append(',').append(getarg(j).toString());
		    }
		}

	    } else {
		b.append(c);			// $<unknown>: just copy
	    }

	} else {
	    b.append(c);			// not $; use as is
	}
    }
    return b;
}

// trace() adds line and file information to trace_prototype();
String trace() {
    StringBuffer b = this.trace_prototype();
    if (parent != null && parent.file != null) {
	b.append(" from line " + parent.line + " in " + parent.file);
    }
    return b.toString();
}


//  getarg(n) -- get the image of argument n for traceback purposes

vString getarg(int n) {
    if (arguments[n] == null || arguments.length <= n) {
	return iNew.String("???");	// shouldn't happen, but handle it
    } else {
	return arguments[n].report();
    }
}



} // class iClosure

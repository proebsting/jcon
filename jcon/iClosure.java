package rts;

public abstract class iClosure {


public iEnv env;		// enclosing environment
public iClosure parent;		// enclosing closure
public vDescriptor[] arguments;	// argument list

public boolean initialized;	// first-time flag
public int PC;			// "program counter" (initially = 1)

public vDescriptor retvalue;	// value returned/suspended, or null for failure
public boolean returned;	// flag to indicate resumption unnecessary

public Object o;		// arbitrary storage for RTS methods

public String file;		// location in source file
public int line;
public int column;



public iClosure() {		// constructor
    PC = 1;
}



public final void resume() {
    try {
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
        } catch (OutOfMemoryError e) {
            iRuntime.error(307);	// #%#%# really out of memory.
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
		int j = s.indexOf('$');
		if (j >= 0) {			// xxx$yyyyy format
                    b.append(s.substring(j+1));
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

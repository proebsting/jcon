//#%#%#% later merge with iInterface?

package rts;

public class iRuntime {



//  upto(c, s) -- is any char of c contained in s?  (like Icon's upto())

static boolean upto(String c, String s) {
    for (int i = 0; i < c.length(); i++) {
    	if (s.indexOf(c.charAt(i)) >= 0) {
	    return true;
	}
    }
    return false;
}



// error(n, d) -- diagnose runtime error.
//
// never returns.
// to keep Java happy, follow error() calls with "return null" in caller.

public static void error(int n)  { error(n, null); }

public static void error(int n, vDescriptor d)
{
    throw new iError(n,d);
}



//  bomb(s) -- abort with message due to internal error

public static void bomb(String s)
{
    System.out.flush();
    System.err.println();
    System.err.println("Runtime malfunction: " + s);
    System.exit(1);
}

public static void bomb(Exception e)
{
    System.out.flush();
    System.err.println();
    System.err.println("Runtime malfunction: Java exception");
    e.printStackTrace();
    System.exit(1);
}



//  argVal(args[], i)    -- return arg i, defaulting to &null
//  argVal(args[], i, e) -- return arg i, signalling error e if missing
//
//  (both assume that arg arrays have already been dereferenced.)

public static vValue argVal(vDescriptor[] args, int index)
{
    if (args.length <= index) {
	return iNew.Null();
    } else {
    	return (vValue) args[index];
    }
}

public static vValue argVal(vDescriptor[] args, int index, int errcode)
{
    if (args.length <= index) {
    	iRuntime.error(errcode);
	return null;
    } else {
    	return (vValue) args[index];
    }
}

// argSubject(args, index) handles string defaulting in scanning functions.
// returns &subject's vString if args[index] defaulted
// o/w returns vString value.

public static vString argSubject(vDescriptor[] args, int index) {
    if (index >= args.length || args[index].deref() instanceof vNull) {
        return (vString) k$subject.self.deref();
    }
    return args[index].mkString();
}

// argPos(args, index) handles defaulting of &pos in scanning functions.
// returns &pos's value if args[index-1] defaulted,
// o/w returns 1 if args[index] defaulted,
// o/w returns argument's integer value.

public static long argPos(vDescriptor[] args, int index) {
    if ((index - args.length > 0) // both defaulted
       || ((index - args.length == 0) && (args[index-1].deref() instanceof vNull))
       || ((args[index-1].deref() instanceof vNull) && (args[index].deref() instanceof vNull))
       ) {
        return ((vInteger)k$pos.self.deref()).value;
    }
    if ((index - args.length == 0) || (args[index].deref() instanceof vNull)) {
        return 1;
    }
    return args[index].mkInteger().value;
}


public static void display(iClosure parent) {
	// do the call chain.
	for (iClosure p = parent; p != null; p = p.parent) {
		String s = p.getClass().getName();
		int j = s.lastIndexOf('$');
		if (j >= 0) {                   // xxx$file$yyyyy format
		    s = s.substring(j+1);
		}
		System.out.println(s + " local identifiers:");
		p.locals();
		if (p.names == null) {
			continue;
		}
		for (int i = 0; p.names[i] != null; i++) {
			System.out.println("   " + p.names[i] + " = " + p.variables[i].image());
		}
	}

	// do the globals
	// #%#%# not sorted....
	System.out.println();
	System.out.println("global identifiers:");
	java.util.Enumeration e = iEnv.symtab.keys();
	while (e.hasMoreElements()) {
		String s = (String) e.nextElement();
		vVariable v = (vVariable) iEnv.symtab.get(s);
		System.out.println("   " + s + " = " + v.image());
	}
}

public static void exit(int status, iClosure parent) {
	if (k$dump.dump != 0) {
		iRuntime.display(parent);
	}
	System.exit(status);
}


} // class iRuntime

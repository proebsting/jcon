//  iRuntime.java -- miscellaneous runtime methods



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

public static void error(int n)			{ error(n, null); }
public static void error(int n, vDescriptor d)	{ throw new iError(n,d); }



//  bomb(s) -- abort with message due to internal error
//
//  these methods take a very quick exit and avoid the usual shutdown code.

public static void bomb(String s) {
    k$output.file.flush();
    k$errout.file.flush();
    System.err.println();
    System.err.println("Runtime malfunction: " + s);
    System.exit(1);
}

public static void bomb(Exception e) {
    k$output.file.flush();
    k$errout.file.flush();
    System.err.println();
    System.err.println("Runtime malfunction: Java exception");
    e.printStackTrace();
    System.exit(1);
}



//  argVal(args[], i)    -- return arg i, defaulting to &null
//  argVal(args[], i, e) -- return arg i, signalling error e if missing
//
//  (both assume that arg arrays have already been dereferenced.)

public static vValue argVal(vDescriptor[] args, int index) {
    if (args.length <= index) {
	return vNull.New();
    } else {
	return (vValue) args[index];
    }
}

public static vValue argVal(vDescriptor[] args, int index, int errcode) {
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
    if (index >= args.length || args[index].isNull()) {
	return (vString) k$subject.self.deref();
    }
    return args[index].mkString();
}

// argPos(args, index) handles defaulting of &pos in scanning functions.
// returns &pos's value if args[index-1] defaulted,
// o/w returns 1 if args[index] defaulted,
// o/w returns argument's integer value.

public static long argPos(vDescriptor[] args, int index) {
    if ((index - args.length > 0)	// both defaulted
	|| ((index - args.length == 0) && args[index-1].isNull())
	|| ((args[index-1].isNull()) && (args[index].isNull()))
    ) {
	return ((vInteger)k$pos.self.deref()).value;
    }
    if ((index - args.length == 0) || args[index].isNull()) {
	return 1;
    }
    return args[index].mkInteger().value;
}



public static void display(vFile f, iClosure parent, long ancestors) {

    // do the current coexpression
    f.writes(iEnv.cur_coexp.report());
    f.newline();
    f.newline();

    // do the call chain.
    for (iClosure p = parent; p != null; p = p.parent) {
	if (ancestors-- <= 0) {
	    break;
	}
	String s = p.getClass().getName();
	int j = s.lastIndexOf('$');
	if (j >= 0) {				// xxx$file$yyyyy format
	    s = s.substring(j+1);
	}
	f.println(s + " local identifiers:");
	p.locals();
	if (p.names == null) {
	    continue;
	}
	for (int i = 0; p.names[i] != null; i++) {
	    f.println("   " + p.names[i] + " = " +
		p.variables[i].image());
	}
    }

    // do the globals
    f.newline();
    f.println("global identifiers:");
    int i = 0;
    vString[] a = new vString[iEnv.symtab.size()];
    java.util.Enumeration e = iEnv.symtab.keys();
    while (e.hasMoreElements()) {
	a[i++] = vString.New((String) e.nextElement());
    }
    iSort.sort(a);
    for (i = 0; i < a.length; i++) {
	vVariable v = (vVariable) iEnv.symtab.get(a[i].toString());
	f.println("   " + a[i] + " = " + v.image());
    }
}



//  exit()-- common termination routine for all exits except internal errors.

public static void exit(int status, iClosure parent) {
    vFile.shutdown();				// flush output files etc.
    if (k$dump.dump != 0) {			// honor &dump
	iRuntime.display(k$errout.file, parent, 1000000);
    }
    System.exit(status);			// shut down
}


} // class iRuntime

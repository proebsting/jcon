//  iRuntime.java -- miscellaneous runtime methods



package rts;

public final class iRuntime {



//  arg(args[], i)    -- return arg i, defaulting to &null
//  arg(args[], i, e) -- return arg i, signalling error e if missing

private static vNull vnull = vNull.New();

public static vDescriptor arg(vDescriptor[] args, int index) {
    if (args.length <= index) {
	return vnull;
    } else {
	return args[index];
    }
}



//  display(f) -- display global variables (only) on f

public static void display(vFile f) {

    // do the current coexpression
    f.writes(iEnv.cur_coexp.report());
    
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
	f.println("   " + a[i] + " = " + v.Deref().report());
    }
}



//  error(n, d) -- diagnose runtime error.
//
//  never returns.
//  to keep Java happy, follow error() calls with "return null" in caller.

public static void error(int n)			{ error(n, null); }
public static void error(int n, vDescriptor d)	{ throw new iError(n,d); }



//  bomb(s) -- abort with message due to internal error
//
//  these methods take a very quick exit and avoid the usual shutdown code.

public static void bomb(String s) {
    iKeyword.output.file().flush();
    iKeyword.errout.file().flush();
    System.err.println();
    System.err.println("Runtime malfunction: " + s);
    (new Exception()).printStackTrace();
    System.exit(1);
}

public static void bomb(Throwable t) {
    iKeyword.output.file().flush();
    iKeyword.errout.file().flush();
    System.err.println();
    System.err.println("Runtime malfunction: Java exception");
    t.printStackTrace();
    System.exit(1);
}

public static void bomb(String s, Throwable t) {
    iKeyword.output.file().flush();
    iKeyword.errout.file().flush();
    System.err.println();
    System.err.println("Runtime malfunction: " + s);
    t.printStackTrace();
    System.exit(1);
}



//  exit()-- common termination routine for all exits except internal errors.

public static void exit(int status) {
    if (iKeyword.dump.check()) {		// honor &dump
	display(iKeyword.errout.file());
    }
    vFile.shutdown();				// flush output files etc.
    System.exit(status);			// shut down
}


} // class iRuntime

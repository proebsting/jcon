package rts;

public final class iInterface {

static String name;
static String args[];


public static vList marshal( vDescriptor[] args, int len) {
    vDescriptor[] a = new vDescriptor[len];

    int varlen = args.length - len + 1;
    if (varlen < 0) {
	varlen = 0;
    }
    vDescriptor[] varray = new vDescriptor[varlen];
    for (int i = 0; i < varlen; i++) {
	varray[i] = args[len+i-1];
    }
    return vList.New(varray);
}



static java.util.Hashtable fileTable = new java.util.Hashtable();
public static void link(iFile f, String name) {
    if (fileTable.containsKey(name)) {
	return;
    }
    f.link();
    fileTable.put(name, f);
}


// program startup

public static void start(String[] args, String name, iExecutable exe) {
    iInterface.args = args;
    iInterface.name = name;
    exe.start();
}

public static void start(iFile[] files, String[] names) {
    java.util.Enumeration e;

    for (int i = 0; i < files.length; i++) {
	link(files[i], names[i]);
    }

    e = fileTable.elements();
    while (e.hasMoreElements()) {
	iFile f = (iFile) e.nextElement();
	f.unresolved();
    }
    iEnv.declareInvoke("main");

    iOperators.announce();		// install operators
    iBuiltins.announce();		// install built-in functions
    wBuiltins.announce();		// install built-in graphics functions
    iKeyword.announce();		// install keywords

    e = fileTable.elements();
    while (e.hasMoreElements()) {
	iFile f = (iFile) e.nextElement();
	f.declare();
    }

    if (System.getProperty("-u") != null) {
        e = fileTable.elements();
        while (e.hasMoreElements()) {
	    iFile f = (iFile) e.nextElement();
	    f.announce_unresolved();
        }
	return;
    }

    e = fileTable.elements();
    while (e.hasMoreElements()) {
	iFile f = (iFile) e.nextElement();
	f.resolve();
    }

    iKeyword.progname.set(vString.New(name));

    // find main()
    vDescriptor m = iEnv.resolve("main");
    if (m == null || !(m.Deref() instanceof vProc)) {
	System.err.println();
	System.err.println("Run-time error 117 in startup code");
	System.err.println("missing main procedure");
	System.exit(1);	// not iRuntime.exit: main coexp not set up
    }
    vProc p = (vProc) m.Deref();

    // create arglist only if main() declares a parameter;
    // this keeps List serial numbers in sync with v9
    vDescriptor a = vNull.New();
    vDescriptor[] argArray;
    if (! (p instanceof vProc0)) {
	vDescriptor[] vargs = new vDescriptor[args.length];
	for (int i = 0; i < args.length; i++) {
	    vargs[i] = vString.New(args[i]);
	}
	a = vList.New(vargs);
	argArray = new vDescriptor[1];
	argArray[0] = a;
    } else {
	argArray = new vDescriptor[0];
    }

    // initialize &trace from $TRACE
    try {
	String s = System.getProperty("TRACE", "0");
	long t = Long.parseLong(s);
	iKeyword.trace.set(t);
    } catch (Throwable t) {
	iKeyword.trace.set(0);
    }

    iEnv.cur_coexp = vCoexp.New(new vProcClosure(p, argArray));
    iKeyword.main.set(iEnv.cur_coexp);
    iKeyword.current.set(iEnv.cur_coexp);
    iEnv.cur_coexp.lock.V();

    try {
	iKeyword.time.reset();				// zero &time
        iEnv.cur_coexp.run();
	iRuntime.exit(0);
    } catch (iError err) {
	err.report();
	iRuntime.bomb("iError.report() returned");
    } catch (Throwable t) {
	iRuntime.bomb(t);
    };

}



} // class iInterface

package rts;

public class iInterface {



public static void init() {    // general runtime initialization
    (new iBuiltins()).announce();	// install built-in functions
    (new wBuiltins()).announce();	// install built-in graphics functions
    (new iKeywords()).announce();	// install keywords
}

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
public static void link(String name) {
    if (fileTable.containsKey(name)) {
	return;
    }
    try {
	Class c = Class.forName(name);
	Object o = c.newInstance();
	iFile file = (iFile) o;
	file.link();
	fileTable.put(name, file);
    } catch (Throwable e) {
	System.err.println();
	System.err.println("startup error linking " + name + ": " + e);
	String s = e.getMessage();
	if (s != null) {
	    System.err.println("   " + s);
	}
	if (e instanceof ExceptionInInitializerError) {
	    e = ((ExceptionInInitializerError) e).getException();
	}
	e.printStackTrace();
	iRuntime.exit(1);
    }
}

public static void start(String[] filenames, String[] args, String name) {
    java.util.Enumeration e;

    for (int i = 0; i < filenames.length; i++) {
	link(filenames[i]);
    }
    e = fileTable.elements();
    while (e.hasMoreElements()) {
	iFile file = (iFile) e.nextElement();
	file.unresolved();
    }
    iEnv.declareInvoke("main");

    iInterface.init();
    e = fileTable.elements();
    while (e.hasMoreElements()) {
	iFile file = (iFile) e.nextElement();
	file.declare();
    }
    e = fileTable.elements();
    while (e.hasMoreElements()) {
	iFile file = (iFile) e.nextElement();
	file.resolve();
    }

    k$progname.self.set(vString.New(name));
    vDescriptor m = iEnv.resolve("main");
    if (m == null || !(m.Deref() instanceof vProc)) {
	System.err.println();
	System.err.println("Run-time error 117 in startup code");
	System.err.println("missing main procedure");
	System.exit(1);	// not iRuntime.exit: main coexp not set up
    }
    vProc p = (vProc) m.Deref();
    vDescriptor[] vargs = new vDescriptor[args.length];
    for (int i = 0; i < args.length; i++) {
	vargs[i] = vString.New(args[i]);
    }
    vDescriptor[] v = { vList.New(vargs) };

    // initialize &trace from $TRACE
    try {
	long t = Long.parseLong(System.getProperty("TRACE", "0"));
	k$trace.self.Assign(vInteger.New(t));
    } catch (Exception x) {
	// nothing
    }

    k$time.reset();				// zero &time

    //#%#%#%# need to set up a coexpression, but for now...
    try {
	p.Call(v);
	iRuntime.exit(0);
    } catch (iError err) {
	err.printStackTrace();
	err.report();
	iRuntime.bomb("iError.report() returned");
    } catch (Throwable t) {
	iRuntime.bomb(t);
    };

    //#%#% iEnv.main = new vCoexp(p, v);
    iEnv.cur_coexp = iEnv.main;
    iEnv.main.lock.V();
    iEnv.main.run();
    iRuntime.exit(0);
}



} // class iInterface

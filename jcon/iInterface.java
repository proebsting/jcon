package rts;

public class iInterface {

    public static void init() {    // general runtime initialization
	(new iBuiltins()).announce();
	(new wBuiltins()).announce();
	(new iKeywords()).announce();
	(new iOperators()).announce();
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
	return iNew.List(varray);
    }

    static java.util.Hashtable fileTable = new java.util.Hashtable();
    public static void link(String name) {
	if (fileTable.containsKey(name)) {
	    return;
	}
	Class c = null;
	try {
	    c = Class.forName(name);
	} catch (ClassNotFoundException e) {
	    System.err.println();
	    System.err.println("linking error in startup code: " +
		e.toString());
	    System.err.println("error linking file " + name);
	    iRuntime.exit(1, null);
	}
	Object o = null;
	try {
	    o = c.newInstance();
	} catch (InstantiationException e) {
	    System.err.println();
	    System.err.println("linking error in startup code: " +
		e.toString());
	    System.err.println("error linking file " + name);
	    iRuntime.exit(1, null);
	} catch (IllegalAccessException e) {
	    System.err.println();
	    System.err.println("linking error in startup code: " +
		e.toString());
	    System.err.println("error linking file " + name);
	    iRuntime.exit(1, null);
	}
	iFile file = (iFile) o;
	file.link();
	fileTable.put(name, file);
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

	k$progname.name = name;
	vDescriptor m = iEnv.resolve("main");
	if (m == null || !(m.deref() instanceof vProc)) {
	    System.err.println();
	    System.err.println("Run-time error 117 in startup code");
	    System.err.println("missing main procedure");
	    iRuntime.exit(1, null);
	}
	vProc p = (vProc) m.deref();
	vDescriptor[] v;
	if (p.args == 0) {
	    v = new vDescriptor[0];
	} else {
	    v = new vDescriptor[1];
	    vDescriptor[] vargs = new vDescriptor[args.length];
	    for (int i = 0; i < args.length; i++) {
		vargs[i] = iNew.String(args[i]);
	    }
	    v[0] = iNew.List(vargs);
	}
	iClosure closure = p.instantiate(v, null);
	k$time.reset();			// zero &time
	iEnv.main = new vCoexp(closure);
	iEnv.cur_coexp = iEnv.main;
	iEnv.main.lock.V();
	iEnv.main.run();
	iRuntime.exit(0, null);
    }

    public static iClosure Instantiate(vDescriptor f, vDescriptor[] args, iClosure parent) {
	long len = args.length > 0 ? (long) args.length : 1;  // due to proc(x,0)'s defn.
	vValue fn = f.Proc(len);
	if (fn == null) {
	    return new iErrorClosure(f.deref(), args, parent);
	}
	return fn.instantiate(args, parent);
    }
}

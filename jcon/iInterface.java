package rts;

public class iInterface {

	public static void init() {    // general runtime initialization
		(new iBuiltins()).announce();
		(new iKeywords()).announce();
		(new iOperators()).announce();
	}

	public static vDescriptor[] marshall(
	    vDescriptor[] args, int len, boolean varargs) {

		vDescriptor[] a = new vDescriptor[len];

		int max = varargs ? len-1 : len;
		for (int i = 0; i < max; i++) {
			if (i < args.length) {
				a[i] = args[i].deref();
			} else {
				a[i] = iNew.Null();
			}
		}
		if (varargs) {
			int varlen = args.length - len + 1;
			if (varlen < 0) {
				varlen = 0;
			}
			vDescriptor[] varray = new vDescriptor[len];
			for (int i = 0; i < varlen; i++) {
				varray[i] = a[len+i-1];
			}
			a[len] = iNew.List(varray);
		}
		return a;
	}

	static java.util.Hashtable fileTable = new java.util.Hashtable();
	static void announce(String name) {
		if (fileTable.containsKey(name)) {
			return;
		}
		try {
			Class c = Class.forName(name);
			Object o = c.newInstance();
			iFile file = (iFile) o;
			file.announce();
			fileTable.put(name, file);
		} catch (ClassNotFoundException e) {
			System.err.println();
			System.err.println("linking error in startup code");
			System.err.println("error linking file " + name);
			iRuntime.exit(1, null);
		} catch (InstantiationException e) {
			System.err.println();
			System.err.println("linking error in startup code");
			System.err.println("error linking file " + name);
			iRuntime.exit(1, null);
		} catch (IllegalAccessException e) {
			System.err.println();
			System.err.println("linking error in startup code");
			System.err.println("error linking file " + name);
			iRuntime.exit(1, null);
		}
	}

	public static void start(String[] filenames, String[] args, String name) {
		for (int i = 0; i < filenames.length; i++) {
			announce(filenames[i]);
		}
		iInterface.init();
		java.util.Enumeration e = fileTable.elements();
		while (e.hasMoreElements()) {
			iFile file = (iFile) e.nextElement();
			file.link();
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

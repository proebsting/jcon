class iInterface {

	static void init(iEnv env) {	// general runtime initialization
		(new iBuiltins()).announce(env);
		(new iKeywords()).announce(env);
		(new iOperators()).announce(env);
	}

	static vDescriptor[] marshall(
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

	static void start(iFile[] files, String[] args) {
		iEnv env = iNew.Env();
		for (int i = 0; i < files.length; i++) {
			files[i].announce(env);
		}
		iInterface.init(env);
		vDescriptor p = env.resolve("main").deref();
		vDescriptor[] vargs = new vDescriptor[args.length];
		for (int i = 0; i < args.length; i++) {
			vargs[i] = iNew.String(args[i]);
		}
		vDescriptor list = iNew.List(vargs);
		vDescriptor[] v = { list };
		iClosure closure = p.instantiate(v, null);
		vCoexp coexp = new vCoexp(closure);
		env.cur_coexp = coexp;
		coexp.lock.V();
		coexp.run();
		System.exit(0);
	}
}

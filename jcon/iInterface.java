class iInterface {

	static void init(iEnv env) {	// general runtime initialization
		(new iBuiltins()).announce(env);
		(new iKeywords()).announce(env);
		(new iOperators()).announce(env);
	}

	static vDescriptor[] marshall(
	    vDescriptor[] args, int len, boolean accumulate) {

		vDescriptor[] a = new vDescriptor[len];

		// KLUDGE: do not handle accumulate yet.
		for (int i = 0; i < len; i++) {
			if (i < args.length) {
				a[i] = args[i].deref();
			} else {
				a[i] = iNew.Null();
			}
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

class iInterface {

	static void init(iEnv env) {	// general runtime initialization
		(new iBuiltins()).announce(env);
		(new iKeywords()).announce(env);
		(new iOperators()).announce(env);
	}

	static vDescriptor[] main_args(String[] args) {
		vDescriptor[] vargs = new vDescriptor[args.length];
		for (int i = 0; i < args.length; i++) {
			vargs[i] = iNew.String(args[i]);
		}
		return vargs;
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
}

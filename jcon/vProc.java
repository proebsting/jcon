class vProc extends vValue {
	
	Class proc;
	iEnv env;

	vProc(Class c, iEnv e) {
		env = e;
		proc = c;
	}

	vValue deref() {
		return this;
	}

	String image() {
		String s = proc.getName();
		if (s.charAt(0) == 'f') {
			return "function " + s.substring(2);
		} else {
			return "procedure " + s.substring(2);
		}
	}

	iClosure instantiate(vDescriptor[] args) {
		iClosure c = null;

		try {
			c = (iClosure) proc.newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		c.closure(env, args);
		return c;
	}

}

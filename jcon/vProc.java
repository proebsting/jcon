package rts;

public class vProc extends vValue {
	
	Class proc;
	iEnv env;

	vProc(Class c, iEnv e) {
		env = e;
		proc = c;
	}

	public vValue deref() {
		return this;
	}

	int rank() {
		return 80;		// procedures rank after co-expressions
	}

	String image() {

		String s = proc.getName();
		int j = s.indexOf('$');
		if (j >= 0) {
                    j = j + 1;
                } else {
                    j = 0;
		}

		if (s.charAt(0) == 'p') {
			return "procedure " + s.substring(j);
		} else {
			return "function " + s.substring(j);
		}
	}

	String type() { 
		return "procedure";
	}

	public iClosure instantiate(vDescriptor[] args, iClosure parent) {
		iClosure c = null;

		try {
			c = (iClosure) proc.newInstance();
		} catch (InstantiationException e) {
			iRuntime.bomb(e);
		} catch (IllegalAccessException e) {
			iRuntime.bomb(e);
		}
		c.closure(env, args, parent);
		return c;
	}

}

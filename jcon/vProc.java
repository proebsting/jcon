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

	String image() {

		String s = proc.getName();

		//#%#%# this code has guilty knowledge of internals of others
		if (s.charAt(0) == 'p') {
			return "procedure " + s.substring(2);
		} else {
			return "function " + s.substring(6);
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

abstract class iClosure {

	int PC;
	vDescriptor retvalue;
	boolean initialized;
	iClosure parent;

	iEnv env;
	vDescriptor[] arguments;

	Object o;		// arbitrary storage for RTS methods

	iClosure() {
		PC = 1;
	}

	final void resume() {
		try {
			nextval();
		} catch (iError e) {
			//#%#%# check &error here and fail or:
			e.report(this);
		}
	}

	abstract void nextval();

	void closure(iEnv e, vDescriptor[] a, iClosure parent) {
		env = e;
		arguments = a;
		this.parent = parent;
	}


	// trace -- report this call for traceback purposes

	void trace() {
	    System.err.print("   " + this.getClass().getName() + "(");
	    if (arguments != null && arguments.length > 0) {
		printarg(0);
		for (int i = 1; i < arguments.length; i++) {
		    System.err.print(",");
		    printarg(i);
		}
		System.err.println(")");
	    }
	}

	void printarg(int n) {
	    if (arguments[n] == null) {
	    	System.err.print("???");	//#%#%# shouldn't happen
	    } else {
	    	System.err.print(arguments[n].report());
	    }
	}



} // class iClosure

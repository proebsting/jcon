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
			e.error();
		}
	}

	abstract void nextval();

	void closure(iEnv e, vDescriptor[] a, iClosure parent) {
		env = e;
		arguments = a;
		this.parent = parent;
	}

} // class iClosure

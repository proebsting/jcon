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

	abstract void resume();

	void closure(iEnv e, vDescriptor[] a, iClosure parent) {
		env = e;
		arguments = a;
		this.parent = parent;
	}
}

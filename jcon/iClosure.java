abstract class iClosure {

	int PC;
	vDescriptor retvalue;
	boolean initialized;

	iEnv env;
	vDescriptor[] arguments;

	Object o;		// arbitrary storage for RTS methods

	iClosure() {
		PC = 1;
	}

	abstract void resume();

	void closure(iEnv e, vDescriptor[] a) {
		env = e;
		arguments = a;
	}
}

abstract class iClosure {

	int PC;
	vDescriptor retvalue;
	boolean initialized;

	iEnv env;
	vDescriptor[] arguments;

	Object o;		// arbitrary storage for RTS methods

	abstract void resume();

	void closure(iEnv e, vDescriptor[] a) {
		PC = 1;
		env = e;
		arguments = a;
	}
}

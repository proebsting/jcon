package rts;

public abstract class iUnaryFunctionClosure extends iClosure {

	// Class for simple binary operations that return at most one value
	// but do not want their arguments dereferenced

	vDescriptor argument;

	public void nextval() {
		if (PC == 1) {
			argument = argument.deref();
			retvalue = function(argument);
			returned = true;
			PC = 0;
		} else {
			retvalue = null;
		}
	}

	abstract vDescriptor function(vDescriptor arg);

	void closure(vDescriptor[] args, iClosure parent) {
		argument = args[0];
		this.parent = parent;
	}
	void closure(vDescriptor arg0, iClosure parent) {
		argument = arg0;
		this.parent = parent;
	}

	String getarg(int n) {
		switch (n) {
		default: return "???";
		case 0 : return argument.report();
		}
	}
}

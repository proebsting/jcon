package rts;

public abstract class iBinaryFunctionClosure extends iClosure {

	// Class for simple binary operations that return at most one value
	// but do not want their arguments dereferenced

	vDescriptor argument0, argument1;

	public void nextval() {
		if (PC == 1) {
			argument0 = argument0.deref();
			argument1 = argument1.deref();
			retvalue = function(argument0, argument1);
			returned = true;
			PC = 0;
		} else {
			retvalue = null;
		}
	}

	abstract vDescriptor function(vDescriptor arg0, vDescriptor arg1);

	void closure(vDescriptor[] args, iClosure parent) {
		argument0 = args[0];
		argument1 = args[1];
		this.parent = parent;
	}
	void closure(vDescriptor arg0, vDescriptor arg1, iClosure parent) {
		argument0 = arg0;
		argument1 = arg1;
		this.parent = parent;
	}

	String getarg(int n) {
		switch (n) {
		default: return "???";
		case 0 : return argument0.report();
		case 1 : return argument1.report();
		}
	}
}

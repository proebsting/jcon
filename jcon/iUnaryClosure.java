package rts;

public abstract class iUnaryClosure extends iClosure {

	vDescriptor argument;

	abstract vDescriptor function(vDescriptor arg);

	public vDescriptor call(vDescriptor arg, iClosure parent) {
		closure(arg, parent);
		resume();
		return retvalue;
	}

	public void closure(vDescriptor[] args, iClosure parent) {
		init();
		argument = args[0];
		this.parent = parent;
	}
	public void closure(vDescriptor arg0, iClosure parent) {
		init();
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

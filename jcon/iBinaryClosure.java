package rts;

public abstract class iBinaryClosure extends iClosure {

	vDescriptor argument0, argument1;

	abstract vDescriptor function(vDescriptor arg0, vDescriptor arg1);

	public vDescriptor call(vDescriptor arg0, vDescriptor arg1, iClosure parent) {
		closure(arg0, arg1, parent);
		resume();
		return retvalue;
	}

	public void closure(vDescriptor[] args, iClosure parent) {
		init();
		argument0 = args[0];
		argument1 = args[1];
		this.parent = parent;
	}
	public void closure(vDescriptor arg0,vDescriptor arg1,iClosure parent) {
		init();
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

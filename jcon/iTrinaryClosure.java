package rts;

public abstract class iTrinaryClosure extends iClosure {

	vDescriptor argument0, argument1, argument2;

	abstract vDescriptor function();

	abstract public vDescriptor call(vDescriptor arg0, vDescriptor arg1, vDescriptor arg2, iClosure parent); 

	public void closure(vDescriptor[] args, iClosure parent) {
		init();
		argument0 = args[0];
		argument1 = args[1];
		argument2 = args[2];
		this.parent = parent;
	}
	public void closure(vDescriptor arg0,vDescriptor arg1, vDescriptor arg2, iClosure parent) {
		init();
		argument0 = arg0;
		argument1 = arg1;
		argument2 = arg2;
		this.parent = parent;
	}

	vString getarg(int n) {
		switch (n) {
		default: return iNew.String("???");
		case 0 : return argument0.report();
		case 1 : return argument1.report();
		case 2 : return argument2.report();
		}
	}
}

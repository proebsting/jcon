package rts;

public abstract class iBinaryClosure extends iClosure {

    vDescriptor argument0, argument1;



abstract vDescriptor function();

abstract public vDescriptor call(
    vDescriptor arg0, vDescriptor arg1, iClosure parent);



public void closure(vDescriptor[] args, iClosure parent) {
    init();
    argument0 = args[0];
    argument1 = args[1];
    this.parent = parent;
}

public void closure(vDescriptor arg0, vDescriptor arg1, iClosure parent) {
    init();
    argument0 = arg0;
    argument1 = arg1;
    this.parent = parent;
}

vString getarg(int n) {
    switch (n) {
	default: return iNew.String("???");
	case 0 : return argument0.report();
	case 1 : return argument1.report();
    }
}



} // class iBinaryClosure

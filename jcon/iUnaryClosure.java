package rts;

public abstract class iUnaryClosure extends iClosure {

    vDescriptor argument;



abstract vDescriptor function(vDescriptor arg);

public vDescriptor call(vDescriptor arg, iClosure parent) {
    closure(arg, parent);
    try {
	try {
	    return nextval();
	} catch (OutOfMemoryError e) {
	    iRuntime.error(307);	// out of memory
	    return null;
	}
    } catch (iError e) {
	e.report(this);		// returns only on error->failure conversion
	return null;
    }
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

vString getarg(int n) {
    switch (n) {
	default: return vString.New("???");
	case 0 : return argument.report();
    }
}


} // class iUnaryClosure

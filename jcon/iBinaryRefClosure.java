// Class for simple binary operations that return at most one value
// but do not want their arguments dereferenced

package rts;

public abstract class iBinaryRefClosure extends iBinaryClosure {



public vDescriptor nextval() {
    if (PC == 1) {
	vDescriptor v = function();
	PC = 0;
	return v;
    } else {
	return null;
    }
}

public vDescriptor call(vDescriptor arg0, vDescriptor arg1, iClosure parent) {
    argument0 = arg0;
    argument1 = arg1;
    this.parent = parent;
    try {
	try {
	    return function();
	} catch (OutOfMemoryError e) {
	    iRuntime.error(307);	// #%#% really out of memory.
	    return null;
	}
    } catch (iError e) {
	e.report(this);  // returns only on error->failure conversion.
	return null;
    }
}



} // class iBinaryRefClosure

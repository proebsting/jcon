// Class for simple functions that return at most one value.
// Arguments are dereferenced.

package rts;

public abstract class iValueClosure extends iFunctionClosure {



abstract vDescriptor function(vDescriptor[] args);

public vDescriptor nextval() {
    if (PC == 1) {
	for (int i = 0; i < arguments.length; i++) {
	    arguments[i] = arguments[i].deref();
	}
	vDescriptor v = function(arguments);
	PC = 0;
	return v;
    } else {
	return null;
    }
}



} // class iValueClosure

// Class for simple binary operations that return at most one value
// but do not want their arguments dereferenced

package rts;

public abstract class iUnaryRefClosure extends iUnaryClosure {



public vDescriptor nextval() {
    if (PC == 1) {
	vDescriptor v = function(argument);
	PC = 0;
	return v;
    } else {
	return null;
    }
}



} // class iUnaryRefClosure

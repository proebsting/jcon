// Class for simple binary operations that return at most one value
// but do not want their arguments dereferenced

package rts;

public abstract class iUnaryValueClosure extends iUnaryClosure {



public vDescriptor nextval() {
    if (PC == 1) {
	argument = argument.deref();
	vDescriptor v = function(argument);
	PC = 0;
	return v;
    } else {
	return null;
    }
}



}  // class iUnaryValueClosure

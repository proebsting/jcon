package rts;

public abstract class iUnaryRefClosure extends iUnaryClosure {

	// Class for simple binary operations that return at most one value
	// but do not want their arguments dereferenced

	public vDescriptor nextval() {
		if (PC == 1) {
			vDescriptor v = function(argument);
			PC = 0;
			return v;
		} else {
			return null;
		}
	}
}

package rts;

public abstract class iBinaryRefClosure extends iBinaryClosure {

	// Class for simple binary operations that return at most one value
	// but do not want their arguments dereferenced

	public vDescriptor nextval() {
		if (PC == 1) {
			vDescriptor v = function(argument0, argument1);
			returned = true;
			PC = 0;
			return v;
		} else {
			return null;
		}
	}
}

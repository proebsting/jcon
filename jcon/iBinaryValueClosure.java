package rts;

public abstract class iBinaryValueClosure extends iBinaryClosure {

	// Class for simple binary operations that return at most one value
	// and want their arguments dereferenced

	public vDescriptor nextval() {
		if (PC == 1) {
			argument0 = argument0.deref();
			argument1 = argument1.deref();
			vDescriptor v = function(argument0, argument1);
			returned = true;
			PC = 0;
			return v;
		} else {
			return null;
		}
	}
}

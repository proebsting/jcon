package rts;

public abstract class iBinaryValueClosure extends iBinaryClosure {

	// Class for simple binary operations that return at most one value
	// but do not want their arguments dereferenced

	public void nextval() {
		if (PC == 1) {
			argument0 = argument0.deref();
			argument1 = argument1.deref();
			retvalue = function(argument0, argument1);
			returned = true;
			PC = 0;
		} else {
			retvalue = null;
		}
	}
}

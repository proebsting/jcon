package rts;

public abstract class iUnaryValueClosure extends iUnaryClosure {

	// Class for simple binary operations that return at most one value
	// but do not want their arguments dereferenced

	public void nextval() {
		if (PC == 1) {
			argument = argument.deref();
			retvalue = function(argument);
			returned = true;
			PC = 0;
		} else {
			retvalue = null;
		}
	}
}

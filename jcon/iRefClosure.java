package rts;

public abstract class iRefClosure extends iFunctionClosure {

	// Class for simple operations that return at most one value
	// but do not want their arguments dereferenced

	public void nextval() {
		if (PC == 1) {
			retvalue = function(arguments);
			returned = true;
			PC = 0;
		} else {
			retvalue = null;
		}
	}

	abstract vDescriptor function(vDescriptor[] args);
}

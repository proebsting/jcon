package rts;

public abstract class iRefClosure extends iFunctionClosure {

	// Class for simple operations that return at most one value
	// but do not want their arguments dereferenced

	public vDescriptor nextval() {
		vDescriptor v;

		if (PC == 1) {
			v = function(arguments);
			returned = true;
			PC = 0;
			return v;
		} else {
			return null;
		}
	}

	abstract vDescriptor function(vDescriptor[] args);
}

abstract class iRefClosure extends iClosure {

	// Class for simple operations that return at most one value
	// but do not want their arguments dereferenced

	void nextval() {
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

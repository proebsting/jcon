package rts;

public abstract class iFunctionClosure extends iClosure {

	// Class for simple functions that return at most one value.
	// Arguments are dereferenced.

	void nextval() {
		if (PC == 1) {
			for (int i = 0; i < arguments.length; i++) {
				arguments[i] = arguments[i].deref();
			}
			retvalue = function(arguments);
			returned = true;
			PC = 0;
		} else {
			retvalue = null;
		}
	}

	abstract vDescriptor function(vDescriptor[] args);
}

package rts;

public abstract class iValueClosure extends iFunctionClosure {

	// Class for simple functions that return at most one value.
	// Arguments are dereferenced.

	public vDescriptor nextval() {
		if (PC == 1) {
			for (int i = 0; i < arguments.length; i++) {
				arguments[i] = arguments[i].deref();
			}
			vDescriptor v = function(arguments);
			returned = true;
			PC = 0;
			return v;
		} else {
			return null;
		}
	}

	abstract vDescriptor function(vDescriptor[] args);
}

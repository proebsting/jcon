// Class for simple operations that return at most one value
// but do not want their arguments dereferenced

package rts;

public abstract class iRefClosure extends iFunctionClosure {



abstract vDescriptor function(vDescriptor[] args);

public vDescriptor nextval() {
    vDescriptor v;

    if (PC == 1) {
	v = function(arguments);
	PC = 0;
	return v;
    } else {
	return null;
    }
}



} // class iRefClosure

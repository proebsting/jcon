//  iError -- an Icon run-time error, thrown as an exception

package rts;


public class iError extends Error {

    int num;				// error number
    vDescriptor desc;			// offending value


iError(int num, vDescriptor desc) {	// constructor
    this.num = num;
    this.desc = desc;
}


void report(iClosure c) {		// print message and abort

    // if &error is zero, issue message and abort
    // if &error is not zero, decrement it and set other error keywords
    if (k$error.error != 0) {
	k$error.error--;
	k$errornumber.number = iNew.Integer(this.num);
	k$errortext.text = iNew.String(iRunerr.text(num));
	k$errorvalue.value = (this.desc == null) ? null : this.desc.deref();
	return;
    }

    System.out.flush();
    System.err.println();
    System.err.println("Run-time error " + num);
    if (c.parent != null) {
	if (c.parent.file != null) {
	    System.err.println(
		"File " + c.parent.file + "; Line " + c.parent.line);
	}
    }
    System.err.println(iRunerr.text(num));
    if (desc != null) {
	System.err.println("offending value: " + desc.report());
    }
    System.err.println("Traceback:");

    try {
	traceback(c, iConfig.MaxTraceback);	// traceback to limited depth
    } catch (OutOfMemoryError e) {
	System.err.println("   [out of memory; traceback truncated]");
    }
    iRuntime.exit(1, c.parent);
}


//  traceback(c, n) -- trace back up to n previous closure levels

static void traceback(iClosure c, int n) {

    if (c == null) {			// if end of the line
	return;
    }

    if (n == 0) {			// if recursion limit reached
	System.err.println("   ...");
	return;
    }

    traceback(c.parent, n - 1);		// print ancestry first

    System.err.println("   " + c.trace());  // report this closure
}




} // class iError



//  iErrorClosure exists just to raise instantiation errors

class iErrorClosure extends iValueClosure {

    vValue value;

    iErrorClosure(vValue value, vDescriptor[] args, iClosure parent) {
	init();
	this.value = value;
	this.arguments = args;
	this.parent = parent;
    }

    vDescriptor function(vDescriptor[] args) {
	iRuntime.error(106, value);	// procedure or integer expected
	return null;
    }

    String trace()	{ return value.report() + super.trace(); }

    String tfmt()	{ return "($*)"; }	// format for super.trace()
}

//  iError -- an Icon run-time error, thrown as an exception


class iError extends Error {

    int num;				// error number
    vDescriptor desc;			// offending value


iError(int num, vDescriptor desc) {	// constructor
    this.num = num;
    this.desc = desc;
}


void report(iClosure c) {		// print message and abort

    //#%#% if &error is zero, issue message and abort
    //#%#% if &error is not zero, decrement it and set other error keywords
    //#%#% throw exception for handling as failure at proper point

    System.out.flush();
    System.err.println("Run-time error " + num);
    System.err.println(iRunerr.text(num));
    if (desc != null) {
        System.err.println("offending value: " + desc.report());
    }
    System.err.println("Traceback:");
    traceback(c, 100);		// limit to 100 levels  //#%#%# 100?
    System.exit(1);
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

    c.trace();				// report this closure
}




} // class iError

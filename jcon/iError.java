//  iError -- an Icon run-time error, thrown as an exception

package rts;

public class iError extends Error {

    int num;				// error number
    vDescriptor desc;			// offending value



iError(int num, vDescriptor desc) {	// constructor
    this.num = num;
    this.desc = desc;
}



void report() {				// print message and abort

    // if &error is zero, issue message and abort
    // if &error is not zero, decrement it and set other error keywords
    if (k$error.self.check()) {
	k$errornumber.self.set(vInteger.New(this.num));
	k$errortext.self.set(vString.New(iRunerr.text(num)));
	k$errorvalue.self.set((this.desc == null) ? null : this.desc.Deref());
	return;
    }

    k$output.file.flush();
    vFile f = k$errout.file;
    f.newline();
    f.println("Run-time error " + num);
    //#%#% here is where we would give file/line info
    f.println(iRunerr.text(num));
    if (desc != null) {
	f.println("offending value: " + desc.report());
    }
    //#%#% here is where we would do traceback

    iRuntime.exit(1);
}



} // class iError

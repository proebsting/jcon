//  iError -- an Icon run-time error, thrown as an exception

package rts;

public class iError extends Error {

    int num;				// error number
    vDescriptor desc;			// offending value
    String message = "";		// propagated message
    String call = "<runtime system>";
    String filename;
    int lineno;



iError(int num, vDescriptor desc) {	// constructor
    this.num = num;
    this.desc = desc;
}



void report() {				// print message and abort


    k$output.file.flush();
    vFile f = k$errout.file;
    f.newline();
    f.println("Run-time error " + num);
    if (filename != null) {
        f.println("File " + filename + "; Line " + lineno);
    }
    f.println(iRunerr.text(num));
    if (desc != null) {
	f.println("offending value: " + desc.report());
    }
    f.println("Traceback:");
    f.println("   " + call);
    f.println(message);

    this.printStackTrace(); 	//#%#% temporary

    iRuntime.exit(1);
}

public vDescriptor propagate(String fname, int lineno, String procname, vDescriptor[] args) {
    // if &error is zero, issue message and abort
    // if &error is not zero, decrement it and set other error keywords
    if (k$error.self.check()) {
	k$errornumber.self.set(vInteger.New(this.num));
	k$errortext.self.set(vString.New(iRunerr.text(num)));
	k$errorvalue.self.set((this.desc == null) ? null : this.desc.Deref());
        return null;
    } else {
	if (filename == null) {
	    this.lineno = lineno;
	    this.filename = fname;
	}
	message = "   " + call + " from line " + lineno + " in " + fname + "\n" + message;
	call = procname + "(";
	if (args.length > 0) {
	    if (args[0] == null) {
	        call += "~";
	    } else {
	        call += args[0].Deref().report();
	    }
	    for (int i = 1; i < args.length; i++) {
	        if (args[i] == null) {
	            call += ",~";
	        } else {
	            call += "," + args[i].Deref().report();
	        }
	    }
	}
	call += ")";
        throw this;
    }
}


} // class iError

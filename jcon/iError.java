//  iError -- an Icon run-time error, thrown as an exception

package rts;

public class iError extends Error {

    int num;				// error number
    vDescriptor desc;			// offending value
    String message = "";		// propagated message
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
    f.print(message);
    // this.printStackTrace(); 	//#%#% temporary
    iRuntime.exit(1);
}

public void propagate(String fname, int lineno, vDescriptor record,
		             String field) {
    String call;
    call = "{"
	   + record.Deref().report()
	   + "."
	   + field
	   + "}";
    propagate(fname, lineno, call);
}

public void propagate(String fname, int lineno, String template,
		             vDescriptor a1) {
    String call;
    call = "{"
	   + template
	   + " "
	   + a1.Deref().report()
	   + "}";
    propagate(fname, lineno, call);
}

public void propagate(String fname, int lineno, String template,
		             vDescriptor a1, vDescriptor a2) {
    String call;
    call = "{"
	   + a1.Deref().report()
	   + " "
	   + template
	   + " "
	   + a2.Deref().report()
	   + "}";
    propagate(fname, lineno, call);
}

public void propagate(String fname, int lineno, String template,
		             vDescriptor a1, vDescriptor a2, vDescriptor a3) {
    String call;
    call = "{"
	   + template
	   + " "
	   + a1.Deref().report()
	   + ", "
	   + a2.Deref().report()
	   + ", "
	   + a3.Deref().report()
	   + "}";
    propagate(fname, lineno, call);
}

public void propagate(String fname, int lineno,
		      vDescriptor a, vDescriptor[] args) {
    String arg0 = a.Deref().report().toString();
    if (arg0.startsWith("procedure ")) {
	arg0 = arg0.substring(10);
    }
    propagate(fname, lineno, arg0 + args2string(args));
}

String args2string(vDescriptor[] args) {
    String call = "(";
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
    return call;
}

public void propagate(String procname, vDescriptor[] args) {
    String call = procname + args2string(args);
    propagate(null, 0, call);
}

void propagate(String fname, int lineno, String call) {
    // if &error is zero, issue message and abort
    // if &error is not zero, decrement it and set other error keywords
    if (k$error.self.check()) {
	k$errornumber.self.set(vInteger.New(this.num));
	k$errortext.self.set(vString.New(iRunerr.text(num)));
	k$errorvalue.self.set((this.desc == null) ? null : this.desc.Deref());
	return;
    } else {
	if (filename == null) {
	    this.lineno = lineno;
	    this.filename = fname;
	}
	String coord = "";
	if (fname != null) {
	    coord = " from line " + lineno + " in " + fname;
	}
	message = "   " + call + coord + "\n" + message;
        throw this;
    }
}



} // class iError

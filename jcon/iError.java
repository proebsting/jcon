//  iError -- an Icon run-time error, thrown as an exception

package rts;

public final class iError extends Error {

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
    iKeyword.output.file().flush();
    vFile f = iKeyword.errout.file();
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
    iRuntime.exit(1);
}

public void propagate(String fname, int lineno, vDescriptor record,
		             String field) {
    String call;
    call = "{"
	   + record.Deref().report()
	   + " . "
	   + field
	   + "}";
    propagate(fname, lineno, call);
}

private String expand(String template, vDescriptor[] v) {
    int si = 0;
    String s = "";
    for (int vi = 0; vi < v.length; vi++) {
	int i = template.indexOf('$', si);
	if (i < 0) {
	    break;
	}
	s += template.substring(si, i);
	s += v[vi].Deref().report();
	si = i+1;
    }
    s += template.substring(si);
    s += "";
    return s;
}

public void propagate(String fname, int lineno, String template,
		             vDescriptor a1) {
    vDescriptor[] args = { a1 };
    String call = expand(template, args);
    propagate(fname, lineno, call);
}

public void propagate(String fname, int lineno, String template,
		             vDescriptor a1, vDescriptor a2) {
    vDescriptor[] args = { a1, a2 };
    String call = expand(template, args);
    propagate(fname, lineno, call);
}

public void propagate(String fname, int lineno, String template,
		             vDescriptor a1, vDescriptor a2, vDescriptor a3) {
    vDescriptor[] args = { a1, a2, a3 };
    String call = expand(template, args);
    propagate(fname, lineno, call);
}

public void propagate(String fname, int lineno, 
		             vDescriptor a, vDescriptor arglist) {
    String call = stripped(a)
		  + " ! "
		  + arglist.Deref().report().toString()
		  ;
    propagate(fname, lineno, call);
}

public void propagate(String fname, int lineno,
		      vDescriptor a, vDescriptor[] args) {
    propagate(fname, lineno, stripped(a) + args2string(args));
}

String stripped(vDescriptor x) {
    String s = x.Deref().report().toString();
    if (s.startsWith("procedure ")) {
	s = s.substring(10);
    } else if (s.startsWith("function ")) {
	s = s.substring(9);
    }
    return s;
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

// Called by generated code---it cannot handle error conversion.
public void propagate(String procname, vDescriptor[] args) {
    // if &error is not zero, print diagnostic, reset to zero, issue
    if (iKeyword.error.check()) {
	System.err.println("Attempted error conversion not possible:");
	System.err.println("procedure " + procname + " was not compiled with -fd");
	iKeyword.errornumber.set(vInteger.New(0));
    }
    String call = procname + args2string(args);
    propagate(null, 0, call);
}

void propagate(String fname, int lineno, String call) {
    // if &error is zero, issue message and abort
    // if &error is not zero, decrement it and set other error keywords
    if (iKeyword.error.check()) {
	iKeyword.errornumber.set(vInteger.New(this.num));
	iKeyword.errortext.set(vString.New(iRunerr.text(num)));
	iKeyword.errorvalue.set((this.desc == null) ? null : this.desc.Deref());
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

//  fIO.java -- functions dealing with I/O

package rts;

import java.io.*;



class fIO {

    // print(f, arglist, newline) -- helper for write(), writes(), stop()

    static vDescriptor print(vFile f, vDescriptor[] args, boolean nl) {
	vDescriptor d = vNull.New();	// last argument processed
	for (int i = 0; i < args.length; i++) {
	    d = args[i];
	    if (d instanceof vFile) {	// if file value
		if (i > 0 && nl) {
		    f.newline();	// write newline
		}
		f = (vFile) d;		// change files
	    } else {
		f.writes(args[i].write());
	    }
	}
	if (nl) {
	    f.newline();
	}
	return d;
    }
}



class f$open extends iValueClosure {				// open(s1,s2)
    static vString defmode = vString.New("r");
    vDescriptor function(vDescriptor[] args) {
	String fname = vString.argDescr(args, 0).toString();
	String mode = vString.argDescr(args, 1, defmode).toString();
	String validFlags = "rwabcptugRWABCPTUG";
	for (int i = 0; i < mode.length(); i++) {
	    if (validFlags.indexOf(mode.charAt(i)) < 0) {
		iRuntime.error(209, args[1]);
	    }
	}
	return vFile.New(fname, mode, args);
    }
}



class f$flush extends iValueClosure {				// flush(f)
    vDescriptor function(vDescriptor[] args) {
	return(vFile.argVal(args, 0).flush());
    }
}



class f$close extends iValueClosure {				// close(f)
    vDescriptor function(vDescriptor[] args) {
	return(vFile.argVal(args, 0).close());
    }
}



class f$read extends iValueClosure {				// read(f)
    vDescriptor function(vDescriptor[] args) {
	return(vFile.argVal(args, 0, k$input.file).read());
    }
}



class f$reads extends iValueClosure {				// reads(f,n)
    vDescriptor function(vDescriptor[] args) {
	vFile f = vFile.argVal(args, 0, k$input.file);
	long n = vInteger.argVal(args, 1, 1);
	if (n <= 0) {
	    iRuntime.error(205, args[1]);
	    return null;
	}
	return f.reads(n);
    }
}



class f$write extends iValueClosure {				// write(...)
    vDescriptor function(vDescriptor[] args) {
	return fIO.print(k$output.file, args, true);
    }
}



class f$writes extends iValueClosure {				// writes(...)
    vDescriptor function(vDescriptor[] args) {
	return fIO.print(k$output.file, args, false);
    }
}



class f$stop extends iValueClosure {				// stop(...)
    vDescriptor function(vDescriptor[] args) {
	k$output.file.flush();			// flush stdout
	fIO.print(k$errout.file, args, true);	// write msg
	iRuntime.exit(1, parent);		// exit
	return null;	// not reached
    }
}



class f$seek extends iValueClosure {				// seek(f,i)
    vDescriptor function(vDescriptor[] args) {
	vFile f = vFile.argVal(args, 0);
	long n = vInteger.argVal(args, 1);
	return f.seek(n);
    }
}



class f$where extends iValueClosure {				// where(f)
    vDescriptor function(vDescriptor[] args) {
	return(vFile.argVal(args, 0).where());
    }
}



class f$remove extends iValueClosure {				// remove(s)
    vDescriptor function(vDescriptor[] args) {
	String s = vString.argDescr(args, 0).toString();
	java.io.File f = new java.io.File(s);
	if (f.delete()) {
	    return vNull.New();
	} else {
	    return null;
	}
    }
}



class f$rename extends iValueClosure {				// rename(s1,s2)
    vDescriptor function(vDescriptor[] args) {
	String s1 = vString.argDescr(args, 0).toString();
	String s2 = vString.argDescr(args, 1).toString();
	java.io.File f1 = new java.io.File(s1);
	java.io.File f2 = new java.io.File(s2);
	if (f1.renameTo(f2)) {
	    return vNull.New();
	} else {
	    return null;
	}
    }
}

//  fIO.java -- functions dealing with I/O

package rts;

import java.io.*;



class fIO {

    // print(f, arglist, newline) -- helper for write(), writes(), stop()

    static vDescriptor print(vFile f, vDescriptor[] v, boolean nl) {
	vDescriptor d = vNull.New();	// last argument processed
	for (int i = 0; i < v.length; i++) {
	    d = v[i].Deref();
	    if (d instanceof vFile) {	// if file value
		if (i > 0 && nl) {
		    f.newline();	// write newline
		}
		f = (vFile) d;		// change files
	    } else {
		f.writes(d.write());
	    }
	}
	if (nl) {
	    f.newline();
	}
	return d;
    }
}



class f$open extends vProcV {				// open(s1,s2,...)
    public vDescriptor Call(vDescriptor[] v) {
	String fname;
	String mode;
	String validFlags = "rwabcptugRWABCPTUG";

	fname = iRuntime.arg(v, 0).mkString().toString();
	vDescriptor m = iRuntime.arg(v, 1).Deref();
	mode = m.isnull() ? "r" : m.mkString().toString();

	for (int i = 0; i < mode.length(); i++) {
	    if (validFlags.indexOf(mode.charAt(i)) < 0) {
		iRuntime.error(209, m);
	    }
	}
	return vFile.New(fname, mode, v);
    }
}



class f$flush extends vProc1 {				// flush(f)
    public vDescriptor Call(vDescriptor a) {
	return(vFile.arg(a).flush());
    }
}



class f$close extends vProc1 {				// close(f)
    public vDescriptor Call(vDescriptor a) {
	return(vFile.arg(a).close());
    }
}



class f$read extends vProc1 {				// read(f)
    public vDescriptor Call(vDescriptor a) {
	return(vFile.arg(a, k$input.file).read());
    }
}



class f$reads extends vProc2 {				// reads(f,n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	vFile f = vFile.arg(a, k$input.file);
	long n = b.isnull() ? 1 : b.mkInteger().value;
	if (n <= 0) {
	    iRuntime.error(205, b);
	    return null;
	}
	return f.reads(n);
    }
}



class f$write extends vProcV {				// write(...)
    public vDescriptor Call(vDescriptor[] v) {
	return fIO.print(k$output.file, v, true);
    }
}



class f$writes extends vProcV {				// writes(...)
    public vDescriptor Call(vDescriptor[] v) {
	return fIO.print(k$output.file, v, false);
    }
}



class f$stop extends vProcV {				// stop(...)
    public vDescriptor Call(vDescriptor[] v) {
	k$output.file.flush();			// flush stdout
	fIO.print(k$errout.file, v, true);	// write msg
	iRuntime.exit(1);			// exit
	return null;	// not reached
    }
}



class f$seek extends vProc2 {				// seek(f,i)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return vFile.arg(a).seek(b.mkInteger().value);
    }
}



class f$where extends vProc1 {				// where(f)
    public vDescriptor Call(vDescriptor a) {
	return(vFile.arg(a).where());
    }
}



class f$remove extends vProc1 {				// remove(s)
    public vDescriptor Call(vDescriptor a) {
	String s = a.mkString().toString();
	java.io.File f = new java.io.File(s);
	if (f.delete()) {
	    return vNull.New();
	} else {
	    return null;
	}
    }
}



class f$rename extends vProc2 {				// rename(s1,s2)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	String s1 = a.mkString().toString();
	String s2 = b.mkString().toString();
	java.io.File f1 = new java.io.File(s1);
	java.io.File f2 = new java.io.File(s2);
	if (f1.renameTo(f2)) {
	    return vNull.New();
	} else {
	    return null;
	}
    }
}

//  fIO.java -- functions dealing with I/O

package rts;

import java.io.*;



final class fIO extends iInstantiate {
    public static fIO self = new fIO();
    public vProc instantiate(String name) {
        if (name.compareTo( "f$open" ) == 0) return new f$open();
        if (name.compareTo( "f$flush" ) == 0) return new f$flush();
        if (name.compareTo( "f$close" ) == 0) return new f$close();
        if (name.compareTo( "f$read" ) == 0) return new f$read();
        if (name.compareTo( "f$reads" ) == 0) return new f$reads();
        if (name.compareTo( "f$write" ) == 0) return new f$write();
        if (name.compareTo( "f$writes" ) == 0) return new f$writes();
        if (name.compareTo( "f$stop" ) == 0) return new f$stop();
        if (name.compareTo( "f$seek" ) == 0) return new f$seek();
        if (name.compareTo( "f$where" ) == 0) return new f$where();
        if (name.compareTo( "f$remove" ) == 0) return new f$remove();
        if (name.compareTo( "f$rename" ) == 0) return new f$rename();
        return null;
    } // vProc instantiate(String)

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



final class f$open extends vProcV {				// open(s1,s2,...)
    public vDescriptor Call(vDescriptor[] v) {
	String fname;
	String mode;
	String validFlags = "rwabcptugxRWABCPTUGX";

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



final class f$flush extends vProc1 {				// flush(f)
    public vDescriptor Call(vDescriptor a) {
	return(vFile.arg(a).flush());
    }
}



final class f$close extends vProc1 {				// close(f)
    public vDescriptor Call(vDescriptor a) {
	return(vFile.arg(a).close());
    }
}



final class f$read extends vProc1 {				// read(f)
    public vDescriptor Call(vDescriptor a) {
	return(vFile.arg(a, iKeyword.input.file()).read());
    }
}



final class f$reads extends vProc2 {				// reads(f,n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	vFile f = vFile.arg(a, iKeyword.input.file());
	long n = b.isnull() ? 1 : b.mkInteger().value;
	if (n <= 0) {
	    iRuntime.error(205, b);
	    return null;
	}
	return f.reads(n);
    }
}



final class f$write extends vProcV {				// write(...)

    static vDescriptor a0[] = new vDescriptor[0];
    static vDescriptor a1[] = new vDescriptor[1];
    static vDescriptor a2[] = new vDescriptor[2];
    static vDescriptor a3[] = new vDescriptor[3];
    static vDescriptor a4[] = new vDescriptor[4];
    static vDescriptor a5[] = new vDescriptor[5];
    static vDescriptor a6[] = new vDescriptor[6];
    static vDescriptor a7[] = new vDescriptor[7];
    static vDescriptor a8[] = new vDescriptor[8];
    static vDescriptor a9[] = new vDescriptor[9];

    public vDescriptor Call(vDescriptor[] v) {
	return fIO.print(iKeyword.output.file(), v, true);
    }

    public vDescriptor Call() {
	return fIO.print(iKeyword.output.file(), a0, true);
    }

    public vDescriptor Call(vDescriptor a) {
	a1[0] = a;
	return fIO.print(iKeyword.output.file(), a1, true);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	a2[0] = a;
	a2[1] = b;
	return fIO.print(iKeyword.output.file(), a2, true);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	a3[0] = a;
	a3[1] = b;
	a3[2] = c;
	return fIO.print(iKeyword.output.file(), a3, true);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	a4[0] = a;
	a4[1] = b;
	a4[2] = c;
	a4[3] = d;
	return fIO.print(iKeyword.output.file(), a4, true);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	a5[0] = a;
	a5[1] = b;
	a5[2] = c;
	a5[3] = d;
	a5[4] = e;
	return fIO.print(iKeyword.output.file(), a5, true);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	a6[0] = a;
	a6[1] = b;
	a6[2] = c;
	a6[3] = d;
	a6[4] = e;
	a6[5] = f;
	return fIO.print(iKeyword.output.file(), a6, true);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	a7[0] = a;
	a7[1] = b;
	a7[2] = c;
	a7[3] = d;
	a7[4] = e;
	a7[5] = f;
	a7[6] = g;
	return fIO.print(iKeyword.output.file(), a7, true);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	a8[0] = a;
	a8[1] = b;
	a8[2] = c;
	a8[3] = d;
	a8[4] = e;
	a8[5] = f;
	a8[6] = g;
	a8[7] = h;
	return fIO.print(iKeyword.output.file(), a8, true);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	a9[0] = a;
	a9[1] = b;
	a9[2] = c;
	a9[3] = d;
	a9[4] = e;
	a9[5] = f;
	a9[6] = g;
	a9[7] = h;
	a9[8] = i;
	return fIO.print(iKeyword.output.file(), a9, true);
    }

}



final class f$writes extends vProcV {				// writes(...)

    static vDescriptor a0[] = new vDescriptor[0];
    static vDescriptor a1[] = new vDescriptor[1];
    static vDescriptor a2[] = new vDescriptor[2];
    static vDescriptor a3[] = new vDescriptor[3];
    static vDescriptor a4[] = new vDescriptor[4];
    static vDescriptor a5[] = new vDescriptor[5];
    static vDescriptor a6[] = new vDescriptor[6];
    static vDescriptor a7[] = new vDescriptor[7];
    static vDescriptor a8[] = new vDescriptor[8];
    static vDescriptor a9[] = new vDescriptor[9];

    public vDescriptor Call(vDescriptor[] v) {
	return fIO.print(iKeyword.output.file(), v, false);
    }

    public vDescriptor Call() {
	return fIO.print(iKeyword.output.file(), a0, false);
    }

    public vDescriptor Call(vDescriptor a) {
	a1[0] = a;
	return fIO.print(iKeyword.output.file(), a1, false);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	a2[0] = a;
	a2[1] = b;
	return fIO.print(iKeyword.output.file(), a2, false);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	a3[0] = a;
	a3[1] = b;
	a3[2] = c;
	return fIO.print(iKeyword.output.file(), a3, false);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	a4[0] = a;
	a4[1] = b;
	a4[2] = c;
	a4[3] = d;
	return fIO.print(iKeyword.output.file(), a4, false);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	a5[0] = a;
	a5[1] = b;
	a5[2] = c;
	a5[3] = d;
	a5[4] = e;
	return fIO.print(iKeyword.output.file(), a5, false);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	a6[0] = a;
	a6[1] = b;
	a6[2] = c;
	a6[3] = d;
	a6[4] = e;
	a6[5] = f;
	return fIO.print(iKeyword.output.file(), a6, false);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	a7[0] = a;
	a7[1] = b;
	a7[2] = c;
	a7[3] = d;
	a7[4] = e;
	a7[5] = f;
	a7[6] = g;
	return fIO.print(iKeyword.output.file(), a7, false);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	a8[0] = a;
	a8[1] = b;
	a8[2] = c;
	a8[3] = d;
	a8[4] = e;
	a8[5] = f;
	a8[6] = g;
	a8[7] = h;
	return fIO.print(iKeyword.output.file(), a8, false);
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	a9[0] = a;
	a9[1] = b;
	a9[2] = c;
	a9[3] = d;
	a9[4] = e;
	a9[5] = f;
	a9[6] = g;
	a9[7] = h;
	a9[8] = i;
	return fIO.print(iKeyword.output.file(), a9, false);
    }
}



final class f$stop extends vProcV {				// stop(...)
    public vDescriptor Call(vDescriptor[] v) {
	iKeyword.output.file().flush();			// flush stdout
	fIO.print(iKeyword.errout.file(), v, true);	// write msg
	iRuntime.exit(1);				// exit
	return null;	// not reached
    }
}



final class f$seek extends vProc2 {				// seek(f,i)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return vFile.arg(a).seek(b.mkInteger().value);
    }
}



final class f$where extends vProc1 {				// where(f)
    public vDescriptor Call(vDescriptor a) {
	return(vFile.arg(a).where());
    }
}



final class f$remove extends vProc1 {				// remove(s)
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



final class f$rename extends vProc2 {				// rename(s1,s2)
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

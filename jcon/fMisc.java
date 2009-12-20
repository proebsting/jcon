//  fMisc.java -- miscellaneous built-in functions

package jcon;

import java.io.*;
import java.util.*;

final class fMisc extends iInstantiate {
    public static fMisc self = new fMisc();
    public vProc instantiate(String name) {
        if (name.equals("f$name")) return new f$name();
        if (name.equals("f$display")) return new f$display();
        if (name.equals("f$variable")) return new f$variable();
        if (name.equals("f$function")) return new f$function();
        if (name.equals("f$args")) return new f$args();
        if (name.equals("f$proc")) return new f$proc();
        if (name.equals("f$collect")) return new f$collect();
        if (name.equals("f$delay")) return new f$delay();
        if (name.equals("f$exit")) return new f$exit();
        if (name.equals("f$getenv")) return new f$getenv();
        if (name.equals("f$system")) return new f$system();
        if (name.equals("f$errorclear")) return new f$errorclear();
        if (name.equals("f$runerr")) return new f$runerr();
        return null;
    } // vProc instantiate(String)
}


final class f$name extends vProc1 {				// name(v)
    public vDescriptor Call(vDescriptor a) {
	return a.Name();
    }
}



final class f$display extends vProc2 {				// display(i,f)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.isnull()) {
	    a.mkInteger();		// validate and ignore level count
	}
	iRuntime.display(vFile.arg(b, iKeyword.errout.file()));
	return vNull.New();
    }
}



final class f$variable extends vProc1 {				// variable(x)
    public vDescriptor Call(vDescriptor a) {
	String s = a.mkString().toString();
	vVariable v = iEnv.symtab.get(s);
	if (v != null) {
	    return v;
	}
	if (s.length() > 1 && s.charAt(0) == '&') {
	    String k = s.substring(1);
	    vVariable o = iEnv.getKeyVar(k);
	    if (o != null) {
		return o;
	    }
	}
	return null; /*FAIL*/
    }
}



final class f$function extends vProc0 {				// function()
    public vDescriptor Call() {
	final vValue[] v = iSort.sort(iEnv.listBuiltins());
	return new vClosure() {
	    int i = 0;
	    public vDescriptor Resume() {
		if (i < v.length) {
		    retval = v[i++];
		    return this;
		} else {
		    return null;
		}
	    }
	}.Resume();
    }
}



final class f$args extends vProc1 {				// args(x)
    public vDescriptor Call(vDescriptor a) {
	return a.Args();
    }
}



final class f$proc extends vProc2 {				// proc(s, i)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	vValue v = a.Deref();
	long i = b.isnull() ? 1 : b.mkInteger().value;
	if (i < 0 || i > 3) {
	    iRuntime.error(205, b);
	}
	try {
	    return v.mkProc((int) i);
	} catch (iError e) {
	    return null;
	}
    }
}



final class f$collect extends vProc2 {				// collect(i,j)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	long i1 = a.isnull() ? 0 : a.mkInteger().value;
	long i2 = b.isnull() ? 0 : b.mkInteger().value;
	if (i1 == 0) {
	    System.gc();
	    return vNull.New();
	} else if (i1 > 0 && i1 < 4) {
	    int ii2 = (int)i2;
	    if (i2 < 0 || i2 != (long)ii2) {
		iRuntime.error(205, b);
	    }
	    System.gc();
	    if (ii2 > 0) {
		try {
		    byte[] dummy = new byte[ii2];
		    // the following (not the preceding) line
		    // actually triggers the out of memory error.
		    dummy[0] = dummy[ii2-1] = 1;
		} catch (OutOfMemoryError e) {
		    return null;
		}
	    }
	    return vNull.New();
	} else {
	    iRuntime.error(205, a);
	    return null;
	}
    }
}



final class f$delay extends vProc1 {				// delay(i)
    public vDescriptor Call(vDescriptor a) {
	int i = a.isnull() ? 1 : (int) a.mkInteger().value;
	try {
	    Thread.sleep(i);
	} catch (InterruptedException e) {
	    // nothing; just return
	}
	return vNull.New();
    }
}



final class f$exit extends vProc1 {				// exit(n)
    public vDescriptor Call(vDescriptor a) {
	int n = a.isnull() ? 0 : (int) a.mkInteger().value;
	iRuntime.exit(n);
	return null;	// not reached
    }
}



final class f$getenv extends vProc1 {				// getenv(s)
    public vDescriptor Call(vDescriptor a) {
	return iSystem.getenv(a.mkString().toString());
    }
}



final class f$system extends vProc1 {				// system(s)
    public vDescriptor Call(vDescriptor a) {
	String s = a.mkString().toString();
	int status;
	try {
	    Process p = iSystem.command(s);		// start process
	    p.getOutputStream().close();		// close its stdin
	    status = p.waitFor();			// wait for completion
	    vFile.copy(p.getInputStream(),iKeyword.output.file()); //copy stdout
	    vFile.copy(p.getErrorStream(),iKeyword.errout.file()); //copy stderr
	} catch (Throwable e) {
	    status = -1;
	}
	return vInteger.New(status);
    }
}



final class f$errorclear extends vProc0 {			// errorclear()
    public vDescriptor Call() {
	iKeyword.errornumber.set(null);
	iKeyword.errortext.set(null);
	iKeyword.errorvalue.set(null);
	return vNull.New();
    }
}



final class f$runerr extends vProc2 {				// runerr(i,x)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	long i = a.mkInteger().value;
	vDescriptor x = b.Deref();
	if (x.isnull()) {
	    iRuntime.error((int) i);
	} else {
	    iRuntime.error((int) i, x);
	}
	return vNull.New();
    }
}

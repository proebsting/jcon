//  fMisc.java -- miscellaneous built-in functions

package rts;

import java.io.*;
import java.util.*;

class fMisc {} //dummy



class f$name extends vProc1 {					// name(v)
    public vDescriptor Call(vDescriptor a) {
	return a.Name();
    }
}



class f$display extends vProc2 {				// display(i,f)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!a.isnull()) {
	    a.mkInteger();		// validate and ignore level count
	}
	iRuntime.display(vFile.arg(b, k$errout.file));
	return vNull.New();
    }
}



class f$variable extends vProc1 {				// variable(x)
    public vDescriptor Call(vDescriptor a) {
	String s = a.mkString().toString();
	vVariable v = (vVariable) iEnv.symtab.get(s);
	if (v != null) {
	    return v;
	}
	if (s.length() > 1 && s.charAt(0) == '&') {
	    String k = s.substring(1);
	    Object o = iEnv.getKey(k);
	    if (o != null && o instanceof vVariable) {
		return (vVariable) o;
	    }
	}
	return null; /*FAIL*/
    }
}



class f$function extends vProc0 {				// function()
    public vDescriptor Call() {
	final java.util.Enumeration e = iEnv.enumBuiltins();
	return new vClosure() {
	    public vDescriptor Resume() {
		if (e.hasMoreElements()) {
		    retval = vString.New((String) e.nextElement());
		    return this;
		} else {
		    return null;
		}
	    }
	}.Resume();
    }
}



class f$args extends vProc1 {					// args(x)
    public vDescriptor Call(vDescriptor a) {
	return a.Args();
    }
}



class f$proc extends vProc2 {					// proc(s, i)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	vValue v = a.Deref();
	long i = b.isnull() ? 1 : b.mkInteger().value;
	if (i < 0 || i > 3) {
	    iRuntime.error(205, b);
	}
	return v.Proc(i);
    }
}



class f$collect extends vProc2 {				// collect(i,j)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	long i1 = a.isnull() ? 0 : a.mkInteger().value;
	long i2 = b.isnull() ? 0 : b.mkInteger().value;
	vNull n = vNull.New();
	if (i1 == 0) {
	    System.gc();
	    return n;
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
	    return n;
	} else {
	    iRuntime.error(205, a);
	    return null;
	}
    }
}



class f$delay extends vProc1 {					// delay(i)
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



class f$exit extends vProc1 {					// exit(n)
    public vDescriptor Call(vDescriptor a) {
	int n = a.isnull() ? 0 : (int) a.mkInteger().value;
	iRuntime.exit(n);
	return null;	// not reached
    }
}



class f$getenv extends vProc1 {					// getenv(s)

    static Hashtable env = new Hashtable();

    static {				// initialization on first call
	try {
	    // warning: ugly unixisms follow
	    Process p = Runtime.getRuntime().exec("env");
	    BufferedReader d = new BufferedReader(
		new InputStreamReader(p.getInputStream()));
	    String s;
	    while ((s = d.readLine()) != null) {
		s = s.trim();
		int n = s.indexOf('=');
		if (n > 0) {
		    String key = s.substring(0, n);
		    String val = s.substring(n + 1);
		    env.put(key, vString.New(val));
		}
	    }
	    p.destroy();
	} catch (Exception e1) {
	    // nothing; table remains empty, all calls fail
	}
    }

    public vDescriptor Call(vDescriptor a) {
	return (vString) env.get(a.mkString().toString());
    }
}



class f$system extends vProc1 {					// system(s)
    public vDescriptor Call(vDescriptor a) {
	String argv[] = { "sh", "-c", a.mkString().toString() };
	int status;
	try {
	    Process p = Runtime.getRuntime().exec(argv); // start process
	    p.getOutputStream().close();		 // close its stdin
	    status = p.waitFor();			 // wait for completion
	    vFile.copy(p.getInputStream(),k$output.file);// copy stdout
	    vFile.copy(p.getErrorStream(),k$errout.file);// copy stderr
	} catch (Throwable e) {
	    status = -1;
	}
	return vInteger.New(status);
    }
}



class f$errorclear extends vProc0 {				// errorclear()
    public vDescriptor Call() {
	k$errornumber.self.set(null);
	k$errortext.self.set(null);
	k$errorvalue.self.set(null);
	return vNull.New();
    }
}



class f$runerr extends vProc2 {					// runerr(i,x)
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

//  fMisc.java -- miscellaneous built-in functions

package rts;

import java.io.*;
import java.util.*;

class fMisc {} //dummy



class f$name extends iRefClosure {				// name(v)
    vDescriptor function(vDescriptor[] args) {
	if (args.length < 1) {
	    iRuntime.error(111);
	}
	return args[0].Name();
    }
}



class f$display extends iValueClosure {				// display(x)
    vDescriptor function(vDescriptor[] args) {
	//#%#% arguments are validated but ignored
	long level = vInteger.argVal(args, 0, 1000000);
	vFile f = vFile.argVal(args, 1, k$errout.file);
	iRuntime.display(parent);
	return iNew.Null();
    }
}



class f$variable extends iValueClosure {			// variable(x)
    vDescriptor function(vDescriptor[] args) {
	vString s = (vString) iRuntime.argVal(args, 0, 103);
	String sval = s.toString();
	parent.locals();
	for (int i = 0; parent.names[i] != null; i++) {
	    if (sval.equals(parent.names[i])) {
		return parent.variables[i];
	    }
	}
	vVariable v = (vVariable) iEnv.symtab.get(sval);
	if (v != null) {
	    return v;
	}
	if (sval.length() > 1 && sval.charAt(0) == '&') {
	    String k = sval.substring(1);
	    Object o = iEnv.keytab.get(k);
	    if (o != null && o instanceof vVariable) {
		return (vVariable) o;
	    }
	}
	return null;
    }
}



class f$function extends iClosure {				// function()
    java.util.Enumeration e;

    public vDescriptor nextval() {
	if (PC == 1) {
	    e = iEnv.builtintab.keys();
	    PC = 2;
	}
	if (e.hasMoreElements()) {
	    return iNew.String((String) e.nextElement());
	} else {
	    return null;
	}
    }
}



class f$args extends iValueClosure {				// args(x)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args, 0).Args();
    }
}



class f$proc extends iValueClosure {				// proc(s, i)
    vDescriptor function(vDescriptor[] args) {
	vValue v = iRuntime.argVal(args, 0);
	long i = vInteger.argVal(args, 1, 1);
	if (i < 0 || i > 3) {
	    iRuntime.error(205, args[1]);
	}
	return v.Proc(i);
    }
}



class f$collect extends iValueClosure {				// collect(i,j)
    vDescriptor function(vDescriptor[] args) {
	long i1 = vInteger.argVal(args, 0, 0);
	long i2 = vInteger.argVal(args, 1, 0);
	vNull n = iNew.Null();
	if (i1 == 0) {
	    System.gc();
	    return n;
	} else if (i1 > 0 && i1 < 4) {
	    if (i2 < 0) {
		iRuntime.error(205, args[1]);
	    }
	    int ii2 = (int)i2;
	    if (i2 < 0 || i2 != (long)ii2) {
		iRuntime.error(205, args[1]);
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
	    iRuntime.error(205, args[0]);
	    return null;
	}
    }
}



class f$delay extends iValueClosure {				// delay(i)
    vDescriptor function(vDescriptor[] args) {
	int i = (int) vInteger.argVal(args, 0, 1);
	try {
	    Thread.sleep(i);
	} catch (InterruptedException e) {
	    // nothing; just return
	}
	return iNew.Null();
    }
}



class f$exit extends iValueClosure {				// exit(n)
    vDescriptor function(vDescriptor[] args) {
	int n = (int) vInteger.argVal(args, 0, 0);
	iRuntime.exit(n, parent);
	return null;	// not reached
    }
}



class f$getenv extends iValueClosure {				// getenv(s)

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
		    env.put(key, iNew.String(val));
		}
	    }
	    p.destroy();
	} catch (Exception e1) {
	    // nothing; table remains empty, all calls fail
	}
    }

    vDescriptor function(vDescriptor[] args) {
	return (vString) env.get(vString.argDescr(args, 0).toString());
    }
}



class f$system extends iValueClosure {				// system(s)
    vDescriptor function(vDescriptor[] args) {
	String argv[] = { "sh", "-c", vString.argDescr(args, 0).toString() };
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
    return iNew.Integer(status);
    }
}



class f$errorclear extends iValueClosure {			// errorclear()
    vDescriptor function(vDescriptor[] args) {
	k$errornumber.number = null;
	k$errortext.text = null;
	k$errorvalue.value = null;
	return iNew.Null();
    }
}



class f$runerr extends iValueClosure {				// runerr(i,x)
    vDescriptor function(vDescriptor[] args) {
	long i = vInteger.argVal(args, 0);
	vDescriptor x = iRuntime.argVal(args, 1);
	if (x instanceof vNull) {
	    iRuntime.error((int) i);
	} else {
	    iRuntime.error((int) i, x);
	}
	return iNew.Null();
    }
}

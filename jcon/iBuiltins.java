//  iBuiltins.java -- built-in functions

package rts;

import java.io.*;
import java.util.*;



public class iBuiltins extends iFile {

static final String PREFIX = "rts.f$";	// classname prefix for built-in funcs



void announce() {
	declare("abs", 1);
	declare("acos", 1);
	declare("any", 4);
	declare("args", 1);
	declare("asin", 1);
	declare("atan", 2);
	declare("bal", 6);
	declare("center", 3);
	declare("char", 1);
	declare("close", 1);
	declare("collect", 2);
	declare("copy", 1);
	declare("cos", 1);
	declare("cset", 1);
	declare("delay", 1);
	declare("delete", 2);
	declare("detab", -1);
	declare("display", 2);
	declare("dtor", 1);
	declare("entab", -1);
	declare("errorclear", 0);
	declare("exit", 1);
	declare("exp", 1);
	declare("find", 4);
	declare("flush", 1);
	declare("function", 0);
	declare("get", 1);
	declare("getenv", 1);
	declare("iand", 2);
	declare("icom", 1);
	declare("image", 1);
	declare("insert", 3);
	declare("integer", 1);
	declare("ior", 2);
	declare("ishift", 2);
	declare("ixor", 2);
	declare("key", 1);
	declare("left", 3);
	declare("list", 2);
	declare("log", 2);
	declare("many", 4);
	declare("map", 3);
	declare("match", 4);
	declare("member", 2);
	declare("move", 1);
	declare("name", 1);
	declare("numeric", 1);
	declare("open", 2);
	declare("ord", 1);
	declare("pop", 1);
	declare("pos", 1);
	declare("proc", 2);
	declare("pull", 1);
	declare("push", -2);
	declare("put", -2);
	declare("read", 1);
	declare("reads", 2);
	declare("real", 1);
	declare("remove", 1);
	declare("rename", 2);
	declare("repl", 2);
	declare("reverse", 1);
	declare("right", 3);
	declare("rtod", 1);
	declare("runerr", 2);
	declare("seek", 2);
	declare("seq", 2);
	declare("serial", 1);
	declare("set", 1);
	declare("sin", 1);
	declare("sort", 2);
	declare("sortf", 2);
	declare("sqrt", 1);
	declare("stop", -1);
	declare("string", 1);
	declare("system", 1);
	declare("tab", 1);
	declare("table", 1);
	declare("tan", 1);
	declare("trim", 2);
	declare("type", 1);
	declare("upto", 4);
	declare("variable", 1);
	declare("where", 1);
	declare("write", -1);
	declare("writes", -1);
}


static void declare(String name, int args)
{
	iEnv.declareBuiltin(name, iNew.Proc(
	    "function " + name, PREFIX + name, args));
}



} // class iBuiltins



//------------------------------------------  miscellaneous functions follow


class f$name extends iRefClosure {				// name(v)
	vDescriptor function(vDescriptor[] args) {
		if (args.length < 1) {
			iRuntime.error(111);
		}
		return args[0].Name();
	}
}

class f$display extends iValueClosure {			// display(x)
	vDescriptor function(vDescriptor[] args) {

		// #%#%#% currently ignores arguments
		iRuntime.display(parent);
		return iNew.Null();
	}
}

class f$variable extends iValueClosure {			// variable(x)
	vDescriptor function(vDescriptor[] args) {
		vString s = (vString) iRuntime.argVal(args, 0, 103);
		parent.locals();
		for (int i = 0; parent.names[i] != null; i++) {
			if (s.value.equals(parent.names[i])) {
				return parent.variables[i];
			}
		}
		vVariable v = (vVariable) iEnv.symtab.get(s.value);
		if (v != null) {
			return v;
		}
		if (s.value.length() > 1 && s.value.charAt(0) == '&') {
			String k = s.value.substring(1);
			Object o = iEnv.keytab.get(k);
			if (o != null && o instanceof vVariable) {
				return (vVariable) o;
			}
		}
		return null;
	}
}

class f$collect extends iValueClosure {			// collect(i,j)
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

class f$image extends iValueClosure {			// image(x)
	vDescriptor function(vDescriptor[] args) {
		return iNew.String(iRuntime.argVal(args, 0).image());
	}
}



class f$type extends iValueClosure {				// type(x)
	vDescriptor function(vDescriptor[] args) {
		return iNew.String(iRuntime.argVal(args, 0).type());
	}
}



class f$serial extends iValueClosure {			// serial(x)
	vDescriptor function(vDescriptor[] args) {
		return iRuntime.argVal(args, 0).Serial();
	}
}



class f$copy extends iValueClosure {				// copy(x)
	vDescriptor function(vDescriptor[] args) {
		return iRuntime.argVal(args, 0).Copy();
	}
}



class f$integer extends iValueClosure {			// integer(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkInteger();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$numeric extends iValueClosure {			// numeric(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkNumeric();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$real extends iValueClosure {				// real(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkReal();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$string extends iValueClosure {			// string(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkString();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$cset extends iValueClosure {				// cset(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkCset();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$delay extends iValueClosure {			// delay(i)
	vDescriptor function(vDescriptor[] args) {
		int i = (int) vInteger.argVal(args, 0, 1);
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// nothing; just return		//#%#%??
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

class f$function extends iClosure {				// function()
	java.util.Enumeration e;

	public void nextval() {
		if (e == null) {
			e = iEnv.builtintab.keys();
		}
		if (e.hasMoreElements()) {
			retvalue = iNew.String((String) e.nextElement());
		} else {
			retvalue = null;
		}
	}
}

class f$seq extends iClosure {					// seq(i1,i2)
	long i1, i2;

	public void nextval() {
		if (PC == 1) {
			PC = 2;
			i1 = vInteger.argVal(arguments, 0, 1);
			i2 = vInteger.argVal(arguments, 1, 1);
		} else {
			i1 += i2;
		}
		retvalue = iNew.Integer(i1);
	}
}

class f$remove extends iValueClosure {			// remove(s)
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		java.io.File f = new java.io.File(s);
		if (f.delete()) {
			return iNew.Null();
		} else {
			return null;
		}
	}
}

class f$rename extends iValueClosure {			// rename(s1,s2)
	vDescriptor function(vDescriptor[] args) {
		String s1 = vString.argVal(args, 0);
		String s2 = vString.argVal(args, 1);
		java.io.File f1 = new java.io.File(s1);
		java.io.File f2 = new java.io.File(s2);
		if (f1.renameTo(f2)) {
			return iNew.Null();
		} else {
			return null;
		}
	}
}

class f$system extends iValueClosure {			// system(s)
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		int status;
		try {
			// #%#%# new process's stdin/stdout/stderr are
			// disconnected.  See class Runtime....
			Process p = Runtime.getRuntime().exec(s);
			status = p.waitFor();
		} catch (Throwable e) {
			status = -1;
		}
		return iNew.Integer(status);
	}
}

class f$getenv extends iValueClosure {			// getenv(s)


	static Hashtable env = new Hashtable();

	static {				// initialization on first call
		try {
			//#%#% warning: ugly unixisms follow
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
		return (vString) env.get(vString.argVal(args, 0));
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

class f$runerr extends iValueClosure {			// runerr(i,x)
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

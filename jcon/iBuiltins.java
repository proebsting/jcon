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
	declare("display", 2);
	declare("dtor", 1);
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
    try {
	iEnv.declareBuiltin(name, iNew.Proc(Class.forName(PREFIX + name), args));
    } catch (ClassNotFoundException e) {
	iRuntime.bomb("cannot declare builtin function " + name + "()");
    }
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

class f$display extends iFunctionClosure {			// display(x)
	vDescriptor function(vDescriptor[] args) {

		// #%#%#% currently ignores arguments
		iRuntime.display(parent);
		return iNew.Null();
	}
}

class f$variable extends iFunctionClosure {			// variable(x)
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

class f$collect extends iFunctionClosure {			// collect(i,j)
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
			try {
				byte[] dummy = new byte[ii2];
				// the following (not the preceding) line
				// actually triggers the out of memory error.
				dummy[0] = dummy[ii2-1] = 1;
			} catch (OutOfMemoryError e) {
				return null;
			}
			return n;
		} else {
			iRuntime.error(205, args[0]);
			return null;
		}
	}
}

class f$args extends iFunctionClosure {				// args(x)
	vDescriptor function(vDescriptor[] args) {
		return iRuntime.argVal(args, 0).Args();
	}
}

class f$proc extends iFunctionClosure {				// proc(s, i)
	vDescriptor function(vDescriptor[] args) {
		vValue v = iRuntime.argVal(args, 0);
		vInteger i;
		if (args.length < 2) {
			i = iNew.Integer(1);
		} else {
			i = args[1].mkInteger();
		}
		if (i.value < 0 || i.value > 3) {
			iRuntime.error(205, i);
		}
		return v.Proc(i.value);
	}
}

class f$image extends iFunctionClosure {			// image(x)
	vDescriptor function(vDescriptor[] args) {
		return iNew.String(iRuntime.argVal(args, 0).image());
	}
}



class f$type extends iFunctionClosure {				// type(x)
	vDescriptor function(vDescriptor[] args) {
		return iNew.String(iRuntime.argVal(args, 0).type());
	}
}



class f$serial extends iFunctionClosure {			// serial(x)
	vDescriptor function(vDescriptor[] args) {
		return iRuntime.argVal(args, 0).Serial();
	}
}



class f$copy extends iFunctionClosure {				// copy(x)
	vDescriptor function(vDescriptor[] args) {
		return iRuntime.argVal(args, 0).Copy();
	}
}



class f$integer extends iFunctionClosure {			// integer(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkInteger();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$numeric extends iFunctionClosure {			// numeric(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkNumeric();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$real extends iFunctionClosure {				// real(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkReal();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$string extends iFunctionClosure {			// string(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkString();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$cset extends iFunctionClosure {				// cset(x)
	vDescriptor function(vDescriptor[] args) {
		try {
			return iRuntime.argVal(args, 0).mkCset();
		} catch (iError e) {
			return null; /*FAIL*/
		}
	}
}



class f$delay extends iFunctionClosure {			// delay(i)
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



class f$exit extends iFunctionClosure {				// exit(n)
	vDescriptor function(vDescriptor[] args) {
		int n = (int) vInteger.argVal(args, 0, 0);
		iRuntime.exit(n, parent);
		return null;	// not reached
	}
}

class f$function extends iClosure {				// function()
	java.util.Enumeration e;

	void nextval() {
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
	vInteger i1;
	vInteger i2;

	void nextval() {
		if (i1 == null) {
			i1 = (arguments.length >= 1) ?
				arguments[0].mkInteger(): iNew.Integer(1);
			i2 = (arguments.length >= 2) ?
				arguments[1].mkInteger(): iNew.Integer(1);
		}
		retvalue = i1;
		i1 = iNew.Integer(i1.value + i2.value);
	}
}

class f$remove extends iFunctionClosure {			// remove(s)
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

class f$rename extends iFunctionClosure {			// rename(s1,s2)
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

class f$system extends iFunctionClosure {			// system(s)
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

class f$getenv extends iFunctionClosure {			// getenv(s)

	static Hashtable env = new Hashtable();

	static {				// initialization on first call
		try {
			Process p =
				Runtime.getRuntime().exec("/usr/bin/env");
			DataInputStream d =
				new DataInputStream(p.getInputStream());
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

class f$errorclear extends iFunctionClosure {			// errorclear()
	vDescriptor function(vDescriptor[] args) {
		k$errornumber.number = null;
		k$errortext.text = null;
		k$errorvalue.value = null;
		return iNew.Null();
	}
}

class f$runerr extends iFunctionClosure {			// runerr(i,x)
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

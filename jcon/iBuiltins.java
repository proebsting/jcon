//  iBuiltins.java -- built-in functions

package rts;



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
	declare("char", 1);
	declare("cos", 1);
	declare("copy", 1);
	declare("cset", 1);
	declare("delay", 1);
	declare("delete", 2);
	declare("display", 2);
	declare("dtor", 1);
	declare("exit", 1);
	declare("exp", 1);
	declare("find", 4);
	declare("get", 1);
	declare("iand", 2);
	declare("icom", 1);
	declare("image", 1);
	declare("insert", 3);
	declare("integer", 1);
	declare("ior", 2);
	declare("ishift", 2);
	declare("ixor", 2);
	declare("key", 1);
	declare("list", 2);
	declare("log", 2);
	declare("many", 4);
	declare("match", 4);
	declare("member", 2);
	declare("move", 1);
	declare("name", 1);
	declare("numeric", 1);
	declare("ord", 1);
	declare("pop", 1);
	declare("proc", 2);
	declare("pull", 1);
	declare("push", -2);
	declare("put", -2);
	declare("read", 1);
	declare("real", 1);
	declare("repl", 2);
	declare("reverse", 1);
	declare("right", 3);
	declare("rtod", 1);
	declare("serial", 1);
	declare("set", 1);
	declare("sin", 1);
	declare("sort", 2);
	declare("sqrt", 1);
	declare("stop", -1);
	declare("string", 1);
	declare("tab", 1);
	declare("table", 1);
	declare("tan", 1);
	declare("type", 1);
	declare("upto", 4);
	declare("variable", 1);
	declare("write", -1);
	declare("writes", -1);
}


static void declare(String name, int args)
{
    try {
	iEnv.declareGlobal(name,
	    iNew.SimpleVar(name, iNew.Proc(Class.forName(PREFIX + name), args)));
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

		// do the call chain.
		for (iClosure p = parent; p != null; p = p.parent) {
			String s = p.getClass().getName();
			int j = s.indexOf('$');
			if (j >= 0) {                   // xxx$yyyyy format
			    s = s.substring(j+1);
			}
			System.out.println(s + " local identifiers:");
			p.locals();
			if (p.names == null) {
				continue;
			}
			for (int i = 0; p.names[i] != null; i++) {
				System.out.println("   " + p.names[i] + " = " + p.variables[i].image());
			}
		}

		// do the globals
		// #%#%# not sorted....
		System.out.println();
		System.out.println("global identifiers:");
		java.util.Enumeration e = iEnv.symtab.keys();
		while (e.hasMoreElements()) {
			String s = (String) e.nextElement();
			vVariable v = (vVariable) iEnv.symtab.get(s);
			System.out.println("   " + s + " = " + v.image());
		}
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
		System.exit(n);					// exit
		return null;	// not reached
	}
}

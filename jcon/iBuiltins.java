//  iBuiltins.java -- built-in functions

package rts;



public class iBuiltins extends iFile {

static final String PREFIX = "rts.f$";	// classname prefix for built-in funcs



void announce() {
	declare("abs");
	declare("acos");
	declare("asin");
	declare("atan");
	declare("char");
	declare("cos");
	declare("copy");
	declare("cset");
	declare("delay");
	declare("delete");
	declare("display");
	declare("dtor");
	declare("exit");
	declare("exp");
	declare("get");
	declare("iand");
	declare("icom");
	declare("image");
	declare("insert");
	declare("integer");
	declare("ior");
	declare("ishift");
	declare("ixor");
	declare("key");
	declare("list");
	declare("log");
	declare("member");
	declare("name");
	declare("numeric");
	declare("ord");
	declare("pop");
	declare("pull");
	declare("push");
	declare("put");
	declare("read");
	declare("real");
	declare("repl");
	declare("reverse");
	declare("right");
	declare("rtod");
	declare("serial");
	declare("set");
	declare("sin");
	declare("sort");
	declare("sqrt");
	declare("stop");
	declare("string");
	declare("table");
	declare("tan");
	declare("type");
	declare("variable");
	declare("write");
	declare("writes");
}


static void declare(String name)
{
    try {
	iEnv.declareGlobal(name,
	    iNew.SimpleVar(name, iNew.Proc(Class.forName(PREFIX + name))));
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

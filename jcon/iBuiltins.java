//  iBuiltins.java -- built-in functions

package rts;



public class iBuiltins extends iFile {

static final String PREFIX = "rts.f$";	// classname prefix for built-in funcs



void announce(iEnv env) {
	declare(env, "abs");
	declare(env, "acos");
	declare(env, "asin");
	declare(env, "atan");
	declare(env, "char");
	declare(env, "cos");
	declare(env, "copy");
	declare(env, "cset");
	declare(env, "delay");
	declare(env, "delete");
	declare(env, "dtor");
	declare(env, "exit");
	declare(env, "exp");
	declare(env, "get");
	declare(env, "iand");
	declare(env, "icom");
	declare(env, "image");
	declare(env, "insert");
	declare(env, "integer");
	declare(env, "ior");
	declare(env, "ishift");
	declare(env, "ixor");
	declare(env, "key");
	declare(env, "list");
	declare(env, "log");
	declare(env, "member");
	declare(env, "numeric");
	declare(env, "ord");
	declare(env, "pop");
	declare(env, "pull");
	declare(env, "push");
	declare(env, "put");
	declare(env, "read");
	declare(env, "real");
	declare(env, "repl");
	declare(env, "reverse");
	declare(env, "right");
	declare(env, "rtod");
	declare(env, "serial");
	declare(env, "set");
	declare(env, "sin");
	declare(env, "sort");
	declare(env, "sqrt");
	declare(env, "stop");
	declare(env, "string");
	declare(env, "table");
	declare(env, "tan");
	declare(env, "type");
	declare(env, "write");
	declare(env, "writes");
}


static void declare(iEnv env, String name)
{
    try {
	env.declareGlobal(name,
	    iNew.Proc(Class.forName(PREFIX + name), env));
    } catch (ClassNotFoundException e) {
	iRuntime.bomb("cannot declare builtin function " + name + "()");
    }
}



} // class iBuiltins



//------------------------------------------  miscellaneous functions follow



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

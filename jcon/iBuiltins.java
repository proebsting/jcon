//  iBuiltins.java -- built-in functions

package rts;



public class iBuiltins extends iFile {

static final String PREFIX = "rts.f$";	// classname prefix for built-in funcs



void announce(iEnv env) {
	declare(env, "delay");
	declare(env, "delete");
	declare(env, "exit");
	declare(env, "get");
	declare(env, "image");
	declare(env, "insert");
	declare(env, "key");
	declare(env, "list");
	declare(env, "member");
	declare(env, "pop");
	declare(env, "pull");
	declare(env, "push");
	declare(env, "put");
	declare(env, "read");
	declare(env, "repl");
	declare(env, "right");
	declare(env, "set");
	declare(env, "sort");
	declare(env, "stop");
	declare(env, "table");
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

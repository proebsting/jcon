//  iBuiltins.java -- built-in functions

import java.io.*;



class iBuiltins extends iFile {



static final String PREFIX = "f$";	// classname prefix for built-in funcs



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
	declare(env, "right");
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





//------------------------------------------  individual functions follow


class f$type extends iFunctionClosure {				// type(x)
	vDescriptor function(vDescriptor[] args) {
		if (args.length == 0) {
		    return iNew.String("null");
		} else {
		    return iNew.String(args[0].type());
		}
	}
}


class f$image extends iFunctionClosure {			// image(x)
	vDescriptor function(vDescriptor[] args) {
		if (args.length == 0) {
		    return iNew.String("&null");
		} else {
		    return iNew.String(args[0].image());
		}
	}
}


class f$right extends iFunctionClosure {			// right(s,i,s)
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		long llen = vInteger.argVal(args, 1, 1);
		String pad = vString.argVal(args, 2, " ");

		int len = (int)llen;
		if (len < 0 || (long)len != llen) {
			iRuntime.error(205, args[1]);
		}
		if (pad.length() == 0) {
			return args[0];
		}
		if (len <= s.length()) {
			return iNew.String(
				s.substring(s.length() - len, s.length()));
		}

		StringBuffer b = new StringBuffer(len + pad.length());
		int n = len - s.length();	// amount of padding needed
		while (b.length() < n) {
			b.append(pad);		// pad until sufficient
		}
		b.setLength(n);			// truncate any extra
		b.append(s);			// add original string
		
		return iNew.String(b.toString());
	}
}



class oOutput {		// common helper for write(), writes(), stop()

	// #%#%#% overly simplified: does not handle file switching

	static vDescriptor print(PrintStream p, vDescriptor[] args) {
		for (int i = 0; i < args.length; i++) {
			p.print(args[i].write());
		}
		if (args.length == 0) {
		    return iNew.Null();
		} else {
		    return args[args.length - 1];
		}
		
	}

}


class f$write extends iFunctionClosure {			// write(...)
	vDescriptor function(vDescriptor[] args) {
		vDescriptor result = oOutput.print(System.out, args);
		System.out.println();
		return result;
	}
}


class f$writes extends iFunctionClosure {			// writes(...)
	vDescriptor function(vDescriptor[] args) {
		return oOutput.print(System.out, args);
	}
}


class f$stop extends iFunctionClosure {				// stop(...)
	vDescriptor function(vDescriptor[] args) {
		System.out.flush();				// flush stdout
		oOutput.print(System.err, args);		// write msg
		System.err.println();
		System.exit(1);					// exit
		return null;	// not reached
	}
}


class f$exit extends iFunctionClosure {				// exit(n)
	vDescriptor function(vDescriptor[] args) {
		int n = (int) vInteger.argVal(args, 0, 0);
		System.exit(n);					// exit
		return null;	// not reached
	}
}


class f$table extends iFunctionClosure {			// table(x)
	vDescriptor function(vDescriptor[] args) {
		vValue x;
		if (args.length > 1) {
			x = (vValue) args[0].deref();
		} else {
			x = iNew.Null();
		}
		return iNew.Table(x);
	}
}

class f$delete extends iFunctionClosure {			// delete(X,x)
	vDescriptor function(vDescriptor[] args) {
		if (args.length != 2) {
			iRuntime.error(122);
			return null;
		} else {
			return args[0].Delete(args[1]);
		}
	}
}

class f$member extends iFunctionClosure {			// member(X,x)
	vDescriptor function(vDescriptor[] args) {
		if (args.length != 2) {
			iRuntime.error(122);
			return null;
		} else {
			return args[0].Member(args[1]);
		}
	}
}

class f$insert extends iFunctionClosure {			// insert(X,x,y)
	vDescriptor function(vDescriptor[] args) {
		if (args.length != 3) {
			iRuntime.error(122);
			return null;
		} else {
			return args[0].Insert(args[1], args[2]);
		}
	}
}

class f$key extends iClosure {					//  key(X)
	void nextval() {
		retvalue = arguments[0].Key(this);
	}
	String tfmt() { return "key($1)"; }
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



class f$list extends iFunctionClosure {				// list(i, x)
	vDescriptor function(vDescriptor[] args) {
		vValue x;
		int i = (int) vInteger.argVal(args, 0, 0);
		if (args.length > 1) {
			x = (vValue) args[1];
		} else {
			x = iNew.Null();
		}
		return iNew.List(i, x);
	}
}



class f$push extends iFunctionClosure {				// push(L, x...)
	vDescriptor function(vDescriptor[] args) {
		if (args.length == 0) {
			iRuntime.error(108, iNew.Null());
		} else if (args.length == 1) {
			args[0].Push(iNew.Null());
		} else {
			for (int i = 1; i < args.length; i++)
				args[0].Push(args[i]);
		}
		return args[0];
	}
}



class f$pull extends iFunctionClosure {				// pull(L)
	vDescriptor function(vDescriptor[] args) {
		if (args.length == 0) {
			iRuntime.error(108, iNew.Null());
			return null;
		} else {
			return args[0].Pull();
		}
	}
}



class f$pop extends iFunctionClosure {				// pop(L)
	vDescriptor function(vDescriptor[] args) {
		if (args.length == 0) {
			iRuntime.error(108, iNew.Null());
			return null;
		} else {
			return args[0].Pop();
		}
	}
}



class f$get extends iFunctionClosure {				// get(L)
	vDescriptor function(vDescriptor[] args) {
		if (args.length == 0) {
			iRuntime.error(108, iNew.Null());
			return null;
		} else {
			return args[0].Get();
		}
	}
}



class f$put extends iFunctionClosure {				// put(L, x...)
	vDescriptor function(vDescriptor[] args) {
		if (args.length == 0) {
			iRuntime.error(108, iNew.Null());
		} else if (args.length == 1) {
			args[0].Put(iNew.Null());
		} else {
			for (int i = 1; i < args.length; i++)
				args[0].Put(args[i]);
		}
		return args[0];
	}
}

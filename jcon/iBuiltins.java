//  iBuiltins.java -- built-in functions

class iBuiltins extends iFile {


static final String PREFIX = "f$";	// classname prefix for built-in funcs


void announce(iEnv env) {
	declare(env, "image");
	declare(env, "right");
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


class f$write extends iFunctionClosure {			// write(...)
	// #%#%#% overly simplified code for "write"
	vDescriptor function(vDescriptor[] args) {
		for (int i = 0; i < args.length; i++) {
			System.out.print(args[i].write());
		}
		System.out.println();
		return args[args.length-1];
	}
}


class f$writes extends iFunctionClosure {			// writes(...)
	// #%#%#% overly simplified code for "writes"
	vDescriptor function(vDescriptor[] args) {
		for (int i = 0; i < args.length; i++) {
			System.out.print(args[i].write());
		}
		return args[args.length-1];
	}
}

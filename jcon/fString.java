//  fString.java -- functions operating on Icon strings

package rts;



class f$char extends iFunctionClosure {				// char()
	vDescriptor function(vDescriptor[] args) {
		long i = vInteger.argVal(args, 0);
		if (i < 0 || i > Character.MAX_VALUE) {
			iRuntime.error(205, args[0]);
		}
		return iNew.String((char) i);
	}
}



class f$ord extends iFunctionClosure {				// ord()
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		if (s.length() != 1) {
			iRuntime.error(205, args[0]);
		}
		return iNew.Integer(s.charAt(0));
	}
}



class f$repl extends iFunctionClosure {				// repl()
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		long i = vInteger.argVal(args, 1);
		if (i < 0) {
			iRuntime.error(205, args[1]);
		}
		String t = "";
		for (; i > 0; i--) {
			t += s;
		}
		return iNew.String(t);
	}
}



class f$reverse extends iFunctionClosure {			// reverse()
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		StringBuffer b = new StringBuffer(s.length());
		for (int i = s.length() - 1; i >= 0; i--) {
			b.append(s.charAt(i));
		}
		return iNew.String(b.toString());
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

//  fString.java -- functions operating on Icon strings

package rts;



class f$repl extends iFunctionClosure {				// repl()
	vDescriptor function(vDescriptor[] args) {
		String s = iRuntime.argVal(args, 0, 103).mkString().value;
		long i = iRuntime.argVal(args, 1, 101).mkInteger().value;
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

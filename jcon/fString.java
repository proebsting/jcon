//  fString.java -- functions operating on Icon strings

package rts;

class fString {} // dummy



class f$char extends iValueClosure {				// char()
	vDescriptor function(vDescriptor[] args) {
		long i = vInteger.argVal(args, 0);
		if (i < 0 || i > vCset.MAX_VALUE) {
			iRuntime.error(205, args[0]);
		}
		return iNew.String((char) i);
	}
}



class f$ord extends iValueClosure {				// ord()
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		if (s.length() != 1) {
			iRuntime.error(205, args[0]);
		}
		return iNew.Integer(s.charAt(0));
	}
}



class f$repl extends iValueClosure {				// repl()
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



class f$reverse extends iValueClosure {			// reverse()
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		StringBuffer b = new StringBuffer(s.length());
		for (int i = s.length() - 1; i >= 0; i--) {
			b.append(s.charAt(i));
		}
		return iNew.String(b.toString());
	}
}



class f$left extends iValueClosure {			// left(s,i,s)
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
			return iNew.String(s.substring(0,len));
		}

		StringBuffer b = new StringBuffer(len + pad.length());
		b.append(s);			// original string
		b.append(pad.substring((len-s.length()) % pad.length(), pad.length()));
		while (b.length() < len) {
			b.append(pad);		// pad until sufficient
		}
		b.setLength(len);		// truncate any extra
		
		return iNew.String(b.toString());
	}
}

class f$right extends iValueClosure {			// right(s,i,s)
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

class f$center extends iValueClosure {			// right(s,i,s)
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		long llen = vInteger.argVal(args, 1, 1);
		String pad = vString.argVal(args, 2, " ");

		int len = (int)llen;
		if (len < 0 || (long)len != llen) {
			iRuntime.error(205, args[1]);
		}
		if (pad.length() == 0) {
			pad = " ";
		}
		if (len <= s.length()) {
			int start = (s.length()-len+1)/2;
			return iNew.String(s.substring(start, start+len));
		}

		int n = (len-s.length())/2;	// amount of padding needed
		StringBuffer b = new StringBuffer(len + pad.length());
		while (b.length() < n) {
			b.append(pad);		// pad until sufficient
		}
		b.setLength(n);			// truncate any extra
		b.append(s);			// add original string
		b.append(pad.substring((len-s.length()-n) % pad.length(), pad.length()));
		while (b.length() < len) {
			b.append(pad);		// pad until sufficient
		}
		b.setLength(len);		// truncate any extra
		
		return iNew.String(b.toString());
	}
}

class f$trim extends iValueClosure {				// trim(s,c)
	vDescriptor function(vDescriptor[] args) {
		String s = vString.argVal(args, 0);
		vCset c = vCset.argVal(args, 1, ' ', ' ');

		int i;
		for (i = s.length(); i > 0; i--) {
			if (!c.member(s.charAt(i-1))) {
				return iNew.String(s.substring(0,i));
			}
		}
		return iNew.String("");
	}
}

class f$map extends iValueClosure {				// map(s1,s2,s3)

	static char[] map, initmap;
	static String s2prev, s3prev;

	static {
		initmap = new char[(int) vCset.MAX_VALUE + 1];
		for (char i = 0; i <= vCset.MAX_VALUE; i++) {
			initmap[i] = i;
		}
	}

	vDescriptor function(vDescriptor[] args) {
		String s1 = vString.argVal(args, 0);
		String s2 = vString.argVal(args, 1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		String s3 = vString.argVal(args, 2, "abcdefghijklmnopqrstuvwxyz");

		if (s2.length() != s3.length()) {
			iRuntime.error(208);
		}

		if (s2 != s2prev || s3 != s3prev) {
			map = new char[(int) vCset.MAX_VALUE + 1];
			System.arraycopy(initmap, 0, map, 0, map.length);
			for (int i = 0; i < s2.length(); i++) {
				map[s2.charAt(i)] = s3.charAt(i);
			}
			s2prev = s2;
			s3prev = s3;
		}

		char[] s = new char[s1.length()];
		for (int i = 0; i < s1.length(); i++) {
			s[i] = map[s1.charAt(i)];
		}
		return iNew.String(new String(s));
	}
}

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
		vString s = vString.argDescr(args, 0);
		if (s.length() != 1) {
			iRuntime.error(205, args[0]);
		}
		return iNew.Integer(s.charAt(0));
	}
}



class f$repl extends iValueClosure {				// repl()
	vDescriptor function(vDescriptor[] args) {
		byte[] s = vString.argDescr(args, 0).getBytes();
		long ln = vInteger.argVal(args, 1, 1);
		int n = (int) vInteger.argVal(args, 1);
		if (n < 0 || (long)n != ln) {
			iRuntime.error(205, args[1]);
		}
		int w = s.length;		// width of one item
		int z = n * w;			// total length
		byte[] t = new byte[z];
		for (int i = 0; i < w; i++) {
		    byte c = s[i];
		    for (int k = i; k < z; k += w) {
			t[k] = c;
		    }
		}
		return iNew.String(t);
	}
}



class f$reverse extends iValueClosure {			// reverse()
	vDescriptor function(vDescriptor[] args) {
		byte[] s = vString.argDescr(args, 0).getBytes();
		byte[] t = new byte[s.length];
		int n = s.length;
		for (int i = 0; i < n; i++) {
		    t[i] = s[n - i - 1];
		}
		return iNew.String(t);
	}
}



class f$left extends iValueClosure {			// left(s,i,s)
	static vString space = iNew.String(' ');
	vDescriptor function(vDescriptor[] args) {
		vString s = vString.argDescr(args, 0);
		long llen = vInteger.argVal(args, 1, 1);
		byte[] pad = vString.argDescr(args, 2, space).getBytes();

		int dstlen = (int)llen;
		if (dstlen < 0 || (long)dstlen != llen) {
			iRuntime.error(205, args[1]);
		}
		if (dstlen <= s.length()) {
			return iNew.String(s, 1, dstlen + 1);
		}
		if (pad.length == 0) {
			pad = space.getBytes();
		}

		byte[] src = s.getBytes();
		byte[] dst = new byte[dstlen];
		int srclen = src.length;
		int padlen = pad.length;

		for (int i = padlen - 1; i >= 0; i--) {
		   byte c = pad[i];
		   for (int k = dstlen - padlen + i; k >= srclen; k -= padlen) {
		      dst[k] = c;
		   }
		}

		System.arraycopy(src, 0, dst, 0, src.length);
		return iNew.String(dst);
	}
}

class f$right extends iValueClosure {			// right(s,i,s)
	static vString space = iNew.String(' ');
	vDescriptor function(vDescriptor[] args) {
		vString s = vString.argDescr(args, 0);
		long llen = vInteger.argVal(args, 1, 1);
		byte[] pad = vString.argDescr(args, 2, space).getBytes();

		int dstlen = (int)llen;
		if (dstlen < 0 || (long)dstlen != llen) {
			iRuntime.error(205, args[1]);
		}
		int srclen = s.length();
		if (dstlen <= srclen) {
			return iNew.String(s, srclen + 1 - dstlen, srclen + 1);
		}
		if (pad.length == 0) {
			pad = space.getBytes();
		}
		int padlen = pad.length;

		byte[] src = s.getBytes();
		byte[] dst = new byte[dstlen];
		int offset = dstlen - srclen;

		for (int i = 0; i < padlen; i++) {
		    byte c = pad[i];
		    for (int k = i; k < offset; k += padlen) {
			dst[k] = c;
		    }
		}

		System.arraycopy(src, 0, dst, offset, srclen);
		return iNew.String(dst);
	}
}

class f$center extends iValueClosure {			// center(s,i,s)
	static vString space = iNew.String(' ');
	vDescriptor function(vDescriptor[] args) {
		vString s = vString.argDescr(args, 0);
		long llen = vInteger.argVal(args, 1, 1);
		byte[] pad = vString.argDescr(args, 2, space).getBytes();

		int dstlen = (int)llen;
		if (dstlen < 0 || (long)dstlen != llen) {
			iRuntime.error(205, args[1]);
		}
		int srclen = s.length();

		if (dstlen <= srclen) {
			int offset = (srclen - dstlen + 1) / 2;
			return iNew.String(s, offset + 1, offset + dstlen + 1);
		}

		if (pad.length == 0) {
			pad = space.getBytes();
		}
		int padlen = pad.length;

		byte[] src = s.getBytes();
		byte[] dst = new byte[dstlen];
		int offset = (dstlen - srclen) / 2;

		// pad on left
		for (int i = 0; i < padlen; i++) {
		    byte c = pad[i];
		    for (int k = i; k < offset; k += padlen) {
			dst[k] = c;
		    }
		}

		// pad on right
		int rmar = dstlen - srclen - offset;
		for (int i = padlen - 1; i >= 0; i--) {
		   byte c = pad[i];
		   for (int k = dstlen - padlen + i; k >= rmar; k -= padlen) {
		      dst[k] = c;
		   }
		}

		// now copy string atop background
		System.arraycopy(src, 0, dst, offset, srclen);
		return iNew.String(dst);
	}
}

class f$trim extends iValueClosure {				// trim(s,c)
	static vCset defset = iNew.Cset(' ', ' ');
	vDescriptor function(vDescriptor[] args) {
		vString s = vString.argDescr(args, 0);
		vCset c = vCset.argVal(args, 1, defset);

		byte[] b = s.getBytes();
		int i;
		for (i = b.length - 1; i >= 0; i--) {
			if (!c.member((char)b[i])) {
				break;
			}
		}
		return iNew.String(s, 1, i + 2);
	}
}

class f$map extends iValueClosure {				// map(s1,s2,s3)

	static int[] map, initmap;
	static vString s2def, s3def;
	static vString s2prev, s3prev;

	static {
		s2def = iNew.String("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		s3def = iNew.String("abcdefghijklmnopqrstuvwxyz");
		initmap = new int[(int) vCset.MAX_VALUE + 1];
		for (int i = 0; i <= vCset.MAX_VALUE; i++) {
			initmap[i] = i;
		}
	}

	vDescriptor function(vDescriptor[] args) {
		vString s1 = vString.argDescr(args, 0);
		vString s2 = vString.argDescr(args, 1, s2def);
		vString s3 = vString.argDescr(args, 2, s3def);

		if (s2.length() != s3.length()) {
			iRuntime.error(208);
		}

		if (s2 != s2prev || s3 != s3prev) {
			map = new int[(int) vCset.MAX_VALUE + 1];
			System.arraycopy(initmap, 0, map, 0, map.length);
			for (int i = 0; i < s2.length(); i++) {
				map[s2.charAt(i)] = s3.charAt(i);
			}
			s2prev = s2;
			s3prev = s3;
		}

		byte[] b = new byte[s1.length()];
		for (int i = 0; i < s1.length(); i++) {
			b[i] = (byte)map[s1.charAt(i)];
		}
		return iNew.String(b);
	}
}

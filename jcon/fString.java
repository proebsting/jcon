//  fString.java -- functions operating on Icon strings

package rts;

class fString {} // dummy



class f$char extends vProc1 {				// char()
    public vDescriptor Call(vDescriptor a) {
	long i = a.mkInteger().value;
	if (i < 0 || i > vCset.MAX_VALUE) {
	    iRuntime.error(205, a);
	}
	return vString.New((char) i);
    }
}



class f$ord extends vProc1 {				// ord()
    public vDescriptor Call(vDescriptor a) {
	vString s = a.mkString();
	if (s.length() != 1) {
	    iRuntime.error(205, a);
	}
	return vInteger.New(s.charAt(0));
    }
}



class f$repl extends vProc2 {				// repl()
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	byte[] s = a.mkString().getBytes();
	long ln = b.isnull() ? 1 : b.mkInteger().value;
	int n = (int) ln;
	if (n < 0 || (long)n != ln) {
	    iRuntime.error(205, b);
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
	return vString.New(t);
    }
}



class f$reverse extends vProc1 {				// reverse()
    public vDescriptor Call(vDescriptor a) {
	byte[] s = a.mkString().getBytes();
	byte[] t = new byte[s.length];
	int n = s.length;
	for (int i = 0; i < n; i++) {
	    t[i] = s[n - i - 1];
	}
	return vString.New(t);
    }
}



class f$left extends vProc3 {				// left(s,i,s)
    static byte[] onespace = { (byte) ' ' };

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	vString s = a.mkString();
	long llen = b.isnull() ? 1 : b.mkInteger().value;
	byte[] pad = c.isnull() ? onespace : c.mkString().getBytes();

	int dstlen = (int)llen;
	if (dstlen < 0 || (long)dstlen != llen) {
	    iRuntime.error(205, b);
	}
	if (dstlen <= s.length()) {
	    return vString.New(s, 1, dstlen + 1);
	}
	if (pad.length == 0) {
	    pad = onespace;
	}

	byte[] src = s.getBytes();
	byte[] dst = new byte[dstlen];
	int srclen = src.length;
	int padlen = pad.length;

	for (int i = padlen - 1; i >= 0; i--) {
	    byte ch = pad[i];
	    for (int k = dstlen - padlen + i; k >= srclen; k -= padlen) {
		dst[k] = ch;
	    }
	}

	System.arraycopy(src, 0, dst, 0, src.length);
	return vString.New(dst);
    }
}



class f$right extends vProc3 {				// right(s,i,s)
    static byte[] onespace = { (byte) ' ' };

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	vString s = a.mkString();
	long llen = b.isnull() ? 1 : b.mkInteger().value;
	byte[] pad = c.isnull() ? onespace : c.mkString().getBytes();

	int dstlen = (int)llen;
	if (dstlen < 0 || (long)dstlen != llen) {
	    iRuntime.error(205, b);
	}
	int srclen = s.length();
	if (dstlen <= srclen) {
	    return vString.New(s, srclen + 1 - dstlen, srclen + 1);
	}
	if (pad.length == 0) {
	    pad = onespace;
	}
	int padlen = pad.length;

	byte[] src = s.getBytes();
	byte[] dst = new byte[dstlen];
	int offset = dstlen - srclen;

	for (int i = 0; i < padlen; i++) {
	    byte ch = pad[i];
	    for (int k = i; k < offset; k += padlen) {
		dst[k] = ch;
	    }
	}

	System.arraycopy(src, 0, dst, offset, srclen);
	return vString.New(dst);
    }
}



class f$center extends vProc3 {				// center(s,i,s)
    static byte[] onespace = { (byte) ' ' };

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	vString s = a.mkString();
	long llen = b.isnull() ? 1 : b.mkInteger().value;
	byte[] pad = c.isnull() ? onespace : c.mkString().getBytes();

	int dstlen = (int)llen;
	if (dstlen < 0 || (long)dstlen != llen) {
	    iRuntime.error(205, b);
	}
	int srclen = s.length();

	if (dstlen <= srclen) {
	    int offset = (srclen - dstlen + 1) / 2;
	    return vString.New(s, offset + 1, offset + dstlen + 1);
	}

	if (pad.length == 0) {
	    pad = onespace;
	}
	int padlen = pad.length;

	byte[] src = s.getBytes();
	byte[] dst = new byte[dstlen];
	int offset = (dstlen - srclen) / 2;

	// pad on left
	for (int i = 0; i < padlen; i++) {
	    byte ch = pad[i];
	    for (int k = i; k < offset; k += padlen) {
		dst[k] = ch;
	    }
	}

	// pad on right
	int rmar = dstlen - srclen - offset;
	for (int i = padlen - 1; i >= 0; i--) {
	    byte ch = pad[i];
	    for (int k = dstlen - padlen + i; k >= rmar; k -= padlen) {
		dst[k] = ch;
	    }
	}

	// finally, copy string
	System.arraycopy(src, 0, dst, offset, srclen);
	return vString.New(dst);
    }
}



class f$trim extends vProc2 {				// trim(s,c)
    static vCset defset = vCset.New(' ');

    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	vString s = a.mkString();
	byte[] t = s.getBytes();
	vCset c = b.isnull() ? defset : b.mkCset();
	int i;
	for (i = t.length - 1; i >= 0; i--) {
	    if (!c.member(t[i])) {
		break;
	    }
	}
	return vString.New(s, 1, i + 2);
    }
}



class f$map extends vProc3 {				// map(s1,s2,s3)

    static int[] map, initmap;
    static vString s2def, s3def;
    static vString s2prev, s3prev;
    static {
	s2def = vString.New("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	s3def = vString.New("abcdefghijklmnopqrstuvwxyz");
	initmap = new int[(int) vCset.MAX_VALUE + 1];
	for (int i = 0; i <= vCset.MAX_VALUE; i++) {
	    initmap[i] = i;
	}
    }

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	vString s1 = a.mkString();
	vString s2 = b.isnull() ? s2def : b.mkString();
	vString s3 = c.isnull() ? s3def : b.mkString();
	byte b1[] = s1.getBytes();
	byte b2[] = s2.getBytes();
	byte b3[] = s3.getBytes();

	int n = b2.length;
	if (n != b3.length) {
	    iRuntime.error(208);
	}

	if (s2 != s2prev || s3 != s3prev) {
	    map = new int[(int) vCset.MAX_VALUE + 1];
	    System.arraycopy(initmap, 0, map, 0, map.length);
	    for (int i = 0; i < n; i++) {
		map[b2[i] & 0xFF] = b3[i];
	    }
	    s2prev = s2;
	    s3prev = s3;
	}

	n = b1.length;
	byte[] s = new byte[n];
	for (int i = 0; i < n; i++) {
	    s[i] = (byte)map[b1[i] & 0xFF];
	}
	return vString.New(s);
    }
}

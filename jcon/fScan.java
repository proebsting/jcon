//  fScan.java -- built-in string scanning functions

package rts;



final class fScan {

    //  fScan.subj(arg) -- return explicit or defaulted subject argument

    static vString subj(vDescriptor arg) {
	return arg.isnull() ? k$subject.get() : arg.mkString();
    }

    //  fScan.pos(subjarg, subjval, posarg) -- return posEq of pos argument
    //
    //  subjval is the value returned by fScan.subj (above).
    //  Note that the default pos value depends on whether subj was defaulted.

    static int pos(
		vDescriptor subjarg, vString subjval, vDescriptor posarg) {
	if (posarg.isnull()) {
	    if (subjarg.isnull()) {
		return (int) k$pos.get().value;
	    } else {
		return 1;
	    }
	} else {
	    return subjval.posEq(posarg.mkInteger().value);
	}
    }
 
} // class fScan



final class f$pos extends vProc1 {			// pos(i)
    public vDescriptor Call(vDescriptor a) {
	long i = a.mkInteger().value;
	vString s = k$subject.get();
	vInteger p = k$pos.get();
	if (s.posEq(i) == p.value) {
	    return p;
	}
	return null;
    }
}



final class f$any extends vProc4 {			// any(c,s,i1,i2)

    public vDescriptor Call(
	    vDescriptor a, vDescriptor b, vDescriptor c, vDescriptor d) {
	vCset cs = a.mkCset();
	vString s = fScan.subj(b);
	int i1 = fScan.pos(b, s, c);
	int i2 = s.posEq(d.isnull() ? 0 : d.mkInteger().value);

	if (i1 == 0 || i2 == 0) {
	    return null;
	}
	if (i1 > i2) {
	    int tmp = i1;
	    i1 = i2;
	    i2 = tmp;
	}
	if (i1 < i2 && cs.member(s.charAt(i1-1))) {
	    return vInteger.New(i1+1);
	}
	return null;
    }

    // additional entry point for common case
    public vDescriptor Call(vDescriptor a) {
	vCset cs = a.mkCset();
	vString subj = k$subject.get();			// &subject
	int zpos = (int) k$pos.get().value - 1;		// zero-based &pos
	if (zpos < subj.length() && cs.member(subj.charAt(zpos))) {
	    return vInteger.New(zpos + 2);
	} else {
	    return null;
	}
    }
}



final class f$many extends vProc4 {			// many(c,s,i1,i2)

    public vDescriptor Call(
	    vDescriptor a, vDescriptor b, vDescriptor c, vDescriptor d) {
	vCset cs = a.mkCset();
	vString s = fScan.subj(b);
	int i1 = fScan.pos(b, s, c);
	int i2 = s.posEq(d.isnull() ? 0 : d.mkInteger().value);

	if (i1 == 0 || i2 == 0) {
	    return null;
	}
	if (i1 > i2) {
	    int tmp = i1;
	    i1 = i2;
	    i2 = tmp;
	}
	if (i1 >= i2) {
	    return null;
	}
	byte t[] = s.getBytes();
	int i;
	for (i = i1; i < i2; i++) {
	    if (!cs.member(t[i-1])) {
		break;
	    }
	}
	if (i == i1) {
	    return null;
	} else {
	    return vInteger.New(i);
	}
    }

    // additional entry point for common case
    public vDescriptor Call(vDescriptor a) {
	vCset cs = a.mkCset();
	vString subj = k$subject.get();			// &subject
	int zpos = (int) k$pos.get().value - 1;		// zero-based &pos
	long zmax = subj.length();
	byte t[] = subj.getBytes();
	if (zpos >= zmax || !cs.member(t[zpos])) {
	    return null;
	}
	while (++zpos < zmax && cs.member(t[zpos])) {
	    ;
	}
	return vInteger.New(zpos + 1);
    }
}



final class f$match extends vProc4 {			// match(s1,s2,i1,i2)
    public vDescriptor Call(
	    vDescriptor a, vDescriptor b, vDescriptor c, vDescriptor d) {
	vString s1 = a.mkString();
	vString s2 = fScan.subj(b);
	int i1 = fScan.pos(b, s2, c);
	int i2 = s2.posEq(d.isnull() ? 0 : d.mkInteger().value);

	if (i1 == 0 || i2 == 0) {
	    return null;
	}
	if (i1 > i2) {
	    int tmp = i1;
	    i1 = i2;
	    i2 = tmp;
	}

	int len1 = s1.length();
	int len2 = s2.length();

	if (i1 > i2 - len1) {
	    return null;
	}
	if (len2 < i1 + len1 - 1) {
	    return null;
	}
	if (s1.matches(s2, i1 - 1)) {
	    return vInteger.New(i1 + len1);
	} else {
	    return null;
	}
    }
}



final class f$find extends vProc4 {			// find(s1,s2,i1,i2)
    public vDescriptor Call(
	vDescriptor a, vDescriptor b, vDescriptor c, vDescriptor d) {

	final vString s1 = a.mkString();
	final vString s2 = fScan.subj(b);
	final int i1 = fScan.pos(b, s2, c);
	final int i2 = s2.posEq(d.isnull() ? 0 : d.mkInteger().value);
	if (i1 == 0 || i2 == 0) {	// if posn out of range
	    return null;
	}
	final int p1;
	final int p2;
	if (i1 <= i2) {
	    p1 = i1;
	    p2 = i2;
	} else {
	    p1 = i2;
	    p2 = i1;
	}

	return new vClosure() {
	    int i = p1;
	    public vDescriptor Resume() {
		int lim = p2 - s1.length();
		while (i <= lim) {
		    if (s1.matches(s2, i - 1)) {
			retval = vInteger.New(i++);
			return this;
		    }
		    i++;
		}
	    return null;
	    }

	}.Resume();
    }
}



final class f$upto extends vProc4 {			// upto(c,s2,i1,i2)
    public vDescriptor Call(
	vDescriptor a, vDescriptor b, vDescriptor c, vDescriptor d) {

	final vCset cs = a.mkCset();
	final vString s = fScan.subj(b);
	final int i1 = fScan.pos(b, s, c);
	final int i2 = s.posEq(d.isnull() ? 0 : d.mkInteger().value);
	if (i1 == 0 || i2 == 0) {
	    return null;
	}
	final int p1;
	final int p2;
	if (i1 <= i2) {
	    p1 = i1;
	    p2 = i2;
	} else {
	    p1 = i2;
	    p2 = i1;
	}

	return new vClosure() {
	    byte[] b = s.getBytes();
	    int i = p1;
	    public vDescriptor Resume() {
		for (; i < p2; i++) {
		    if (cs.member(b[i-1])) {
			i++;
			retval = vInteger.New(i-1);
			return this;
		    }
		}
		return null;
	    }
	}.Resume();
    }

    // additional entry point for common case
    public vDescriptor Call(vDescriptor a) {
	final vCset cs = a.mkCset();
	final vString subj = k$subject.get();		// &subject
	int zpos = (int) k$pos.get().value - 1;		// zero-based &pos
	final byte t[] = subj.getBytes();

	while (true) {
	    if (zpos >= t.length) {
		return null; /*FAIL*/
	    }
	    if (cs.member(t[zpos])) {
		break;
	    }
	    zpos++;
	}
	final int zstart = zpos;

	return new vClosure() {
	    { retval = vInteger.New(zstart + 1); }
	    int i = zstart;
	    public vDescriptor Resume() {
		while (++i < t.length) {
		    if (cs.member(t[i])) {
			return vInteger.New(i+1);
		    }
		}
		return null;
	    }
	};
    }
}



final class f$bal extends vProc6 {			// bal(c1,c2,c3,s,i1,i2)
    static vCset c1def = vCset.New(0, vCset.MAX_VALUE);
    static vCset c2def = vCset.New('(');
    static vCset c3def = vCset.New(')');

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
	vDescriptor d, vDescriptor e, vDescriptor f) {

	final vCset c1 = a.isnull() ? c1def : a.mkCset();
	final vCset c2 = b.isnull() ? c2def : b.mkCset();
	final vCset c3 = c.isnull() ? c3def : c.mkCset();
	final vString s = fScan.subj(d);
	final int i1 = fScan.pos(d, s, e);
	final int i2 = s.posEq(f.isnull() ? 0 : f.mkInteger().value);
	if (i1 == 0 || i2 == 0) {
	    return null;
	}
	final int p1, p2;
	if (i1 <= i2) {
	    p1 = i1;
	    p2 = i2;
	} else {
	    p1 = i2;
	    p2 = i1;
	}

	return new vClosure() {
	    int i = p1;
	    byte[] b = s.getBytes();
	    int balance = 0;

	    public vDescriptor Resume() {
		for (; i < p2; i++) {
		    if (balance == 0 && c1.member(b[i-1])) {
			i = i+1;
			retval = vInteger.New(i-1);
			return this;
		    }
		    if (c2.member(s.charAt(i-1))) {
			balance++;
		    } else if (c3.member(b[i-1])) {
			balance--;
			if (balance < 0) {
			    return null;
			}
		    }
		}
		return null;
	    }
	}.Resume();
    }
}



final class f$move extends vProc1 {			// move(j)
    public vDescriptor Call(vDescriptor a) {
	final vInteger oldpos = k$pos.get();
	final int i = (int) oldpos.value;
	final int j = (int) a.mkInteger().value;
	final vString s = k$subject.get();
	int k = i + j - 1;
	if (k < 0 || k > s.length()) {
	    return null;
	} 
	k$pos.set(i + j);
	return new vClosure() {
	    {
		if (j >= 0) {
		    retval = vString.New(s, i, i + j);
		} else {
		    retval = vString.New(s, i + j, i);
		}
	    }
	    public vDescriptor Resume() {
		k$pos.set(oldpos.value);
		return null;
	    }
	};
    }
}



final class f$tab extends vProc1 {			// tab(j)
    public vDescriptor Call(vDescriptor a) {
	final vInteger oldpos = k$pos.get();
	final vString s = k$subject.get();
	final int i = (int) oldpos.value;
	final int j = (int) s.posEq(a.mkInteger().value);
	if (j == 0) {
	    return null;
	}
	k$pos.set(j);
	return new vClosure() {
	    {
		if (i < j) {
		    retval = vString.New(s, i, j);
		} else {
		    retval = vString.New(s, j, i);
		}
	    }
	    public vDescriptor Resume() {
		k$pos.set(oldpos.value);
		return null;
	    }
	};
    }
}

// builtin string scanning functions

package rts;

class fScanning {} // dummy



class f$pos extends iValueClosure {			// pos(i)
    vDescriptor function(vDescriptor[] args) {
	long i = vInteger.argVal(args, 0);
	vString s = (vString) k$subject.self.deref();
	vInteger p = (vInteger) k$pos.self.deref();
	if (s.posEq(i) == p.value) {
	    return k$pos.self.deref();
	}
	return null;
    }
}



class f$any extends iValueClosure {			// any(c,s,i1,i2)
    vDescriptor function(vDescriptor[] args) {
	vCset c = vCset.argVal(args, 0);
	vString s = iRuntime.argSubject(args, 1);
	int i1 = s.posEq(iRuntime.argPos(args, 2));
	int i2 = s.posEq(vInteger.argVal(args, 3, 0));

	if (i1 == 0 || i2 == 0) {
	    return null;
	}
	if (i1 > i2) {
	    int tmp = i1;
	    i1 = i2;
	    i2 = tmp;
	}
	if (i1 < i2 && c.member(s.charAt(i1-1))) {
	    return vInteger.New(i1+1);
	}
	return null;
    }
}



class f$many extends iValueClosure {			// many(c,s,i1,i2)
    vDescriptor function(vDescriptor[] args) {
	vCset c = vCset.argVal(args, 0);
	vString s = iRuntime.argSubject(args, 1);
	int i1 = s.posEq(iRuntime.argPos(args, 2));
	int i2 = s.posEq(vInteger.argVal(args, 3, 0));

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
	byte b[] = s.getBytes();
	int i;
	for (i = i1; i < i2; i++) {
	    if (!c.member(b[i-1])) {
		break;
	    }
	}
	if (i == i1) {
	    return null;
	} else {
	    return vInteger.New(i);
	}
    }
}



class f$match extends iValueClosure {			// match(s1,s2,i1,i2)
    vDescriptor function(vDescriptor[] args) {
	vString s1 = vString.argDescr(args, 0);
	vString s2 = iRuntime.argSubject(args, 1);
	int i1 = s2.posEq(iRuntime.argPos(args, 2));
	int i2 = s2.posEq(vInteger.argVal(args, 3, 0));

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



class f$find extends iClosure {				// find(s1,s2,i1,i2)

    vString s1;
    vString s2;
    int i1;
    int i2;

    public vDescriptor nextval() {

	if (PC == 1) {
	    for (int i = 0; i < arguments.length; i++) {
		arguments[i] = arguments[i].deref();
	    }
	    s1 = vString.argDescr(arguments, 0);
	    s2 = iRuntime.argSubject(arguments, 1);
	    i1 = s2.posEq(iRuntime.argPos(arguments, 2));
	    i2 = s2.posEq(vInteger.argVal(arguments, 3, 0));
	    if (i1 == 0 || i2 == 0) {
		return null;
	    }
	    if (i1 > i2) {
		int tmp = i1;
		i1 = i2;
		i2 = tmp;
	    }
	    PC = 2;
	}

	int lim = i2 - s1.length();
	while (i1 <= lim) {
	    if (s1.matches(s2, i1 - 1)) {
		return vInteger.New(i1++);
	    }
	    i1++;
	}

	return null;
    }
}



class f$upto extends iClosure {				// upto(c,s2,i1,i2)

    vCset c;
    vString s;
    int i1;
    int i2;
    byte[] b;

    public vDescriptor nextval() {

	if (PC == 1) {
	    for (int i = 0; i < arguments.length; i++) {
		arguments[i] = arguments[i].deref();
	    }
	    c = vCset.argVal(arguments, 0);
	    s = iRuntime.argSubject(arguments, 1);
	    i1 = s.posEq(iRuntime.argPos(arguments, 2));
	    i2 = s.posEq(vInteger.argVal(arguments, 3, 0));
	    if (i1 == 0 || i2 == 0) {
		return null;
	    }
	    if (i1 > i2) {
		int tmp = i1;
		i1 = i2;
		i2 = tmp;
	    }
	    b = s.getBytes();
	    PC = 2;
	}

	for (; i1 < i2; i1++) {
	    if (c.member(b[i1-1])) {
		i1 = i1+1;
		return vInteger.New(i1-1);
	    }
	}
	return null;
    }
}



class f$bal extends iClosure {				// bal(c1,c2,c3,s,i1,i2)

    vCset c1;
    vCset c2;
    vCset c3;
    vString s;
    int i1;
    int i2;
    byte[] b;

    static vCset c1def = vCset.New(0, vCset.MAX_VALUE);
    static vCset c2def = vCset.New('(');
    static vCset c3def = vCset.New(')');

    public vDescriptor nextval() {

	if (PC == 1) {
	    for (int i = 0; i < arguments.length; i++) {
		arguments[i] = arguments[i].deref();
	    }
	    c1 = vCset.argVal(arguments, 0, c1def);
	    c2 = vCset.argVal(arguments, 1, c2def);
	    c3 = vCset.argVal(arguments, 2, c3def);
	    s = iRuntime.argSubject(arguments, 3);
	    i1 = s.posEq(iRuntime.argPos(arguments, 4));
	    i2 = s.posEq(vInteger.argVal(arguments, 5, 0));
	    if (i1 == 0 || i2 == 0) {
		return null;
	    }
	    if (i1 > i2) {
		int tmp = i1;
		i1 = i2;
		i2 = tmp;
	    }
	    b = s.getBytes();
	    PC = 2;
	}

	int balance = 0;
	for (; i1 < i2; i1++) {
	    if (balance == 0 && c1.member(b[i1-1])) {
		i1 = i1+1;
		return vInteger.New(i1-1);
	    }
	    if (c2.member(s.charAt(i1-1))) {
		balance++;
	    } else if (c3.member(b[i1-1])) {
		balance--;
		if (balance < 0) {
		    return null;
		}
	    }
	}
	return null;
    }
}



class f$move extends iClosure {				// move(j)

    vInteger oldpos;

    public vDescriptor nextval() {

	if (PC == 1) {
	    oldpos = (vInteger) k$pos.self.deref();
	    int i = (int) oldpos.value;
	    int j = (int) vInteger.argVal(arguments, 0);
	    int k = i + j - 1;
	    vString s = (vString) k$subject.self.deref();
	    PC = 2;
	    if (k < 0 || k > s.length()) {
		return null;
	    } else {
		k$pos.self.SafeAssign(vInteger.New(i + j));
		if (j >= 0) {
		    return vString.New(s, i, i + j);
		} else {
		    return vString.New(s, i + j, i);
		}
	    }
	} else {
	    k$pos.self.Assign(oldpos);
	    return null;
	}
    }
}



class f$tab extends iClosure {				// tab(j)

    vInteger oldpos;

    public vDescriptor nextval() {

	if (PC == 1) {
	    oldpos = (vInteger) k$pos.self.deref();
	    vString s = (vString) k$subject.self.deref();
	    int i = (int) oldpos.value;
	    int j = (int) s.posEq(vInteger.argVal(arguments, 0));
	    PC = 2;
	    if (j == 0) {
		return null;
	    } else {
		k$pos.self.SafeAssign(vInteger.New(j));
		if (i < j) {
		    return vString.New(s, i, j);
		} else {
		    return vString.New(s, j, i);
		}
	    }
	} else {
	    k$pos.self.Assign(oldpos);
	    return null;
	}
    }
}

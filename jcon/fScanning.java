// builtin string scanning functions

package rts;

class fScanning {} // dummy

class f$pos extends iFunctionClosure {			// pos(i)
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

class f$any extends iFunctionClosure {			// any(c,s,i1,i2)
    vDescriptor function(vDescriptor[] args) {
	vCset c = vCset.argVal(args, 0);
	vString s = iRuntime.argSubject(args, 1);
	int i1 = s.posEq(iRuntime.argPos(args, 2));
	int i2 = s.posEq(vInteger.argVal(args, 3, 0));

	if (i1 < i2 && c.member(s.value.charAt(i1-1))) {
	    return iNew.Integer(i1+1);
	}
	return null;
    }
}

class f$many extends iFunctionClosure {			// many(c,s,i1,i2)
    vDescriptor function(vDescriptor[] args) {
	vCset c = vCset.argVal(args, 0);
	vString s = iRuntime.argSubject(args, 1);
	int i1 = s.posEq(iRuntime.argPos(args, 2));
	int i2 = s.posEq(vInteger.argVal(args, 3, 0));

	if (i1 >= i2) {
	    return null;
	}
	int i;
	for (i = i1; i < i2; i++) {
	    if (!c.member(s.value.charAt(i-1))) {
		break;
	    }
	}
	if (i == i1) {
	    return null;
	} else {
	    return iNew.Integer(i);
	}
    }
}

class f$match extends iFunctionClosure {			// match(s1,s2,i1,i2)
    vDescriptor function(vDescriptor[] args) {
	String s1 = vString.argVal(args, 0);
	vString s2 = iRuntime.argSubject(args, 1);
	int i1 = s2.posEq(iRuntime.argPos(args, 2));
	int i2 = s2.posEq(vInteger.argVal(args, 3, 0));

	if (i1 > i2) {
	    return null;
	}
	if (s2.value.length() < i1+s1.length()-1) {
	    return null;
	}
	if (s1.equals( s2.value.substring(i1-1, i1+s1.length()-1) )) {
	    return iNew.Integer(i1+s1.length());
	} else {
	    return null;
	}
    }
}

class f$find extends iClosure {				// find(s1,s2,i1,i2)

    String s1;
    vString s2;
    int i1;
    int i2;

    void nextval() {

        if (s1 == null) {
	    for (int i = 0; i < arguments.length; i++) {
		arguments[i] = arguments[i].deref();
	    }
	    s1 = vString.argVal(arguments, 0);
	    s2 = iRuntime.argSubject(arguments, 1);
	    i1 = s2.posEq(iRuntime.argPos(arguments, 2));
	    i2 = s2.posEq(vInteger.argVal(arguments, 3, 0));
	}

	if (i1 > i2) {
	    retvalue = null;
	}
	if (s2.value.length() < i1+s1.length()-1) {
	    retvalue = null;
	}
	int i = s2.value.indexOf(s1, i1-1);
	if (i >= 0) {
	    i1 = i+2;
	    retvalue = iNew.Integer(i+1);
	} else {
	    retvalue = null;
	}
    }
}

class f$upto extends iClosure {				// upto(c,s2,i1,i2)

    vCset c;
    vString s;
    int i1;
    int i2;

    void nextval() {

        if (c == null) {
	    for (int i = 0; i < arguments.length; i++) {
		arguments[i] = arguments[i].deref();
	    }
	    c = vCset.argVal(arguments, 0);
	    s = iRuntime.argSubject(arguments, 1);
	    i1 = s.posEq(iRuntime.argPos(arguments, 2));
	    i2 = s.posEq(vInteger.argVal(arguments, 3, 0));
	}

	for (; i1 < i2; i1++) {
	    if (c.member(s.value.charAt(i1-1))) {
		i1 = i1+1;
	        retvalue = iNew.Integer(i1-1);
		return;
	    }
	}
	retvalue = null;
    }
}

class f$bal extends iClosure {				// bal(c1,c2,c3,s,i1,i2)

    vCset c1;
    vCset c2;
    vCset c3;
    vString s;
    int i1;
    int i2;

    void nextval() {

        if (c1 == null) {
	    for (int i = 0; i < arguments.length; i++) {
		arguments[i] = arguments[i].deref();
	    }
	    // %#%#%# c1 should default to &cset....
	    c1 = vCset.argVal(arguments, 0, 0, 255);
	    c2 = vCset.argVal(arguments, 1, '(', '(');
	    c3 = vCset.argVal(arguments, 2, ')', ')');
	    s = iRuntime.argSubject(arguments, 3);
	    i1 = s.posEq(iRuntime.argPos(arguments, 4));
	    i2 = s.posEq(vInteger.argVal(arguments, 5, 0));
	}

	int balance = 0;
	for (; i1 < i2; i1++) {
	    if (balance == 0 && c1.member(s.value.charAt(i1-1))) {
		i1 = i1+1;
	        retvalue = iNew.Integer(i1-1);
		return;
	    }
	    if (c2.member(s.value.charAt(i1-1))) {
		balance++;
	    }
	    if (c3.member(s.value.charAt(i1-1))) {
		balance--;
		if (balance < 0) {
		    retvalue = null;
		    return;
		}
	    }
	}
	retvalue = null;
    }
}

class f$move extends iClosure {				// move(i)

    vInteger oldpos;

    void nextval() {

        if (oldpos == null) {
	    oldpos = (vInteger) k$pos.self.deref();
	    long i = vInteger.argVal(arguments, 0);
	    vString s = (vString) k$subject.self.deref();
	    if (oldpos.value + i - 1 > s.value.length()) {
		retvalue = null;
	    } else {
	        k$pos.self.Assign(iNew.Integer(oldpos.value+i));
		retvalue = iNew.String(s.value.substring((int)oldpos.value-1, (int)oldpos.value+(int)i-1));
	    }
	} else {
	    k$pos.self.Assign(oldpos);
	    retvalue = null;
	}
    }
}

class f$tab extends iClosure {				// tab(i)

    vInteger oldpos;

    void nextval() {

        if (oldpos == null) {
	    oldpos = (vInteger) k$pos.self.deref();
	    long i = vInteger.argVal(arguments, 0);
	    vString s = (vString) k$subject.self.deref();
	    i = s.posEq(i);
	    if (i == 0) {
		retvalue = null;
	    } else {
	        k$pos.self.Assign(iNew.Integer(i));
		retvalue = iNew.String(s.value.substring((int)oldpos.value-1, (int)i-1));
	    }
	} else {
	    k$pos.self.Assign(oldpos);
	    retvalue = null;
	}
    }
}

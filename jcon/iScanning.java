// builtin string scanning functions

package rts;

class f$pos extends iFunctionClosure {			// pos(i)
    vDescriptor function(vDescriptor[] args) {
	long i;

	if (args.length == 0) {
            iRuntime.error(101);
	}
	i = args[0].mkInteger().value;
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
	vCset c;
	vString s;
	int i1;
	int i2;

	if (args.length == 0) {
            iRuntime.error(104);
	}
	c = args[0].mkCset();
	s = iRuntime.argSubject(args, 1);
	i1 = s.posEq(iRuntime.argPos(args, 2));
	i2 = s.posEq((args.length >= 4) ? args[3].mkInteger().value : 0);

	if (i1 < i2 && c.member(s.value.charAt(i1-1))) {
	    return iNew.Integer(i1+1);
	}
	return null;
    }
}

class f$many extends iFunctionClosure {			// many(c,s,i1,i2)
    vDescriptor function(vDescriptor[] args) {
	vCset c;
	vString s;
	int i1;
	int i2;

	if (args.length == 0) {
            iRuntime.error(104);
	}
	c = args[0].mkCset();
	s = iRuntime.argSubject(args, 1);
	i1 = s.posEq(iRuntime.argPos(args, 2));
	i2 = s.posEq((args.length >= 4) ? args[3].mkInteger().value : 0);

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
	vString s1;
	vString s2;
	int i1;
	int i2;

	if (args.length == 0) {
            iRuntime.error(103);
	}
	s1 = args[0].mkString();
	s2 = iRuntime.argSubject(args, 1);
	i1 = s2.posEq(iRuntime.argPos(args, 2));
	i2 = s2.posEq((args.length >= 4) ? args[3].mkInteger().value : 0);

	if (i1 > i2) {
	    return null;
	}
	if (s2.value.length() < i1+s1.value.length()-1) {
	    return null;
	}
	if (s1.value.equals( s2.value.substring(i1-1, i1+s1.value.length()-1) )) {
	    return iNew.Integer(i1+s1.value.length());
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

    void nextval() {

        if (s1 == null) {
	    if (arguments.length == 0) {
                iRuntime.error(103);
	    }
	    s1 = arguments[0].mkString();
	    s2 = iRuntime.argSubject(arguments, 1);
	    i1 = s2.posEq(iRuntime.argPos(arguments, 2));
	    i2 = s2.posEq((arguments.length >= 4) ? arguments[3].mkInteger().value : 0);
	}

	if (i1 > i2) {
	    retvalue = null;
	}
	if (s2.value.length() < i1+s1.value.length()-1) {
	    retvalue = null;
	}
	int i = s2.value.indexOf(s1.value, i1-1);
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
	    if (arguments.length == 0) {
                iRuntime.error(103);
	    }
	    c = arguments[0].mkCset();
	    s = iRuntime.argSubject(arguments, 1);
	    i1 = s.posEq(iRuntime.argPos(arguments, 2));
	    i2 = s.posEq((arguments.length >= 4) ? arguments[3].mkInteger().value : 0);
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
	    // %#%#%# c1 should default to &cset....
	    c1 = (arguments.length < 1) ? iNew.Cset(0,256) : arguments[0].mkCset();
	    c2 = (arguments.length < 2) ? iNew.Cset('(','(') : arguments[1].mkCset();
	    c3 = (arguments.length < 3) ? iNew.Cset(')',')') : arguments[2].mkCset();
	    s = iRuntime.argSubject(arguments, 3);
	    i1 = s.posEq(iRuntime.argPos(arguments, 4));
	    i2 = s.posEq((arguments.length >= 4) ? arguments[3].mkInteger().value : 0);
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
	    if (arguments.length == 0) {
                iRuntime.error(103);
	    }
	    oldpos = (vInteger) k$pos.self.deref();
	    int i = (int) arguments[0].mkInteger().value;
	    vString s = (vString) k$subject.self.deref();
	    if (oldpos.value + i - 1 > s.value.length()) {
		retvalue = null;
	    } else {
	        k$pos.self.Assign(iNew.Integer(oldpos.value+i));
		retvalue = iNew.String(s.value.substring((int)oldpos.value-1, (int)oldpos.value+i-1));
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
	    if (arguments.length == 0) {
                iRuntime.error(103);
	    }
	    oldpos = (vInteger) k$pos.self.deref();
	    int i = (int) arguments[0].mkInteger().value;
	    vString s = (vString) k$subject.self.deref();
	    i = s.posEq(i);
	    if (i == 0 || i < oldpos.value) {
		retvalue = null;
	    } else {
	        k$pos.self.Assign(iNew.Integer(i));
		retvalue = iNew.String(s.value.substring((int)oldpos.value-1, i-1));
	    }
	} else {
	    k$pos.self.Assign(oldpos);
	    retvalue = null;
	}
    }
}

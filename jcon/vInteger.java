package rts;

public final class vInteger extends vNumeric {

    long value;



static vInteger intlist[] =			// cache for "common" integers
    new vInteger[iConfig.MaxCachedInt + 1 - iConfig.MinCachedInt];



//  constructors

public static vInteger New(double x) {		// int from real
    return New((long) x);			//#%#% ignoring overflow
}

public static vInteger New(String x) {		// int from string
    return New(Long.parseLong(x));		// can throw exception
}

public static vInteger New(long x) {
    if (x > iConfig.MaxCachedInt || x < iConfig.MinCachedInt) {
	return new vInteger(x);
    }
    int i = (int)x - iConfig.MinCachedInt;
    vInteger v = intlist[i];
    if (v != null) {
	return v;
    }
    return intlist[i] = new vInteger(x);
}

private vInteger(long n) { value = n; }



//  runtime primitives

public int hashCode()	{ return (int) ((13255 * value) >> 10); } // same as v9

public boolean equals(Object o)	{
    return (o instanceof vInteger) && (((vInteger)o).value == value);
}

vInteger mkInteger()	{ return this; }

vReal mkReal()		{ return vReal.New(this.value); }

vString mkString() {
    if (cachedString == null) {
       cachedString = vString.New(Long.toString(value));
    }
    return cachedString;
}

vProc mkProc() {
    // return new vIntegerProc(this); 
    iRuntime.bomb("vInteger.mkProc() NYI");	//#%#%#%
    return null;
}

vString write()		{ return mkString(); }
vString image()		{ return mkString(); }

static vString typestring = vString.New("integer");
public vString Type()	{ return typestring; }

int rank()		{ return 10; }	// integers sort right after &null

int compareTo(vValue v) {
    long x = ((vInteger) v).value;
    if (this.value < x) {
	return -1;
    } else if (this.value > x) {
	return 1;
    } else {
	return 0;
    }
}

public vDescriptor ProcessArgs(vDescriptor x) {			// i ! X
    vValue[] a = x.mkArray(126);
    long i = (value > 0) ? (value - 1) : (a.length + value);
    if (i >= 0 && i < a.length) {
	return a[(int) i];
    } else {
	return null; /*FAIL*/
    }
}



//  static methods for argument processing and defaulting

static long argVal(vDescriptor[] args, int index) {		// required arg
    if (index >= args.length) {
	iRuntime.error(101);
	return 0;
    } else {
	return args[index].mkInteger().value;
    }
}

static long argVal(vDescriptor[] args, int index, int dflt) {	// optional arg
    if (index >= args.length || args[index].isnull()) {
	return dflt;
    } else {
	return args[index].mkInteger().value;
    }
}



//  radixParse(s) -- parse radix-specified integer literal
//
//  assumes s has been trimmed
//  returns vInteger, or null

static vInteger radixParse(String s) {
    boolean negate = false;

    int i = s.indexOf('r');
    if (i == -1) {
	i = s.indexOf('R');
    }
    if (i < 1 || i > s.length() - 2) {
	return null;
    }

    long base = 0;
    try {
	base = Long.parseLong(s.substring(0,i));
    } catch (NumberFormatException e) {
    }
    if (base < 0) {
	negate = true;
	base = -base;
    }
    if (base < 2 || base > 36) {
	return null;
    }

    long v = 0;
    long lim = Long.MAX_VALUE / base;
    for (i = i + 1; i < s.length(); i++) {
	if (v > lim) {		// this overflow check is slightly too simple
	    return null;
	}
	char c = s.charAt(i);
	int n;
	if (c >= '0' && c <= '9') {
	    n = c - '0';
	} else if (c >= 'A' && c <= 'Z') {
	    n = c - 'A' + 10;
	} else if (c >= 'a' && c <= 'z') {
	    n = c - 'a' + 10;
	} else {
	    return null;
	}
	if (n >= base) {
	    return null;
	}
	v = base * v + n;
    }
    if (negate) {
	v = -v;
    }
    return New(v);
}



//  unary operations

public vNumeric Negate() {
    if (value == Long.MIN_VALUE) {
	iRuntime.error(203);
    }
    return New(-value);
}

public vDescriptor Select() {
    if (value > 0) {
	return New(1 + k$random.choose(value));
    } else if (value == 0) {
	return vReal.New(k$random.nextVal());
    } else {
	iRuntime.error(205, this);
	return null;
    }
}

public vNumeric Abs() {
    if (this.value >= 0 ) {
	return this;
    } else if (this.value == Long.MIN_VALUE) {
	iRuntime.error(203);
	return null;
    } else {
	return New(-this.value);
    }
}

public vInteger Limit() {
    if (value > 0) {
	return this;
    } else {
	return null; /*FAIL*/
    }
}



// binary arithmetic operators

public vNumeric Add(vDescriptor v)	{ return v.AddInto(this); }
public vNumeric Sub(vDescriptor v)	{ return v.SubFrom(this); }
public vNumeric Mul(vDescriptor v)	{ return v.MulInto(this); }
public vNumeric Div(vDescriptor v)	{ return v.DivInto(this); }
public vNumeric Mod(vDescriptor v)	{ return v.ModInto(this); }
public vNumeric Power(vDescriptor v)	{ return v.PowerOf(this); }


vNumeric AddInto(vReal a)	{ return vReal.New(a.value + this.value); }

vNumeric AddInto(vInteger i) {
    long a = i.value;
    long b = this.value;
    long t = a + b;
    if ((~(a ^ b) & (t ^ a)) < 0) {
	iRuntime.error(203);
    }
    return New(t);
}


vNumeric SubFrom(vReal a)	{ return vReal.New(a.value - this.value); }

vNumeric SubFrom(vInteger i) {
    long a = i.value;
    long b = this.value;
    long d = a - b;
    if (((a ^ b) & (d ^ a)) < 0) {
	iRuntime.error(203);
    }
    return New(d);
}


vNumeric MulInto(vReal a)	{ return vReal.New(a.value * this.value); }

vNumeric MulInto(vInteger i) {
    long a = i.value;
    long b = this.value;
    long p = a * b;
    if ((a == (short) a) && (b == (short) b)) {
	return New(p);			// overflow not possible
    }
    // the following is adapted from v9 and is taken on faith
    if (b == 0) {
	return New(p);
    }
    if ((a ^ b) >= 0) {
	if ((a >= 0) ? (a > Long.MAX_VALUE / b) : (a < Long.MAX_VALUE / b)) {
	    iRuntime.error(203);
	}
    } else if (b != -1) {
	if ((a >= 0) ? (a > Long.MIN_VALUE / b) : (a < Long.MIN_VALUE / b)) {
	    iRuntime.error(203);
	}
    }
    return New(p);
}


vNumeric DivInto(vReal a)	{ return this.mkReal().DivInto(a); }

vNumeric DivInto(vInteger i) {
    long a = i.value;
    long b = this.value;

    if (b == 0) {
	iRuntime.error(201);
    } else if (b == -1 && a == Long.MIN_VALUE) {
	iRuntime.error(203);
    }
    return New(a / b);
}


vNumeric ModInto(vReal a)	{ return this.mkReal().ModInto(a); }

vNumeric ModInto(vInteger i) {
    long a = i.value;
    long b = this.value;

    if (b == 0) {
	iRuntime.error(202);
    } else if (b == -1 && a == Long.MIN_VALUE) {
	iRuntime.error(203);
    }
    return New(a % b);
}


vNumeric PowerOf(vReal r) {			// r ^ i
    double x = r.value;
    long y = this.value;

    if (y <= 0) {
	if (x == 0.0) {
	    iRuntime.error(204);
	}
	x = 1.0 / x;
	y = -y;
    }
    // #%#% need to add overflow check
    // #%#% could do this more efficiently, too
    double p = 1.0;
    for (long j = 0; j < y; j++) {
	p *= x;
    }
    return vReal.New(p);
}

vNumeric PowerOf(vInteger i) {			// i ^ i
    long x = i.value;
    long y = this.value;
    if (x == 0 && y <= 0) {
	iRuntime.error(204);
    }
    if (y < 0) {
	if (x == 1) {
	    return New(1);
	} else if (x == -1) {
	    y = -y;
	} else {
	    return New(0);
	}
    }
    // #%#% need to add overflow check
    // #%#% could do this more efficiently, too
    long p = 1L;
    for (long j = 0; j < y; j++) {
	p *= x;
    }
    return New(p);
}



//  numeric comparisons

public vNumeric NLess(vDescriptor v)		{ return v.BkwLess(this); }
public vNumeric NLessEq(vDescriptor v)		{ return v.BkwLessEq(this); }
public vNumeric NEqual(vDescriptor v)		{ return v.BkwEqual(this); }
public vNumeric NUnequal(vDescriptor v)		{ return v.BkwUnequal(this); }
public vNumeric NGreater(vDescriptor v)		{ return v.BkwGreater(this); }
public vNumeric NGreaterEq(vDescriptor v)	{ return v.BkwGreaterEq(this); }

vNumeric BkwLess(vInteger a)	{ return (a.value <  this.value) ? this : null;}
vNumeric BkwLessEq(vInteger a)	{ return (a.value <= this.value) ? this : null;}
vNumeric BkwEqual(vInteger a)	{ return (a.value == this.value) ? this : null;}
vNumeric BkwUnequal(vInteger a)	{ return (a.value != this.value) ? this : null;}
vNumeric BkwGreater(vInteger a)	{ return (a.value >  this.value) ? this : null;}
vNumeric BkwGreaterEq(vInteger a){return (a.value >= this.value) ? this : null;}

vNumeric BkwLess(vReal a)
    { return (a.value <  this.value) ? vReal.New(this.value) : null;}
vNumeric BkwLessEq(vReal a)
    { return (a.value <= this.value) ? vReal.New(this.value) : null;}
vNumeric BkwEqual(vReal a)
    { return (a.value == this.value) ? vReal.New(this.value) : null;}
vNumeric BkwUnequal(vReal a)
    { return (a.value != this.value) ? vReal.New(this.value) : null;}
vNumeric BkwGreater(vReal a)
    { return (a.value >  this.value) ? vReal.New(this.value) : null;}
vNumeric BkwGreaterEq(vReal a)
    { return (a.value >= this.value) ? vReal.New(this.value) : null;}



//  i to j by k   (i.ToBy(j,k))

public vDescriptor ToBy(vDescriptor v2, vDescriptor v3) {
    final long i = this.value;
    final long j = v2.mkInteger().value;
    final long k = (v3 == null) ? 1 : v3.mkInteger().value;

    if (k > 0) {			// positive increment
	if (i > j) {
	    return null; /*FAIL*/	// no iterations
	} else if (i == j) {
	    return this;		// just once
	} else return new vClosure() {
	    long n = i;
	    { retval = vInteger.this; }

	    public vDescriptor Resume() {
		long oldn = n;
		if ((n += k) <= j && n > oldn) {  // 2nd test catches overflow
		    return New(n);
		} else {
		    return null; /*FAIL*/
		}
	    };
	};

    } else if (k < 0) {			// negative increment
	if (i < j) {
	    return null; /*FAIL*/	// no iterations
	} else if (i == j) {
	    return this;		// just once
	} else return new vClosure() {
	    long n = i;
	    { retval = vInteger.this; }

	    public vDescriptor Resume() {
		long oldn = n;
		if ((n += k) >= j && n < oldn) {  // 2nd test catches overflow
		    return New(n);
		} else {
		    return null; /*FAIL*/
		}
	    };
	};

    } else {				// increment is zero
        iRuntime.error(211, v3);
        return null;
    }
}



//  i(a, b, c, ...)

public vDescriptor Call(vDescriptor v[]) {
    int i;
    if (value < 0) {
	i = v.length + (int) value;
    } else {
	i = (int) value - 1;
    }
    if (i >= 0 && i < v.length) {
	return v[i - 1].Deref();
    } else {
	return null; /*FAIL*/
    }
}
public vDescriptor Call() {
    return null; /*FAIL*/
}
public vDescriptor Call(vDescriptor a) {
    if (value == 1 || value == -1) {
	return a.Deref();
    } else {
	return null; /*FAIL*/
    }
}
public vDescriptor Call(vDescriptor a, vDescriptor b) {
	switch ((int) value) {
	    case 1:  case -2:	return a.Deref();
	    case 2:  case -1:	return b.Deref();
	    default:		return null; /*FAIL*/
	}
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	switch ((int) value) {
	    case 1:  case -3:	return a.Deref();
	    case 2:  case -2:	return b.Deref();
	    case 3:  case -1:	return c.Deref();
	    default:		return null; /*FAIL*/
	}
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	switch ((int) value) {
	    case 1:  case -4:	return a.Deref();
	    case 2:  case -3:	return b.Deref();
	    case 3:  case -2:	return c.Deref();
	    case 4:  case -1:	return d.Deref();
	    default:		return null; /*FAIL*/
	}
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	switch ((int) value) {
	    case 1:  case -5:	return a.Deref();
	    case 2:  case -4:	return b.Deref();
	    case 3:  case -3:	return c.Deref();
	    case 4:  case -2:	return d.Deref();
	    case 5:  case -1:	return e.Deref();
	    default:		return null; /*FAIL*/
	}
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	switch ((int) value) {
	    case 1:  case -6:	return a.Deref();
	    case 2:  case -5:	return b.Deref();
	    case 3:  case -4:	return c.Deref();
	    case 4:  case -3:	return d.Deref();
	    case 5:  case -2:	return e.Deref();
	    case 6:  case -1:	return f.Deref();
	    default:		return null; /*FAIL*/
	}
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	switch ((int) value) {
	    case 1:  case -7:	return a.Deref();
	    case 2:  case -6:	return b.Deref();
	    case 3:  case -5:	return c.Deref();
	    case 4:  case -4:	return d.Deref();
	    case 5:  case -3:	return e.Deref();
	    case 6:  case -2:	return f.Deref();
	    case 7:  case -1:	return g.Deref();
	    default:		return null; /*FAIL*/
	}
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	switch ((int) value) {
	    case 1:  case -8:	return a.Deref();
	    case 2:  case -7:	return b.Deref();
	    case 3:  case -6:	return c.Deref();
	    case 4:  case -5:	return d.Deref();
	    case 5:  case -4:	return e.Deref();
	    case 6:  case -3:	return f.Deref();
	    case 7:  case -2:	return g.Deref();
	    case 8:  case -1:	return h.Deref();
	    default:		return null; /*FAIL*/
	}
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	switch ((int) value) {
	    case 1:  case -9:	return a.Deref();
	    case 2:  case -8:	return b.Deref();
	    case 3:  case -7:	return c.Deref();
	    case 4:  case -6:	return d.Deref();
	    case 5:  case -5:	return e.Deref();
	    case 6:  case -4:	return f.Deref();
	    case 7:  case -3:	return g.Deref();
	    case 8:  case -2:	return h.Deref();
	    case 9:  case -1:	return i.Deref();
	    default:		return null; /*FAIL*/
	}
}



} // class vInteger

package rts;

public class vInteger extends vNumeric {

    long value;



//#%#% need to add overflow checking throughout



private static vInteger intlist[] =		// cache for "common" integers
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

vValue getproc()	{ return new vIntegerProc(this); }

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

public vNumeric NLess(vDescriptor v)		{ return v.RevLess(this); }
public vNumeric NLessEq(vDescriptor v)		{ return v.RevLessEq(this); }
public vNumeric NEqual(vDescriptor v)		{ return v.RevEqual(this); }
public vNumeric NUnequal(vDescriptor v)		{ return v.RevUnequal(this); }
public vNumeric NGreater(vDescriptor v)		{ return v.RevGreater(this); }
public vNumeric NGreaterEq(vDescriptor v)	{ return v.RevGreaterEq(this); }

vNumeric RevLess(vInteger a)	{ return (a.value <  this.value) ? this : null;}
vNumeric RevLessEq(vInteger a)	{ return (a.value <= this.value) ? this : null;}
vNumeric RevEqual(vInteger a)	{ return (a.value == this.value) ? this : null;}
vNumeric RevUnequal(vInteger a)	{ return (a.value != this.value) ? this : null;}
vNumeric RevGreater(vInteger a)	{ return (a.value >  this.value) ? this : null;}
vNumeric RevGreaterEq(vInteger a){return (a.value >= this.value) ? this : null;}

vNumeric RevLess(vReal a)
    { return (a.value <  this.value) ? vReal.New(this.value) : null;}
vNumeric RevLessEq(vReal a)
    { return (a.value <= this.value) ? vReal.New(this.value) : null;}
vNumeric RevEqual(vReal a)
    { return (a.value == this.value) ? vReal.New(this.value) : null;}
vNumeric RevUnequal(vReal a)
    { return (a.value != this.value) ? vReal.New(this.value) : null;}
vNumeric RevGreater(vReal a)
    { return (a.value >  this.value) ? vReal.New(this.value) : null;}
vNumeric RevGreaterEq(vReal a)
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

	    public vDescriptor resume() {
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

	    public vDescriptor resume() {
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



} // class vInteger



class vIntegerProc extends vValue {
    vInteger value;

    vIntegerProc(vInteger value) {
	this.value = value;
    }

    public iClosure instantiate (vDescriptor[] args, iClosure parent) {
	return new iIntegerClosure(this.value, args, parent);
    }

    vValue getproc()	{ return this; }

    vString image()	{ return value.mkString().surround("function ", ""); }

    static vString typestring = vString.New("procedure");
    public vString Type()	{ return typestring; }

    int rank()			{ return 80; }		// integer "procedure"
    int compareTo(vValue v)
			{ return vProc.compareLastWord(this.image(),v.image());}

    public vInteger Args()	{ return vInteger.New(-1); }
}


class iIntegerClosure extends iRefClosure {
    vInteger value;

    iIntegerClosure(vInteger value, vDescriptor[] args, iClosure parent) {
	init();
	this.value = value;
	arguments = args;
	this.parent = parent;
    }

    vDescriptor function(vDescriptor[] args) {
	long i = value.value;
	if (i <= 0) {
	    i += args.length + 1;
	}
	i -= 1;
	if (i < 0 || i >= args.length) {
	    return null;
	}
	return args[(int)i];
    }
}

package rts;

public class vInteger extends vNumeric {

    long value;



private static vInteger intlist[] =		// cache for "common" integers
    new vInteger[iConfig.MaxCachedInt + 1 - iConfig.MinCachedInt];



//  constructors

public static vInteger New(double x) {		// int from real
    return New((long) x);			//#%#% what about overflow?
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
vString type()		{ return typestring; }

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



//  c.argument0.NumBoth(c) -- tandem coercion to numeric
//
//  converts c.argument0 & c.argument1 to both Integer or both Real
//  assumes that c.argument0 == this

void NumBoth(iBinaryValueClosure c) {
    vDescriptor a1 = c.argument1;

    if (a1 instanceof vInteger) {
	return;
    }
    if (!(a1 instanceof vReal)) {
	if ((c.argument1 = a1.mkNumeric()) instanceof vInteger) {
	    return;
	}
    }
    c.argument0 = this.mkReal();
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
    if (index >= args.length || args[index].isNull()) {
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
	if (v > lim) {		// #%#% this check is slightly too simple
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
    return vInteger.New(v);
}



//  operations
//
//  #%#% overflow is not detected by integer operations

vNumeric Negate()	{ return vInteger.New(-value); }

vDescriptor Select() {
    if (value > 0) {
	return vInteger.New(1 + k$random.choose(value));
    } else if (value == 0) {
	return vReal.New(k$random.nextVal());
    } else {
	iRuntime.error(205, this);
	return null;
    }
}

vValue Add(vDescriptor v) {
    return vInteger.New(this.value + ((vInteger)v).value);
}

vValue Sub(vDescriptor v) {
    return vInteger.New(this.value - ((vInteger)v).value);
}

vValue Mul(vDescriptor v) {
    return vInteger.New(this.value * ((vInteger)v).value);
}

vValue Div(vDescriptor v) {
    try {
	return vInteger.New(this.value / ((vInteger)v).value);
    } catch (ArithmeticException e) {
	iRuntime.error(201);
    }
    return null;
}

vValue Mod(vDescriptor v) {
    try {
	return vInteger.New(this.value % ((vInteger)v).value);
    } catch (ArithmeticException e) {
	iRuntime.error(202);
    }
    return null;
}

vValue Abs() {
    if (this.value >= 0 ) {
	return this;
    } else {
	return vInteger.New(-this.value);
    }
}

vValue Power(vDescriptor v) {
    if (! (v instanceof vInteger)) {
	return this.mkReal().Power(v);
    }
    long x = this.value;
    long y = ((vInteger)v).value;
    if (x == 0 && y <= 0) {
	iRuntime.error(204);
    }
    if (y < 0) {
	if (x == 1) {
	    return vInteger.New(1);
	} else if (x == -1) {
	    y = -y;
	} else {
	    return vInteger.New(0);
	}
    }
    //#%#%# slow computation; should use powers of 2
    long p = 1L;
    for (long i = 0; i < y; i++) {
	p *= x;		//#%#%# totally ignoring overflow.
    }
    return vInteger.New(p);
}




//  numeric comparisons

vValue NLess(vDescriptor v) {
    vInteger vi = (vInteger) v;
    return (this.value < vi.value) ? vi : null;
}

vValue NLessEq(vDescriptor v) {
    vInteger vi = (vInteger) v;
    return (this.value <= vi.value) ? vi : null;
}

vValue NEqual(vDescriptor v) {
    vInteger vi = (vInteger) v;
    return (this.value == vi.value) ? vi : null;
}

vValue NUnequal(vDescriptor v) {
    vInteger vi = (vInteger) v;
    return (this.value != vi.value) ? vi : null;
}

vValue NGreaterEq(vDescriptor v) {
    vInteger vi = (vInteger) v;
    return (this.value >= vi.value) ? vi : null;
}

vValue NGreater(vDescriptor v) {
    vInteger vi = (vInteger) v;
    return (this.value > vi.value) ? vi : null;
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
    vString type()	{ return typestring; }

    int rank()		{ return 80; }		// integer "procedure"
    int compareTo(vValue v)
			{ return vProc.compareLastWord(this.image(),v.image());}

    vInteger Args()	{ return vInteger.New(-1); }
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

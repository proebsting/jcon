package rts;

public final class vInteger extends vNumeric {

    public long value;



static vInteger intlist[] =			// cache for "common" integers
    new vInteger[iConfig.MaxCachedInt + 1 - iConfig.MinCachedInt];



//  constructors

public static vInteger New(double x) {		// int from real
    if (x > (double)Long.MAX_VALUE || x < (double)Long.MIN_VALUE) {
	iRuntime.error(203);
    }
    return New((long) x);
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



//  convert to BigInt for subsequent processing

private static vBigInt Big(vInteger i)	{ return vBigInt.New(i.value); }



//  runtime primitives

public int hashCode()	{ return (int) ((13255 * value) >> 10); } // same as v9

public boolean equals(Object o)	{
    return (o instanceof vInteger) && (((vInteger)o).value == value);
}

public vInteger mkInteger()	{ return this; }
public vNumeric mkFixed()	{ return this; }
public vReal mkReal()		{ return vReal.New(this.value); }

public vString mkString() {
    if (cachedString == null) {
       cachedString = vString.New(Long.toString(value));
    }
    return cachedString;
}

public vString write()		{ return mkString(); }
public vString image()		{ return mkString(); }

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



//  unary operations

public vNumeric Negate() {
    if (value == Long.MIN_VALUE) {
	return Big(this).Negate();
    } else {
	return New(-value);
    }
}

public vDescriptor Select() {
    if (value > 0) {
	return New(1 + iKeyword.random.choose(value));
    } else if (value == 0) {
	return vReal.New(iKeyword.random.nextVal());
    } else {
	iRuntime.error(205, this);
	return null;
    }
}

public vNumeric Abs() {
    if (this.value >= 0 ) {
	return this;
    } else if (this.value == Long.MIN_VALUE) {
	return Big(this).Abs();
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
vNumeric AddInto(vBigInt a)	{ return Big(this).AddInto(a); }

vNumeric AddInto(vInteger i) {
    long a = i.value;
    long b = this.value;
    long t = a + b;
    if ((~(a ^ b) & (t ^ a)) < 0) {
	return Big(this).AddInto(i);
    }
    return New(t);
}


vNumeric SubFrom(vReal a)	{ return vReal.New(a.value - this.value); }
vNumeric SubFrom(vBigInt a)	{ return Big(this).SubFrom(a); }

vNumeric SubFrom(vInteger i) {
    long a = i.value;
    long b = this.value;
    long d = a - b;
    if (((a ^ b) & (d ^ a)) < 0) {
	return Big(this).SubFrom(i);
    }
    return New(d);
}


vNumeric MulInto(vReal a)	{ return vReal.New(a.value * this.value); }
vNumeric MulInto(vBigInt a)	{ return Big(this).MulInto(a); }

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
	    return Big(this).MulInto(i);
	}
    } else if (b != -1) {
	if ((a >= 0) ? (a > Long.MIN_VALUE / b) : (a < Long.MIN_VALUE / b)) {
	    return Big(this).MulInto(i);
	}
    }
    return New(p);
}


vNumeric DivInto(vReal a)	{ return this.mkReal().DivInto(a); }
vNumeric DivInto(vBigInt a)	{ return Big(this).DivInto(a); }

vNumeric DivInto(vInteger i) {
    long a = i.value;
    long b = this.value;

    if (b == 0) {
	iRuntime.error(201);
    }
    if (b == -1 && a == Long.MIN_VALUE) {
	return Big(i).Negate();
    } else {
	return New(a / b);
    }
}


vNumeric ModInto(vReal a)	{ return this.mkReal().ModInto(a); }
vNumeric ModInto(vBigInt a)	{ return Big(this).ModInto(a); }

vNumeric ModInto(vInteger i) {
    long a = i.value;
    long b = this.value;

    if (b == 0) {
	iRuntime.error(202);
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

    double v = 1.0;
    while (y > 0) {
	if ((y & 1) != 0) {
	    v *= x;
	}
	y >>= 1;
	x *= x;
    }
    return vReal.New(v);
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

    long v = 1L;
    while (y > 0) {
	if (Math.abs(x) > Integer.MAX_VALUE) {	// conservative but quick test
	    return Big(i).ToPower(this);
	}
	if ((y & 1) != 0) {
	    v *= x;
	}
	y >>= 1;
	x *= x;
    }
    return New(v);
}

vNumeric PowerOf(vBigInt i) {			// i ^ I
    return i.ToPower(this);
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

vNumeric BkwLess(vBigInt a)		{ return Big(this).BkwLess(a); }
vNumeric BkwLessEq(vBigInt a)		{ return Big(this).BkwLessEq(a); }
vNumeric BkwEqual(vBigInt a)		{ return Big(this).BkwEqual(a); }
vNumeric BkwUnequal(vBigInt a)		{ return Big(this).BkwUnequal(a); }
vNumeric BkwGreater(vBigInt a)		{ return Big(this).BkwGreater(a); }
vNumeric BkwGreaterEq(vBigInt a)	{ return Big(this).BkwGreaterEq(a); }



//  bitwise operations

vNumeric And(vNumeric j)	{ return j.And(this); }
vNumeric Or(vNumeric j)		{ return j.Or(this); }
vNumeric Xor(vNumeric j)	{ return j.Xor(this); }

vNumeric Compl()		{ return New(~this.value); }
vNumeric And(vInteger j)	{ return New(this.value & j.value); }
vNumeric Or(vInteger j)		{ return New(this.value | j.value); }
vNumeric Xor(vInteger j)	{ return New(this.value ^ j.value); }

vNumeric Shift(vInteger j) {
    long n = j.value;
    long v = this.value;
    if (n > 0) {		// shift left
	if (v == (short)v && n <= 32) {
	    return vInteger.New(v << n);	// safe to do this
	} else {
	    return Big(this).Shift(j);		// no, use large-integer code
	}
    } else if (n == 0) {	// no shift
	return this;
    } else {			// shift right
	if (n > -64) {
	    return vInteger.New(v >> -n);	// shift with with sign extend
	} else {
	    return vInteger.New(v >> 63);	// fill with sign
	}
    }
}



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

public vProc mkProc(int nargs) {
    final vValue n = this;	// vInteger provokes JDK1.1/1.2 compiler bug
    return new vProcV() {
	public vDescriptor Call(vDescriptor v[]) { return n.Call(v); }
    };
}

public vDescriptor Call(vDescriptor v[]) {
    int i;
    if (value < 0) {
	i = v.length + (int) value;
    } else {
	i = (int) value - 1;
    }
    if (i >= 0 && i < v.length) {
	return v[i].Deref();
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

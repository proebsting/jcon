//  vBigInt.java -- Icon "large" (arbitrary precision) integers
//
//  It is assumed throughout that only values not representable by a
//  Java "long" are stored as vBigInts.  See Result() below.



package rts;

import java.math.BigInteger;
import java.util.*;

public final class vBigInt extends vNumeric {

    private BigInteger value;

    static final BigInteger MinInteger = BigInteger.valueOf(Long.MIN_VALUE);
    static final BigInteger MaxInteger = BigInteger.valueOf(Long.MAX_VALUE);



//  factories and constructors

public static vBigInt New(long n)	 { return new vBigInt(n); }
public static vBigInt New(double d)	 { return new vBigInt(d); }
public static vBigInt New(String s)	 { return new vBigInt(s); }
public static vBigInt New(BigInteger v)	 { return new vBigInt(v); }

private vBigInt(long n) {
    value = BigInteger.valueOf(n);
}

private vBigInt(double d) {
    if (d <= Long.MAX_VALUE && d >= Long.MIN_VALUE) {
	value = BigInteger.valueOf((long)d);
    } else {
	// from Java VM Spec, 1997, page 99
	long bits = Double.doubleToLongBits(d);
	int e = (int)((bits >> 52) & 0x7FFL);
	long m = (e == 0) ?
	    (bits & 0xFFFFFFFFFFFFFL) << 1 :
	    (bits & 0xFFFFFFFFFFFFFL) | 0x10000000000000L;
	value = BigInteger.valueOf(m).shiftLeft(e - 1075);
	if (bits < 0) {
	    value = value.negate();
	}
    }
}

private vBigInt(String s) {
    value = new BigInteger(s);
}

private vBigInt(BigInteger v) {
    value = v;
}



//  convert to Java double for mixed-mode operations

double mkDouble() {
    double d = value.doubleValue();
    if (Double.isInfinite(d)) {
	iRuntime.error(204);
    }
    return d;
}



//  parse radix-specified large integer

public static vNumeric radixParse(byte[] data, int i, int j) {
    byte c;
    int b, n;
    boolean neg = false;

    if (i >= j) {
	return null;		// empty
    }

    c = data[i];
    if (c == '-') {
	neg = true;
	i++;
    }

    if ((i < j) && ((b = data[i] - '0') >= 0) && (b < 10)) {
	i++;
    } else {
	return null;		// failed; no digit
    }

    while ((i < j) && ((n = data[i] - '0') >= 0) && (n < 10)) {
	b = 10 * b + n;
	if (b > 36) {
	    return null;	// radix too big
	}
	i++;
    }

    if (i + 1 >= j) {
	return null;		// out of data
    }
    c = data[i++];
    if ((b < 2) || (c != 'r' && c != 'R')) {
	return null;		// bad radix spec
    }

    // parse remainder of string using specified radix
    BigInteger base = BigInteger.valueOf(b);
    BigInteger v = BigInteger.valueOf(0);
    while (i < j) {
	c = data[i++];
	if (c >= '0' && c <= '9') {
	    n = c - '0';
	} else if (c >= 'A' && c <= 'Z') {
	    n = c - 'A' + 10;
	} else if (c >= 'a' && c <= 'z') {
	    n = c - 'a' + 10;
	} else {
	    return null;	// illegal digit
	}
	if (n >= b) {
	    return null;	// digit exceeds radix
	}
	v = v.multiply(base).add(BigInteger.valueOf(n));
    }

    return Result(neg ? v.negate() : v);
}



//  runtime primitives

public int hashCode()		{ return value.hashCode(); }

public boolean equals(Object o) {
    return (o instanceof vBigInt) && (((vBigInt)o).value.equals(value));
}

public vInteger mkInteger()	{ iRuntime.error(101, this); return null; }
public vNumeric mkFixed()	{ return this; }
public vReal mkReal()		{ return vReal.New(mkDouble()); }

public vString mkString() {
    if (cachedString == null) {
       cachedString = vString.New(value.toString());
    }
    return cachedString;
}

public vString write()		{ return mkString(); }

public vString image()	{
    int ndigits = (int) (value.bitLength() * 0.3010299956639812);
					//     1 / log2(10)
    if (ndigits < iConfig.MaxIntDigits) {
	return mkString();
    } else {
	return vString.New("integer(~10^" + ndigits + ")");
    }
}



private static vString typestring = vString.New("integer");
public vString Type()		{ return typestring; }

// negatives sort just before integers (rank 10); positives sort after
int rank()			{ return (value.signum() < 0 ? 9 : 11); }

int compareTo(vValue v)		{ return value.compareTo(((vBigInt)v).value); }



public vDescriptor ProcessArgs(vDescriptor x) {			// i ! X
    return null; /*FAIL*/	// no way this can be in range
}



//  create vBigInteger or vInteger result as appropriate.

public static vNumeric Result(BigInteger v) {
    if (v.compareTo(MaxInteger) > 0 || v.compareTo(MinInteger) < 0) {
	return vBigInt.New(v);
    } else {
	return vInteger.New(v.longValue());
    }
}




//  unary operations

public vNumeric Negate() {					// -I
    return Result(value.negate());
}

public vNumeric Abs() {						// abs(I)
    return Result(value.abs());
}

public vInteger Limit() {					// e \ I
    iRuntime.error(101);
    return null;
}

public vDescriptor Select() {					// ?I
    // get top bits from Icon RNG
    // get bottom bits from Java RNG using &random as seed
    Random r = new Random(iKeyword.random.get());
    double d = value.doubleValue() * iKeyword.random.nextVal();
    BigInteger b = new BigInteger(value.bitLength() - 24, r);
    return Result(new vBigInt(d).value.xor(b));
}



// binary arithmetic operators

public vNumeric Add(vDescriptor v)	{ return v.AddInto(this); }
public vNumeric Sub(vDescriptor v)	{ return v.SubFrom(this); }
public vNumeric Mul(vDescriptor v)	{ return v.MulInto(this); }
public vNumeric Div(vDescriptor v)	{ return v.DivInto(this); }
public vNumeric Mod(vDescriptor v)	{ return v.ModInto(this); }
public vNumeric Power(vDescriptor v)	{ return v.PowerOf(this); }



vNumeric AddInto(vInteger a) { return AddInto(New(a.value)); }
vNumeric AddInto(vReal a)    { return vReal.New(a.value + this.mkReal().value);}
vNumeric AddInto(vBigInt a)  { return Result(a.value.add(this.value)); }

vNumeric SubFrom(vInteger a) { return SubFrom(New(a.value)); }
vNumeric SubFrom(vReal a)    { return vReal.New(a.value - this.mkReal().value);}
vNumeric SubFrom(vBigInt a)  { return Result(a.value.subtract(this.value)); }

vNumeric MulInto(vInteger a) { return MulInto(New(a.value)); }
vNumeric MulInto(vReal a)    { return vReal.New(a.value * this.mkReal().value);}
vNumeric MulInto(vBigInt a)  { return Result(a.value.multiply(this.value)); }

vNumeric DivInto(vInteger a) { return DivInto(New(a.value)); }
vNumeric DivInto(vReal a)    { return this.mkReal().DivInto(a); }

vNumeric DivInto(vBigInt a)  {
    if (this.value.signum() == 0) {
	iRuntime.error(202);
    }
    return Result(a.value.divide(this.value));
}

vNumeric ModInto(vInteger a) { return ModInto(New(a.value)); }
vNumeric ModInto(vReal a)    { return this.mkReal().ModInto(a); }

vNumeric ModInto(vBigInt a)  {
    if (this.value.signum() == 0) {
	iRuntime.error(202);
    }
    return Result(a.value.remainder(this.value));
}



vNumeric PowerOf(vReal r)	{ iRuntime.error(101, this); return null; } 
vNumeric PowerOf(vInteger i)	{ iRuntime.error(101, this); return null; }

//  v.toPower(i) -- called from vInteger, which doesn't do large arithmetic

vNumeric ToPower(vInteger i) {
    BigInteger x = this.value;
    int y = (int) i.value;

    if (x.signum() == 0 && y <= 0) {
	iRuntime.error(204);
	return null;
    } else if (y < 0) {
	return vInteger.New(0);
    } else if (y == 0) {
	return vInteger.New(1);
    } else if (y < Integer.MAX_VALUE) {
	return Result(x.pow(y));
    } else {
	iRuntime.error(101, i);
	return null;
    }
}



//  numeric comparisons

public vNumeric NLess(vDescriptor v)		{ return v.BkwLess(this); }
public vNumeric NLessEq(vDescriptor v)		{ return v.BkwLessEq(this); }
public vNumeric NEqual(vDescriptor v)		{ return v.BkwEqual(this); }
public vNumeric NUnequal(vDescriptor v)		{ return v.BkwUnequal(this); }
public vNumeric NGreater(vDescriptor v)		{ return v.BkwGreater(this); }
public vNumeric NGreaterEq(vDescriptor v)	{ return v.BkwGreaterEq(this); }

vNumeric BkwLess(vInteger a)
    { return BigInteger.valueOf(a.value).compareTo(value) < 0 ? this : null; }
vNumeric BkwLessEq(vInteger a)
    { return BigInteger.valueOf(a.value).compareTo(value) <= 0 ? this : null; }
vNumeric BkwEqual(vInteger a)
    { return BigInteger.valueOf(a.value).compareTo(value) == 0 ? this : null; }
vNumeric BkwUnequal(vInteger a)
    { return BigInteger.valueOf(a.value).compareTo(value) != 0 ? this : null; }
vNumeric BkwGreater(vInteger a)
    { return BigInteger.valueOf(a.value).compareTo(value) > 0 ? this : null; }
vNumeric BkwGreaterEq(vInteger a)
    { return BigInteger.valueOf(a.value).compareTo(value) >= 0 ? this : null; }

vNumeric BkwLess(vReal a) {
    double d = this.mkDouble();
    return (a.value <  d) ? vReal.New(d) : null;
}
vNumeric BkwLessEq(vReal a) {
    double d = this.mkDouble();
    return (a.value <= d) ? vReal.New(d) : null;
}
vNumeric BkwEqual(vReal a) {
    double d = this.mkDouble();
    return (a.value == d) ? vReal.New(d) : null;
}
vNumeric BkwUnequal(vReal a) {
    double d = this.mkDouble();
    return (a.value != d) ? vReal.New(d) : null;
}
vNumeric BkwGreater(vReal a) {
    double d = this.mkDouble();
    return (a.value >  d) ? vReal.New(d) : null;
}
vNumeric BkwGreaterEq(vReal a) {
    double d = this.mkDouble();
    return (a.value >= d) ? vReal.New(d) : null;
}

vNumeric BkwLess(vBigInt a)
    { return a.value.compareTo(this.value) < 0 ? this : null; }
vNumeric BkwLessEq(vBigInt a)
    { return a.value.compareTo(this.value) <= 0 ? this : null; }
vNumeric BkwEqual(vBigInt a)
    { return a.value.compareTo(this.value) == 0 ? this : null; }
vNumeric BkwUnequal(vBigInt a)
    { return a.value.compareTo(this.value) != 0 ? this : null; }
vNumeric BkwGreater(vBigInt a)
    { return a.value.compareTo(this.value) > 0 ? this : null; }
vNumeric BkwGreaterEq(vBigInt a)
    { return a.value.compareTo(this.value) >= 0 ? this : null; }



//  bitwise operations

vNumeric And(vInteger j)	{ return this.And(New(j.value)); }
vNumeric Or(vInteger j)		{ return this.Or(New(j.value)); }
vNumeric Xor(vInteger j)	{ return this.Xor(New(j.value)); }

vNumeric Compl()		{ return New(this.value.not()); }

vNumeric And(vNumeric j) {
    BigInteger b;
    if (j instanceof vBigInt) {
	b = ((vBigInt)j).value;
    } else {
	b = BigInteger.valueOf(((vInteger)j).value);
    }
    return Result(this.value.and(b));
}

vNumeric Or(vNumeric j) {
    BigInteger b;
    if (j instanceof vBigInt) {
	b = ((vBigInt)j).value;
    } else {
	b = BigInteger.valueOf(((vInteger)j).value);
    }
    return Result(this.value.or(b));
}

vNumeric Xor(vNumeric j) {
    BigInteger b;
    if (j instanceof vBigInt) {
	b = ((vBigInt)j).value;
    } else {
	b = BigInteger.valueOf(((vInteger)j).value);
    }
    return Result(this.value.xor(b));
}

vNumeric Shift(vInteger j) {
    long n = j.value;
    if (n > Integer.MAX_VALUE) {
	iRuntime.error(101, j);
	return null;
    } else if (n < Integer.MIN_VALUE) {
	return vInteger.New(0);
    } else {
	return Result(this.value.shiftLeft((int)n));
    }
}



//  i to j by k   (i.ToBy(j,k))

static vInteger vOne = vInteger.New(1);
static vInteger vZero = vInteger.New(0);

public static vDescriptor ToBy(vNumeric v1, vDescriptor v2, vDescriptor v3) {
    final vNumeric i = v1;
    final vNumeric j = v2.mkFixed();
    final vNumeric k = (v3 == null) ? vOne : v3.mkFixed();

    if (k.NGreater(vZero) != null) {	// positive increment
	if (i.NGreater(j) != null) {
	    return null; /*FAIL*/	// no iterations
	} else if (i.NEqual(j) != null) {
	    return i;			// just once
	} else return new vClosure() {
	    vNumeric n = i;
	    { retval = i; }

	    public vDescriptor Resume() {
		n = n.Add(k);
		if (n.NLessEq(j) != null) {
		    return n;
		} else {
		    return null; /*FAIL*/
		}
	    };
	};

    } else if (k.NLess(vZero) != null) {	// negative increment
	if (i.NLess(j) != null) {
	    return null; /*FAIL*/	// no iterations
	} else if (i.NEqual(j) != null) {
	    return i;			// just once
	} else return new vClosure() {
	    vNumeric n = i;
	    { retval = i; }

	    public vDescriptor Resume() {
		n = n.Add(k);
		if (n.NGreaterEq(j) != null) {
		    return n;
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

public vDescriptor ToBy(vDescriptor v2, vDescriptor v3) {
    return ToBy(this, v2, v3);
}



//  i(a, b, c, ...)

public vDescriptor Call(vDescriptor v[])		{ return null; /*FAIL*/}
public vDescriptor Call()				{ return null; /*FAIL*/}
public vDescriptor Call(vDescriptor a)			{ return null; /*FAIL*/}
public vDescriptor Call(vDescriptor a, vDescriptor b)	{ return null; /*FAIL*/}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return null; /*FAIL*/
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	return null; /*FAIL*/
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	return null; /*FAIL*/
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return null; /*FAIL*/
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return null; /*FAIL*/
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	return null; /*FAIL*/
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return null; /*FAIL*/
}



} // class vBigInt

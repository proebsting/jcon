//  vBigInt.java -- Icon "large" (arbitrary precision) integers
//
//  It is assumed throughout that only values not representable by a
//  Java "long" are stored as vBigInts.  See Result() below.



package rts;

import java.math.BigInteger;

public final class vBigInt extends vNumeric {

    private BigInteger value;

    static final int MaxDigits = 30;	//#%#% move to iConfig.java

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
    iRuntime.bomb("BigInt(double) NYI");	//#%#%#%
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



//  runtime primitives

public int hashCode()		{ return value.hashCode(); }

public boolean equals(Object o) {
    return (o instanceof vBigInt) && (((vBigInt)o).value.equals(value));
}

vInteger mkInteger()	{ iRuntime.error(101, this); return null; }
vNumeric mkFixed()	{ return this; }
vReal mkReal()		{ return vReal.New(mkDouble()); }

vString mkString() {
    if (cachedString == null) {
       cachedString = vString.New(value.toString());
    }
    return cachedString;
}

vString write()		{ return mkString(); }

vString image()	{
    int ndigits = (int) (value.bitCount() * 0.3010299956639812 + 0.5);
					//     1 / log2(10)
    if (ndigits >= MaxDigits) {
	return mkString();
    } else {
	return vString.New("integer(~10^" + ndigits + ")");
    }
}



static vString typestring = vString.New("integer");
public vString Type()		{ return typestring; }

// negatives sort just before integers (rank 10); positives sort after
int rank()			{ return (value.signum() < 0 ? 9 : 11); }

int compareTo(vValue v)		{ return value.compareTo(((vBigInt)v).value); }



public vDescriptor ProcessArgs(vDescriptor x) {			// i ! X
    return null; /*FAIL*/	// no way this can be in range
}



//  create vBigInteger or vInteger result as appropriate.

private static vNumeric Result(BigInteger v) {
    if (v.compareTo(MaxInteger) > 0 || v.compareTo(MinInteger) < 0) {
	return vBigInt.New(v);
    } else {
	return vInteger.New(v.longValue());
    }
}




//  unary operations

public vNumeric Negate() {
    return Result(value.negate());
}

public vDescriptor Select() {
    iRuntime.bomb("?BigInt NYI");	//#%#%#%#%
    return null;
}

public vNumeric Abs() {
    return Result(value.abs());
}

public vInteger Limit() {
    iRuntime.error(101);
    return null;
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
    long y = i.value;

    if (x.signum() == 0 && y <= 0) {
	iRuntime.error(204);
    } else if (y < 0) {
	return vInteger.New(0);
    } else if (y == 0) {
	return vInteger.New(1);
    }

    BigInteger v = BigInteger.valueOf(1);
    while (y > 0) {
	if ((y & 1) != 0) {
	    v = v.multiply(x);
	}
	y >>= 1;
	x = x.multiply(x);
    }
    return Result(v);
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



//  i to j by k   (i.ToBy(j,k))

public vDescriptor ToBy(vDescriptor v2, vDescriptor v3) {
    iRuntime.error(101, this);		// out of range
    return null;
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

package rts;

public final class vReal extends vNumeric {

    double value;



// constructors

public static vReal New(double x)	{ return new vReal(x); }
public static vReal New(String x)	{ return new vReal(x); }

private vReal(double x)		{ value = x; }

private vReal(String s)		{ value = Double.valueOf(s).doubleValue(); }



// runtime primitives

public int hashCode()	{ return (int) (value  * 1129.27586206896558); } // = v9

public boolean equals(Object o)	{
    return (o instanceof vReal) && (((vReal)o).value == value);
}

vReal mkReal()		{ return this; }

vInteger mkInteger()	{
    if (value < Long.MIN_VALUE || value > Long.MAX_VALUE) {
	iRuntime.error(101, this);
    }
    return vInteger.New(this.value);
}

vNumeric mkFixed() {
    if (value < Long.MIN_VALUE || value > Long.MAX_VALUE) {
	return vBigInt.New(this.value);
    }
    return vInteger.New(this.value);
}

vString write()		{ return this.mkString(); }
vString image()		{ return this.mkString(); }

vString mkString() {
    if (cachedString != null) {
	return cachedString;
    }
    String s = Double.toString(value + 0.0);	// +0.0 eliminates "-0"
    if (s.indexOf('E') >= 0) {
	s = s.replace('E','e');			// if E notation, change to 'e'
    }
    return cachedString = vString.New(s);
}



static vString typestring = vString.New("real");
public vString Type()	{ return typestring; }

int rank()		{ return 20; }		// reals sort after integers

int compareTo(vValue v) {
    double x = ((vReal) v).value;
    if (this.value < x) {
	return -1;
    } else if (this.value > x) {
	return 1;
    } else {
	return 0;
    }
}



//  static methods for argument processing and defaulting

static double argVal(vDescriptor[] args, int index) {		// required arg
    if (index >= args.length) {
	iRuntime.error(102);
	return 0.0;
    } else {
	return args[index].mkReal().value;
    }
}

static double argVal(vDescriptor[] args, int index, double dflt) { // opt arg
    if (index >= args.length || args[index].isnull()) {
	return dflt;
    } else {
	return args[index].mkReal().value;
    }
}



// operations

public vNumeric Negate()	{ return vReal.New(-value); }

public vDescriptor Select()	{ return this.mkInteger().Select(); }



public vNumeric Add(vDescriptor v)	{ return v.AddInto(this); }
public vNumeric Sub(vDescriptor v)	{ return v.SubFrom(this); }
public vNumeric Mul(vDescriptor v)	{ return v.MulInto(this); }
public vNumeric Div(vDescriptor v)	{ return v.DivInto(this); }
public vNumeric Mod(vDescriptor v)	{ return v.ModInto(this); }
public vNumeric Power(vDescriptor v)	{ return v.PowerOf(this); }



vNumeric AddInto(vReal a)	{ return New(a.value + this.value); }
vNumeric AddInto(vInteger a)	{ return New(a.value + this.value); }
vNumeric AddInto(vBigInt a)	{ return New(a.mkReal().value + this.value); }

vNumeric SubFrom(vReal a)	{ return New(a.value - this.value); }
vNumeric SubFrom(vInteger a)	{ return New(a.value - this.value); }
vNumeric SubFrom(vBigInt a)	{ return New(a.mkReal().value - this.value); }

vNumeric MulInto(vReal a)	{ return New(a.value * this.value); }
vNumeric MulInto(vInteger a)	{ return New(a.value * this.value); }
vNumeric MulInto(vBigInt a)	{ return New(a.mkReal().value * this.value); }



vNumeric DivInto(vReal a) {
    double d = a.value / this.value;
    if (Double.isInfinite(d)) {
	iRuntime.error(204);
    }
    return New(d);
}

vNumeric DivInto(vInteger a) {
    double d = a.value / this.value;
    if (Double.isInfinite(d)) {
	iRuntime.error(204);
    }
    return New(d);
}

vNumeric DivInto(vBigInt a) {
    double d = a.mkDouble() / this.value;
    if (Double.isInfinite(d)) {
	iRuntime.error(204);
    }
    return New(d);
}



vNumeric ModInto(vReal a) {
    if (this.value == 0.0) {
	iRuntime.error(204);
    }
    return New(a.value % this.value);
}

vNumeric ModInto(vInteger a) {
    if (this.value == 0.0) {
	iRuntime.error(204);
    }
    return New(a.value % this.value);
}

vNumeric ModInto(vBigInt a) {
    if (this.value == 0.0) {
	iRuntime.error(204);
    }
    return New(a.mkDouble() % this.value);
}



vNumeric PowerOf(vReal a) {
    double x = a.value;
    double y = this.value;
    if (x < 0.0) {
	iRuntime.error(206);	// no offending value (v9 compatible)
    } else if (x == 0.0 && y <= 0.0) {
	iRuntime.error(204);
    }
    return New(Math.pow(x, y));
}

vNumeric PowerOf(vInteger a) {
    double x = a.value;
    double y = this.value;
    if (x == 0.0 && y <= 0.0) {
	iRuntime.error(204);
    }
    return New(Math.pow(x, y));
}

vNumeric PowerOf(vBigInt a) {
    double x = a.mkDouble();
    double y = this.value;
    if (x < 0.0) {
	iRuntime.error(206);	// no offending value (v9 compatible)
    } else if (x == 0.0 && y <= 0.0) {
	iRuntime.error(204);
    }
    return New(Math.pow(x, y));
}



public vNumeric Abs() {
    if (this.value >= 0 ) {
	return this;
    } else {
	return vReal.New(-this.value);
    }
}



vNumeric And(vNumeric j)	{ return vBigInt.New(value).And(j); }
vNumeric Or(vNumeric j)		{ return vBigInt.New(value).Or(j); }
vNumeric Xor(vNumeric j)	{ return vBigInt.New(value).Xor(j); }

vNumeric Compl()		{ return vBigInt.New(value).Compl(); }
vNumeric And(vInteger j)	{ return vBigInt.New(value).And(j); }
vNumeric Or(vInteger j)		{ return vBigInt.New(value).Or(j); }
vNumeric Xor(vInteger j)	{ return vBigInt.New(value).Xor(j); }
vNumeric Shift(vInteger j)	{ return vBigInt.New(value).Shift(j); }



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

vNumeric BkwLess(vReal a)	{ return (a.value <  this.value) ? this : null;}
vNumeric BkwLessEq(vReal a)	{ return (a.value <= this.value) ? this : null;}
vNumeric BkwEqual(vReal a)	{ return (a.value == this.value) ? this : null;}
vNumeric BkwUnequal(vReal a)	{ return (a.value != this.value) ? this : null;}
vNumeric BkwGreater(vReal a)	{ return (a.value >  this.value) ? this : null;}
vNumeric BkwGreaterEq(vReal a)	{ return (a.value >= this.value) ? this : null;}

vNumeric BkwLess(vBigInt a)	{ return (a.mkDouble() <  value) ? this : null;}
vNumeric BkwLessEq(vBigInt a)	{ return (a.mkDouble() <= value) ? this : null;}
vNumeric BkwEqual(vBigInt a)	{ return (a.mkDouble() == value) ? this : null;}
vNumeric BkwUnequal(vBigInt a)	{ return (a.mkDouble() != value) ? this : null;}
vNumeric BkwGreater(vBigInt a)	{ return (a.mkDouble() >  value) ? this : null;}
vNumeric BkwGreaterEq(vBigInt a){ return (a.mkDouble() >= value) ? this : null;}



} // class vReal

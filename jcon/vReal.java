package rts;

public class vReal extends vNumeric {

    double value;



// constructors

public static vReal New(double x)	{ return new vReal(x); }
public static vReal New(String x)	{ return new vReal(x); }

private vReal(double x)		{ value = x; }
private vReal(String s)		{ value = Double.valueOf(s).doubleValue(); }
				//#%#% handles all cases? what about errs?


// runtime primitives

public int hashCode()	{ return (int) (value  * 1129.27586206896558); } // = v9

public boolean equals(Object o)	{
    return (o instanceof vReal) && (((vReal)o).value == value);
}

vReal mkReal()		{ return this; }

vInteger mkInteger()	{
    if (value < Long.MIN_VALUE || value > Long.MAX_VALUE) {
	iRuntime.error(101, this);
	return null;
    } else {
	return vInteger.New(this.value);
    }
}

vString write()		{ return this.mkString(); }
vString image()		{ return this.mkString(); }

vString mkString() {	// #%#% differs from v9 formatting
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
vString type()		{ return typestring; }

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



//  c.argument0.NumBoth(c) -- tandem coercion to numeric
//
//  given that arg0 (== this) is Real, always convert arg1 to Real

void NumBoth(iBinaryValueClosure c) {
    c.argument1 = c.argument1.mkReal();
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
    if (index >= args.length || args[index].isNull()) {
	return dflt;
    } else {
	return args[index].mkReal().value;
    }
}



// operations

vNumeric Negate()	{ return vReal.New(-value); }

vDescriptor Select()	{ return this.mkInteger().Select(); }

vValue Power(vDescriptor v) {
    if (this.value < 0.0 && (v instanceof vReal)) {
	iRuntime.error(206);	// no offending value (v9 compatible)
    }
    vReal y = v.mkReal();
    if (this.value == 0.0 && y.value <= 0.0) {
	iRuntime.error(204);
    }
    return vReal.New(Math.pow(this.value, y.value));
}

vValue Add(vDescriptor v) {
    return vReal.New(this.value + ((vReal)v).value);
}

vValue Sub(vDescriptor v) {
    return vReal.New(this.value - ((vReal)v).value);
}

vValue Mul(vDescriptor v) {
    return vReal.New(this.value * ((vReal)v).value);
}

vValue Div(vDescriptor v) {
    if (((vReal)v).value == 0) {
	iRuntime.error(204);
    }
    return vReal.New(this.value / ((vReal)v).value);
}

vValue Mod(vDescriptor v) {
    if (((vReal)v).value == 0) {
	iRuntime.error(204);
    }
    return vReal.New(this.value % ((vReal)v).value);
}

vValue Abs() {
    if (this.value >= 0 ) {
	return this;
    } else {
	return vReal.New(-this.value);
    }
}



//  numeric comparisons

vValue NLess(vDescriptor v) {
    vReal vr = (vReal) v;
    return (this.value < vr.value) ? vr : null;
}

vValue NLessEq(vDescriptor v) {
    vReal vr = (vReal) v;
    return (this.value <= vr.value) ? vr : null;
}

vValue NEqual(vDescriptor v) {
    vReal vr = (vReal) v;
    return (this.value == vr.value) ? vr : null;
}

vValue NUnequal(vDescriptor v) {
    vReal vr = (vReal) v;
    return (this.value != vr.value) ? vr : null;
}

vValue NGreaterEq(vDescriptor v) {
    vReal vr = (vReal) v;
    return (this.value >= vr.value) ? vr : null;
}

vValue NGreater(vDescriptor v) {
    vReal vr = (vReal) v;
    return (this.value > vr.value) ? vr : null;
}



} // class vReal

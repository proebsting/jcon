package rts;

public class vReal extends vNumeric {

    double value;



// constructors

vReal(double x)		{ value = x; }
vReal(long n)		{ value = n; }
vReal(String s)		{ value = Double.valueOf(s).doubleValue(); }
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
	    return iNew.Integer(this.value);
    }
}

vString mkString()	{ return iNew.String(this.image()); }

String write()		{ return this.image(); }

String image()		{	//#%#%# differs from v9 formatting 
    String s = String.valueOf(value + 0.0);	 // +0.0 is to eliminate "-0"
    if (s.indexOf('E') >= 0) {
	return s.replace('E','e');	// if E notation, just change to 'e'
    }
    if (Double.isInfinite(value) || Double.isNaN(value) || s.indexOf('.')>=0) {
	return s;		// don't edit if Inf, NaN, or if "." present
    } else {
	return s + ".0";	// add decimal point if missing
    }
}

String type()		{ return "real"; }

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

static double argVal(vDescriptor[] args, int index)		// required arg
{
    if (index >= args.length) {
	iRuntime.error(102);
	return 0.0;
    } else {
	return args[index].mkReal().value;
    }
}

static double argVal(vDescriptor[] args, int index, double dflt) // optional arg
{
    if (index >= args.length || args[index] instanceof vNull) {
	return dflt;
    } else {
	return args[index].mkReal().value;
    }
}



// operations

vNumeric Negate()	{ return iNew.Real(-value); }

vDescriptor Select()	{ return this.mkInteger().Select(); }

vValue Power(vDescriptor v) {
    return iNew.Real(Math.pow(this.value, ((vReal)v).value));
} 

vValue Add(vDescriptor v) {
    return iNew.Real(this.value + ((vReal)v).value);
} 

vValue Sub(vDescriptor v) {
    return iNew.Real(this.value - ((vReal)v).value);
} 

vValue Mul(vDescriptor v) {
    return iNew.Real(this.value * ((vReal)v).value);
} 

vValue Div(vDescriptor v) {
    if (((vReal)v).value == 0) {
	iRuntime.error(204);
    }
    return iNew.Real(this.value / ((vReal)v).value);
} 

vValue Mod(vDescriptor v) {
    if (((vReal)v).value == 0) {
	iRuntime.error(204);
    }
    return iNew.Real(this.value % ((vReal)v).value);
	//#%#% is that the correct definition of % for Icon?
} 

vValue Abs() {
    if (this.value >= 0 ) {
    	return this;
    } else {
    	return iNew.Real(-this.value);
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

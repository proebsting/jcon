package rts;

public class vInteger extends vNumeric {

    long value;



//  constructors

vInteger(long n)	{ value = n; }
vInteger(double x)	{ value = (long)x; } //#%#%#% handle overflow
vInteger(String s)	{ value = Long.parseLong(s); } //#%#% errs??



//  runtime primitives

public int hashCode()	{ return (int) ((13255 * value) >> 10); } // same as v9

public boolean equals(Object o)	{
	return (o instanceof vInteger) && (((vInteger)o).value == value);
}

vInteger mkInteger()	{ return this; }
vReal mkReal()		{ return iNew.Real(this.value); }
vString mkString()	{ return iNew.String(String.valueOf(value)); }

String write()		{ return String.valueOf(value); }
String image()		{ return String.valueOf(value); }
String type()		{ return "integer"; }

int rank()		{ return 10; } 	// integers sort right after &null

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

vValue getproc() {
	return new vIntegerProc(this);
}



//  static methods for argument processing and defaulting

static long argVal(vDescriptor[] args, int index)		// required arg
{
    if (index >= args.length) {
	iRuntime.error(101);
	return 0;
    } else {
	return args[index].mkInteger().value;
    }
}

static long argVal(vDescriptor[] args, int index, int dflt)	// optional arg
{
    if (index >= args.length || args[index] instanceof vNull) {
	return dflt;
    } else {
	return args[index].mkInteger().value;
    }
}



//  operations

vNumeric Negate()	{ return iNew.Integer(-value); }

vDescriptor Select() {
    if (value > 0) {
	return iNew.Integer(1 + k$random.random(value));
    } else if (value == 0) {
	return iNew.Real(k$random.random());
    } else {
	iRuntime.error(205, this);
	return null;
    }
}


//#%#%#% need to rewrite these to handle overflow:
vValue Power(vDescriptor v) {
    long x = this.value;
    long y = ((vInteger)v).value;
    if (x == 0 && y <= 0) {
	iRuntime.error(204);
    }
    long p = 1L;
    for (long i = 0; i < y; i++) {
	p *= x;		//#%#%# totally ignoring overflow.
    }
    return iNew.Integer(p);
} 

vValue Add(vDescriptor v) {
    return iNew.Integer(this.value + ((vInteger)v).value);
} 

vValue Sub(vDescriptor v) {
    return iNew.Integer(this.value - ((vInteger)v).value);
} 

vValue Mul(vDescriptor v) {
    return iNew.Integer(this.value * ((vInteger)v).value);
} 

vValue Div(vDescriptor v) {
    if (((vInteger)v).value == 0) {
	iRuntime.error(201);
    }
    return iNew.Integer(this.value / ((vInteger)v).value);
} 

vValue Mod(vDescriptor v) {
    if (((vInteger)v).value == 0) {
	iRuntime.error(202);
    }
    return iNew.Integer(this.value % ((vInteger)v).value);
} 

vValue Abs() {
    if (this.value >= 0 ) {
    	return this;
    } else {
    	return iNew.Integer(-this.value);
    }
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

	String image()	{ return "function " + this.value.value; }

	String type()	{ return "procedure"; }

	int rank()	{ return 80; }	// integer "procedure"

	vInteger Args()	{ return iNew.Integer(-1); }

	int compareTo(vValue v) {
                String s1 = this.image();
                String s2 = v.image();
                s1 = s1.substring(s1.lastIndexOf(' ') + 1);
                s2 = s2.substring(s2.lastIndexOf(' ') + 1);
                return s1.compareTo(s2);
	}
}


class iIntegerClosure extends iRefClosure {
	vInteger value;

    iIntegerClosure(vInteger value, vDescriptor[] args, iClosure parent) {
        this.value = value;
	arguments = args;
	this.parent = parent;
    }

    vDescriptor function(vDescriptor[] args) {
	long i = value.value;
	if (i <= 0) {
	    i += args.length;
	}
	i -= 1;
	if (i < 0 || i >= args.length) {
		return null;
	}
	return args[(int)i];
    }
}

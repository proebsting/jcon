package rts;

public abstract class vNumeric extends vValue {


// all subclasses must implement:
//  Add, Sub, Mul, Div, Mod, Abs
//  NLess, NLessEq, NEqual, NUnequal, NGreaterEq, NGreater


vNumeric mkNumeric()		{ return this; }
vCset mkCset()			{ return this.mkString().mkCset(); }

vDescriptor Bang(iClosure c)	{ return this.mkString().Bang(c); }
vInteger Size()			{ return this.mkString().Size(); }
vValue Concat(vDescriptor v)	{ return this.mkString().Concat(v); }

vDescriptor Index(vValue i)		{ return this.mkString().Index(i); }
vDescriptor Section(vValue i, vValue j)	{ return this.mkString().Section(i,j); }

vValue Complement()		{ return this.mkCset().Complement(); }
vValue Intersect(vDescriptor x)	{ return this.mkCset().Intersect(x); }
vValue Union(vDescriptor x)	{ return this.mkCset().Union(x); }
vValue Diff(vDescriptor x)	{ return this.mkCset().Diff(x); }

abstract vValue Abs();

vValue getproc()		{ return this.mkInteger().getproc(); }



//  Coerce(iBinaryValueClosure) -- coerce two args to be both integer or both real

static void Coerce(iBinaryValueClosure c) {
    vDescriptor arg0, arg1;
    arg0 = c.argument0;
    arg1 = c.argument1;

    for (;;) {		// repeats at most once

	if (arg0 instanceof vInteger) {

	    if (arg1 instanceof vInteger) {
		return;
	    } else if (arg1 instanceof vReal) {
		c.argument0 = arg0.mkReal();
		return;
	    } else {
		arg1 = c.argument1 = arg1.mkNumeric();
		// and repeat
	    }

	} else if (arg0 instanceof vReal) {
	    c.argument1 = arg1.mkReal();
	    return;

	} else {
	    arg0 = c.argument0 = arg0.mkNumeric();
	    arg1 = c.argument1 = arg1.mkNumeric();
	    // and repeat
	}

    }
}



} // class vNumeric

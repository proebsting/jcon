package rts;

public abstract class vNumeric extends vValue {


// all subclasses must implement the following methods and their reversals:
//  Add, Sub, Mul, Div, Mod, Power, Abs
//  NLess, NLessEq, NEqual, NUnequal, NGreaterEq, NGreater


protected vString cachedString;		// cached string equivalent

public vNumeric Numerate()		{ return this; }

vCset mkCset()				{ return this.mkString().mkCset(); }
vProc mkProc()				{ return this.mkInteger().mkProc(); }


// these operators coerce any numeric value to string
// (this code is not in vValue because non-numerics are handled differently)

public vDescriptor Bang()		{ return this.mkString().Bang(); }
public vInteger Size()			{ return this.mkString().Size(); }
public vString Concat(vDescriptor v)	{ return this.mkString().Concat(v); }

public vDescriptor Index(vDescriptor i){ return this.mkString().Index(i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
					{ return this.mkString().Section(i,j); }

public vValue Complement()		{ return this.mkCset().Complement(); }
public vValue Intersect(vDescriptor x)	{ return this.mkCset().Intersect(x); }
public vValue Union(vDescriptor x)	{ return this.mkCset().Union(x); }
public vValue Diff(vDescriptor x)	{ return this.mkCset().Diff(x); }



} // class vNumeric

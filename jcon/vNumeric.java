package jcon;

public abstract class vNumeric extends vValue {


// all subclasses must implement the following methods and their reversals:
//  Add, Sub, Mul, Div, Mod, Power, Abs
//  NLess, NLessEq, NEqual, NUnequal, NGreaterEq, NGreater


protected vString cachedString;		// cached string equivalent

public vNumeric Numerate()		{ return this; }

public vCset mkCset()			{ return this.mkString().mkCset(); }
public vProc mkProc(int i)		{ return this.mkInteger().mkProc(i); }


// these operators coerce any numeric value to string
// (this code is not in vValue because non-numerics are handled differently)

public vDescriptor Bang()		{ return this.mkString().Bang(); }
public vInteger Size()			{ return this.mkString().Size(); }
public vString Concat(vDescriptor v)	{ return this.mkString().Concat(v); }

public vDescriptor Index(vDescriptor i){ return this.mkString().Index(i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
					{ return this.mkString().Section(i,j); }

public vCset Complement()		{ return this.mkCset().Complement(); }
public vValue Intersect(vDescriptor x)	{ return this.mkCset().Intersect(x); }
public vValue Union(vDescriptor x)	{ return this.mkCset().Union(x); }
public vValue Diff(vDescriptor x)	{ return this.mkCset().Diff(x); }


// bitwise ops are called after conversion to Numeric (vInteger or vBigInt)

abstract vNumeric And(vNumeric j);	// iand(i, j)
abstract vNumeric Or(vNumeric j);	// ior(i, j)
abstract vNumeric Xor(vNumeric j);	// ixor(i, j)

abstract vNumeric Compl();		// icompl(i)
abstract vNumeric And(vInteger j);	// iand(i, j)
abstract vNumeric Or(vInteger j);	// ior(i, j)
abstract vNumeric Xor(vInteger j);	// ixor(i, j)
abstract vNumeric Shift(vInteger j);	// ishift(i, j)



} // class vNumeric

package rts;

public abstract class vNumeric extends vValue {


// all subclasses must implement the following methods and their reversals:
//  Add, Sub, Mul, Div, Mod, Power, Abs
//  NLess, NLessEq, NEqual, NUnequal, NGreaterEq, NGreater


protected vString cachedString;		// cached string equivalent

vNumeric mkNumeric()			{ return this; }
vCset mkCset()				{ return this.mkString().mkCset(); }

//#%#%#% probably need more conversions added to this list:
//#%#%#% (Or should there be fewer... and move these to vValue?)

public vDescriptor Bang()		{ return this.mkString().Bang(); }
public vInteger Size()			{ return this.mkString().Size(); }
public vString Concat(vDescriptor v)	{ return this.mkString().Concat(v); }

public vDescriptor Index(vDescriptor i){ return this.mkString().Index(i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
					{ return this.mkString().Section(i,j); }
public vDescriptor SectPlus(vDescriptor i, vDescriptor j)
					{ return this.mkString().SectPlus(i,j);}
public vDescriptor SectMinus(vDescriptor i, vDescriptor j)
					{return this.mkString().SectMinus(i,j);}

public vValue Complement()		{ return this.mkCset().Complement(); }
public vValue Intersect(vDescriptor x)	{ return this.mkCset().Intersect(x); }
public vValue Union(vDescriptor x)	{ return this.mkCset().Union(x); }
public vValue Diff(vDescriptor x)	{ return this.mkCset().Diff(x); }

vValue getproc()			{ return this.mkInteger().getproc(); }



} // class vNumeric

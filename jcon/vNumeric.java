abstract class vNumeric extends vValue {


// all subclasses must implement:
//  Add, Sub, Mul, Div, Mod
//  NLess, NLessEq, NEqual, NUnequal, NGreaterEq, NGreater


vNumeric mkNumeric()		{ return this; }
vCset mkCset()			{ return this.mkString().mkCset(); }

vDescriptor Bang(iClosure c)	{ return this.mkString().Bang(c); }
vInteger Size()			{ return this.mkString().Size(); }
vValue Concat(vDescriptor v)	{ return this.mkString().Concat(v); }



//  instantiate:  try to convert to integer
//  if that fails, call super.instantiate() for proper error processing

iClosure instantiate (vDescriptor[] args, iClosure parent) {
    try {
    	return this.mkInteger().instantiate(args, parent);
    } catch (iError e) {
    	return super.instantiate(args, parent);
    }
}



//  Coerce(args) -- coerce array of two args to be both integer or both real

static void Coerce(vDescriptor d[]) {
    d[0] = d[0].mkNumeric();
    d[1] = d[1].mkNumeric();
    if (d[0] instanceof vReal) {
	d[1] = d[1].mkReal();
    } else if (d[1] instanceof vReal) {
	d[0] = d[0].mkReal();
    }
}

vValue Complement()		{ return this.mkCset().Complement(); }
vValue Intersect(vDescriptor x)	{ return this.mkCset().Intersect(x); }
vValue Union(vDescriptor x)	{ return this.mkCset().Union(x); }
vValue Diff(vDescriptor x)	{ return this.mkCset().Diff(x); }

} // class vNumeric

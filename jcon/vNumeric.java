abstract class vNumeric extends vValue {


// all subclasses must implement:
//  Add, Sub, Mul, Div, Mod
//  NLess, NLessEq, NEqual, NUnequal, NGreaterEq, NGreater


vNumeric mkNumeric()		{ return this; }

vDescriptor Bang(iClosure c)	{ return this.mkString().Bang(c); }
vInteger Size()			{ return this.mkString().Size(); }
vValue Concat(vDescriptor v)	{ return this.mkString().Concat(v); }


static void Coerce(vDescriptor d[]) {
    d[0] = d[0].mkNumeric();
    d[1] = d[1].mkNumeric();
    if (d[0] instanceof vReal) {
	d[1] = d[1].mkReal();
    } else if (d[1] instanceof vReal) {
	d[0] = d[0].mkReal();
    }
}


} // class vNumeric

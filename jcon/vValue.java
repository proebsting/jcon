//  vValue -- vDescriptor subclass for Icon values.
//
//  vValue is an abstract class encompassing values of all Icon types.
//  Each type implements a concrete class; all records share one class.


abstract class vValue extends vDescriptor {

    // many of these are default methods, often overridden by individual types

    iClosure instantiate(vDescriptor[] args, iClosure parent)
				{ iRuntime.error(106, this); return null; }

    // dereferencing and assignment
    vValue deref()		{ return this; }
    vVariable Assign(vValue x)	{ iRuntime.error(111,this);return null;}

    // accessing functions
    vVariable field(String s)	{ iRuntime.error(107, this); return null;}

    // type conversion
    vString mkString()		{ iRuntime.error(103, this); return null; }
    vInteger mkInteger()	{ iRuntime.error(101, this); return null; }
    vReal mkReal()		{ iRuntime.error(102, this); return null; }
    vNumeric mkNumeric()	{ iRuntime.error(102, this); return null; }

    String write()		{ return this.mkString().write(); }
    String report()		{ return this.image(); }
    abstract String image();	// required of all subclasses

    // simple unary operators that always return values
    vNumeric Negate()		{ return this.mkNumeric().Negate(); }
    vInteger Size()		{ iRuntime.error(112, this); return null; }

    // simple unary operators that may return variables
    vDescriptor isNull()	{ return null; /*FAIL*/ }
    vDescriptor isntNull()	{ return this; }
    vDescriptor Select()	{ iRuntime.error(113, this); return null; }

    // unary operators that are generators
    vDescriptor Bang(iClosure c){ iRuntime.error(116, this); return null; }

    // unary operations on variables
    vDescriptor SelectVar(vSimpleVar v)
    				{ return this.deref().Select(); }
    vDescriptor BangVar(iClosure c, vSimpleVar v)
    				{ return this.deref().Bang(c); }

    // subscripting
    vDescriptor Index(vValue i)				// x1[x2]
    				{ iRuntime.error(114, this); return null; }
    vDescriptor Section(vValue i, vValue j)		// x1[i1:i2]
    				{ iRuntime.error(110, this); return null; }
    vDescriptor IndexVar(vSimpleVar v, vValue i)
				{ return this.deref().Index(i); }
    vDescriptor SectionVar(vSimpleVar v, vValue i, vValue j)
				{ return this.deref().Section(i,j); }

    // simple binary operators
    vValue Add(vDescriptor v)	{ iRuntime.error(102, this); return null; }
    vValue Sub(vDescriptor v)	{ iRuntime.error(102, this); return null; }
    vValue Mul(vDescriptor v)	{ iRuntime.error(102, this); return null; }
    vValue Div(vDescriptor v)	{ iRuntime.error(102, this); return null; }
    vValue Mod(vDescriptor v)	{ iRuntime.error(102, this); return null; }

    vValue NLess(vDescriptor v)      { iRuntime.error(102, this); return null; }
    vValue NLessEq(vDescriptor v)    { iRuntime.error(102, this); return null; }
    vValue NEqual(vDescriptor v)     { iRuntime.error(102, this); return null; }
    vValue NUnequal(vDescriptor v)   { iRuntime.error(102, this); return null; }
    vValue NGreaterEq(vDescriptor v) { iRuntime.error(102, this); return null; }
    vValue NGreater(vDescriptor v)   { iRuntime.error(102, this); return null; }
}

//  vValue -- vDescriptor subclass for Icon values.
//
//  vValue is an abstract class encompassing values of all Icon types.
//  Each type implements a concrete class; all records share one class.


package rts;

public abstract class vValue extends vDescriptor {

    // many of these are default methods, often overridden by individual types

    public iClosure instantiate(vDescriptor[] args, iClosure parent)
    	{ return new iErrorClosure(this, args, parent); }  // will gen err 106

    // dereferencing and assignment
    public vValue deref()		{ return this; }
    public vVariable Assign(vValue x)	{ iRuntime.error(111,this);return null;}

    // sorting
    abstract int rank();	// required of all subclasses (all different)
    int compareTo(vValue v)	{ return 0; }//#%#% for procs & others undefined

    // accessing functions
    vVariable field(String s)	{ iRuntime.error(107, this); return null;}

    // type conversion
    vString mkString()		{ iRuntime.error(103, this); return null; }
    vInteger mkInteger()	{ iRuntime.error(101, this); return null; }
    vReal mkReal()		{ iRuntime.error(102, this); return null; }
    vNumeric mkNumeric()	{ iRuntime.error(102, this); return null; }
    vCset mkCset()		{ iRuntime.error(104, this); return null; }
    vDescriptor[] mkArgs()	{ iRuntime.error(126, this); return null; }

    String write()		{ return this.mkString().write(); }
    String report()		{ return this.image(); }
    abstract String image();	// required of all subclasses

    // simple unary operators that always return values
    vNumeric Negate()		{ return this.mkNumeric().Negate(); }
    vInteger Size()		{ iRuntime.error(112, this); return null; }
    vInteger Serial()		{ return null; /*FAIL*/ }
    vValue Copy()		{ return this; }

    // simple unary operators that may return variables
    vDescriptor isNull()	{ return null; /*FAIL*/ }
    vDescriptor isntNull()	{ return this; }
    vDescriptor Select()	{ iRuntime.error(113, this); return null; }

    // unary operators that are generators
    vDescriptor Bang(iClosure c){ iRuntime.error(116, this); return null; }

    // unary operations on variables
    vDescriptor SelectVar(vVariable v)
    				{ return this.deref().Select(); }
    vDescriptor BangVar(iClosure c, vVariable v)
    				{ return this.deref().Bang(c); }

    // subscripting
    vDescriptor Index(vValue i)				// x1[x2]
    				{ iRuntime.error(114, this); return null; }
    vDescriptor Section(vValue i, vValue j)		// x1[i1:i2]
    				{ iRuntime.error(110, this); return null; }
    vDescriptor IndexVar(vVariable v, vValue i)
				{ return this.deref().Index(i); }
    vDescriptor SectionVar(vVariable v, vValue i, vValue j)
				{ return this.deref().Section(i,j); }

    // simple binary operators
    vValue Power(vDescriptor v)	{ iRuntime.error(102, this); return null; }
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

    vValue LLess(vDescriptor v)      { iRuntime.error(103, this); return null; }
    vValue LLessEq(vDescriptor v)    { iRuntime.error(103, this); return null; }
    vValue LEqual(vDescriptor v)     { iRuntime.error(103, this); return null; }
    vValue LUnequal(vDescriptor v)   { iRuntime.error(103, this); return null; }
    vValue LGreaterEq(vDescriptor v) { iRuntime.error(103, this); return null; }
    vValue LGreater(vDescriptor v)   { iRuntime.error(103, this); return null; }

    vValue Concat(vDescriptor v)     { iRuntime.error(103, this); return null; }

    vValue ListConcat(vDescriptor v)	{ iRuntime.error(108, this); return null; }

    // variable operations
    vString Name()		{ iRuntime.error(111, this); return null; }
    vInteger Args()		{ iRuntime.error(106, this); return null; }
    vValue Proc(long i)		{ return this.getproc(); }
    vValue getproc()		{ return null; }

    // list operations
    vValue Push(vDescriptor v)	{ iRuntime.error(108, this); return null; }
    vValue Pull()		{ iRuntime.error(108, this); return null; }
    vValue Pop()		{ iRuntime.error(108, this); return null; }
    vValue Get()		{ iRuntime.error(108, this); return null; }
    vValue Put(vDescriptor v)	{ iRuntime.error(108, this); return null; }

    // table operations
    vValue Key(iClosure c)	 { iRuntime.error(124, this); return null; }
    vValue Member(vDescriptor i) { iRuntime.error(122, this); return null; }
    vValue Delete(vDescriptor i) { iRuntime.error(122, this); return null; }
    vValue Insert(vDescriptor i, vDescriptor val)
				 { iRuntime.error(122, this); return null; }

    // set operations
    vValue Complement()		    { iRuntime.error(104, this); return null; }
    vValue Union(vDescriptor x)	    { iRuntime.error(120, this); return null; }
    vValue Intersect(vDescriptor x) { iRuntime.error(120, this); return null; }
    vValue Diff(vDescriptor x)	    { iRuntime.error(120, this); return null; }

    // coexpression operations
    vValue Refresh()		{ iRuntime.error(118, this); return null; }

    // other data operations
    vValue Sort(vDescriptor i)	{ iRuntime.error(115, this); return null; }
}

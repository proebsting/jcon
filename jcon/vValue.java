//  vValue -- vDescriptor subclass for Icon values.
//
//  vValue is an abstract class encompassing values of all Icon types.
//  Each type implements a concrete class; all records share one class.

package rts;

public abstract class vValue extends vDescriptor {



// special vValue methods required of all subclasses

abstract int rank();			// rank of type in sorting
abstract int compareTo(vValue v);	// compare to value of same rank


// vValue methods with defaults
vValue getproc()		{ return null; }



// vDescriptor methods (see it for a commented version of this list).
// Many of these are default methods, often overridden by individual types.

boolean isnull()		{ return false; }
vString mkString()		{ iRuntime.error(103, this); return null; }
vInteger mkInteger()		{ iRuntime.error(101, this); return null; }
vReal mkReal()			{ iRuntime.error(102, this); return null; }
vNumeric mkNumeric()		{ iRuntime.error(102, this); return null; }
vCset mkCset()			{ iRuntime.error(104, this); return null; }
vDescriptor[] mkArgs()		{ iRuntime.error(126, this); return null; }
vValue[] mkArray()		{ iRuntime.error(125, this); return null; }

vString write()			{ return this.mkString(); }
vString report()		{ return this.image(); }
abstract vString image();		// required of all subclasses

public vValue Deref()		{ return this; }
public vVariable Assign(vDescriptor x) { iRuntime.error(111,this); return null;}
public vVariable SubjAssign(vDescriptor x)
				{ iRuntime.error(111,this); return null; }

public vDescriptor resume()	{ return null; /*FAIL*/ }
public vInteger Limit()		{ return this.mkInteger().Limit(); }
public vDescriptor Conjunction(vDescriptor x)	{ return x; }
public vDescriptor ProcessArgs(vDescriptor x)
				{ iRuntime.error(106, this); return null; }
public vDescriptor Activate(vDescriptor x)
				{ iRuntime.error(118, this); return null; }
public vDescriptor ToBy(vDescriptor j, vDescriptor k)
				{ return this.mkInteger().ToBy(j, k); }

public vNumeric Numerate()	{ return this.mkNumeric(); }
public vNumeric Negate()	{ return this.mkNumeric().Negate(); }
public vInteger Size()		{ iRuntime.error(112, this); return null; }
public vValue Complement()	{ iRuntime.error(104, this); return null; }
public vCoexp Refresh()		{ iRuntime.error(118, this); return null; }
public vString TabMatch()	{ return this.mkString().TabMatch(); }
public vNumeric Abs()		{ return this.mkNumeric().Abs(); }
public vValue Copy()		{ return this; }
public abstract vString Type();		// required of all subclasses

public vDescriptor IsNull() 	{ return null; /*FAIL*/ }
public vDescriptor IsntNull() 	{ return this; }
public vDescriptor Select()	{ iRuntime.error(113, this); return null; }
public vDescriptor Bang()	{ iRuntime.error(116, this); return null; }
public vDescriptor Key()	{ iRuntime.error(124, this); return null; }

public vString Name()		{ iRuntime.error(111, this); return null; }
public vInteger Args()		{ iRuntime.error(106, this); return null; }
public vValue Proc(long i)	{ return this.getproc(); }

public vVariable Field(String s){ iRuntime.error(107, this); return null;}
public vDescriptor Index(vDescriptor i)
				{ iRuntime.error(114, this); return null; }
public vDescriptor Section(vDescriptor i, vDescriptor j)
				{ iRuntime.error(110, this); return null; }
public vDescriptor SectPlus(vDescriptor i, vDescriptor j)
				{ iRuntime.error(110, this); return null; }
public vDescriptor SectMinus(vDescriptor i, vDescriptor j)
				{ iRuntime.error(110, this); return null; }

public vNumeric Add(vDescriptor v)	{ return mkNumeric().Add(v); }
public vNumeric Sub(vDescriptor v)	{ return mkNumeric().Sub(v); }
public vNumeric Mul(vDescriptor v)	{ return mkNumeric().Mul(v); }
public vNumeric Div(vDescriptor v)	{ return mkNumeric().Div(v); }
public vNumeric Mod(vDescriptor v)	{ return mkNumeric().Mod(v); }
public vNumeric Power(vDescriptor v)	{ return mkNumeric().Power(v); }

public vNumeric NLess(vDescriptor v)	{ return mkNumeric().NLess(v); }
public vNumeric NLessEq(vDescriptor v)	{ return mkNumeric().NLessEq(v); }
public vNumeric NEqual(vDescriptor v)	{ return mkNumeric().NEqual(v); }
public vNumeric NUnequal(vDescriptor v)	{ return mkNumeric().NUnequal(v); }
public vNumeric NGreaterEq(vDescriptor v){return mkNumeric().NGreaterEq(v); }
public vNumeric NGreater(vDescriptor v)	{ return mkNumeric().NGreater(v); }

public vString LLess(vDescriptor v)	{ return mkString().LLess(v); }
public vString LLessEq(vDescriptor v)	{ return mkString().LLessEq(v); }
public vString LEqual(vDescriptor v)	{ return mkString().LEqual(v); }
public vString LUnequal(vDescriptor v)	{ return mkString().LUnequal(v); }
public vString LGreaterEq(vDescriptor v){ return mkString().LGreaterEq(v); }
public vString LGreater(vDescriptor v)	{ return mkString().LGreater(v); }

public vValue VEqual(vDescriptor v) {	vValue vv = (vValue) v;	//  a === b
					return this.equals(vv) ? vv : null;
					}
public vValue VUnequal(vDescriptor v) {	vValue vv = (vValue) v;	//  a ~=== b
					return this.equals(vv) ? null : vv;
					}

public vString Concat(vDescriptor v)	{ return mkString().Concat(v); }
public vList ListConcat(vDescriptor v)	{ iRuntime.error(108,this);return null;}

public vList Push(vDescriptor v)	{ iRuntime.error(108,this);return null;}
public vValue Pull()			{ iRuntime.error(108,this);return null;}
public vValue Pop()			{ iRuntime.error(108,this);return null;}
public vValue Get()			{ iRuntime.error(108,this);return null;}
public vList Put(vDescriptor v)		{ iRuntime.error(108,this);return null;}

public vValue Member(vDescriptor i)	{ iRuntime.error(122,this);return null;}
public vValue Delete(vDescriptor i)	{ iRuntime.error(122,this);return null;}
public vValue Insert(vDescriptor i, vDescriptor val)
					{ iRuntime.error(122,this);return null;}

public vValue Intersect(vDescriptor x)	{ iRuntime.error(120,this);return null;}
public vValue Union(vDescriptor x)	{ iRuntime.error(120,this);return null;}
public vValue Diff(vDescriptor x)	{ iRuntime.error(120,this);return null;}

public vInteger Serial()		{ return null; /*FAIL*/ }
public vList Sort(int i)		{ iRuntime.error(115,this);return null;}

vNumeric AddInto(vInteger a)		{ return mkNumeric().AddInto(a); }
vNumeric SubFrom(vInteger a)		{ return mkNumeric().SubFrom(a); }
vNumeric MulInto(vInteger a)		{ return mkNumeric().MulInto(a); }
vNumeric DivInto(vInteger a)		{ return mkNumeric().DivInto(a); }
vNumeric ModInto(vInteger a)		{ return mkNumeric().ModInto(a); }
vNumeric PowerOf(vInteger a)		{ return mkNumeric().PowerOf(a); }

vNumeric RevLess(vInteger a)		{ return mkNumeric().RevLess(a); }
vNumeric RevLessEq(vInteger a)		{ return mkNumeric().RevLessEq(a); }
vNumeric RevEqual(vInteger a)		{ return mkNumeric().RevEqual(a); }
vNumeric RevUnequal(vInteger a)		{ return mkNumeric().RevUnequal(a); }
vNumeric RevGreaterEq(vInteger a)	{ return mkNumeric().RevGreaterEq(a); }
vNumeric RevGreater(vInteger a)		{ return mkNumeric().RevGreater(a); }

vNumeric AddInto(vReal a)		{ return mkNumeric().AddInto(a); }
vNumeric SubFrom(vReal a)		{ return mkNumeric().SubFrom(a); }
vNumeric MulInto(vReal a)		{ return mkNumeric().MulInto(a); }
vNumeric DivInto(vReal a)		{ return mkNumeric().DivInto(a); }
vNumeric ModInto(vReal a)		{ return mkNumeric().ModInto(a); }
vNumeric PowerOf(vReal a)		{ return mkNumeric().PowerOf(a); }

vNumeric RevLess(vReal a)		{ return mkNumeric().RevLess(a); }
vNumeric RevLessEq(vReal a)		{ return mkNumeric().RevLessEq(a); }
vNumeric RevEqual(vReal a)		{ return mkNumeric().RevEqual(a); }
vNumeric RevUnequal(vReal a)		{ return mkNumeric().RevUnequal(a); }
vNumeric RevGreaterEq(vReal a)		{ return mkNumeric().RevGreaterEq(a); }
vNumeric RevGreater(vReal a)		{ return mkNumeric().RevGreater(a); }




//#%#% ????
public vDescriptor SelectVar(vVariable v) { return this.Deref().Select(); }
public vDescriptor BangVar(vVariable v) { return this.Deref().Bang(); }
public vDescriptor IndexVar(vVariable v, vDescriptor i)
				{ return this.Deref().Index(i); }
public vDescriptor SectionVar(vVariable v, vDescriptor i, vDescriptor j)
				{ return this.Deref().Section(i, j); }


//#%#% to be handled:
public vVariable Swap(vDescriptor x)      {iRuntime.bomb("Swap"); return null; }
public vDescriptor RevAssign(vDescriptor x){iRuntime.bomb("RevAssign"); return null;}
public vDescriptor RevSwap(vDescriptor x)  {iRuntime.bomb("RevSwap"); return null; }



public iClosure instantiate(vDescriptor[] args, iClosure parent) //#%#%#%
    { return new iErrorClosure(this, args, parent); }  // will gen err 106

} // class vValue

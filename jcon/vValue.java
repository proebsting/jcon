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
public vVariable Assign(vDescriptor x){ iRuntime.error(111,this); return null; }

public vDescriptor resume()	{ return null; /*FAIL*/ }
public vInteger Limit()		{ return this.mkInteger().Limit(); }
public vDescriptor Conjunction(vDescriptor x)	{ return x; }
public vDescriptor ProcessArgs(vDescriptor x)
				{ iRuntime.error(106, this); return null; }
public vDescriptor Activate(vDescriptor x)
				{ iRuntime.error(118, this); return null; }
public vDescriptor ToBy(vDescriptor j, vDescriptor k)
				{ return this.mkInteger().ToBy(j, k); }

public vNumeric Numerate()	{ return this.mkNumeric().Numerate(); }
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

public vNumeric Add(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric Sub(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric Mul(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric Div(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric Mod(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric Power(vDescriptor v)
				{ iRuntime.error(102, this); return null; }

public vNumeric NLess(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric NLessEq(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric NEqual(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric NUnequal(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric NGreaterEq(vDescriptor v)
				{ iRuntime.error(102, this); return null; }
public vNumeric NGreater(vDescriptor v)
				{ iRuntime.error(102, this); return null; }

public vString LLess(vDescriptor v)
				{ iRuntime.error(103, this); return null; }
public vString LLessEq(vDescriptor v)
				{ iRuntime.error(103, this); return null; }
public vString LEqual(vDescriptor v)
				{ iRuntime.error(103, this); return null; }
public vString LUnequal(vDescriptor v)
				{ iRuntime.error(103, this); return null; }
public vString LGreaterEq(vDescriptor v){ iRuntime.error(103, this); return null; }
public vString LGreater(vDescriptor v)
				{ iRuntime.error(103, this); return null; }

public vValue VEqual(vDescriptor v) {	// ===
				vValue vv = (vValue) v;
				return this.equals(vv) ? vv : null;
				}
public vValue VUnequal(vDescriptor v) {	// ~===
				vValue vv = (vValue) v;
				return this.equals(vv) ? null : vv;
				}

public vString Concat(vDescriptor v)
				{ iRuntime.error(103, this); return null; }
public vList ListConcat(vDescriptor v)
				{ iRuntime.error(108, this); return null; }

public vList Push(vDescriptor v){ iRuntime.error(108, this); return null; }
public vValue Pull()		{ iRuntime.error(108, this); return null; }
public vValue Pop()		{ iRuntime.error(108, this); return null; }
public vValue Get()		{ iRuntime.error(108, this); return null; }
public vList Put(vDescriptor v) { iRuntime.error(108, this); return null; }

public vValue Member(vDescriptor i)  { iRuntime.error(122, this); return null; }
public vValue Delete(vDescriptor i)  { iRuntime.error(122, this); return null; }
public vValue Insert(vDescriptor i, vDescriptor val)
			    	{ iRuntime.error(122, this); return null; }

public vValue Intersect(vDescriptor x){ iRuntime.error(120, this); return null;}
public vValue Union(vDescriptor x)    { iRuntime.error(120, this); return null;}
public vValue Diff(vDescriptor x)     { iRuntime.error(120, this); return null;}

public vInteger Serial()	{ return null; /*FAIL*/ }
public vList Sort(int i)	{ iRuntime.error(115, this); return null; }

vNumeric AddInto(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric SubFrom(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric MulInto(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric DivInto(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric ModInto(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric PowerOf(vInteger a)	{ iRuntime.error(102, this); return null; }

vNumeric RevLess(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric RevLessEq(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric RevEqual(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric RevUnequal(vInteger a)	{ iRuntime.error(102, this); return null; }
vNumeric RevGreaterEq(vInteger a){iRuntime.error(102, this); return null; }
vNumeric RevGreater(vInteger a)	{ iRuntime.error(102, this); return null; }

vNumeric AddInto(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric SubFrom(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric MulInto(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric DivInto(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric ModInto(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric PowerOf(vReal a)	{ iRuntime.error(102, this); return null; }

vNumeric RevLess(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric RevLessEq(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric RevEqual(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric RevUnequal(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric RevGreaterEq(vReal a)	{ iRuntime.error(102, this); return null; }
vNumeric RevGreater(vReal a)	{ iRuntime.error(102, this); return null; }




//#%#% ????
public vDescriptor SelectVar(vVariable v) { return this.Deref().Select(); }
public vDescriptor BangVar(vVariable v) { return this.Deref().Bang(); }
public vDescriptor IndexVar(vVariable v, vDescriptor i)
				{ return this.Deref().Index(i); }
public vDescriptor SectionVar(vVariable v, vDescriptor i, vDescriptor j)
				{ return this.Deref().Section(i, j); }


//#%#% to be handled:
public vVariable Swap(vDescriptor x)      {iRuntime.bomb("Swap"); return null; }
public vVariable SubjAssign(vDescriptor x) {iRuntime.bomb("SubjAssign");return null;}
public vDescriptor RevAssign(vDescriptor x){iRuntime.bomb("RevAssign"); return null;}
public vDescriptor RevSwap(vDescriptor x)  {iRuntime.bomb("RevSwap"); return null; }



public iClosure instantiate(vDescriptor[] args, iClosure parent) //#%#%#%
    { return new iErrorClosure(this, args, parent); }  // will gen err 106

} // class vValue

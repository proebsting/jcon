//  vValue -- vDescriptor subclass for Icon values.
//
//  vValue is an abstract class encompassing values of all Icon types.
//  Each type implements a concrete class; all records share one class.

package rts;

public abstract class vValue extends vDescriptor {



// special vValue methods required of all subclasses

abstract int rank();			// rank of type in sorting
abstract int compareTo(vValue v);	// compare to value of same rank



// vDescriptor methods (see it for a commented version of this list).
// Many of these are default methods, often overridden by individual types.

boolean isnull()		{ return false; }
vString mkString()		{ iRuntime.error(103, this); return null; }
vInteger mkInteger()		{ iRuntime.error(101, this); return null; }
vReal mkReal()			{ iRuntime.error(102, this); return null; }
vCset mkCset()			{ iRuntime.error(104, this); return null; }
vProc mkProc(int i)		{ iRuntime.error(106, this); return null; }
vValue[] mkArray(int errno)	{ iRuntime.error(errno, this); return null; }

vString write()			{ return this.mkString(); }
vString report()		{ return this.image(); }
abstract vString image();		// required of all subclasses

public vValue Deref()		{ return this; }
public vVariable Assign(vDescriptor x) { iRuntime.error(111,this); return null;}
public vVariable SubjAssign(vDescriptor x)
				{ iRuntime.error(111,this); return null; }

public vDescriptor Resume()	{ return null; /*FAIL*/ }
public vInteger Limit()		{ return this.mkInteger().Limit(); }
public vDescriptor Conjunction(vDescriptor x)	{ return x; }
public vDescriptor ProcessArgs(vDescriptor x)
				{ iRuntime.error(106, this); return null; }
public vDescriptor Activate(vDescriptor x)
				{ iRuntime.error(118, this); return null; }
public vDescriptor ToBy(vDescriptor j, vDescriptor k)
				{ return this.mkInteger().ToBy(j, k); }

public vNumeric Numerate()	{ iRuntime.error(102, this); return null; }
public vNumeric Negate()	{ return this.Numerate().Negate(); }
public vInteger Size()		{ iRuntime.error(112, this); return null; }
public vValue Complement()	{ iRuntime.error(104, this); return null; }
public vCoexp Refresh()		{ iRuntime.error(118, this); return null; }
public vDescriptor TabMatch()	{ return this.mkString().TabMatch(); }
public vNumeric Abs()		{ return this.Numerate().Abs(); }
public vValue Copy()		{ return this; }
public abstract vString Type();		// required of all subclasses

public vDescriptor IsNull() 	{ return null; /*FAIL*/ }
public vDescriptor IsntNull() 	{ return this; }
public vDescriptor Select()	{ iRuntime.error(113, this); return null; }
public vDescriptor Bang()	{ iRuntime.error(116, this); return null; }
public vDescriptor Key()	{ iRuntime.error(124, this); return null; }

public vString Name()		{ iRuntime.error(111, this); return null; }
public vInteger Args()		{ iRuntime.error(106, this); return null; }

public vVariable Field(String s){ iRuntime.error(107, this); return null;}
public vDescriptor Index(vDescriptor i)
				{ iRuntime.error(114, this); return null; }
public vDescriptor Section(vDescriptor i, vDescriptor j)
				{ iRuntime.error(110, this); return null; }

public vNumeric Add(vDescriptor v)	{ return Numerate().Add(v); }
public vNumeric Sub(vDescriptor v)	{ return Numerate().Sub(v); }
public vNumeric Mul(vDescriptor v)	{ return Numerate().Mul(v); }
public vNumeric Div(vDescriptor v)	{ return Numerate().Div(v); }
public vNumeric Mod(vDescriptor v)	{ return Numerate().Mod(v); }
public vNumeric Power(vDescriptor v)	{ return Numerate().Power(v); }

public vNumeric NLess(vDescriptor v)	{ return Numerate().NLess(v); }
public vNumeric NLessEq(vDescriptor v)	{ return Numerate().NLessEq(v); }
public vNumeric NEqual(vDescriptor v)	{ return Numerate().NEqual(v); }
public vNumeric NUnequal(vDescriptor v)	{ return Numerate().NUnequal(v); }
public vNumeric NGreaterEq(vDescriptor v){return Numerate().NGreaterEq(v); }
public vNumeric NGreater(vDescriptor v)	{ return Numerate().NGreater(v); }

public vString LLess(vDescriptor v)	{ return mkString().LLess(v); }
public vString LLessEq(vDescriptor v)	{ return mkString().LLessEq(v); }
public vString LEqual(vDescriptor v)	{ return mkString().LEqual(v); }
public vString LUnequal(vDescriptor v)	{ return mkString().LUnequal(v); }
public vString LGreaterEq(vDescriptor v){ return mkString().LGreaterEq(v); }
public vString LGreater(vDescriptor v)	{ return mkString().LGreater(v); }

public vValue VEqual(vDescriptor v) {	vValue vv = v.Deref();	//  a === b
					return this.equals(vv) ? vv : null;
					}
public vValue VUnequal(vDescriptor v) {	vValue vv = v.Deref();	//  a ~=== b
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
public vList Sort(int i)    { return vList.New(iSort.sort(this.mkArray(115))); }

vNumeric AddInto(vInteger a)		{ return Numerate().AddInto(a); }
vNumeric SubFrom(vInteger a)		{ return Numerate().SubFrom(a); }
vNumeric MulInto(vInteger a)		{ return Numerate().MulInto(a); }
vNumeric DivInto(vInteger a)		{ return Numerate().DivInto(a); }
vNumeric ModInto(vInteger a)		{ return Numerate().ModInto(a); }
vNumeric PowerOf(vInteger a)		{ return Numerate().PowerOf(a); }

vNumeric BkwLess(vInteger a)		{ return Numerate().BkwLess(a); }
vNumeric BkwLessEq(vInteger a)		{ return Numerate().BkwLessEq(a); }
vNumeric BkwEqual(vInteger a)		{ return Numerate().BkwEqual(a); }
vNumeric BkwUnequal(vInteger a)		{ return Numerate().BkwUnequal(a); }
vNumeric BkwGreaterEq(vInteger a)	{ return Numerate().BkwGreaterEq(a); }
vNumeric BkwGreater(vInteger a)		{ return Numerate().BkwGreater(a); }

vNumeric AddInto(vReal a)		{ return Numerate().AddInto(a); }
vNumeric SubFrom(vReal a)		{ return Numerate().SubFrom(a); }
vNumeric MulInto(vReal a)		{ return Numerate().MulInto(a); }
vNumeric DivInto(vReal a)		{ return Numerate().DivInto(a); }
vNumeric ModInto(vReal a)		{ return Numerate().ModInto(a); }
vNumeric PowerOf(vReal a)		{ return Numerate().PowerOf(a); }

vNumeric BkwLess(vReal a)		{ return Numerate().BkwLess(a); }
vNumeric BkwLessEq(vReal a)		{ return Numerate().BkwLessEq(a); }
vNumeric BkwEqual(vReal a)		{ return Numerate().BkwEqual(a); }
vNumeric BkwUnequal(vReal a)		{ return Numerate().BkwUnequal(a); }
vNumeric BkwGreaterEq(vReal a)		{ return Numerate().BkwGreaterEq(a); }
vNumeric BkwGreater(vReal a)		{ return Numerate().BkwGreater(a); }

public vDescriptor SelectVar(vVariable v) { return this.Deref().Select(); }
public vDescriptor BangVar(vVariable v) { return this.Deref().Bang(); }
public vDescriptor IndexVar(vVariable v, vDescriptor i)
					{ return this.Deref().Index(i); }
public vDescriptor SectionVar(vVariable v, vDescriptor i, vDescriptor j)
					{ return this.Deref().Section(i, j); }



public vDescriptor Call(vDescriptor v[]) {
	return this.mkProc((v.length > 0) ? v.length : -1).Call(v);
}
public vDescriptor Call() {
	return this.mkProc(-1).Call();		// -1 so not proc(0)!
}
public vDescriptor Call(vDescriptor a) {
	return this.mkProc(1).Call(a);
}
public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return this.mkProc(2).Call(a, b);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c){
	return this.mkProc(3).Call(a, b, c);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	return this.mkProc(4).Call(a, b, c, d);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	return this.mkProc(5).Call(a, b, c, d, e);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return this.mkProc(6).Call(a, b, c, d, e, f);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return this.mkProc(7).Call(a, b, c, d, e, f, g);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	return this.mkProc(8).Call(a, b, c, d, e, f, g, h);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return this.mkProc(9).Call(a, b, c, d, e, f, g, h, i);
}



} // class vValue

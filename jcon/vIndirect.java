//  vIndirect -- vDescriptor subclass for indirect pointers.
//
//  Subclassed by k$Value (special keywords) and vVariable (variables).

package rts;

public abstract class vIndirect extends vDescriptor {



// must be implemented by subclasses

public abstract vValue Deref();			// dereference
public abstract vVariable Assign(vDescriptor x);// assign
public abstract vString Name();			// name



// overriding java.lang.Object

public boolean equals(Object o)		{ return Deref().equals(o); }
public int hashCode()			{ return Deref().hashCode(); }



// For many operations on variables, default action is to dereference and retry.
// (See vDescriptor.java for a commented version of this method list.)

boolean isnull()			{ return Deref().isnull(); }
vString mkString()			{ return Deref().mkString(); }
vInteger mkInteger()			{ return Deref().mkInteger(); }
vReal mkReal()				{ return Deref().mkReal(); }
vNumeric mkNumeric()			{ return Deref().mkNumeric(); }
vCset mkCset()				{ return Deref().mkCset(); }
vDescriptor[] mkArgs()			{ return Deref().mkArgs(); }
vValue[] mkArray()			{ return Deref().mkArray(); }

vString write()				{ return Deref().write(); }
vString image()				{ return Deref().image(); }
vString report()			{ return Deref().report(); }

public vVariable SubjAssign(vDescriptor x)	{ return this.Assign(x); }
public vDescriptor resume()			{ return null; /*FAIL*/ }
public vInteger Limit()				{ return Deref().Limit(); }
public vDescriptor Conjunction(vDescriptor x)	{return Deref().Conjunction(x);}
public vDescriptor ProcessArgs(vDescriptor x)   {return Deref().ProcessArgs(x);}
public vDescriptor Activate(vDescriptor x)	{ return Deref().Activate(x); }
public vDescriptor ToBy(vDescriptor j,vDescriptor k) {return Deref().ToBy(j,k);}

public vNumeric Negate()		{ return Deref().Negate(); }
public vNumeric Numerate()		{ return Deref().Numerate(); }
public vInteger Size()			{ return Deref().Size(); }
public vValue Complement()		{ return Deref().Complement(); }
public vCoexp Refresh()			{ return Deref().Refresh(); }
public vString TabMatch()		{ return Deref().TabMatch(); }
public vNumeric Abs()			{ return Deref().Abs(); }
public vValue Copy()			{ return Deref().Copy(); }
public vString Type()			{ return Deref().Type(); }

public vDescriptor IsNull()		{ return Deref().IsNull(); }
public vDescriptor IsntNull()		{ return Deref().IsntNull(); }
public vDescriptor Select()		{ return Deref().Select(); }
public vDescriptor Bang()		{ return Deref().Bang(); }
public vDescriptor Key()		{ return Deref().Key(); }

public vInteger Args()			{ return Deref().Args(); }
public vValue Proc(long i)		{ return Deref().Proc(i); }

public vVariable Field(String s)	{ return Deref().Field(s); }
public vDescriptor Index(vDescriptor i)	{ return Deref().Index(i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
					{ return Deref().Section(i, j); }
public vDescriptor SectPlus(vDescriptor i, vDescriptor j)
					{ return Deref().SectPlus(i, j); }
public vDescriptor SectMinus(vDescriptor i, vDescriptor j)
					{ return Deref().SectMinus(i, j); }

public vNumeric Add(vDescriptor v)	{ return Deref().Add(v); }
public vNumeric Sub(vDescriptor v)	{ return Deref().Sub(v); }
public vNumeric Mul(vDescriptor v)	{ return Deref().Mul(v); }
public vNumeric Div(vDescriptor v)	{ return Deref().Div(v); }
public vNumeric Mod(vDescriptor v)	{ return Deref().Mod(v); }
public vNumeric Power(vDescriptor v)	{ return Deref().Power(v); }

public vNumeric NLess(vDescriptor v)	{ return Deref().NLess(v); }
public vNumeric NLessEq(vDescriptor v)	{ return Deref().NLessEq(v); }
public vNumeric NEqual(vDescriptor v)	{ return Deref().NEqual(v); }
public vNumeric NUnequal(vDescriptor v)	{ return Deref().NUnequal(v); }
public vNumeric NGreaterEq(vDescriptor v){ return Deref().NGreaterEq(v); }
public vNumeric NGreater(vDescriptor v)	{ return Deref().NGreater(v); }

public vString LLess(vDescriptor v)	{ return Deref().LLess(v); }
public vString LLessEq(vDescriptor v)	{ return Deref().LLessEq(v); }
public vString LEqual(vDescriptor v)	{ return Deref().LEqual(v); }
public vString LUnequal(vDescriptor v)	{ return Deref().LUnequal(v); }
public vString LGreaterEq(vDescriptor v){ return Deref().LGreaterEq(v); }
public vString LGreater(vDescriptor v)	{ return Deref().LGreater(v); }

public vValue VEqual(vDescriptor v)	{ return Deref().VEqual(v); }
public vValue VUnequal(vDescriptor v)	{ return Deref().VUnequal(v); }

public vString Concat(vDescriptor v)	{ return Deref().Concat(v); }
public vList ListConcat(vDescriptor v)	{ return Deref().ListConcat(v); }

public vList Push(vDescriptor v)	{ return Deref().Push(v); }
public vValue Pull()			{ return Deref().Pull(); }
public vValue Pop()			{ return Deref().Pop(); }
public vValue Get()			{ return Deref().Get(); }
public vList Put(vDescriptor v)		{ return Deref().Put(v); }

public vValue Member(vDescriptor i)	{ return Deref().Member(i); }
public vValue Delete(vDescriptor i)	{ return Deref().Delete(i); }
public vValue Insert(vDescriptor i, vDescriptor val)
					{ return Deref().Insert(i, val); }

public vValue Intersect(vDescriptor x)	{ return Deref().Intersect(x); }
public vValue Union(vDescriptor x)	{ return Deref().Union(x); }
public vValue Diff(vDescriptor x)	{ return Deref().Diff(x); }

public vInteger Serial()		{ return Deref().Serial(); }
public vList Sort(int i)		{ return Deref().Sort(i); }

vNumeric AddInto(vInteger a)		{ return Deref().AddInto(a); }
vNumeric SubFrom(vInteger a)		{ return Deref().SubFrom(a); }
vNumeric MulInto(vInteger a)		{ return Deref().MulInto(a); }
vNumeric DivInto(vInteger a)		{ return Deref().DivInto(a); }
vNumeric ModInto(vInteger a)		{ return Deref().ModInto(a); }
vNumeric PowerOf(vInteger a)		{ return Deref().PowerOf(a); }

vNumeric RevLess(vInteger a)		{ return Deref().RevLess(a); }
vNumeric RevLessEq(vInteger a)		{ return Deref().RevLessEq(a); }
vNumeric RevEqual(vInteger a)		{ return Deref().RevEqual(a); }
vNumeric RevUnequal(vInteger a)		{ return Deref().RevUnequal(a); }
vNumeric RevGreaterEq(vInteger a)	{ return Deref().RevGreaterEq(a); }
vNumeric RevGreater(vInteger a)		{ return Deref().RevGreater(a); }

vNumeric AddInto(vReal a)		{ return Deref().AddInto(a); }
vNumeric SubFrom(vReal a)		{ return Deref().SubFrom(a); }
vNumeric MulInto(vReal a)		{ return Deref().MulInto(a); }
vNumeric DivInto(vReal a)		{ return Deref().DivInto(a); }
vNumeric ModInto(vReal a)		{ return Deref().ModInto(a); }
vNumeric PowerOf(vReal a)		{ return Deref().PowerOf(a); }

vNumeric RevLess(vReal a)		{ return Deref().RevLess(a); }
vNumeric RevLessEq(vReal a)		{ return Deref().RevLessEq(a); }
vNumeric RevEqual(vReal a)		{ return Deref().RevEqual(a); }
vNumeric RevUnequal(vReal a)		{ return Deref().RevUnequal(a);}
vNumeric RevGreaterEq(vReal a)		{ return Deref().RevGreaterEq(a) ;}
vNumeric RevGreater(vReal a)		{ return Deref().RevGreater(a); }



//#%#% to be handled:
public vVariable Swap(vDescriptor x)      {iRuntime.bomb("Swap"); return null; }
public vDescriptor RevAssign(vDescriptor x){iRuntime.bomb("RevAssign"); return null;}
public vDescriptor RevSwap(vDescriptor x)  {iRuntime.bomb("RevSwap"); return null; }


public iClosure instantiate(vDescriptor[] args, iClosure parent) //#%#%#%
				{ return Deref().instantiate(args,parent);}



} // class vIndirect

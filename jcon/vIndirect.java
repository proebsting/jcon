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

public boolean equals(Object o)		{ return this.Deref().equals(o); }
public int hashCode()			{ return this.Deref().hashCode(); }



// For many operations on variables, default action is to dereference and retry.
// (See vDescriptor.java for a commented version of this method list.)

boolean isnull()			{ return this.Deref().isnull(); }
vString mkString()			{ return this.Deref().mkString(); }
vInteger mkInteger()			{ return this.Deref().mkInteger(); }
vReal mkReal()				{ return this.Deref().mkReal(); }
vNumeric mkNumeric()			{ return this.Deref().mkNumeric(); }
vCset mkCset()				{ return this.Deref().mkCset(); }
vDescriptor[] mkArgs()			{ return this.Deref().mkArgs(); }
vValue[] mkArray()			{ return this.Deref().mkArray(); }

vString write()				{ return this.Deref().write(); }
vString image()				{ return this.Deref().image(); }
vString report()			{ return this.Deref().report(); }

public vDescriptor resume()		{ return null; /*FAIL*/ }
public vInteger Limit()			{ return this.Deref().Limit(); }
public vDescriptor Conjunction(vDescriptor x)
					{ return this.Deref().Conjunction(x); }
public vDescriptor ProcessArgs(vDescriptor x)
					{ return this.Deref().ProcessArgs(x); }
public vDescriptor Activate(vDescriptor x)
					{ return this.Deref().Activate(x); }
public vDescriptor ToBy(vDescriptor j, vDescriptor k)
					{ return this.Deref().ToBy(j, k); }

public vNumeric Negate()		{ return this.Deref().Negate(); }
public vNumeric Numerate()		{ return this.Deref().Numerate(); }
public vInteger Size()			{ return this.Deref().Size(); }
public vValue Complement()		{ return this.Deref().Complement(); }
public vCoexp Refresh()			{ return this.Deref().Refresh(); }
public vString TabMatch()		{ return this.Deref().TabMatch(); }
public vNumeric Abs()			{ return this.Deref().Abs(); }
public vValue Copy()			{ return this.Deref().Copy(); }
public vString Type()			{ return this.Deref().Type(); }

public vDescriptor IsNull()		{ return this.Deref().IsNull(); }
public vDescriptor IsntNull()		{ return this.Deref().IsntNull(); }
public vDescriptor Select()		{ return this.Deref().Select(); }
public vDescriptor Bang()		{ return this.Deref().Bang(); }
public vDescriptor Key()		{ return this.Deref().Key(); }

public vInteger Args()			{ return this.Deref().Args(); }
public vValue Proc(long i)		{ return this.Deref().Proc(i); }

public vVariable Field(String s)	{ return this.Deref().Field(s); }
public vDescriptor Index(vDescriptor i)	{ return this.Deref().Index(i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
					{ return this.Deref().Section(i, j); }
public vDescriptor SectPlus(vDescriptor i, vDescriptor j)
					{ return this.Deref().SectPlus(i, j); }
public vDescriptor SectMinus(vDescriptor i, vDescriptor j)
					{ return this.Deref().SectMinus(i, j); }

public vNumeric Add(vDescriptor v)	{ return this.Deref().Add(v); }
public vNumeric Sub(vDescriptor v)	{ return this.Deref().Sub(v); }
public vNumeric Mul(vDescriptor v)	{ return this.Deref().Mul(v); }
public vNumeric Div(vDescriptor v)	{ return this.Deref().Div(v); }
public vNumeric Mod(vDescriptor v)	{ return this.Deref().Mod(v); }
public vNumeric Power(vDescriptor v)	{ return this.Deref().Power(v); }

public vNumeric NLess(vDescriptor v)	{ return this.Deref().NLess(v); }
public vNumeric NLessEq(vDescriptor v)	{ return this.Deref().NLessEq(v); }
public vNumeric NEqual(vDescriptor v)	{ return this.Deref().NEqual(v); }
public vNumeric NUnequal(vDescriptor v)	{ return this.Deref().NUnequal(v); }
public vNumeric NGreaterEq(vDescriptor v){ return this.Deref().NGreaterEq(v); }
public vNumeric NGreater(vDescriptor v)	{ return this.Deref().NGreater(v); }

public vString LLess(vDescriptor v)	{ return this.Deref().LLess(v); }
public vString LLessEq(vDescriptor v)	{ return this.Deref().LLessEq(v); }
public vString LEqual(vDescriptor v)	{ return this.Deref().LEqual(v); }
public vString LUnequal(vDescriptor v)	{ return this.Deref().LUnequal(v); }
public vString LGreaterEq(vDescriptor v){ return this.Deref().LGreaterEq(v); }
public vString LGreater(vDescriptor v)	{ return this.Deref().LGreater(v); }

public vValue VEqual(vDescriptor v)	{ return this.Deref().VEqual(v); }
public vValue VUnequal(vDescriptor v)	{ return this.Deref().VUnequal(v); }

public vString Concat(vDescriptor v)	{ return this.Deref().Concat(v); }
public vList ListConcat(vDescriptor v)	{ return this.Deref().ListConcat(v); }

public vList Push(vDescriptor v)	{ return this.Deref().Push(v); }
public vValue Pull()			{ return this.Deref().Pull(); }
public vValue Pop()			{ return this.Deref().Pop(); }
public vValue Get()			{ return this.Deref().Get(); }
public vList Put(vDescriptor v)		{ return this.Deref().Put(v); }

public vValue Member(vDescriptor i)	{ return this.Deref().Member(i); }
public vValue Delete(vDescriptor i)	{ return this.Deref().Delete(i); }
public vValue Insert(vDescriptor i, vDescriptor val)
					{ return this.Deref().Insert(i, val); }

public vValue Intersect(vDescriptor x)	{ return this.Deref().Intersect(x); }
public vValue Union(vDescriptor x)	{ return this.Deref().Union(x); }
public vValue Diff(vDescriptor x)	{ return this.Deref().Diff(x); }

public vInteger Serial()		{ return this.Deref().Serial(); }
public vList Sort(int i)		{ return this.Deref().Sort(i); }

vNumeric AddInto(vInteger a)		{ return this.Deref().AddInto(a); }
vNumeric SubFrom(vInteger a)		{ return this.Deref().SubFrom(a); }
vNumeric MulInto(vInteger a)		{ return this.Deref().MulInto(a); }
vNumeric DivInto(vInteger a)		{ return this.Deref().DivInto(a); }
vNumeric ModInto(vInteger a)		{ return this.Deref().ModInto(a); }
vNumeric PowerOf(vInteger a)		{ return this.Deref().PowerOf(a); }

vNumeric RevLess(vInteger a)		{ return this.Deref().RevLess(a); }
vNumeric RevLessEq(vInteger a)		{ return this.Deref().RevLessEq(a); }
vNumeric RevEqual(vInteger a)		{ return this.Deref().RevEqual(a); }
vNumeric RevUnequal(vInteger a)		{ return this.Deref().RevUnequal(a); }
vNumeric RevGreaterEq(vInteger a)	{ return this.Deref().RevGreaterEq(a); }
vNumeric RevGreater(vInteger a)		{ return this.Deref().RevGreater(a); }

vNumeric AddInto(vReal a)		{ return this.Deref().AddInto(a); }
vNumeric SubFrom(vReal a)		{ return this.Deref().SubFrom(a); }
vNumeric MulInto(vReal a)		{ return this.Deref().MulInto(a); }
vNumeric DivInto(vReal a)		{ return this.Deref().DivInto(a); }
vNumeric ModInto(vReal a)		{ return this.Deref().ModInto(a); }
vNumeric PowerOf(vReal a)		{ return this.Deref().PowerOf(a); }

vNumeric RevLess(vReal a)		{ return this.Deref().RevLess(a); }
vNumeric RevLessEq(vReal a)		{ return this.Deref().RevLessEq(a); }
vNumeric RevEqual(vReal a)		{ return this.Deref().RevEqual(a); }
vNumeric RevUnequal(vReal a)		{ return this.Deref().RevUnequal(a); }
vNumeric RevGreaterEq(vReal a)		{ return this.Deref().RevGreaterEq(a); }
vNumeric RevGreater(vReal a)		{ return this.Deref().RevGreater(a); }



//#%#% to be handled:
public vVariable Swap(vDescriptor x)      {iRuntime.bomb("Swap"); return null; }
public vVariable SubjAssign(vDescriptor x) {iRuntime.bomb("SubjAssign");return null;}
public vDescriptor RevAssign(vDescriptor x){iRuntime.bomb("RevAssign"); return null;}
public vDescriptor RevSwap(vDescriptor x)  {iRuntime.bomb("RevSwap"); return null; }


public iClosure instantiate(vDescriptor[] args, iClosure parent) //#%#%#%
				{ return this.Deref().instantiate(args,parent);}



} // class vIndirect

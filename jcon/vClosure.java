//  vClosure -- a function closure
//
//  A vClosure encapsulates a return value and a suspended generator.
//  Subclasses add instance variables needed to retain state.

package rts;

public abstract class vClosure extends vDescriptor {

    vDescriptor retval;		// suspended value



// every subclass must implement a resume() method

public abstract vDescriptor resume();



// most methods other than resume() just get applied to the retval field.

public boolean equals(Object o)		{ return retval.equals(o); }
public int hashCode()			{ return retval.hashCode(); }

boolean isnull()			{ return retval.isnull(); }
vString mkString()			{ return retval.mkString(); }
vInteger mkInteger()			{ return retval.mkInteger(); }
vReal mkReal()				{ return retval.mkReal(); }
vCset mkCset()				{ return retval.mkCset(); }
vValue[] mkArray(int errno)		{ return retval.mkArray(errno); }

vString write()				{ return retval.write(); }
vString image()				{ return retval.image(); }
vString report()			{ return retval.report(); }

public vValue Deref()			{ return retval.Deref(); }
public vVariable Assign(vDescriptor x)	{ return retval.Assign(x); }
public vVariable Swap(vDescriptor x)	{ return retval.Swap(x); }
public vVariable SubjAssign(vDescriptor x)  { return retval.SubjAssign(x); }
public vDescriptor RevAssign(vDescriptor x) { return retval.RevAssign(x); }
public vDescriptor RevSwap(vDescriptor x)   { return retval.RevSwap(x); }

public vInteger Limit()			{ return retval.Limit(); }
public vDescriptor Conjunction(vDescriptor x) { return retval.Conjunction(x); }
public vDescriptor ProcessArgs(vDescriptor x) { return retval.ProcessArgs(x); }
public vDescriptor Activate(vDescriptor x)    { return retval.Activate(x); }
public vDescriptor ToBy(vDescriptor j, vDescriptor k)
					      { return retval.ToBy(j, k); }

public vNumeric Negate()		{ return retval.Negate(); }
public vNumeric Numerate()		{ return retval.Numerate(); }
public vInteger Size()			{ return retval.Size(); }
public vValue Complement()		{ return retval.Complement(); }
public vCoexp Refresh()			{ return retval.Refresh(); }
public vString TabMatch()		{ return retval.TabMatch(); }
public vNumeric Abs()			{ return retval.Abs(); }
public vValue Copy()			{ return retval.Copy(); }
public vString Type()			{ return retval.Type(); }

public vDescriptor IsNull()		{ return retval.IsNull(); }
public vDescriptor IsntNull()		{ return retval.IsntNull(); }
public vDescriptor Select()		{ return retval.Select(); }
public vDescriptor Bang()		{ return retval.Bang(); }
public vDescriptor Key()		{ return retval.Key(); }

public vString Name()			{ return retval.Name(); }
public vInteger Args()			{ return retval.Args(); }
public vValue Proc(long i)		{ return retval.Proc(i); }

public vVariable Field(String s)	{ return retval.Field(s); }
public vDescriptor Index(vDescriptor i)	{ return retval.Index(i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
					    { return retval.Section(i, j); }
public vDescriptor SectPlus(vDescriptor i, vDescriptor j)
					    { return retval.SectPlus(i, j); }
public vDescriptor SectMinus(vDescriptor i, vDescriptor j)
					    { return retval.SectMinus(i, j); }

public vNumeric Add(vDescriptor v)	{ return retval.Add(v); }
public vNumeric Sub(vDescriptor v)	{ return retval.Sub(v); }
public vNumeric Mul(vDescriptor v)	{ return retval.Mul(v); }
public vNumeric Div(vDescriptor v)	{ return retval.Div(v); }
public vNumeric Mod(vDescriptor v)	{ return retval.Mod(v); }
public vNumeric Power(vDescriptor v)	{ return retval.Power(v); }

public vNumeric NLess(vDescriptor v)	{ return retval.NLess(v); }
public vNumeric NLessEq(vDescriptor v)	{ return retval.NLessEq(v); }
public vNumeric NEqual(vDescriptor v)	{ return retval.NEqual(v); }
public vNumeric NUnequal(vDescriptor v)	{ return retval.NUnequal(v); }
public vNumeric NGreaterEq(vDescriptor v){return retval.NGreaterEq(v); }
public vNumeric NGreater(vDescriptor v)	{ return retval.NGreater(v); }

public vString LLess(vDescriptor v)	{ return retval.LLess(v); }
public vString LLessEq(vDescriptor v)	{ return retval.LLessEq(v); }
public vString LEqual(vDescriptor v)	{ return retval.LEqual(v); }
public vString LUnequal(vDescriptor v)	{ return retval.LUnequal(v); }
public vString LGreaterEq(vDescriptor v){ return retval.LGreaterEq(v); }
public vString LGreater(vDescriptor v)	{ return retval.LGreater(v); }

public vValue VEqual(vDescriptor v)	{ return retval.VEqual(v); }
public vValue VUnequal(vDescriptor v)	{ return retval.VUnequal(v); }

public vString Concat(vDescriptor v)	{ return retval.Concat(v); }
public vList ListConcat(vDescriptor v)	{ return retval.ListConcat(v); }

public vList Push(vDescriptor v)	{ return retval.Push(v); }
public vValue Pull()			{ return retval.Pull(); }
public vValue Pop()			{ return retval.Pop(); }
public vValue Get()			{ return retval.Get(); }
public vList Put(vDescriptor v)		{ return retval.Put(v); }

public vValue Member(vDescriptor i)	{ return retval.Member(i); }
public vValue Delete(vDescriptor i)	{ return retval.Delete(i); }
public vValue Insert(vDescriptor i, vDescriptor val)
					{ return retval.Insert(i, val); }

public vValue Intersect(vDescriptor x)	{ return retval.Intersect(x); }
public vValue Union(vDescriptor x)	{ return retval.Union(x); }
public vValue Diff(vDescriptor x)	{ return retval.Diff(x); }

public vInteger Serial()		{ return retval.Serial(); }
public vList Sort(int i)		{ return retval.Sort(i); }

vNumeric AddInto(vInteger a)		{ return retval.AddInto(a); }
vNumeric SubFrom(vInteger a)		{ return retval.SubFrom(a); }
vNumeric MulInto(vInteger a)		{ return retval.MulInto(a); }
vNumeric DivInto(vInteger a)		{ return retval.DivInto(a); }
vNumeric ModInto(vInteger a)		{ return retval.ModInto(a); }
vNumeric PowerOf(vInteger a)		{ return retval.PowerOf(a); }

vNumeric RevLess(vInteger a)		{ return retval.RevLess(a); }
vNumeric RevLessEq(vInteger a)		{ return retval.RevLessEq(a); }
vNumeric RevEqual(vInteger a)		{ return retval.RevEqual(a); }
vNumeric RevUnequal(vInteger a)		{ return retval.RevUnequal(a); }
vNumeric RevGreaterEq(vInteger a)	{ return retval.RevGreaterEq(a); }
vNumeric RevGreater(vInteger a)		{ return retval.RevGreater(a); }

vNumeric AddInto(vReal a)		{ return retval.AddInto(a); }
vNumeric SubFrom(vReal a)		{ return retval.SubFrom(a); }
vNumeric MulInto(vReal a)		{ return retval.MulInto(a); }
vNumeric DivInto(vReal a)		{ return retval.DivInto(a); }
vNumeric ModInto(vReal a)		{ return retval.ModInto(a); }
vNumeric PowerOf(vReal a)		{ return retval.PowerOf(a); }

vNumeric RevLess(vReal a)		{ return retval.RevLess(a); }
vNumeric RevLessEq(vReal a)		{ return retval.RevLessEq(a); }
vNumeric RevEqual(vReal a)		{ return retval.RevEqual(a); }
vNumeric RevUnequal(vReal a)		{ return retval.RevUnequal(a); }
vNumeric RevGreaterEq(vReal a)		{ return retval.RevGreaterEq(a); }
vNumeric RevGreater(vReal a)		{ return retval.RevGreater(a); }




public iClosure instantiate(vDescriptor[] d, iClosure c) {	//#%#%#%#%
    iRuntime.bomb("vClosure.instantiate");
    return null;
}



} // class vClosure

//  vClosure -- a function closure
//
//  A vClosure encapsulates a return value and a suspended generator.
//  Subclasses add instance variables needed to retain state.

package rts;

public abstract class vClosure extends vDescriptor {

    public vDescriptor retval;		// suspended value



// every subclass must implement a Resume() method

public abstract vDescriptor Resume();


// most methods other than Resume() just get applied to the retval field.

public boolean equals(Object o)		{ return retval.equals(o); }
public int hashCode()			{ return retval.hashCode(); }

public boolean isnull()			{ return retval.isnull(); }
public boolean iswin()			{ return retval.iswin(); }
public vString mkString()		{ return retval.mkString(); }
public vInteger mkInteger()		{ return retval.mkInteger(); }
public vNumeric mkFixed()		{ return retval.mkFixed(); }
public vReal mkReal()			{ return retval.mkReal(); }
public vCset mkCset()			{ return retval.mkCset(); }
public vProc mkProc(int i)		{ return retval.mkProc(i); }
public vValue[] mkArray(int errno)	{ return retval.mkArray(errno); }

public vString write()			{ return retval.write(); }
public vString image()			{ return retval.image(); }
public vString report()			{ return retval.report(); }

public vDescriptor DerefLocal()		{ return this; }
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
public vDescriptor TabMatch()		{ return retval.TabMatch(); }
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

public vVariable Field(String s)	{ return retval.Field(s); }
public vDescriptor Index(vDescriptor i)	{ return retval.Index(i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
					    { return retval.Section(i, j); }

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

vNumeric BkwLess(vInteger a)		{ return retval.BkwLess(a); }
vNumeric BkwLessEq(vInteger a)		{ return retval.BkwLessEq(a); }
vNumeric BkwEqual(vInteger a)		{ return retval.BkwEqual(a); }
vNumeric BkwUnequal(vInteger a)		{ return retval.BkwUnequal(a); }
vNumeric BkwGreaterEq(vInteger a)	{ return retval.BkwGreaterEq(a); }
vNumeric BkwGreater(vInteger a)		{ return retval.BkwGreater(a); }

vNumeric AddInto(vBigInt a)		{ return retval.AddInto(a); }
vNumeric SubFrom(vBigInt a)		{ return retval.SubFrom(a); }
vNumeric MulInto(vBigInt a)		{ return retval.MulInto(a); }
vNumeric DivInto(vBigInt a)		{ return retval.DivInto(a); }
vNumeric ModInto(vBigInt a)		{ return retval.ModInto(a); }
vNumeric PowerOf(vBigInt a)		{ return retval.PowerOf(a); }

vNumeric BkwLess(vBigInt a)		{ return retval.BkwLess(a); }
vNumeric BkwLessEq(vBigInt a)		{ return retval.BkwLessEq(a); }
vNumeric BkwEqual(vBigInt a)		{ return retval.BkwEqual(a); }
vNumeric BkwUnequal(vBigInt a)		{ return retval.BkwUnequal(a); }
vNumeric BkwGreaterEq(vBigInt a)	{ return retval.BkwGreaterEq(a); }
vNumeric BkwGreater(vBigInt a)		{ return retval.BkwGreater(a); }

vNumeric AddInto(vReal a)		{ return retval.AddInto(a); }
vNumeric SubFrom(vReal a)		{ return retval.SubFrom(a); }
vNumeric MulInto(vReal a)		{ return retval.MulInto(a); }
vNumeric DivInto(vReal a)		{ return retval.DivInto(a); }
vNumeric ModInto(vReal a)		{ return retval.ModInto(a); }
vNumeric PowerOf(vReal a)		{ return retval.PowerOf(a); }

vNumeric BkwLess(vReal a)		{ return retval.BkwLess(a); }
vNumeric BkwLessEq(vReal a)		{ return retval.BkwLessEq(a); }
vNumeric BkwEqual(vReal a)		{ return retval.BkwEqual(a); }
vNumeric BkwUnequal(vReal a)		{ return retval.BkwUnequal(a); }
vNumeric BkwGreaterEq(vReal a)		{ return retval.BkwGreaterEq(a); }
vNumeric BkwGreater(vReal a)		{ return retval.BkwGreater(a); }

public vDescriptor Call(vDescriptor v[]) {
	return retval.Call(v);
}
public vDescriptor Call() {
	return retval.Call();
}
public vDescriptor Call(vDescriptor a) {
	return retval.Call(a);
}
public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return retval.Call(a, b);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return retval.Call(a, b, c);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	return retval.Call(a, b, c, d);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	return retval.Call(a, b, c, d, e);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return retval.Call(a, b, c, d, e, f);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return retval.Call(a, b, c, d, e, f, g);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	return retval.Call(a, b, c, d, e, f, g, h);
}
public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return retval.Call(a, b, c, d, e, f, g, h, i);
}

public vClosure refreshcopy() {
    iRuntime.error(902);
    return null;
}


} // class vClosure

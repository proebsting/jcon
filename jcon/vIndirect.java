//  vIndirect -- vDescriptor subclass for indirect pointers.
//
//  Subclassed by k$Value (special keywords) and vVariable (variables).

package rts;

public abstract class vIndirect extends vDescriptor {



// must be implemented:

public abstract vValue deref();			// dereference
public abstract vVariable Assign(vValue x);	// assign
abstract vString Name();			// name


// for many operations on variables, default action is to deref and retry

// these two must be public because they override java.lang.object
public boolean equals(Object o)	{ return this.deref().equals(o); }
public int hashCode()		{ return this.deref().hashCode(); }

public iClosure instantiate(vDescriptor[] args, iClosure parent)
				{ return this.deref().instantiate(args,parent);}

vString mkString()		{ return this.deref().mkString(); }
vInteger mkInteger()		{ return this.deref().mkInteger(); }
vReal mkReal()			{ return this.deref().mkReal(); }
vNumeric mkNumeric()		{ return this.deref().mkNumeric(); }
vCset mkCset()			{ return this.deref().mkCset(); }
vDescriptor[] mkArgs()		{ return this.deref().mkArgs(); }
vValue[] mkArray()		{ return this.deref().mkArray(); }

vString write()			{ return this.deref().write(); }
vString image()			{ return this.deref().image(); }
vString report()		{ return this.deref().report(); }
vString type()			{ return this.deref().type(); }

vVariable field(String s)	{ return this.deref().field(s); }

vDescriptor Select()		{ return this.deref().Select(); }
vDescriptor Bang(iClosure c)	{ return this.deref().Bang(c); }
vDescriptor Index(vValue i)	{ return this.deref().Index(i); }
vDescriptor Section(int i, int j) { return this.deref().Section(i, j); }

vValue Refresh()		{ return this.deref().Refresh(); }
vValue Sort(int i)		{ return this.deref().Sort(i); }

vNumeric Negate()		{ return this.deref().Negate(); }
vInteger Size()			{ return this.deref().Size(); }
vInteger Serial()		{ return this.deref().Serial(); }
vValue Copy()			{ return this.deref().Copy(); }

vValue Add(vDescriptor v)	{ return this.deref().Add(v); }
vValue Sub(vDescriptor v)	{ return this.deref().Sub(v); }
vValue Mul(vDescriptor v)	{ return this.deref().Mul(v); }
vValue Div(vDescriptor v)	{ return this.deref().Div(v); }
vValue Mod(vDescriptor v)	{ return this.deref().Mod(v); }
vValue Power(vDescriptor v)	{ return this.deref().Power(v); }

vValue NLess(vDescriptor v)	{ return this.deref().NLess(v); }
vValue NLessEq(vDescriptor v)	{ return this.deref().NLessEq(v); }
vValue NEqual(vDescriptor v)	{ return this.deref().NEqual(v); }
vValue NUnequal(vDescriptor v)	{ return this.deref().NUnequal(v); }
vValue NGreaterEq(vDescriptor v){ return this.deref().NGreaterEq(v); }
vValue NGreater(vDescriptor v)	{ return this.deref().NGreater(v); }

vValue LLess(vDescriptor v)	{ return this.deref().LLess(v); }
vValue LLessEq(vDescriptor v)	{ return this.deref().LLessEq(v); }
vValue LEqual(vDescriptor v)	{ return this.deref().LEqual(v); }
vValue LUnequal(vDescriptor v)	{ return this.deref().LUnequal(v); }
vValue LGreaterEq(vDescriptor v){ return this.deref().LGreaterEq(v); }
vValue LGreater(vDescriptor v)	{ return this.deref().LGreater(v); }

vValue Concat(vDescriptor v)	{ return this.deref().Concat(v); }

vValue ListConcat(vDescriptor v) { return this.deref().ListConcat(v); }

// list operations
vValue Push(vDescriptor v)	{ return this.deref().Push(v); }
vValue Pull()			{ return this.deref().Pull(); }
vValue Pop()			{ return this.deref().Pop(); }
vValue Get()			{ return this.deref().Get(); }
vValue Put(vDescriptor v)	{ return this.deref().Put(v); }

// table operations
vValue Key(iClosure c)		{ return this.deref().Key(c); }
vValue Member(vDescriptor i)	{ return this.deref().Member(i); }
vValue Delete(vDescriptor i)	{ return this.deref().Delete(i); }
vValue Insert(vDescriptor i, vDescriptor val)
				{ return this.deref().Insert(i, val); }

// set operations
vValue Complement()		{ return this.deref().Complement(); }
vValue Union(vDescriptor x)	{ return this.deref().Union(x); }
vValue Intersect(vDescriptor x)	{ return this.deref().Intersect(x); }
vValue Diff(vDescriptor x)	{ return this.deref().Diff(x); }

vInteger Args()			{ return this.deref().Args(); }
vValue Proc(long i)		{ return this.deref().Proc(i); }



vDescriptor isNull()	{
    if (this.deref().isNull() != null) {
	return this;	// return variable
    } else {
	return null;	// fail
    }
}

vDescriptor isntNull()	{
    if (this.deref().isNull() != null) {
	return null;	// fail
    } else {
	return this;	// return variable
    }
}



} // class vIndirect

//  vVariable -- vDescriptor subclass for Icon variables.
//
//  Variables represent assignable expressions in Icon.
//
//  The subclass vSimpleVar is used for Icon global and local variables 
//  and, when further subclassed, for certain keywords such as &subject.
//
//  The abstract subclass vVarExpr represents a variable expression such
//  as a substring, list or table index, etc.  Each VarExpr points to
//  an underlying SimpleVar.


package rts;

public abstract class vVariable extends vDescriptor {


    // must be implemented:

    public abstract vValue deref();		// dereference
    public abstract vVariable Assign(vValue x);	// assign
    abstract String report();			// report for traceback


    // for many operations on variables, default action is to deref and retry

    // these two must be public because they override java.lang.object
    public boolean equals(Object o)	{ return this.deref().equals(o); }
    public int hashCode()		{ return this.deref().hashCode(); }

    public iClosure instantiate(vDescriptor[] args, iClosure parent)
			{ return this.deref().instantiate(args, parent); }

    vString mkString()			{ return this.deref().mkString(); }
    vInteger mkInteger()		{ return this.deref().mkInteger(); }
    vReal mkReal()			{ return this.deref().mkReal(); }
    vNumeric mkNumeric()		{ return this.deref().mkNumeric(); }
    vCset mkCset()			{ return this.deref().mkCset(); }
    vDescriptor[] mkArgs()		{ return this.deref().mkArgs(); }
    vValue[] mkArray()			{ return this.deref().mkArray(); }

    String write()			{ return this.deref().write(); }
    String image()			{ return this.deref().image(); }
    String type()			{ return this.deref().type(); }

    vVariable field(String s)		{ return this.deref().field(s); }

    vNumeric Negate()			{ return this.deref().Negate(); }
    vInteger Size()			{ return this.deref().Size(); }
    vInteger Serial()			{ return this.deref().Serial(); }
    vValue Copy()			{ return this.deref().Copy(); }

    vValue Power(vDescriptor v)		{ return this.deref().Power(v); }
    vValue Add(vDescriptor v)		{ return this.deref().Add(v); }
    vValue Sub(vDescriptor v)		{ return this.deref().Sub(v); }
    vValue Mul(vDescriptor v)		{ return this.deref().Mul(v); }
    vValue Div(vDescriptor v)		{ return this.deref().Div(v); }
    vValue Mod(vDescriptor v)		{ return this.deref().Mod(v); }

    vValue NLess(vDescriptor v)		{ return this.deref().NLess(v); }
    vValue NLessEq(vDescriptor v)	{ return this.deref().NLessEq(v); }
    vValue NEqual(vDescriptor v)	{ return this.deref().NEqual(v); }
    vValue NUnequal(vDescriptor v)	{ return this.deref().NUnequal(v); }
    vValue NGreaterEq(vDescriptor v)	{ return this.deref().NGreaterEq(v); }
    vValue NGreater(vDescriptor v)	{ return this.deref().NGreater(v); }

    vValue LLess(vDescriptor v)		{ return this.deref().LLess(v); }
    vValue LLessEq(vDescriptor v)	{ return this.deref().LLessEq(v); }
    vValue LEqual(vDescriptor v)	{ return this.deref().LEqual(v); }
    vValue LUnequal(vDescriptor v)	{ return this.deref().LUnequal(v); }
    vValue LGreaterEq(vDescriptor v)	{ return this.deref().LGreaterEq(v); }
    vValue LGreater(vDescriptor v)	{ return this.deref().LGreater(v); }

    vValue Concat(vDescriptor v)	{ return this.deref().Concat(v); }

    vValue ListConcat(vDescriptor v)	{ return this.deref().ListConcat(v); }

    // list operations
    vValue Push(vDescriptor v)		{ return this.deref().Push(v); }
    vValue Pull()			{ return this.deref().Pull(); }
    vValue Pop()			{ return this.deref().Pop(); }
    vValue Get()			{ return this.deref().Get(); }
    vValue Put(vDescriptor v)		{ return this.deref().Put(v); }

    // table operations
    vValue Key(iClosure c)		{ return this.deref().Key(c); }
    vValue Member(vDescriptor i)	{ return this.deref().Member(i); }
    vValue Delete(vDescriptor i)	{ return this.deref().Delete(i); }
    vValue Insert(vDescriptor i, vDescriptor val)
    					{ return this.deref().Insert(i, val); }

    // set operations
    vValue Complement()			{ return this.deref().Complement(); }
    vValue Union(vDescriptor x)		{ return this.deref().Union(x); }
    vValue Intersect(vDescriptor x)	{ return this.deref().Intersect(x); }
    vValue Diff(vDescriptor x)		{ return this.deref().Diff(x); }

    vInteger Args()			{ return this.deref().Args(); }
    vValue Proc(long i)			{ return this.deref().Proc(i); }

    vDescriptor isNull()	{
    	if (this.deref() instanceof vNull) {
	    return this;	// return variable 
	} else {
	    return null;	// fail
	}
    }

    vDescriptor isntNull()	{ 
    	if (this.deref() instanceof vNull) {
	    return null;	// fail
	} else {
	    return this;	// return variable 
	}
    }

    vDescriptor Select()	  { return this.deref().SelectVar(this); }
    vDescriptor Bang(iClosure c)  { return this.deref().BangVar(c, this); }

    vDescriptor Index(vValue i)
    				{ return this.deref().IndexVar(this, i); }
    vDescriptor Section(vValue i, vValue j)
    				{ return this.deref().SectionVar(this, i, j); }

    vValue Refresh()		{ return this.deref().Refresh(); }

    vValue Sort(vDescriptor i)	{ return this.deref().Sort(i); }
}

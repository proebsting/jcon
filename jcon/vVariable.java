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


abstract class vVariable extends vDescriptor {


    // must be implemented:

    abstract vValue deref();			// dereference
    abstract vVariable Assign(vValue x);	// assign
    abstract String report();			// report for traceback


    // for many operations on variables, default action is to deref and retry

    public boolean equals(Object o)	{ return this.deref().equals(o); }

    iClosure instantiate(vDescriptor[] args, iClosure parent)
			{ return this.deref().instantiate(args, parent); }

    vString mkString()			{ return this.deref().mkString(); }
    vInteger mkInteger()		{ return this.deref().mkInteger(); }
    vReal mkReal()			{ return this.deref().mkReal(); }
    vNumeric mkNumeric()		{ return this.deref().mkNumeric(); }

    String write()			{ return this.deref().write(); }
    String image()			{ return this.deref().image(); }
    String type()			{ return this.deref().type(); }

    vVariable field(String s)		{ return this.deref().field(s); }

    vNumeric Negate()			{ return this.deref().Negate(); }
    vInteger Size()			{ return this.deref().Size(); }

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

    vValue Concat(vDescriptor v)	{ return this.deref().Concat(v); }

    // list operations
    vValue Push(vDescriptor v)		{ return this.deref().Push(v); }
    vValue Pull()			{ return this.deref().Pull(); }
    vValue Pop()			{ return this.deref().Pop(); }
    vValue Get()			{ return this.deref().Get(); }
    vValue Put(vDescriptor v)		{ return this.deref().Put(v); }

    // table operations
    vDescriptor Key(iClosure c)		{ return this.deref().Key(c); }
    vDescriptor Member(vDescriptor i)	{ return this.deref().Member(i); }
    vDescriptor Delete(vDescriptor i)	{ return this.deref().Delete(i); }
    vDescriptor Insert(vDescriptor i, vDescriptor val)
    		{ return this.deref().Insert(i, val); }

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
}

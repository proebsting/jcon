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

public abstract class vVariable extends vIndirect {

    // must be implemented:

    public abstract vValue deref();		// dereference
    public abstract vVariable Assign(vValue x);	// assign

    abstract vString Name();			// name
    abstract vString report();			// report for traceback

    // operations that produce vVarExprs

    vDescriptor Select()	  { return this.deref().SelectVar(this); }
    vDescriptor Bang(iClosure c)  { return this.deref().BangVar(c, this); }

    vDescriptor Index(vValue i)
    				{ return this.deref().IndexVar(this, i); }
    vDescriptor Section(vValue i, vValue j)
    				{ return this.deref().SectionVar(this, i, j); }

}

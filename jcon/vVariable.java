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

public abstract vValue Deref();				// dereference
public abstract vVariable Assign(vDescriptor x);	// assign
public abstract vString Name();				// return name
abstract vString report();				// report for traceback



// operations that produce vVarExprs
//#%#%#% probably need more e.g. SectPlus

public vDescriptor Select()	{ return this.Deref().SelectVar(this); }
public vDescriptor Bang()	{ return this.Deref().BangVar(this);}

public vDescriptor Index(vDescriptor i)
				{ return this.Deref().IndexVar(this, i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
				{ return this.Deref().SectionVar(this, i, j); }

public vDescriptor SectPlus(vDescriptor i, vDescriptor j) {	// v[i+:j]
    return Section(i, 
	vInteger.New(i.mkInteger().value + j.mkInteger().value)); //#%#% wrong
}

public vDescriptor SectMinus(vDescriptor i, vDescriptor j) {	// v[i-:j]
    return Section(i, 
	vInteger.New(i.mkInteger().value - j.mkInteger().value)); //#%#% wrong
}



} // class vVariable

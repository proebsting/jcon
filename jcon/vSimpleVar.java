class vSimpleVar extends vVariable {

    vValue value;	// value of variable
    
    vSimpleVar(vValue x)	{ value = x; }		// constructor
    vSimpleVar()		{ value = iNew.Null(); }

    vValue deref()		{ return value; }

    vVariable Assign(vValue x)	{ value = x; return this; }

    String report()	{ return "(variable = " + this.value.report() + ")"; }

}

package rts;

public class vSimpleVar extends vVariable {

    vValue value;	// value of variable

    
    vSimpleVar(vValue x)		{ value = x; }		// constructors
    vSimpleVar()			{ value = iNew.Null(); }


    public vValue deref()		{ return value; }

    public vVariable Assign(vValue x)	{ value = x; return this; }

    String report()	{ return "(variable = " + this.value.report() + ")"; }

}

package rts;

public class vSimpleVar extends vVariable {

    vValue value;	// value of variable
    String name;

    
    vSimpleVar(String name, vValue x) {
        value = x;
        this.name = name;
    }
    vSimpleVar(String name) {
        value = iNew.Null();
        this.name = name;
    }


    public vValue deref()		{ return value; }

    public vVariable Assign(vValue x)	{ value = x; return this; }

    vString Name()	{ return iNew.String(name); }

    String report()	{ return "(variable = " + this.value.report() + ")"; }

}

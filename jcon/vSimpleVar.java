package rts;

public class vSimpleVar extends vVariable {

    vValue value;		// value of variable
    String name;



// constructors

public static vSimpleVar New(String name)
				{ return new vSimpleVar(name, vNull.New()); }

public static vSimpleVar New(String name, vDescriptor x)
				{ return new vSimpleVar(name, x.Deref()); }

vSimpleVar(String name, vValue x) {	// new vSimpleVar(name, value)
    value = x;
    this.name = name;
}



public vValue Deref()			{ return value; }

public vVariable Assign(vDescriptor x)	{ value = x.Deref(); return this; }

public vString Name()			{ return vString.New(name); }

public vString report() {
    return vString.New("(variable = " + this.value.report() + ")");
}



} // class vSimpleVar

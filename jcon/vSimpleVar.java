package rts;

public class vSimpleVar extends vVariable {

    vValue value;		// value of variable
    String name;



// constructors

public static vSimpleVar New(String name)
				{ return new vSimpleVar(name); }

public static vSimpleVar New(String name, vDescriptor x)
				{ return new vSimpleVar(name, x.deref()); }

vSimpleVar(String name, vValue x) {	// new vSimpleVar(name, value)
    value = x;
    this.name = name;
}

vSimpleVar(String name) {		// new vSimpleVar(name)
    value = vNull.New();
    this.name = name;
}




public vValue deref()			{ return value; }

public vVariable Assign(vValue x)	{ value = x; return this; }

vString Name()				{ return vString.New(name); }

vString report() {
    return vString.New("(variable = " + this.value.report() + ")");
}



} // class vSimpleVar

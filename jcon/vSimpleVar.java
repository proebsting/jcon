package rts;

public class vSimpleVar extends vVariable {

    vValue value;		// value of variable
    String name;

// constructors

private static vSimpleVar get(String name) {
    vValue v = vNull.New();
    return new vSimpleVar(name, v);
}

private static vSimpleVar get(String name, vDescriptor x) {
    vValue v = x.Deref();
    return new vSimpleVar(name, v);
}

public static vSimpleVar New(String name) {
    return get(name);
}

public static vSimpleVar New(String name, vDescriptor x) {
    return get(name, x);
}

vSimpleVar(String name, vValue x) {	// new vSimpleVar(name, value)
    value = x;
    this.name = name;
}



public vDescriptor DerefLocal()		{ return this; }	// Not a local.
public vValue Deref()			{ return value; }

public vVariable Assign(vDescriptor x)	{ value = x.Deref(); return this; }

public vString Name()			{ return vString.New(name); }

public vString report() {
    return vString.New("(variable = " + this.value.report() + ")");
}



} // class vSimpleVar

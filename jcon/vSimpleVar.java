package rts;

public class vSimpleVar extends vVariable {

    vValue value;		// value of variable
    String name;



vSimpleVar(String name, vValue x) {	// new vSimpleVar(name, value)
    value = x;
    this.name = name;
}

vSimpleVar(String name) {		// new vSimpleVar(name)
    value = iNew.Null();
    this.name = name;
}


public vValue deref()			{ return value; }

public vVariable Assign(vValue x)	{ value = x; return this; }

vString Name()				{ return iNew.String(name); }

vString report() {
    return iNew.String("(variable = " + this.value.report() + ")");
}



} // class vSimpleVar

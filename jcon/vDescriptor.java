abstract class vDescriptor {

    // methods declared here must be implemented for all descriptor classes

    abstract iClosure instantiate(vDescriptor[] args);	// instantiate proc

    // dereferencing and assignment
    abstract vValue deref();			// dereference
    abstract vVariable Assign(vValue x);	// assign

    // accessing functions
    abstract vVariable field(String s);		// field access

    // type conversion
    abstract vString mkString();		// convert to Icon string
    abstract vInteger mkInteger();		// convert to Icon integer
    abstract vReal mkReal();			// convert to Icon real
    abstract vNumeric mkNumeric();		// convert to Icon numeric

    // conversion to (Java) string for special purposes
    abstract String write();			// for use in write()
    abstract String image();			// for use in image()
    abstract String report();			// for use in error reporting

    // simple unary operators that always return values
    abstract vNumeric Negate();			//  -n
    abstract vInteger Size();			//  *x

    // simple unary operators that may return variables
    abstract vDescriptor isNull();		//  /x
    abstract vDescriptor isntNull();		//  \x
    abstract vDescriptor Select();		//  ?x

    // unary operators that are generators
    abstract vDescriptor Bang(iClosure c);	//  !x

    // simple binary operators
    abstract vValue Add(vDescriptor v);		//  n1 + n2
    abstract vValue Sub(vDescriptor v);		//  n1 - n2
    abstract vValue Mul(vDescriptor v);		//  n1 * n2
    abstract vValue Div(vDescriptor v);		//  n1 / n2
    abstract vValue Mod(vDescriptor v);		//  n1 % n2

    abstract vValue NLess(vDescriptor v);	//  n1 < n2
    abstract vValue NLessEq(vDescriptor v);	//  n1 <= n2
    abstract vValue NEqual(vDescriptor v);	//  n1 = n2
    abstract vValue NUnequal(vDescriptor v);	//  n1 ~= n2
    abstract vValue NGreaterEq(vDescriptor v);	//  n1 >= n2
    abstract vValue NGreater(vDescriptor v);	//  n1 > n2

    //#%#%#%# not-yet-implemented abort
    void NYI(String s) {
	System.err.println("NYI: " + this + "." + (s != null ? s : "?"));
	System.exit(1);
    }
}

//  vDescriptor -- master abstract class.
//
//  A descriptor is the basic object of Icon operation.
//  Descriptors are used for method arguments, intermediate results, etc.
//
//  Descriptors come in two flavors (q.v.):
//	vValue		dereferenced values such as 3, "x", list L, etc.
//	vVariable	assignable objects such as i, a[3], &subject, etc.


package rts;

public abstract class vDescriptor {

    // methods declared here must be implemented for all descriptor classes

    public abstract iClosure instantiate(vDescriptor[] args, iClosure parent);
						// instantiate proc

    // dereferencing and assignment
    public abstract vValue deref();		// dereference
    public abstract vVariable Assign(vValue x);	// assign

    // access functions
    abstract vVariable field(String s);		// field access

    // type conversion
    abstract vString mkString();		// convert to Icon string
    abstract vInteger mkInteger();		// convert to Icon integer
    abstract vReal mkReal();			// convert to Icon real
    abstract vNumeric mkNumeric();		// convert to Icon numeric
    abstract vCset mkCset();			// convert to Icon numeric
    abstract vDescriptor[] mkArgs();		// convert to Icon argument list
    abstract vValue[] mkArray();		// convert to array for sorting

    // tandem numeric conversion: overridden in vReal and vInteger
    void NumBoth(iBinaryValueClosure c) {
	(c.argument0 = this.mkNumeric()).NumBoth(c);
    }

    // conversion to (Java) string for special purposes
    abstract String write();			// convert for use in write()
    abstract String image();			// convert for use in image()
    abstract String report();			// convert for error reporting
    abstract String type();			// return name of type

    // simple unary operators that always return values
    abstract vNumeric Negate();			//  -n
    abstract vInteger Size();			//  *x
    abstract vInteger Serial();			//  serial(x)
    abstract vValue Copy();			//  copy(x)

    // simple unary operators that may return variables
    abstract vDescriptor isNull();		//  /x
    abstract vDescriptor isntNull();		//  \x
    abstract vDescriptor Select();		//  ?x

    // unary operators that are generators
    abstract vDescriptor Bang(iClosure c);	//  !x

    // subscripting
    abstract vDescriptor Index(vValue v);		 // x1[x2]
    abstract vDescriptor Section(vValue v1, vValue v2);  // x1[i1:i2]

    // simple binary operators
    abstract vValue Add(vDescriptor v);		//  n1 + n2
    abstract vValue Sub(vDescriptor v);		//  n1 - n2
    abstract vValue Mul(vDescriptor v);		//  n1 * n2
    abstract vValue Div(vDescriptor v);		//  n1 / n2
    abstract vValue Mod(vDescriptor v);		//  n1 % n2
    abstract vValue Power(vDescriptor v);	//  n1 ^ n2

    abstract vValue NLess(vDescriptor v);	//  n1 < n2
    abstract vValue NLessEq(vDescriptor v);	//  n1 <= n2
    abstract vValue NEqual(vDescriptor v);	//  n1 = n2
    abstract vValue NUnequal(vDescriptor v);	//  n1 ~= n2
    abstract vValue NGreaterEq(vDescriptor v);	//  n1 >= n2
    abstract vValue NGreater(vDescriptor v);	//  n1 > n2

    abstract vValue LLess(vDescriptor v);	//  n1 << n2
    abstract vValue LLessEq(vDescriptor v);	//  n1 <<= n2
    abstract vValue LEqual(vDescriptor v);	//  n1 == n2
    abstract vValue LUnequal(vDescriptor v);	//  n1 ~== n2
    abstract vValue LGreaterEq(vDescriptor v);	//  n1 >>= n2
    abstract vValue LGreater(vDescriptor v);	//  n1 >> n2

    abstract vValue Concat(vDescriptor v);	//  s1 || s2

    abstract vValue ListConcat(vDescriptor v);	//  L1 ||| L2

    // variable operations
    abstract vString Name();
    abstract vInteger Args();
    abstract vValue Proc(long i);

    // list operations
    abstract vValue Push(vDescriptor v);
    abstract vValue Pull();
    abstract vValue Pop();
    abstract vValue Get();
    abstract vValue Put(vDescriptor v);

    // table/set operations
    abstract vValue Key(iClosure c);
    abstract vValue Member(vDescriptor i);
    abstract vValue Delete(vDescriptor i);
    abstract vValue Insert(vDescriptor i, vDescriptor val);

    // set/cset operations
    abstract vValue Complement();
    abstract vValue Intersect(vDescriptor i);
    abstract vValue Union(vDescriptor i);
    abstract vValue Diff(vDescriptor i);

    // coexpression operations
    abstract vValue Refresh();

    // other data-oriented operations
    abstract vValue Sort(int i);		// sort(X, i)

    //#%#%#%# not-yet-implemented abort
    void NYI(String s) {
	System.err.println("NYI: " + this + "." + (s != null ? s : "?"));
	System.exit(1);
    }

    public iClosure instantiate(vDescriptor arg0, vDescriptor arg1, vDescriptor arg2, iClosure parent) {
	vDescriptor[] args = { arg0, arg1, arg2};
	return this.instantiate(args, parent);
    }
    public iClosure instantiate(vDescriptor arg0, vDescriptor arg1, iClosure parent) {
	vDescriptor[] args = { arg0, arg1};
	return this.instantiate(args, parent);
    }
    public iClosure instantiate(vDescriptor arg0, iClosure parent) {
	vDescriptor[] args = { arg0};
	return this.instantiate(args, parent);
    }
}

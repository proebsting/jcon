class vString extends vValue {

    String value;



// constructors

vString(String s)		{ value = s; }
vString mkString()		{ return this; }



// runtime primitives

public int hashCode()		{ return value.hashCode(); }
public boolean equals(Object o)	{
	return (o instanceof vString) && value.equals(((vString)o).value);
}

String write()		{ return value; }
String image()		{ return "\"" + value + "\""; }
				//#%#%#% need to escape special chars
String report()		{ return "\"" + value + "\""; }
				//#%#%#% trim length, besides escaping
String type()		{ return "string"; }

vNumeric mkNumeric()	{
    String s = value.trim();
    try {
	return iNew.Integer(Long.parseLong(s));		//#%#% not exactly right
    } catch (NumberFormatException e) {
    }
    try {
	return iNew.Real(Double.valueOf(s).doubleValue());  //#%#% likewise
    } catch (NumberFormatException e) {
    }
    iRuntime.error(102, this);
    return null;
}

vCset mkCset() {
    return new vCset(this.value);
}


//  static methods for argument processing and defaulting

static String argVal(vDescriptor[] args, int index)		// required arg
{
    if (index >= args.length) {
	iRuntime.error(103);
	return null;
    } else {
	return args[index].mkString().value;
    }
}

static String argVal(vDescriptor[] args, int index, String dflt) // optional arg
{
    if (index >= args.length || args[index] instanceof vNull) {
	return dflt;
    } else {
	return args[index].mkString().value;
    }
}



//  s.posEq(n) -- return positive equivalent of position n in string s,
//		  or zero if out of bounds

int posEq(long n)
{
    long len = ((String) value).length();
    if (n <= 0) {
    	n += len + 1;
    }
    if (n > 0 && n <= len + 1) {
    	return (int)n;
    } else {
    	return 0;
    }
}



//  operations



vInteger Size()	{
    return iNew.Integer(value.length());
}



vValue Concat(vDescriptor v) {
    return iNew.String(this.value + v.mkString().value);
}



vDescriptor Index(vValue i) {
    int m = this.posEq(i.mkInteger().value);
    if (m == 0 || m > value.length()) {
    	return null; /*FAIL*/
    }
    return iNew.String(value.substring(m-1, m));
}

vDescriptor IndexVar(vVariable v, vValue i) {
    int m = this.posEq(i.mkInteger().value);
    if (m == 0 || m > value.length()) {
    	return null; /*FAIL*/
    }
    return iNew.Substring(v, m, m+1);
}

vDescriptor Section(vValue i, vValue j) {
    int m = this.posEq(i.mkInteger().value);
    int n = this.posEq(j.mkInteger().value);
    if (m == 0 || n == 0) {
    	return null; /*FAIL*/
    }
    if (m > n) {
	return iNew.String(value.substring(n-1, m-1));
    } else {
	return iNew.String(value.substring(m-1, n-1));
    }
}

vDescriptor SectionVar(vVariable v, vValue i, vValue j) {
    int m = this.posEq(i.mkInteger().value);
    int n = this.posEq(j.mkInteger().value);
    if (m == 0 || n == 0) {
    	return null; /*FAIL*/
    }
    if (m > n) {
	return iNew.Substring(v, n, m);
    } else {
	return iNew.Substring(v, m, n);
    }
}



vDescriptor Select() {
    if (value.length() == 0) {
	return null; /*FAIL*/
    }
    int i = (int) iRuntime.random(value.length());
    return iNew.String(value.substring(i, i+1));
}

vDescriptor SelectVar(vVariable v) {
    if (value.length() == 0) {
	return null; /*FAIL*/
    }
    int i = (int) iRuntime.random(value.length());
    return iNew.Substring(v, i+1, i+2);
}

vDescriptor Bang(iClosure c) {
    int i;
    if (c.o == null) {
	c.o = new Integer(i = 1);
    } else {
	c.o = new Integer(i = ((Integer)c.o).intValue() + 1);
    }
    if (i > value.length()) {
	return null; /*FAIL*/
    } else {
	return iNew.String(value.substring(i - 1, i));
    }
}

vDescriptor BangVar(iClosure c, vVariable v) {
    int i;
    if (c.o == null) {
	c.o = new Integer(i = 1);
    } else {
	c.o = new Integer(i = ((Integer)c.o).intValue() + 1);
    }
    if (i > ((vString)v.deref()).value.length()) {
	return null; /*FAIL*/
    } else {
	return iNew.Substring(v, i, i+1);
    }
}

iClosure instantiate(vDescriptor[] args, iClosure parent) {
    vDescriptor v;

    v = parent.env.resolve(this.value);
    if (v != null) {
        v = v.deref();
	if (v instanceof vString || v instanceof vNumeric) {
            return new iErrorClosure(this, args, parent);  // will gen err 106
	}
	return v.instantiate(args, parent);
    }
    v = this.mkInteger();
    if (v != null) {
	return v.instantiate(args, parent);
    }
    return new iErrorClosure(this, args, parent);  // will gen err 106
}

vValue LLess(vDescriptor v) {
    if (!(v instanceof vString)) {
	iRuntime.error(103, v);
    }
    String vs = ((vString)v).value;
    return this.value.compareTo(vs) < 0 ? (vValue)v : null;
}
vValue LLessEq(vDescriptor v) {
    if (!(v instanceof vString)) {
	iRuntime.error(103, v);
    }
    String vs = ((vString)v).value;
    return this.value.compareTo(vs) <= 0 ? (vValue)v : null;
}
vValue LEqual(vDescriptor v) {
    if (!(v instanceof vString)) {
	iRuntime.error(103, v);
    }
    String vs = ((vString)v).value;
    return this.value.compareTo(vs) == 0 ? (vValue)v : null;
}
vValue LUnequal(vDescriptor v) {
    if (!(v instanceof vString)) {
	iRuntime.error(103, v);
    }
    String vs = ((vString)v).value;
    return this.value.compareTo(vs) != 0 ? (vValue)v : null;
}
vValue LGreaterEq(vDescriptor v) {
    if (!(v instanceof vString)) {
	iRuntime.error(103, v);
    }
    String vs = ((vString)v).value;
    return this.value.compareTo(vs) >= 0 ? (vValue)v : null;
}
vValue LGreater(vDescriptor v) {
    if (!(v instanceof vString)) {
	iRuntime.error(103, v);
    }
    String vs = ((vString)v).value;
    return this.value.compareTo(vs) > 0 ? (vValue)v : null;
}

} // class vString

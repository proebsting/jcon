class vString extends vValue {

    String value;



// constructors

vString(String s)		{ value = s; }
vString mkString()		{ return this; }



// runtime primitives

String write()		{ return value; }
String image()		{ return "\"" + value + "\""; }
				//#%#%#% need to escape special chars
String report()		{ return "\"" + value + "\""; }
				//#%#%#% trim length, besides escaping

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
    return iNew.String(value.substring(m, m+1));
}

vDescriptor IndexVar(vSimpleVar v, vValue i) {
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
	return iNew.String(value.substring(n, m));
    } else {
	return iNew.String(value.substring(m, n));
    }
}

vDescriptor SectionVar(vSimpleVar v, vValue i, vValue j) {
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
    return iNew.String(value.substring(i+1, i+2));
}

vDescriptor SelectVar(vSimpleVar v) {
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

vDescriptor BangVar(iClosure c, vSimpleVar v) {
    int i;
    if (c.o == null) {
	c.o = new Integer(i = 1);
    } else {
	c.o = new Integer(i = ((Integer)c.o).intValue() + 1);
    }
    if (i > ((vString)v.value).value.length()) {
	return null; /*FAIL*/
    } else {
	return iNew.Substring(v, i, i+1);
    }
}



} // class vString

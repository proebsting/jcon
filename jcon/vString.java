package rts;

public class vString extends vValue {

    String value;



// constructors

vString(String s)		{ value = s; }
vString(char c)			{ value = "" + c; }

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

int rank()		{ return 30; }		// strings rank after reals

int compareTo(vValue v) { return this.value.compareTo(((vString) v).value); }



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

vInteger mkInteger()	{
    try {
	return this.mkNumeric().mkInteger();	// allows integer("3e6")
    } catch (iError e) {
    	iRuntime.error(101, this);
	return null;
    }
}

vReal mkReal()		{
    return this.mkNumeric().mkReal();
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
    int i = (int) k$random.choose(value.length());
    return iNew.String(value.substring(i, i+1));
}

vDescriptor SelectVar(vVariable v) {
    if (value.length() == 0) {
	return null; /*FAIL*/
    }
    int i = (int) k$random.choose(value.length());
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

vValue LLess(vDescriptor v) {
    return this.value.compareTo(((vString)v).value) < 0 ? (vValue)v : null;
}
vValue LLessEq(vDescriptor v) {
    return this.value.compareTo(((vString)v).value) <= 0 ? (vValue)v : null;
}
vValue LEqual(vDescriptor v) {
    return this.value.compareTo(((vString)v).value) == 0 ? (vValue)v : null;
}
vValue LUnequal(vDescriptor v) {
    return this.value.compareTo(((vString)v).value) != 0 ? (vValue)v : null;
}
vValue LGreaterEq(vDescriptor v) {
    return this.value.compareTo(((vString)v).value) >= 0 ? (vValue)v : null;
}
vValue LGreater(vDescriptor v) {
    return this.value.compareTo(((vString)v).value) > 0 ? (vValue)v : null;
}

vValue Complement()		{ return this.mkCset().Complement(); }
vValue Intersect(vDescriptor x)	{ return this.mkCset().Intersect(x); }
vValue Union(vDescriptor x)	{ return this.mkCset().Union(x); }
vValue Diff(vDescriptor x)	{ return this.mkCset().Diff(x); }


vValue Proc(long i) {
    if (i == 0) {
	vValue b = (vValue) iEnv.builtintab.get(this.value);
	if (b == null) {
	    return null;
	}
	return b;
    }
    vDescriptor v = (vDescriptor) iEnv.symtab.get(this.value);
    if (v != null) {
	return v.deref().getproc();
    }
    try {
	return this.mkInteger().getproc();
    } catch (iError e) {
	// ignore
    }
    if (i < 1 || i > 3) {
	return null;
    }
    v = (vDescriptor) iEnv.proctab[(int)i-1].get(this.value);
    if (v != null) {
	return (vValue) v;
    }
    return null;
}

} // class vString

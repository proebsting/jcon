package rts;

public class vString extends vValue {

    String value;



vString(String s)	{ value = s; }		// constructor

vString mkString()	{ return this; }	// value extractor



// runtime primitives

public int hashCode()		{ return value.hashCode(); }
public boolean equals(Object o)	{
	return (o instanceof vString) && value.equals(((vString)o).value);
}

String write()		{ return value; }
String type()		{ return "string"; }

String image()		{ return image(value.length()); }
String report()		{ return image(16); }	 // limit to max of 16 chars

String image(int maxlen) {		// make image, up to maxlen chars
    StringBuffer b = new StringBuffer(maxlen + 5);
    b.append('"');
    int i;
    for (i = 0; i < maxlen && i < value.length(); i++) {
	char c = value.charAt(i);
	if (c == '"') {
	    b.append("\\\"");
	} else {
	    appendEscaped(b, c);
        }
    }
    if (i < value.length()) {
	b.append("...");
    }
    b.append('"');
    return b.toString();
}


int rank()		{ return 30; }		// strings rank after reals

int compareTo(vValue v) { return this.value.compareTo(((vString) v).value); }



vNumeric mkNumeric()	{

    String s = value.trim();	// #%#% too liberal -- trims other than spaces

    if (s.length() > 0 && s.charAt(0) == '+') {	// allow leading +, by trimming 
	s = s.substring(1);
    }

    try {
	return iNew.Integer(Long.parseLong(s));
    } catch (NumberFormatException e) {
    }

    try {
	Double d = Double.valueOf(s);
	if (!d.isInfinite()) {
	    return iNew.Real(d.doubleValue());
	}
    } catch (NumberFormatException e) {
    }

    vInteger v = vInteger.radixParse(s);	// try to parse as radix value
    if (v != null) {
	return v;
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

static vString argDescr(vDescriptor[] args, int index)		// required arg
{
    if (index >= args.length) {
	iRuntime.error(103);
	return null;
    } else {
	return args[index].mkString();
    }
}

static String argVal(vDescriptor[] args, int index)		// required arg
{
    vString s = argDescr(args, index);
    if (s == null) {
    	return null;
    } else {
    	return s.value;
    }
}

static vString argDescr(vDescriptor[] args, int index, vString dflt)	// opt
{
    if (index >= args.length) {
	return dflt;
    } else {
	return args[index].mkString();
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



//  append escaped char to StringBuffer; also used for csets

private static String[] escapes =	// escapes for chars 0x08 - 0x0D
    { "\\b", "\\t", "\\n", "\\v", "\\f", "\\r" };

static void appendEscaped(StringBuffer b, char c)
{
    if (c >= ' ' && c <= '~') {		// printable range
	if (c == '\\') {
	    b.append('\\');
	}
	b.append(c);
    } else if (c > 0xFF) {		// extended Unicode range
	b.append("\\u");
	b.append(Integer.toHexString(0x10000 + c).substring(1));   // 4 digits
    } else if (c >= 0x08 && c <= 0x0D) {
	b.append(escapes[c - 0x08]);	//  \b \t \n \v \f \r
    } else if (c == 0x1B) {
	b.append("\\e");		//  \e
    } else if (c == 0x7F) {
	b.append("\\d");		//  \d
    } else {
	b.append("\\x");		//  \xnn
	b.append(Integer.toHexString(0x100 + c).substring(1));    // 2 digits
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
    if (c.PC == 1) {
	c.o = c;
	c.oint = 0;
	c.PC = 2;
    } else {
	c.oint++;
    }
    if (c.oint >= value.length()) {
	return null; /*FAIL*/
    } else {
	return iNew.String(value.charAt(c.oint));
    }
}

vDescriptor BangVar(iClosure c, vVariable v) {
    int i;
    if (c.PC == 1) {
	c.o = new Integer(i = 1);
	c.PC = 2;
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
    v = (vDescriptor) iEnv.builtintab.get(this.value);
    if (v != null) {
	return v.deref();
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

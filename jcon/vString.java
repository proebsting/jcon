//  vString.java -- java Strings
//
//  This class implements an alternative to java.lang.String that is
//  specifically geared to Icon's needs:
//	-- constituent characters are just eight bits wide (Java bytes)
//	-- operations are not synchronized
//	-- concatenation is done lazily



package rts;

public class vString extends vValue {

    private int tlength;		// total string length
    private vString prefix;		// first part of string (optional)
    private byte[] data;		// character array

    private vNumeric cachedNumeric;	// cached numeric equivalent.



// preallocated empty string and one-character strings

private static vString nullstring = new vString();
private static vString strlist[] = new vString[vCset.MAX_VALUE + 1];

static {
    for (int i = 0; i < strlist.length; i++) {
	strlist[i] = new vString((char) i);
    }
}



// static constructor methods

public static vString New()		{ return nullstring; }

public static vString New(char c)	{ return strlist[c & 0xFF]; }

public static vString New(String s) {
    int len = s.length();
    if (len < 1) {
	return nullstring;
    } else if (len == 1) {
	return strlist[s.charAt(0) & 0xFF];
    } else {
	byte[] b = new byte[len];
	for (int i = 0; i < len; i++)
	    b[i] = (byte) s.charAt(i);
	return new vString(b);
    }
}

public static vString New(byte[] b) {
    int len = b.length;
    if (len < 1) {
	return nullstring;
    } else if (len == 1) {
	return strlist[b[0] & 0xFF];
    } else {
	return new vString(b);
    }
}

public static vString New(vString s, int i, int j) {     // s[i:j], both > 0
    int len = j - i;
    if (len == s.length()) {		// if extracting entire string
	return s;			// reuse it
    } else if (len == 0) {
	return nullstring;
    } else if (len == 1) {
	return strlist[s.charAt(i - 1) & 0xFF];
    } else {
	return new vString(s, i, j);
    }
}

public static vString New(vString s, int i, int j, vString t) {
    return new vString(s, i, j, t);
}



//  constructors

private vString() {				// new vString()
    data = new byte[0];
}

private vString(char c) {			// new vString(char)
    tlength = 1;
    data = new byte[1];
    data[0] = (byte)c;
}

private vString(byte[] b) {			// new vString(b[])
						// embeds b (does not copy it)
    data = b;
    tlength = data.length;
}

private vString(vString s, byte b[]) {		// new vString := s || b[]
						// embeds b (does not copy it)
    tlength = s.tlength + b.length;
    prefix = s;
    data = b;
}

private vString(vString s, int i, int j) {	// new vString := .s[i:j]
    s.flatten();
    tlength = j - i;
    data = new byte[tlength];
    System.arraycopy(s.data, i - 1, data, 0, tlength);
}

private vString(vString s, int i, int j, vString t) {	// new := (s[i:j] := t)

    s.flatten();
    t.flatten();

    tlength = s.tlength - (j - i) + t.tlength;
    data = new byte[tlength];
    int n = s.data.length - j + 1;

    System.arraycopy(s.data, 0, data, 0, i - 1);
    System.arraycopy(t.data, 0, data, i - 1, t.data.length);
    System.arraycopy(s.data, s.data.length - n, data, tlength - n, n);
}



//  special vString methods

final int length() {			// s.length()
    return tlength;
}

final char charAt(int i) {		// s.charAt(i)   [zero-based]
    this.flatten();
    return (char)(data[i] & 0xFF);
}

final boolean matches(vString s, int i){ // match(s, this, i)  [zero-based]
    if (this.tlength > s.tlength - i) {
	return false;
    }
    this.flatten();
    s.flatten();
    byte[] d = s.data;
    for (int j = 0; j < this.tlength; j++) {
	if (this.data[j] != d[i+j]) {
	    return false;
	}
    }
    return true;
}

final boolean identical(vString s) {	// like equals, but assumes vString
    return this.tlength == s.tlength && this.matches(s, 0);
}

final vString concat(vString s) {	// s.concat(s)
    if (tlength == 0) {
	return s;
    }
    if (s.tlength == 0) {
	return this;
    }
    s.flatten();
    return new vString(this, s.data);
}

public final vString surround(String js1, String js2) {	// s.surround(js1, js2)
    this.flatten();
    int len1 = js1.length();
    int len2 = js2.length();
    byte[] b = new byte[len1 + tlength + len2];
    for (int i = 0; i < len1; i++) {
	b[i] = (byte) js1.charAt(i);
    }
    System.arraycopy(data, 0, b, len1, tlength);
    int o = len1 + tlength;
    for (int i = 0; i < len2; i++) {
	b[o + i] = (byte) js2.charAt(i);
    }
    return vString.New(b);
}

public final byte[] getBytes() {	// s.getBytes() rtns data (NOT a copy)
    this.flatten();
    return data;
}

public final String toString() {	// s.toString()
    this.flatten();
    return new String(data);
}

final vInteger toInteger() {		// s.toInteger -- no radix or exponent

    this.flatten();

    int i = 0;
    long v;
    boolean neg = false;
    byte c;

    while (i < tlength && Character.isWhitespace((char)data[i])) {
	i++;
    }

    if (i < tlength) {
	c = data[i];
	if (c == '-') {
	    neg = true;
	    i++;
	} else if (c == '+') {
	    i++;
	}
    }

    if (i < tlength && Character.isDigit((char)(c = data[i]))) {
	v = c - '0';
	i++;
    } else {
	return null;	// failed (here; should try general converter)
    }

    while (i < tlength && Character.isDigit((char)(c = data[i]))) {
	v = 10 * v + c - '0';		//#%#% ignoring overflow
	i++;
	if (v > Long.MAX_VALUE / 10) {
	    break;
	}
    }

    while (i < tlength && Character.isWhitespace((char)data[i])) {
	i++;
    }
    if (i < tlength) {
	return null;
    } else if (neg) {
	return vInteger.New(-v);
    } else {
	return vInteger.New(v);
    }
}



//  internal method to collapse a tree of lazy concatenations
//  (broken into two parts so that the first part gets in-lined where called)

private void flatten() {
    if (this.prefix != null) {
	this.flatten1();
    }
}

private void flatten1() {
    byte[] d = new byte[tlength];
    vString s = this;
    int i = tlength;
    while (i > 0) {
	int j = s.data.length;
	while (j > 0) {
	    d[--i] = s.data[--j];
	}
	s = s.prefix;
    }
    data = d;
    prefix = null;
}



//  overridden java.lang.Object() methods

public int hashCode() {	 // hashcode, consistent whether flattened or not
    vString s = this;
    int n = 0;
    while (s != null) {
	for (int i = s.data.length; i > 0; ) {
	    n = 37 * n + s.data[--i];
	}
	s = s.prefix;
    }
    return n;
}

public boolean equals(Object o)	{
    if (o == null || !(o instanceof vString))
	return false;
    vString s = (vString) o;
    return this.tlength == s.tlength && this.matches(s, 0);
}



//  general vDescriptor primitives

public vString mkString()	{ return this; } // no-op coversion to vString

vString write()		{ return this; }

static vString typestring = vString.New("string");
public vString Type()	{ return typestring; }
int rank()		{ return 30; }		// strings rank after reals

vString image()		{ return image(tlength); }
vString report()	{ return image(16); }	// limit to max of 16 chars


private vString image(int maxlen) {	// make image, up to maxlen chars
    this.flatten();
    vByteBuffer b = new vByteBuffer(maxlen + 5);	// optimistic guess
    b.append('"');
    int i;
    for (i = 0; i < maxlen && i < tlength; i++) {
	char c = (char) data[i];
	if (c == '"') {
	    b.append('\\');
	    b.append('\"');
	} else {
	    appendEscaped(b, c);
	}
    }
    if (i < tlength) {
	b.append('.');
	b.append('.');
	b.append('.');
    }
    b.append('"');
    return b.mkString();
}


int compareTo(vValue v) {		// compare strings lexicographically
    vString s = (vString) v;
    this.flatten();
    s.flatten();
    int i;
    for (i = 0; i < this.tlength && i < s.tlength; i++) {
	int d = this.data[i] - s.data[i];
	if (d != 0) {
	    return d;
	}
    }
    return this.tlength - s.tlength;
}



public vNumeric Numerate() {					// numeric(s)

    if (cachedNumeric != null) {
	return cachedNumeric;
    }

    // first try straightforward conversion to integer
    vInteger v = this.toInteger();
    if (v != null) {
	return cachedNumeric = v;
    }

    // nope, go the long way.  #%#% this could be improved.

    String s = this.toString().trim(); //#%#% too liberal: trims not just spaces

    if (s.length() > 0 && s.charAt(0) == '+') {	// allow leading +, by trimming
	s = s.substring(1);
    }

    try {
	return cachedNumeric = vInteger.New(Long.parseLong(s));
    } catch (NumberFormatException e) {
    }

    try {
	Double d = Double.valueOf(s);
	if (!d.isInfinite()) {
	    return cachedNumeric = vReal.New(d.doubleValue());
	}
    } catch (NumberFormatException e) {
    }

    v = vInteger.radixParse(s);			// try to parse as radix value
    if (v != null) {
	return cachedNumeric = v;
    }

    iRuntime.error(102, this);
    return null;
}

vInteger mkInteger()	{					// integer(s)
    try {
	return this.Numerate().mkInteger();	// allows integer("3e6")
    } catch (iError e) {
	iRuntime.error(101, this);
	return null;
    }
}

vReal mkReal()		{					// real(s)
    return this.Numerate().mkReal();
}

vCset mkCset() {						// cset(s)
    return vCset.New(this);
}



public vProc Proc(long i) {
    String s = this.toString();
    if (i == 0) {
	return (vProc) iEnv.builtintab.get(s);
    }
    
    vDescriptor v = (vDescriptor) iEnv.symtab.get(s);
    if (v != null) {
	return v.Deref().mkProc();
    }
    v = (vDescriptor) iEnv.builtintab.get(s);
    if (v != null) {
	return (vProc) v;
    }
    try {
	return this.mkInteger().mkProc();
    } catch (iError e) {
	// ignore
    }
    if (i < 1 || i > 3) {
	return null;
    }
    v = (vDescriptor) iEnv.proctab[(int)i-1].get(s);
    if (v != null) {
	return (vProc) v;
    }
    return null;
}



// s ! args	//#%#% only handles operators for now

public vDescriptor ProcessArgs(vDescriptor v) {
    return iOperators.invoke(this, v);
}



//  =s : tab(match(s))

public vDescriptor TabMatch() {
    vString subj = (vString) k$subject.self.Deref();
    final vInteger oldpos = (vInteger) k$pos.self.Deref();
    int pos = (int) oldpos.value;
    if (! matches(subj, pos - 1)) {
	return null;
    }
    k$pos.set(pos + tlength);
    final vString newstr = vString.New(subj, pos, pos + tlength);
    return new vClosure () {
	{ retval = newstr; }
        public vDescriptor Resume() {
	    k$pos.self.Assign(oldpos);
	    return null;
        }
    };
}



//  static methods for argument processing and defaulting

static vString argDescr(vDescriptor[] args, int index) {	// required arg
    if (index >= args.length) {
	iRuntime.error(103);
	return null;
    } else {
	return args[index].mkString();
    }
}

static vString argDescr(vDescriptor[] args, int index, vString dflt){ // opt arg
    if (index >= args.length || args[index].isnull()) {
	return dflt;
    } else {
	return args[index].mkString();
    }
}



//  append escaped char to ByteBuffer; also used for csets

private static char[] ecodes = { 'b', 't', 'n', 'v', 'f', 'r' };
private static char[] xcodes =
    { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };

static void appendEscaped(vByteBuffer b, char c) {
    if (c >= ' ' && c <= '~') {		// printable range
	if (c == '\\') {
	    b.append('\\');
	}
	b.append(c);
    } else if (c >= 0x08 && c <= 0x0D) {
	b.append('\\');
	b.append(ecodes[c - 0x08]);	//  \b \t \n \v \f \r
    } else if (c == 0x1B) {
	b.append('\\');
	b.append('e');			//  \e
    } else if (c == 0x7F) {
	b.append('\\');
	b.append('d');			//  \d
    } else {
	b.append('\\');
	b.append('x');			//  \xnn
	b.append(xcodes[(c >> 4) & 0xF]);
	b.append(xcodes[c & 0xF]);
    }
}



//  s.posEq(n) -- return positive equivalent of position n in string s,
//		  or zero if out of bounds

int posEq(long n) {
    if (n <= 0) {
	n += tlength + 1;
    }
    if (n > 0 && n <= tlength + 1) {
	return (int)n;
    } else {
	return 0;
    }
}



//  operations



public vInteger Size()	{
    return vInteger.New(tlength);
}



public vString Concat(vDescriptor v) {
    return this.concat(v.mkString());
}



public vDescriptor Index(vDescriptor i) {
    int m = this.posEq(i.mkInteger().value);
    if (m == 0 || m > tlength) {
	return null; /*FAIL*/
    }
    return vString.New(this, m, m + 1);
}

public vDescriptor IndexVar(vVariable v, vDescriptor i) {
    int m = this.posEq(i.mkInteger().value);
    if (m == 0 || m > tlength) {
	return null; /*FAIL*/
    }
    return vSubstring.New(v, m, m + 1);
}

public vDescriptor Section(vDescriptor i, vDescriptor j) {
    int m = this.posEq(i.mkInteger().value);
    int n = this.posEq(j.mkInteger().value);
    if (m == 0 || n == 0) {
	return null; /*FAIL*/
    }
    if (m > n) {
	return vString.New(this, n, m);
    } else {
	return vString.New(this, m, n);
    }
}



public vDescriptor SectionVar(vVariable v, vDescriptor i, vDescriptor j) {
    int m = this.posEq(i.mkInteger().value);
    int n = this.posEq(j.mkInteger().value);
    if (m == 0 || n == 0) {
	return null; /*FAIL*/
    }
    if (m > n) {
	return vSubstring.New(v, n, m);
    } else {
	return vSubstring.New(v, m, n);
    }
}



public vDescriptor Select() {
    if (tlength == 0) {
	return null; /*FAIL*/
    }
    int i = (int) k$random.choose(tlength);
    return vString.New(charAt(i));
}

public vDescriptor SelectVar(vVariable v) {
    if (tlength == 0) {
	return null; /*FAIL*/
    }
    int i = (int) k$random.choose(tlength);
    return vSubstring.New(v, i+1, i+2);
}



public vDescriptor Bang() {
    if (tlength == 0) {
	return null; /*FAIL*/
    } else if (tlength == 1) {
	return New(charAt(0));	// size 1 string
    }

    return new vClosure() {
	{ retval = New(charAt(0)); }
	int i = 0;
	public vDescriptor Resume() {
	    if (++i >= tlength) {
		return null; /*FAIL*/
	    } else {
		return New(charAt(i));
	    }
	}
    };
}



public vDescriptor BangVar(final vVariable v) {
    final int len = ((vString)v.Deref()).tlength;
    if (len == 0) {
	return null; /*FAIL*/
    }

    return new vClosure() {
	{ retval = vSubstring.New(v, 1, 2); }
	int i = 1;
	public vDescriptor Resume() {
	    if (++i > len) {
		return null; /*FAIL*/
	    } else {
		return vSubstring.New(v, i, i + 1);
	    }
	}
    };
}



public vString LLess(vDescriptor v) {
    vString s = v.mkString();
    return this.compareTo(s) < 0 ? s : null;
}
public vString LLessEq(vDescriptor v) {
    vString s = v.mkString();
    return this.compareTo(s) <= 0 ? s : null;
}
public vString LEqual(vDescriptor v) {
    vString s = v.mkString();
    return this.compareTo(s) == 0 ? s : null;
}
public vString LUnequal(vDescriptor v) {
    vString s = v.mkString();
    return this.compareTo(s) != 0 ? s : null;
}
public vString LGreaterEq(vDescriptor v) {
    vString s = v.mkString();
    return this.compareTo(s) >= 0 ? s : null;
}
public vString LGreater(vDescriptor v) {
    vString s = v.mkString();
    return this.compareTo(s) > 0 ? s : null;
}

public vValue Complement()		{ return this.mkCset().Complement(); }
public vValue Intersect(vDescriptor x)	{ return this.mkCset().Intersect(x); }
public vValue Union(vDescriptor x)	{ return this.mkCset().Union(x); }
public vValue Diff(vDescriptor x)	{ return this.mkCset().Diff(x); }



} // class vString

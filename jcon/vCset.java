package jcon;

public final class vCset extends vValue {

    int size;			// cset size; -1 if unknown
    long w1, w2, w3, w4;	// four words of cset bits

public static final int MAX_VALUE = 255;	// maximum char value in Jcon
private static final int UNKNOWN_SIZE = -1;	// indicates unknown cset size



// preallocated empty cset and one-character csets

private static vCset zcset = new vCset();
private static vCset cslist[] = new vCset[vCset.MAX_VALUE + 1];

static {
    for (int i = 0; i < cslist.length; i++) {
	cslist[i] = new vCset(i, i);
    }
}



// constructor methods

public static vCset New(int c) {
    return cslist[c];
}

public static vCset New(int low, int high) {
    if (low == high) {
	return cslist[low];
    } else {
	return new vCset(low, high);
    }
}

public static vCset New(String s) {
    int len = s.length();
    if (len < 1) {
	return zcset;
    } else if (len == 1) {
	return cslist[s.charAt(0)];
    } else {
	return new vCset(s);
    }
}

public static vCset New(vString s) {
    int len = s.length();
    if (len < 1) {
	return zcset;
    } else if (len == 1) {
	return cslist[s.charAt(0)];
    } else {
	return new vCset(s);
    }
}



// constructors

private vCset()	{				// new Cset()
    size = 0;
}

private vCset(int c, int d) {			// new Cset(c..d)
    size = d - c + 1;
    while (c <= d && c < 64) {
	w1 |= 1L << c++;
    }
    while (c <= d && c < 128) {
	w2 |= 1L << c++;
    }
    while (c <= d && c < 192) {
	w3 |= 1L << c++;
    }
    while (c <= d) {
	w4 |= 1L << c++;
    }
}

private vCset(vString s) {			// new Cset(vString s)
    byte[] b = s.getBytes();
    for (int i = 0; i < b.length; i++) {
	int c = b[i] & 0xFF;
	long m = 1L << c;
	switch (c >> 6) {
	    case 0:  if ((w1 & m) == 0)  { w1 |= m; size++; }  break;
	    case 1:  if ((w2 & m) == 0)  { w2 |= m; size++; }  break;
	    case 2:  if ((w3 & m) == 0)  { w3 |= m; size++; }  break;
	    case 3:  if ((w4 & m) == 0)  { w4 |= m; size++; }  break;
	}
    }
}

private vCset(String s) {			// new Cset(String s)
    for (int i = 0; i < s.length(); i++) {
	int c = s.charAt(i);
	long m = 1L << c;
	switch (c >> 6) {
	    case 0:  if ((w1 & m) == 0)  { w1 |= m; size++; }  break;
	    case 1:  if ((w2 & m) == 0)  { w2 |= m; size++; }  break;
	    case 2:  if ((w3 & m) == 0)  { w3 |= m; size++; }  break;
	    case 3:  if ((w4 & m) == 0)  { w4 |= m; size++; }  break;
	}
    }
}



//  cset member function

public final boolean member(int c) {		// cs.member(c)
    c = c & 0xFF;
    long m = 1L << c;
    switch (c >> 6) {
	case 0: return (w1 & m) != 0;
	case 1: return (w2 & m) != 0;
	case 2: return (w3 & m) != 0;
	case 3: return (w4 & m) != 0;
    }
    return false;	// not reached
}



private static final vString csdigits = vString.New("&digits");
private static final vString csletters = vString.New("&letters");
private static final vString cslcase = vString.New("&lcase");
private static final vString csucase = vString.New("&ucase");
private static final vString csascii = vString.New("&ascii");
private static final vString cscset = vString.New("&cset");

public vString image()	{ return image(MAX_VALUE + 1); }
public vString report()	{ return image(16); }	// limit to max of 16 chars

private vString image(int maxlen) {

    // check for predefined cset using a hardwired decision tree
    if (size < 0) {
	this.Size();			// must know size
    }
    if (size == 52) {
	if (w2 == 0x07FFFFFE07FFFFFEL) {
	    return csletters;
	}
    } else if (size < 52) {
	if (size == 10) {
	    if (w1 == 0x03FF000000000000L) {
		return csdigits;
	    }
	} else if (size == 26) {
	    if (w2 == 0x07FFFFFE00000000L) {
		return cslcase;
	    } else if (w2 == 0x0000000007FFFFFEL) {
		return csucase;
	    }
	}
    } else { // size > 52
	if (size == 256) {
	    return cscset;
	} else if (size == 128 && (w3 | w4) == 0) {
	    return csascii;
	}
    }

    // not a predefined cset
    vByteBuffer b = new vByteBuffer(this.size + 10);  // arbitrary size guess
    int n = 0;
    b.append('\'');
    for (char c = 0; c <= MAX_VALUE; c++) {
	if (this.member(c)) {
	    if (++n > maxlen) {
		b.append('.');
		b.append('.');
		b.append('.');
		break;
	    }
	    if (c == '\'') {
		b.append('\\');
		b.append('\'');
	    } else {
		vString.appendEscaped(b, c);
	    }
	}
    }
    b.append('\'');
    return b.mkString();
}



private static vString typestring = vString.New("cset");
public vString Type()	{ return typestring;}



int rank()		{ return 40; }		// csets sort after strings

int compareTo(vValue v) {
    vCset vset = (vCset) v;
    int i;
    for (i = 0; i <= vCset.MAX_VALUE; i++) {
	if (this.member(i) ^ vset.member(i)) {
	    break;
	}
    }
    if (i > vCset.MAX_VALUE) {
	return 0;		// identical csets
    }

    if (this.member(i)) {	// first bit found in this
	while (++i <= vCset.MAX_VALUE) {
	    if (vset.member(i)) {
		return -1;	// v is not empty
	    }
	}
	return 1;		// v is empty

    } else {			// first bit found in v

	while (++i <= vCset.MAX_VALUE) {
	    if (this.member(i)) {
		return 1;	// this is not empty
	    }
	}
	return -1;		// this is empty
    }
}



public boolean equals(Object o) {
    if (! (o instanceof vCset)) {
	return false;
    }
    vCset v = (vCset) o;
    return ((w1 ^ v.w1) | (w2 ^ v.w2 | (w3 ^ v.w3)) | (w4 ^ v.w4)) == 0;
}

public int hashCode() {
    long x = w1 - w2 - w3 - w4;
    return (int) (x - (x >> 32));
}

public vCset mkCset()		{ return this; }




// the catch clauses in these conversions ensure correct "offending values"

public vNumeric Numerate()	{
    try {
	return this.mkString().Numerate();
    } catch (iError e) {
	iRuntime.error(102, this);
	return null;
    }
}

public vInteger mkInteger() {
    try {
	return this.mkString().mkInteger();
    } catch (iError e) {
	iRuntime.error(101, this);
	return null;
    }
}

public vNumeric mkFixed() {
    try {
	return this.mkString().mkFixed();
    } catch (iError e) {
	iRuntime.error(101, this);
	return null;
    }
}

public vReal mkReal() {
    try {
	return this.mkString().mkReal();
    } catch (iError e) {
	iRuntime.error(102, this);
	return null;
    }
}



public vDescriptor Index(vDescriptor i)
				{ return this.mkString().Index(i); }
public vDescriptor Section(vDescriptor i, vDescriptor j)
				{ return this.mkString().Section(i,j); }



public vString mkString() {			// string(c)
    vByteBuffer b;
    long w;

    if (size == 0) {
	return vString.New();		// known empty cset
    } else if (size > 0) {
	b = new vByteBuffer(size);	// known size
    } else {
	b = new vByteBuffer(64);	// arbitrary guess;
    }

    if (w1 != 0) {
	for (int i = 0; i < 64; i++) {
	    if (((w1 >> i) & 1) != 0) {
		b.append((char) i);
	    }
	}
    }
    if (w2 != 0) {
	for (int i = 64; i < 128; i++) {
	    if (((w2 >> i) & 1) != 0) {
		b.append((char) i);
	    }
	}
    }
    if (w3 != 0) {
	for (int i = 128; i < 192; i++) {
	    if (((w3 >> i) & 1) != 0) {
		b.append((char) i);
	    }
	}
    }
    if (w4 != 0) {
	for (int i = 192; i < 256; i++) {
	    if (((w4 >> i) & 1) != 0) {
		b.append((char) i);
	    }
	}
    }
    return b.mkString();
}



public vInteger Size() {		// *c

    if (size < 0) {			// if size not already known
	size = 0;
	for (int i = 0; i <= MAX_VALUE; i++) {
	    if (this.member(i)) {
		size++;
	    }
	}
    }
    return vInteger.New(size);
}



public vDescriptor Select() {		// ?c
    if (size < 0) {
	this.Size();			// must know size
    }
    if (size == 0) {
	return null; /*FAIL*/
    }
    int n = (int) iKeyword.random.choose(size) + 1;
    int c = -1;
    for (int i = 0; i < n; i++) {
	while (! this.member(++c)) {
	    ;
	}
    }
    return vString.New((char) c);
}



public vDescriptor Bang() {		// !c
    return new vClosure() {
	int i = 0;
	public vDescriptor Resume() {
	    while (i <= MAX_VALUE) {
		if (member(i)) {
		    retval = vString.New((char) i++);
		    return this;
		}
		i++;
	    }
	    return null; /*FAIL*/
	}
    }.Resume();
}



public vValue Complement() {		// ~c
    vCset result = new vCset();
    result.w1 = ~w1;
    result.w2 = ~w2;
    result.w3 = ~w3;
    result.w4 = ~w4;
    if (size >= 0) {
	result.size = MAX_VALUE + 1 - size;
    } else {
	result.size = UNKNOWN_SIZE;
    }
    return result;
}



public vValue Union(vDescriptor x) {	// c1 ++ c2
    vCset r = null;
    try {
	r = x.mkCset();
    } catch (iError e) {
	iRuntime.error(120, x);		// two sets or two csets expected
    }
    vCset result = new vCset();
    result.w1 = w1 | r.w1;
    result.w2 = w2 | r.w2;
    result.w3 = w3 | r.w3;
    result.w4 = w4 | r.w4;
    result.size = UNKNOWN_SIZE;
    return result;
}

public vValue Intersect(vDescriptor x) {// c1 && c2
    vCset r = null;
    try {
	r = x.mkCset();
    } catch (iError e) {
	iRuntime.error(120, x);		// two sets or two csets expected
    }
    vCset result = new vCset();
    result.w1 = w1 & r.w1;
    result.w2 = w2 & r.w2;
    result.w3 = w3 & r.w3;
    result.w4 = w4 & r.w4;
    result.size = UNKNOWN_SIZE;
    return result;
}

public vValue Diff(vDescriptor x) {	// c1 || c2
    vCset r = null;
    try {
	r = x.mkCset();
    } catch (iError e) {
	iRuntime.error(120, x);		// two sets or two csets expected
    }
    vCset result = new vCset();
    result.w1 = w1 & ~r.w1;
    result.w2 = w2 & ~r.w2;
    result.w3 = w3 & ~r.w3;
    result.w4 = w4 & ~r.w4;
    result.size = UNKNOWN_SIZE;
    return result;
}



} // class vCset

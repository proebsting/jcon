package rts;

public class vCset extends vValue {

static final int MAX_VALUE = 255;		// maximum char value in Jcon

    private java.util.BitSet t;


private vCset(java.util.BitSet x) {		// new Cset(Bitset b)
    t = (java.util.BitSet) x.clone();
}

vCset(String s) {				// new Cset(String s)
    t = new java.util.BitSet();
    for (int i = 0; i < s.length(); i++) {
	t.set( (int) s.charAt(i) );
    }
}

vCset(vString s) {				// new Cset(vString s)
    t = new java.util.BitSet();
    for (int i = 0; i < s.length(); i++) {
	t.set( (int) s.charAt(i) );
    }
}

vCset(int low, int high) {			// new Cset(int low, high)
    t = new java.util.BitSet();
    for (int i = low; i <= high; i++) {
	t.set(i);
    }
}



vString image() {	//#%#% should recognize keyword csets & treat specially
    vByteBuffer b = new vByteBuffer(80);	// arbitrary size estimate
    b.append('\'');
    for (char c = 0; c < t.size(); c++) {
	if (t.get(c)) {
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

static vString typestring = iNew.String("cset");
vString type()		{ return typestring;}

int rank()		{ return 40; }		// csets sort after strings

boolean member(char c) {
    return this.t.get((int)c);
}

int compareTo(vValue v) {
    java.util.BitSet vset = ((vCset)v).t;
    int i;
    for (i = 0; i <= vCset.MAX_VALUE; i++) {
	if (this.t.get(i) ^ vset.get(i)) {
	    break;
	}
    }
    if (i > vCset.MAX_VALUE) {
	return 0;		// identical csets
    }

    if (this.t.get(i)) {	// first bit found in this
	while (++i <= vCset.MAX_VALUE) {
	   if (vset.get(i)) {
		return -1;	// v is not empty
	   }
	}
	return 1;		// v is empty

    } else {			// first bit found in v
	while (++i <= vCset.MAX_VALUE) {
	   if (this.t.get(i)) {
		return 1;	// this is not empty
	   }
	}
	return -1;		// this is empty
    }
}

public boolean equals(Object o) {
    return (o instanceof vCset)
	  && (((vCset)o).mkString().equals(this.mkString()));
}

public int hashCode()	{ return this.mkString().hashCode(); }

vCset mkCset()		{ return this; }


// the catch clauses in these conversions ensure correct "offending values"

vNumeric mkNumeric()	{
	try {
		return this.mkString().mkNumeric();
	} catch (iError e) {
		iRuntime.error(102, this);
		return null;
	}
}

vInteger mkInteger() {
	try {
		return this.mkString().mkInteger();
	} catch (iError e) {
		iRuntime.error(101, this);
		return null;
	}
}

vReal mkReal() {
	try {
		return this.mkString().mkReal();
	} catch (iError e) {
		iRuntime.error(102, this);
		return null;
	}
}



vDescriptor Index(vValue i)		{ return this.mkString().Index(i); }
vDescriptor Section(vValue i, vValue j)	{ return this.mkString().Section(i,j); }

vString mkString() {
    vByteBuffer b = new vByteBuffer(80);	// arbitrary size estimate
    for (int i = 0; i < t.size(); i++) {
	if (t.get(i)) {
	    b.append((char)i);
	}
    }
    return b.mkString();
}

vInteger Size() {
    int count = 0;
    for (int i = 0; i < t.size(); i++) {
	if (t.get(i)) {
	    count++;
	}
    }
    return iNew.Integer(count);
}

vDescriptor Select() {
    return this.mkString().Select();
}

vDescriptor Bang(iClosure c) {
    if (c.PC == 1) {
        c.o = c;
	c.oint = 0;
	c.PC = 2;
    }
    for (int k = c.oint; k < t.size(); k++) {
	if (t.get(k)) {
	    c.oint = k+1;
	    return iNew.String((char) k);
	}
    }
    return null;
}

vValue Complement() {
    vCset result = new vCset(0, -1);
    result.t = new java.util.BitSet(vCset.MAX_VALUE);
    for (int i = 0; i < result.t.size(); i++) {
	if (!this.t.get(i)) {
	    result.t.set(i);
	}
    }
    return result;
}

vValue Union(vDescriptor x) {
    vCset right = null;
    try {
	right = x.mkCset();
    } catch (iError e) {
	iRuntime.error(120, x);		// two sets or two csets expected
    }
    // all the bigger/smaller nonsense below is due to Sun's
    // brain-damaged definition of BitSet.or().
    java.util.BitSet bigger;
    java.util.BitSet smaller;
    if (right.t.size() < this.t.size()) {
	bigger = this.t;
	smaller = right.t;
    } else {
	bigger = right.t;
	smaller = this.t;
    }
    vCset result = new vCset(bigger);
    result.t.or(smaller);
    return result;
}

vValue Intersect(vDescriptor x) {
    try {
	x = x.mkCset();
    } catch (iError e) {
	iRuntime.error(120, x);		// two sets or two csets expected
    }
    vCset result = new vCset((java.util.BitSet) ((vCset)x).t);
    result.t.and(this.t);
    return result;
}

vValue Diff(vDescriptor x) {
    try {
	x = x.mkCset();
    } catch (iError e) {
	iRuntime.error(120, x);		// two sets or two csets expected
    }
    java.util.BitSet b = ((vCset)x).t;
    vCset result = new vCset((java.util.BitSet) this.t);
    for (int i = 0; i < b.size(); i++) {
	if (b.get(i)) {
	    result.t.clear(i);
	}
    }
    return result;
}

//  static methods for argument processing and defaulting

static vCset argVal(vDescriptor[] args, int index)		// required arg
{
    if (index >= args.length) {
	iRuntime.error(104);
	return null;
    } else {
	return args[index].mkCset();
    }
}

static vCset argVal(vDescriptor[] args, int index, vCset dflt)	// optional arg
{
    if (index >= args.length || args[index] instanceof vNull) {
	return dflt;
    } else {
	return args[index].mkCset();
    }
}

} // class vCset

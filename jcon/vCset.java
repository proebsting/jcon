package rts;

public class vCset extends vValue {

    java.util.BitSet t;


vCset(java.util.BitSet x) {			// new Cset(Bitset b)
    t = (java.util.BitSet) x.clone();
}

vCset(String s) {				// new Cset(String s)
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



String image() {	//#%#% need to add escapes for special characters
    StringBuffer b = new StringBuffer();
    b.append("'");
    for (int i = 0; i < t.size(); i++) {
	if (t.get(i)) {
	    b.append((char)i);
	}
    }
    b.append("'");
    return b.toString();
}

String report()		{ return image(); }

String type()		{ return "cset";}

int rank()		{ return 40; }		// csets rank after strings

int compareTo(vValue v) {
    return v.mkString().compareTo(v.mkString());  //#%#% horribly slow 
}

public boolean equals(Object o) {
    return (o instanceof vCset)
	  && (((vCset)o).mkString().equals(this.mkString()));
}

public int hashCode()	{ return this.mkString().hashCode(); }

vCset mkCset()		{ return this; }
vNumeric mkNumeric()	{ return this.mkString().mkNumeric(); }
vInteger mkInteger()	{ return this.mkString().mkInteger(); }
vReal mkReal()		{ return this.mkString().mkReal(); }

vString mkString() {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < t.size(); i++) {
	if (t.get(i)) {
	    b.append((char)i);
	}
    }
    return iNew.String(b.toString());
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
    if (c.o == null) {
        c.o = new Integer(0);
    }
    Integer i = (Integer) c.o;
    for (int k = i.intValue(); k < t.size(); k++) {
	if (t.get(k)) {
	    c.o = new Integer(k+1);
	    return iNew.String(String.valueOf((char) k));
	}
    }
    return null;
}

vValue Complement() {
    vCset result = new vCset("");
    result.t = new java.util.BitSet((int)Character.MAX_VALUE);
    for (int i = 0; i < result.t.size(); i++) {
	if (!this.t.get(i)) {
	    result.t.set(i);
	}
    }
    return result;
}

vValue Union(vDescriptor x) {
    vCset right = x.mkCset();
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
    x = x.mkCset();
    vCset result = new vCset((java.util.BitSet) ((vCset)x).t);
    result.t.and(this.t);
    return result;
}

vValue Diff(vDescriptor x) {
    x = x.mkCset();
    java.util.BitSet b = ((vCset)x).t;
    vCset result = new vCset((java.util.BitSet) this.t);
    for (int i = 0; i < b.size(); i++) {
	if (b.get(i)) {
	    result.t.clear(i);
	}
    }
    return result;
}

} // class vCset

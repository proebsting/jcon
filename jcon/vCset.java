class vCset extends vValue {

    java.util.BitSet t;

vCset(java.util.BitSet x) {
    t = (java.util.BitSet) x.clone();
}

vCset(String s) {
    t = new java.util.BitSet();
    for (int i = 0; i < s.length(); i++) {
	t.set( (int) s.charAt(i) );
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

vValue Intersect(vDescriptor x) {
    if (!(x instanceof vCset)) {
        iRuntime.error(120, x);
    }
    vCset result = new vCset((java.util.BitSet) ((vCset)x).t.clone());
    result.t.and(this.t);
    return result;
}

vValue Union(vDescriptor x) {
    if (!(x instanceof vCset)) {
        iRuntime.error(120, x);
    }
    vCset result = new vCset((java.util.BitSet) ((vCset)x).t.clone());
    result.t.or(this.t);
    return result;
}

vValue Diff(vDescriptor x) {
    if (!(x instanceof vCset)) {
        iRuntime.error(120, x);
    }
    java.util.BitSet b = ((vCset)x).t;
    vCset result = new vCset((java.util.BitSet) this.t.clone());
    for (int i = 0; i < b.size(); i++) {
	if (b.get(i)) {
	    result.t.clear(i);
	}
    }
    return result;
}

} // class vCset

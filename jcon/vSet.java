package jcon;

import java.util.*;



public final class vSet extends vStructure {

    private java.util.Hashtable<vValue,vValue> t;


static int nextsn = 1;		// next serial number



// constructors

public static vSet New(vValue x)		{ return new vSet(x); }

private vSet(java.util.Hashtable<vValue,vValue> x) {
    super(nextsn++);
    t = new java.util.Hashtable<vValue,vValue>(x);
}

private vSet(vValue x) {
    super(nextsn++);
    t = new java.util.Hashtable<vValue,vValue>();
    if (x != null && !x.isnull()) {
	if (!(x instanceof vList)) {
	    iRuntime.error(108, x);
	}
	vList list = (vList) x;
	java.util.Enumeration<vSimpleVar> i = list.elements();
	while (i.hasMoreElements()) {
	    vValue v = (i.nextElement()).Deref();
	    t.put(v, v);
	}
    }
}



private static vString typestring = vString.New("set");
public vString Type()		{ return typestring;}

int rank()			{ return 100; }		// sets rank after lists

public vInteger Size()		{ return vInteger.New(t.size()); }

public vValue Copy()		{ return new vSet(this.t); }



public vDescriptor Select() {
    if (t.size() == 0) {
	return null;
    }
    int index = (int) iKeyword.random.choose(t.size());
    java.util.Enumeration<vValue> e = t.keys();
    for (int k = 0; k < index; k++) {
	e.nextElement();
    }
    return (vDescriptor)e.nextElement();
}

public vDescriptor Bang() {
    final vValue a[] = new vValue[t.size()];
    Enumeration<vValue> e = t.keys();
    int i = 0;
    while (e.hasMoreElements()) {
	a[i++] = e.nextElement();
    }
    if (i == 0) {
	return null; /*FAIL*/
    } else if (i == 1) {
	return a[0];  // only one member
    }

    return new vClosure() {
	{ retval = a[0]; }
	int j = 1;
	public vDescriptor Resume() {
	    while (j < a.length) {
		vValue v = a[j++];
		if (t.containsKey(v)) {		// if not stale
		    return v;			// suspend
		}
	    }
	    return null; /*FAIL*/
	}
    };
}

public vValue[] mkArray(int errno) {		// for sort(S)
    if (errno == 126) {		// if not allowed to participate
	iRuntime.error(errno, this);
    }
    vValue a[] = new vValue[t.size()];
    int i = 0;
    for (Enumeration<vValue> e = t.keys(); e.hasMoreElements(); ) {
	a[i++] = e.nextElement();
    }
    return a;
}

public vValue Member(vDescriptor i) {
    vValue v = i.Deref();
    return t.containsKey(v) ? v : null;
}

public vValue Delete(vDescriptor i) {
    t.remove(i.Deref());
    return this;
}

public vValue Insert(vDescriptor i, vDescriptor val) {
    // "val" is unused here; it's only for tables
    vValue v = i.Deref();
    t.put(v, v);
    return this;
}

public vValue Intersect(vDescriptor x) {
    x = x.Deref();
    if (!(x instanceof vSet)) {
	iRuntime.error(120, x);
    }
    vSet rhs = (vSet) x;
    vSet result = new vSet((vValue)null);
    java.util.Enumeration<vValue> i;
    java.util.Hashtable<vValue,vValue> y = rhs.t;
    i = this.t.keys();
    while (i.hasMoreElements()) {
	vValue o = i.nextElement();
	if (y.containsKey(o)) {
	    result.t.put(o, o);
	}
    }
    return result;
}

public vValue Union(vDescriptor x) {
    x = x.Deref();
    if (!(x instanceof vSet)) {
	iRuntime.error(120, x);
    }
    vSet rhs = (vSet) x;
    vSet result = new vSet(rhs.t);
    java.util.Enumeration<vValue> i;
    i = this.t.keys();
    while (i.hasMoreElements()) {
	vValue o = i.nextElement();
	result.t.put(o, o);
    }
    return result;
}

public vValue Diff(vDescriptor x) {
    x = x.Deref();
    if (!(x instanceof vSet)) {
	iRuntime.error(120, x);
    }
    vSet rhs = (vSet)x;
    vSet result = new vSet(this.t);
    java.util.Enumeration<vValue> i;
    java.util.Hashtable<vValue,vValue> y = rhs.t;
    i = y.keys();
    while (i.hasMoreElements()) {
	Object o = i.nextElement();
	if (y.containsKey(o)) {
	    result.t.remove(o);
	}
    }
    return result;
}

} // class vSet

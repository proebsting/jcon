package rts;

import java.util.*;



public class vSet extends vStructure {

    private java.util.Hashtable t;


static int nextsn = 1;		// next serial number



// constructors

public static vSet New(vValue x)		{ return new vSet(x); }

private vSet(java.util.Hashtable x) {
    super(nextsn++);
    t = (java.util.Hashtable) x.clone();
}

private vSet(vValue x) {
    super(nextsn++);
    t = new java.util.Hashtable();
    if (x != null && !x.isNull()) {
	if (!(x instanceof vList)) {
	    iRuntime.error(108, x);
	}
	vList list = (vList) x;
	java.util.Enumeration i = list.elements();
	while (i.hasMoreElements()) {
	    vDescriptor v = (vDescriptor) i.nextElement();
	    v = v.deref();
	    t.put(v, v);
	}
    }
}



static vString typestring = vString.New("set");
vString type()		{ return typestring;}
int rank()		{ return 100; }		// sets rank after lists

vInteger Size()		{ return vInteger.New(t.size()); }

vValue Copy()		{ return new vSet(this.t); }



vDescriptor Select() {
    if (t.size() == 0) {
	return null;
    }
    int index = (int) k$random.choose(t.size());
    java.util.Enumeration e = t.keys();
    for (int k = 0; k < index; k++) {
	e.nextElement();
    }
    return (vDescriptor)e.nextElement();
}

vDescriptor Bang(iClosure c) {
    if (c.PC == 1) {
	vValue a[] = new vValue[t.size()];
	int i = 0;
	java.util.Enumeration e = (java.util.Enumeration) t.keys();
	while (e.hasMoreElements()) {
	    a[i++] = (vValue) e.nextElement();
	}
	c.o = a;
    }
    int i = c.PC++ - 1;
    vValue a[] = (vValue[]) c.o;
    if (i < a.length) {
	return a[i];
    } else {
	return null;
    }
}

vValue Sort(int i) {					// sort(L)
    return vList.New(iUtil.sort(this.mkArray()));
}

vValue[] mkArray() {
    vValue a[] = new vValue[t.size()];
    int i = 0;
    for (Enumeration e = t.keys(); e.hasMoreElements(); ) {
	a[i++] = (vValue)e.nextElement();
    }
    return a;
}

vValue Member(vDescriptor i) {
    return t.containsKey(i) ? (vValue) i : null;
}

vValue Delete(vDescriptor i) {
    t.remove(i);
    return this;
}

vValue Insert(vDescriptor i, vDescriptor val) {
    t.put(i, i);
    return this;
}

vValue Intersect(vDescriptor x) {
    if (!(x instanceof vSet)) {
	iRuntime.error(120, x);
    }
    vSet rhs = (vSet) x;
    vSet result = new vSet((vValue)null);
    java.util.Enumeration i;
    java.util.Hashtable y = rhs.t;
    i = this.t.keys();
    while (i.hasMoreElements()) {
	Object o = i.nextElement();
	if (y.containsKey(o)) {
	    result.t.put(o, o);
	}
    }
    return result;
}

vValue Union(vDescriptor x) {
    if (!(x instanceof vSet)) {
	iRuntime.error(120, x);
    }
    vSet rhs = (vSet) x;
    vSet result = new vSet(rhs.t);
    java.util.Enumeration i;
    i = this.t.keys();
    while (i.hasMoreElements()) {
	Object o = i.nextElement();
	result.t.put(o, o);
    }
    return result;
}

vValue Diff(vDescriptor x) {
    if (!(x instanceof vSet)) {
	iRuntime.error(120, x);
    }
    vSet rhs = (vSet)x;
    vSet result = new vSet(this.t);
    java.util.Enumeration i;
    java.util.Hashtable y = rhs.t;
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

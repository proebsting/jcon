class vSet extends vValue {

    java.util.Hashtable t;

vSet(java.util.Hashtable x) {
    t = (java.util.Hashtable) x.clone();
}

vSet(vValue x) {
    t = new java.util.Hashtable();
    if (x != null && !(x instanceof vNull)) {
        if (!(x instanceof vList)) {
	    iRuntime.error(108, x);
	}
	vList list = (vList) x;
	java.util.Enumeration i = list.elements();
        while ( i.hasMoreElements() ) {
	    vDescriptor v = (vDescriptor) i.nextElement();
	    v = v.deref();
	    t.put(v, v);
	}
    }
}

String image()		{ return "set()"; }

String report()		{ return image(); }

String type()		{ return "set";}

vInteger Size() {
    return iNew.Integer(t.size());
}

vDescriptor Select() {
    if (t.size() == 0) {
        return null;
    }
    int index = (int) iRuntime.random(t.size());
    java.util.Enumeration e = t.keys();
    for (int k = 0; k < index; k++) {
        e.nextElement();
    }
    return (vDescriptor)e.nextElement();
}

vDescriptor Bang(iClosure c) {
    if (c.o == null) {
        c.o = t.keys();
    }
    java.util.Enumeration e = (java.util.Enumeration) c.o;
    return e.hasMoreElements() ? (vDescriptor) e.nextElement() : null;
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
        iRuntime.error(108, x);
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
        iRuntime.error(108, x);
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
        iRuntime.error(108, x);
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

} // class vTable

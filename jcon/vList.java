//  vList -- an Icon list
//
//  java.util.vectors are used to implement lists.
//  push(), pop(), and get() presumably take O(N) time where N is list size.

package rts;

import java.util.*;



public final class vList extends vStructure {

    public Vector v;	// #%#% MS JVM cannot handle this being private.

static int nextsn = 1;				// next serial number



// constructors

public static vList New(int n, vValue x)	{ return new vList(n, x); }
public static vList New(vDescriptor[] elements)	{ return new vList(elements); }
public static vList New(Vector v)		{ return new vList(v); }

private vList(int n, vValue x) {		// new Vlist(n, x)
    super(nextsn++);
    v = new Vector(n);
    for (int i = 0; i < n; i++) {
	v.addElement(new vListVar(this, x));
    }
}

private vList(vDescriptor[] elements) {		// new Vlist(elements[])
    super(nextsn++);
    v = new Vector(elements.length);
    for (int i = 0; i < elements.length; i++) {
	v.addElement(new vListVar(this, elements[i].Deref()));
    }
}

private vList(Vector v) {			// new Vlist(Vector v)
    super(nextsn++);
    this.v = (Vector) v.clone();
}



// runtime primitives



static vString typestring = vString.New("list");
public vString Type()	{ return typestring; }

int rank()		{ return 90; }		// lists sort after procedures



vString report() {				// image for display() & tracebk
    StringBuffer b = new StringBuffer(64);
    vString s;
    int n = v.size();

    b.append("list_").append(this.snum).append(" = [");
    for (int i = 0; i < n; i++) {
	vValue e = ((vListVar) v.elementAt(i)).Deref();
	if (e instanceof vList && ((vList)e).v.size() > 0) {
	    s = e.image();	// don't expand non-empty list recursively
	} else {
	    s = e.report();	// report (NOT image) all others, esp. string
	}
	b.append(s.toString());
	if (i == 2 && n > 6) {
	   b.append(",...,");		// elide center if >6 elements
	   i = n - 4;
	} else if (i < (n - 1)) {
	   b.append(',');
	}
    }
    return vString.New(b.append(']').toString());
}



//  L.posEq(n) -- return positive equivalent of position n in list L,
//		  or zero if out of bounds

int posEq(long n) {
    long len = v.size();
    if (n <= 0) {
	n += len + 1;
    }
    if (n > 0 && n <= len + 1) {
	return (int)n;
    } else {
	return 0;
    }
}



// elements() is used when creating vSets and for the binary "!" operator.
// Elements must be generated in order.

java.util.Enumeration elements() {
    return new vListEnumeration(this.v);
}



//  operations

public vInteger Size()	{				//  *L
    return vInteger.New(v.size());
}

public vValue Copy() {					// copy(L)
    return vList.New(this.v);
}



public vList Push(vDescriptor x) {			// push(L, x)
    v.insertElementAt(new vListVar(this, x.Deref()), 0);
    return this;
}

public vValue Pull() {					// pull(L)
    if (v.size() == 0) {
	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.lastElement();
    v.removeElementAt(v.size()-1);
    return x.Deref();
}

public vValue Pop() {					// pop(L)
    if (v.size() == 0) {
	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.firstElement();
    v.removeElementAt(0);
    return x.Deref();
}

public vValue Get() {					// get(L)
    if (v.size() == 0) {
	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.firstElement();
    v.removeElementAt(0);
    return x.Deref();
}

public vList Put(vDescriptor x) {			// put(L, x)
    v.addElement(new vListVar(this, x.Deref()));
    return this;
}



public vDescriptor Index(vDescriptor i) {		//  L[i]
    int m = this.posEq(i.mkInteger().value);
    if (m == 0 || m > v.size()) {
	return null; /*FAIL*/
    }
    return (vDescriptor) v.elementAt(m - 1);		// return as variable
}



public vDescriptor Section(vDescriptor i, vDescriptor j) {	//  L[i:j]
    int m = this.posEq(i.mkInteger().value);
    int n = this.posEq(j.mkInteger().value);
    if (m == 0 || n == 0) {
	return null; /*FAIL*/
    }
    if (m > n) {
	int t = m;
	m = n;
	n = t;
    }
    vDescriptor a[] = new vDescriptor[n-m];
    for (int k = 0; k < a.length; k++) {
	a[k] = (vDescriptor) v.elementAt(k + m - 1);
    }
    return vList.New(a);
}



public vDescriptor Select() {				//  ?L
    if (v.size() == 0) {
	return null; /*FAIL*/
    }
    return (vDescriptor) v.elementAt((int)k$random.choose(v.size()));
							// return as variable
}



public vDescriptor Bang() {				//  !L
    if (v.size() == 0) {
	return null; /*FAIL*/  // empty list
    }
    return new vClosure() {
	int i = 0;
	{ retval = (vDescriptor) v.elementAt(0); }

	public vDescriptor Resume() {
	    if (++i >= v.size()) {
		return null; /*FAIL*/
	    }
	    return (vDescriptor) v.elementAt(i);
	}
    };
}

public vList ListConcat(vDescriptor v) {		// L1 ||| L2
    v = v.Deref();
    if (!(v instanceof vList)) {
	iRuntime.error(108, v);
    }
    vList result = new vList(this.v);
    for (int i = 0; i < ((vList)v).v.size(); i++) {
	result.v.addElement(((vList)v).v.elementAt(i));
    }
    return result;
}

vValue[] mkArray(int errno) {		// make array for sort(L) or p ! L
    vValue a[] = new vValue[v.size()];
    for (int i = 0; i < a.length; i++) {
	a[i] = ((vDescriptor)v.elementAt(i)).Deref();
    }
    return a;
}

} // class vList



final class vListEnumeration implements java.util.Enumeration {
    java.util.Vector v;
    int i;

    vListEnumeration(java.util.Vector v) {
	this.v = v;
	this.i = 0;
    }

    public boolean hasMoreElements() {
	return i < v.size();
    }

    public Object nextElement() {
	return v.elementAt(i++);
    }
}



final class vListVar extends vSimpleVar {
    vList parent;

    vListVar(vList parent, vValue value) {
	super("N/A", value);
	this.parent = parent;
    }

    public vString Name() {
	java.util.Enumeration e = parent.elements();
	int i = 1;
	while (e.hasMoreElements()) {
	    if (this == e.nextElement()) {
		return vString.New("L[" + i + "]");
	    }
	    i++;
	}
	return null;
    }
}

//  vList -- an Icon list
//
//  java.util.vectors are used to implement lists.
//  push(), pop(), and get() take O(N) time where N is list size.

package rts;

import java.util.*;

public class vList extends vStructure {

    private Vector v;


static int nextsn = 1;				// next serial number


vList(int n, vValue x) {			// new Vlist(n, x)
    super(nextsn++);
    v = new Vector(n);
    for (int i = 0; i < n; i++) {
    	v.addElement(new vListVar(this, x));
    }
}

vList(vDescriptor[] elements) {			// new Vlist(elements[])
    super(nextsn++);
    v = new Vector(elements.length);
    for (int i = 0; i < elements.length; i++) {
    	v.addElement(new vListVar(this, elements[i].deref()));
    }
}

vList(Vector v) {				// new Vlist(Vector v)
    super(nextsn++);
    this.v = (Vector) v.clone();
}



// runtime primitives


vString report()	{ return this.image(); } //#%#% redo with elem details

static vString typestring = iNew.String("list");
vString type()		{ return typestring; }
int rank()		{ return 90; }		// lists sort after procedures




//  L.posEq(n) -- return positive equivalent of position n in list L,
//		  or zero if out of bounds

int posEq(long n)
{
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
int intsize() {
    return v.size();
}



//  operations

vInteger Size()	{					//  *L
    return iNew.Integer(v.size());
}

vValue Copy() {						// copy(L)
    return iNew.List(this.v);
}



vValue Push(vDescriptor x) {				// push(L, x)
    v.insertElementAt(new vListVar(this, x.deref()), 0);
    return this;
}

vValue Pull() {						// pull(L)
    if (v.size() == 0) {
    	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.lastElement();
    v.removeElementAt(v.size()-1);
    return x.deref();
}

vValue Pop() {						// pop(L)
    if (v.size() == 0) {
    	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.firstElement();
    v.removeElementAt(0);
    return x.deref();
}

vValue Get() {						// get(L)
    if (v.size() == 0) {
    	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.firstElement();
    v.removeElementAt(0);
    return x.deref();
}

vValue Put(vDescriptor x) {				// put(L, x)
    v.addElement(new vListVar(this, x.deref()));
    return this;
}



vDescriptor Index(vValue i) {				//  L[i]
    int m = this.posEq(i.mkInteger().value);
    if (m == 0 || m > v.size()) {
    	return null; /*FAIL*/
    }
    return (vDescriptor) v.elementAt(m - 1);		// return as variable
}



vDescriptor Section(vValue i, vValue j) {		//  L[i:j]
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
    return iNew.List(a);
}



vDescriptor Select() {					//  ?L
    if (v.size() == 0) {
	return null; /*FAIL*/
    }
    return (vDescriptor) v.elementAt((int)k$random.choose(v.size()));
    							// return as variable
}



vDescriptor Bang(iClosure c) {				//  !L
    if (c.PC == 1) {
	c.o = c;
	c.oint = 0;
	c.PC = 2;
    } else {
	c.oint++;
    }
    if (c.oint >= v.size()) {
	return null; /*FAIL*/
    } else {
	return (vDescriptor) v.elementAt(c.oint);	// generate as variable
    }
}

vValue ListConcat(vDescriptor v) {			// L1 ||| L2
    v = v.deref();
    if (!(v instanceof vList)) {
	iRuntime.error(108, v);
    }
    vList result = new vList(this.v);
    for (int i = 0; i < ((vList)v).v.size(); i++) {
	result.v.addElement(((vList)v).v.elementAt(i));
    }
    return result;
}

vValue Sort(int i) {					// sort(L)
    return iNew.List(iUtil.sort(this.mkArray()));
}

vValue[] mkArray() {
    vValue a[] = new vValue[v.size()];
    for (int i = 0; i < a.length; i++) {
    	a[i] = ((vDescriptor)v.elementAt(i)).deref();
    }
    return a;
}

vDescriptor[] mkArgs() {
	vDescriptor[] arglist = new vDescriptor[v.size()];
	java.util.Enumeration e = this.elements();
	int i = 0;
	while (e.hasMoreElements()) {
		arglist[i] = (vDescriptor) e.nextElement();
		i++;
	}
	return arglist;
}


} // class vList



class vListEnumeration implements java.util.Enumeration {
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



class vListVar extends vSimpleVar {
	vList parent;

	vListVar(vList parent, vValue value) {
		super("N/A", value);
		this.parent = parent;
	}

	vString Name() {
		java.util.Enumeration e = parent.elements();
		int i = 1;
		while (e.hasMoreElements()) {
			if (this == e.nextElement()) {
				return iNew.String("L[" + i + "]");
			}
			i++;
		}
		// %#%##% can this happen?
		return null;
	}
}

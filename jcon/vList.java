//  vList -- an Icon list
//
//  java.util.vectors are used to implement lists.
//  The *end* of the vector is the *front* of a list,
//  so that push() and pop() are relatively quick.
//  (put and pull will be slow for long lists, at least with JDK 1.0.2).

package rts;

import java.util.*;



public class vList extends vStructure {

    Vector v;



static int nextsn = 1;				// next serial number



vList(int n, vValue x) {			// new Vlist(n, x)
    super(nextsn++);
    v = new Vector(n);
    for (int i = 0; i < n; i++) {
    	v.addElement(iNew.SimpleVar("N/A", x));
    }
}

vList(vDescriptor[] elements) {			// new Vlist(elements[])
    super(nextsn++);
    v = new Vector(elements.length);
    for (int i = elements.length - 1; i >= 0; i--) {	// add back-to-front
    	v.addElement(iNew.SimpleVar("N/A", elements[i]));
    }
}

vList(Vector v) {				// new Vlist(Vector v)
    super(nextsn++);
    this.v = (Vector) v.clone();
}



// runtime primitives

int rank()		{ return 90; }		// lists sort after procedures

String report()		{ return this.image(); } //#%#% redo with elem details

String type()		{ return "list"; }




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
    v.addElement(iNew.SimpleVar("N/A", x));
    return this;
}

vValue Pull() {						// pull(L)
    if (v.size() == 0) {
    	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.firstElement();
    v.removeElementAt(0);
    return x.deref();
}

vValue Pop() {						// pop(L)
    if (v.size() == 0) {
    	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.lastElement();
    v.removeElementAt(v.size()-1);
    return x.deref();
}

vValue Get() {						// get(L)
    if (v.size() == 0) {
    	return null; /*FAIL*/
    }
    vDescriptor x = (vDescriptor) v.lastElement();
    v.removeElementAt(v.size()-1);
    return x.deref();
}

vValue Put(vDescriptor x) {				// put(L, x)
    v.insertElementAt(iNew.SimpleVar("N/A", x), 0);
    return this;
}



vDescriptor Index(vValue i) {				//  L[i]
    int m = this.posEq(i.mkInteger().value);
    if (m == 0 || m > v.size()) {
    	return null; /*FAIL*/
    }
    // index from BACK end of vector
    return (vDescriptor) v.elementAt(v.size() - m);	// return as variable
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
    	a[k] = (vDescriptor) v.elementAt(v.size() - (k + m));
    }
    return iNew.List(a);
}



vDescriptor Select() {					//  ?L
    if (v.size() == 0) {
	return null; /*FAIL*/
    }
    return (vDescriptor) v.elementAt((int)iRuntime.random(v.size()));
    							// return as variable
}



vDescriptor Bang(iClosure c) {				//  !L
    int i;
    if (c.o == null) {
	c.o = new Integer(i = 1);
    } else {
	c.o = new Integer(i = ((Integer)c.o).intValue() + 1);
    }
    if (i > v.size()) {
	return null; /*FAIL*/
    } else {
	// indexing runs from BACK end of vector
	return (vDescriptor) v.elementAt(v.size() - i);	// generate as variable
    }
}



vValue Sort(vDescriptor n) {				// sort(L)
    vValue a[] = new vValue[v.size()];
    for (int i = 0; i < a.length; i++) {
    	a[i] = ((vDescriptor)v.elementAt(i)).deref();
    }
    iUtil.sort(a);
    return iNew.List(a);
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
		this.i = 1;
	}

	public boolean hasMoreElements() {
		return i <= v.size();
	}

	public Object nextElement() {
		i++;
		return v.elementAt(v.size() - i + 1);
	}
}

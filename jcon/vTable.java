package rts;

import java.util.*;

public class vTable extends vStructure {

    java.util.Hashtable t;
    vValue dflt;

static int nextsn = 1;	// next serial number


vTable(vValue dflt) {
    super(nextsn++);
    this.dflt = dflt;
    t = new java.util.Hashtable();
}

vTable(vTable x) {
    super(nextsn++);
    this.dflt = x.dflt;
    this.t = (java.util.Hashtable) x.t.clone();
}



String type()		{ return "table";}

int rank()		{ return 110; }		// tables rank after sets

vInteger Size() {
    return iNew.Integer(t.size());
}

vValue Copy() {						// copy(T)
    return new vTable(this);
}

vDescriptor Index(vValue i) {
    return new vTrappedTable(this, i);
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
    return new vTrappedTable(this, (vDescriptor)e.nextElement());
}

vDescriptor Bang(iClosure c) {
    if (c.o == null) {
        c.o = t.keys();
    }
    java.util.Enumeration e = (java.util.Enumeration) c.o;
    return e.hasMoreElements() ? 
	new vTrappedTable(this, (vDescriptor) e.nextElement()) : null;
}

vValue Key(iClosure c) {
    if (c.o == null) {
        c.o = t.keys();
    }
    java.util.Enumeration e = (java.util.Enumeration) c.o;
    return e.hasMoreElements() ?  (vValue) e.nextElement() : null;
}

vValue Member(vDescriptor i) {
    return t.containsKey(i) ? (vValue) i : null;
}

vValue Delete(vDescriptor i) {
    t.remove(i);
    return this;
}

vValue Insert(vDescriptor i, vDescriptor val) {
    t.put(i, val);
    return this;
}

vValue Sort(vDescriptor n) {

    long flag = n.mkInteger().value;

    vTableElem a[] = new vTableElem[t.size()];	// make array of key/val pairs

    int i = 0;
    for (Enumeration e = t.keys(); e.hasMoreElements(); ) {
        vValue key = (vValue)e.nextElement();
	vValue val = (vValue)t.get(key);
	if ((flag & 1) != 0) {
	    a[i++] = new vTableElem(key, val);	// sort by key
	} else {
	    a[i++] = new vTableElem(val, key);	// sort by value
	}
    }
    iUtil.sort(a);				// sort array of pairs

    vValue b[];
    if (flag <= 2) {				// return list of lists
	b = new vValue[t.size()];
    	vValue pair[] = new vValue[2];
	for (i = 0; i < t.size(); i++) {
	    if ((flag & 1) != 0) {		// sorted by key
		pair[0] = a[i].sortkey;
		pair[1] = a[i].other;
	    } else {				// sorted by value
		pair[0] = a[i].other;
		pair[1] = a[i].sortkey;
	    }
	    b[i] = iNew.List(pair);
	}

    } else { 					// return 2x-long list
    	b = new vValue[2 * t.size()];
	int j = 0;
	for (i = 0; i < t.size(); i++) {
	    if ((flag & 1) != 0) {		// sorted by key
	    	b[j++] = a[i].sortkey;
	    	b[j++] = a[i].other;
	    } else {				// sorted by value
	    	b[j++] = a[i].other;
	    	b[j++] = a[i].sortkey;
	    }
    	}
    }

    return iNew.List(b);			// turn results into Icon list
}

} // class vTable



class vTrappedTable extends vVariable {

    vTable table;
    vDescriptor key;

String report()	{
    return
    	"(variable = " + this.table.report() + "[" + this.key.report() + "])";
}

vTrappedTable(vTable table, vDescriptor key) {
    this.table = table;
    this.key = key;
}

public vValue deref() {
    Object v = table.t.get(key);
    return (v == null) ? table.dflt : (vValue) v;
}

public vVariable Assign(vValue v) {
    table.t.put(key, v);
    return this;
}

vString Name() {
    return iNew.String("T[" + key.image() + "]");
}

} // class vTrappedTable



class vTableElem extends vValue {	// key/value pair for sorting 

    vValue sortkey;	// value used for sorting (table key or value)
    vValue other;	// the other half of the pair

    vTableElem(vValue sortkey, vValue other) {	// constructor
	this.sortkey = sortkey;
	this.other = other;
    }

    int compareTo(vValue v)	{
    	return sortkey.compareTo(((vTableElem)v).sortkey);
    }

    String image()  { return "(" + sortkey.image() + "," + other.image() + ")";}
    String type()   { return "telem"; }
    int rank()      { return -1; }		// never compared to other types

} // class vTableElem

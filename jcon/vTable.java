class vTable extends vValue {

    java.util.Hashtable t;
    vValue dflt;

vTable(vValue dflt) {
    this.dflt = dflt;
    t = new java.util.Hashtable();
}

String image()		{ return "table()"; }

String report()		{ return image(); }

String type()		{ return "table";}

vInteger Size() {
    return iNew.Integer(t.size());
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

vValue deref() {
    Object v = table.t.get(key);
    return (v == null) ? table.dflt : (vValue) v;
}

vVariable Assign(vValue v) {
    table.t.put(key, v);
    return this;
}

} // class vTrappedTable

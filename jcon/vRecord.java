package rts;

public class vRecord extends vStructure {

    String name;
    String[] fieldnames;
    vSimpleVar[] values;



vRecord(String name, int snum, String[] fieldnames, vDescriptor[] inits) {
    super(snum);
    this.name = name;
    this.fieldnames = fieldnames;
    values = new vSimpleVar[fieldnames.length];
    for (int i = 0; i < fieldnames.length; i++) {
	values[i] = iNew.SimpleVar();
    }
    int max = fieldnames.length;
    if (max > inits.length) {
	max = inits.length;
    }
    for (int i = 0; i < max; i++) {
	values[i].Assign((vValue)inits[i]);
    }
}



String type()	{ return name; }

String image()	{ return "record " +name +"_" +snum +"(" +values.length +")"; }

vInteger Size()	{ return iNew.Integer(values.length); }

int rank()	{ return 120; }			// records sort last

int compareTo(vValue v) {
    vRecord r = (vRecord)v;
    int d = this.name.compareTo(r.name);
    if (d == 0) {
    	d = this.snum - r.snum;
    }
    return d;
}


vVariable field(String s) {
    for (int i = 0; i < fieldnames.length; i++) {
	if (s.equals(fieldnames[i])) {
	    return values[i];
	}
    }
    iRuntime.error(207);
    return null;
}


vDescriptor Select() {
    if (values.length == 0) {
	return null; /*FAIL*/
    }
    int i = (int) iRuntime.random(values.length);
    return values[i];
}


vDescriptor Bang(iClosure c) {
    int i;
    if (c.o == null) {
	c.o = new Integer(i = 0);
    } else {
	c.o = new Integer(i = ((Integer)c.o).intValue() + 1);
    }
    if (i >= values.length) {
	return null; /*FAIL*/
    } else {
	return values[i];
    }
}


vValue Sort(vDescriptor n) {
    vValue a[] = new vValue[values.length];
    for (int i = 0; i < values.length; i++) {
	a[i] = values[i].deref();
    }
    iUtil.sort(a);
    return iNew.List(a);
}


} // class vRecord



class iRecordClosure extends iFunctionClosure {

    String name;
    int snum;
    String[] fieldnames;

iRecordClosure(String name, int snum, String[] fieldnames,
		vDescriptor[] args, iClosure parent) {
    super();
    this.name = name;
    this.snum = snum;
    this.parent = parent;
    this.fieldnames = fieldnames;
    arguments = args;
}

vDescriptor function(vDescriptor[] args) {
    return new vRecord(name, snum, fieldnames, args);
}

} // class iRecordClosure

package rts;

public class vRecord extends vStructure {

    private vRecordProc constr;		// constructor
    private vSimpleVar[] values;	// values



vRecord(vRecordProc constr, vDescriptor[] inits) {
    super(constr.nextsn++);
    this.constr = constr;
    values = new vSimpleVar[constr.fieldnames.length];
    for (int i = 0; i < values.length; i++) {
	if (i < inits.length) {
	    values[i] = vSimpleVar.New(constr.varnames[i], inits[i].deref());
	} else {
	    values[i] = vSimpleVar.New(constr.varnames[i]);
	}
    }
}



vString type()	{ return constr.name; }

vString image() {
    return vString.New(
	"record " + constr.name + "_" + snum + "(" + values.length + ")");
}

vInteger Size()	{ return vInteger.New(values.length); }

int rank()	{ return 120; }			// records sort last

int compareTo(vValue v) {
    vRecord r = (vRecord)v;
    if (this.constr != r.constr) {
	return this.constr.name.compareTo(r.constr.name);
    } else {
	return this.snum - r.snum;
    }
}

vValue Copy() {						// copy(R)
    return new vRecord(constr, values);
}


vVariable field(String s) {
    int i = constr.find(s);
    if (i >= 0) {
	return values[i];
    } else {
	iRuntime.error(207);
	return null;
    }
}

vDescriptor Index(vValue i) {
    try {
	long m = i.mkInteger().value;
	if (m <= 0) {
	    m += constr.fieldnames.length + 1;
	}
	if (m < 1 || m > constr.fieldnames.length) {
	    return null; /* FAIL */
	}
	return values[(int)m-1];
    } catch (iError e) {
    }
    try {
	int k = constr.find(i.mkString().toString());
	if (k >= 0) {
	    return values[k];
	}
    } catch (iError e) {
    }
    return null;
}


vDescriptor Select() {
    if (values.length == 0) {
	return null; /*FAIL*/
    }
    int i = (int) k$random.choose(values.length);
    return values[i];
}


vDescriptor Bang(iClosure c) {
    int i;
    if (c.PC == 1) {
	c.o = new Integer(i = 0);
	c.PC = 2;
    } else {
	c.o = new Integer(i = ((Integer)c.o).intValue() + 1);
    }
    if (i >= values.length) {
	return null; /*FAIL*/
    } else {
	return values[i];
    }
}


vValue Sort(int i) {					// sort(L)
    return vList.New(iUtil.sort(this.mkArray()));
}

vValue[] mkArray() {
    vValue a[] = new vValue[values.length];
    for (int i = 0; i < values.length; i++) {
	a[i] = values[i].deref();
    }
    return a;
}

vDescriptor[] mkArgs() {
    return this.mkArray();
}

} // class vRecord

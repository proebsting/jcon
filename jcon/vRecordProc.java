package rts;



public final class vRecordProc extends vProcV {
    vString name;		// name of record Type
    String[] fieldnames;	// names of fields
    String[] varnames;		// variable names
    int nextsn = 1;		// next serial number



// constructors

public static vRecordProc New(String name, String[] fields) {
    return new vRecordProc(name, fields);
}

private vRecordProc(String name, String[] fieldnames) {
    this.name = vString.New(name);
    this.args = fieldnames.length;
    this.img = this.name.surround("record constructor ", "");
    this.fieldnames = fieldnames;
    this.varnames = new String[fieldnames.length];
    for (int i = 0; i < varnames.length; i++) {
	varnames[i] = name + "." + fieldnames[i];
    }
}



// methods

vProc mkProc(int i) {
    return this;
}

public vDescriptor Call(vDescriptor[] v) {
    return new vRecord(this ,v);
}

int find(String s) {
    for (int i = 0; i < fieldnames.length; i++) {
	if (s == fieldnames[i]) {
	    return i;
	}
    }
    // The following exists for indexing records with constructed
    // names.  It is the rare case.
    for (int i = 0; i < fieldnames.length; i++) {
	if (s.equals(fieldnames[i])) {
	    return i;
	}
    }
    return -1;
}



} // class vRecordProc

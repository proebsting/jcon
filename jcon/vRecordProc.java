package rts;



public class vRecordProc extends vValue {
	String name;		// name of record type
	vString vname;		// name of record type, as vString
	String[] fieldnames;	// names of fields
	int nextsn = 1;		// next serial number

	vRecordProc(String name, String[] fieldnames) {
		this.name = name;
		this.vname = iNew.String(name);
		this.fieldnames = fieldnames;
	}

	public iClosure instantiate(vDescriptor[] args, iClosure parent) {
	    return new iRecordClosure(this, args, parent);
	}

	vValue getproc()	{ return this; }

	String image()	{ return "record constructor " + name; }

	static vString typestring = iNew.String("procedure");
	vString type()	{ return typestring; }
	int rank()	{ return 80; }	// record constructors sort with procs

	vInteger Args()	{ return iNew.Integer(fieldnames.length); }

	int compareTo(vValue v) {	// must handle procs, funcs, rec constrs
		String s1 = this.image();
		String s2 = v.image();
		s1 = s1.substring(s1.lastIndexOf(' ') + 1);
		s2 = s2.substring(s2.lastIndexOf(' ') + 1);
		return s1.compareTo(s2);
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
}



class iRecordClosure extends iValueClosure {

    vRecordProc constr;

iRecordClosure(vRecordProc constr, vDescriptor[] args, iClosure parent) {
    super();
    init();
    this.constr = constr;
    this.parent = parent;
    arguments = args;
}

vDescriptor function(vDescriptor[] args) {
    return new vRecord(constr, args);
}

} // class iRecordClosure

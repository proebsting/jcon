package rts;



public class vRecordProc extends vValue {
	String name;
	String[] fieldnames;
	int nextsn = 1;		// next serial number

	vRecordProc(String name, String[] fieldnames) {
		this.name = name;
		this.fieldnames = fieldnames;
	}

	public iClosure instantiate(vDescriptor[] args, iClosure parent) {
	    return new iRecordClosure(this, args, parent);
	}

	String image()	{ return "record constructor " + name; }

	String type()	{ return "procedure"; }

	int rank()	{ return 80; }	// record constructors sort with procs

	int compareTo(vValue v) {	// must handle procs, funcs, rec constrs
		String s1 = this.image();
		String s2 = v.image();
		s1 = s1.substring(s1.lastIndexOf(' ') + 1);
		s2 = s2.substring(s2.lastIndexOf(' ') + 1);
		return s1.compareTo(s2);
	}

	int find(String s) {
		for (int i = 0; i < fieldnames.length; i++) {
			if (s.equals(fieldnames[i])) {
				return i;
			}
		}
		return -1;
	}
}



class iRecordClosure extends iFunctionClosure {

    vRecordProc constr;

iRecordClosure(vRecordProc constr, vDescriptor[] args, iClosure parent) {
    super();
    this.constr = constr;
    this.parent = parent;
    arguments = args;
}

vDescriptor function(vDescriptor[] args) {
    return new vRecord(constr, args);
}

} // class iRecordClosure
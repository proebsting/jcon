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
	    return new iRecordClosure(name, nextsn++, fieldnames, args, parent);
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
}

package rts;

public class vRecordProc extends vValue {
	String name;
	String[] fieldnames;

	vRecordProc(String name, String[] fieldnames) {
		this.name = name;
		this.fieldnames = fieldnames;
	}

	public iClosure instantiate(vDescriptor[] args, iClosure parent) {
		return new iRecordClosure(name, fieldnames, args, parent);
	}

	String image() {
		return "record constructor " + name;
	}

	String type() {
		return "procedure";
	}
}

class vRecordProc extends vValue {
	String name;
	String[] fieldnames;

	vRecordProc(String name, String[] fieldnames) {
		this.name = name;
		this.fieldnames = fieldnames;
	}

	iClosure instantiate(vDescriptor[] args) {
		return new iRecordClosure(name, fieldnames, args);
	}

	String image() {
		return "record constructor " + name;
	}
}

class vRecord extends vValue {
	String name;
	String[] fieldnames;
	vSimpleVar[] values;

	vRecord(String name, String[] fieldnames, vDescriptor[] inits) {
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

	vVariable field(String s) {
		for (int i = 0; i < fieldnames.length; i++) {
			if (s.equals(fieldnames[i])) {
				return values[i];
			}
		}
		iRuntime.error(207);
		return null;
	}

	String image() {
		return "record " + name + "()";
	}
}

class iRecordClosure extends iFunctionClosure {
	String name;
	String[] fieldnames;

	iRecordClosure(String name, String[] fieldnames, vDescriptor[] args) {
		super();
		this.name = name;
		this.fieldnames = fieldnames;
		arguments = args;
	}

	vDescriptor function(vDescriptor[] args) {
		return new vRecord(name, fieldnames, args);
	}
}

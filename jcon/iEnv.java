import java.util.Hashtable;

class iEnv {
	Hashtable symtab;
	Hashtable keytab;
	Hashtable proctab;

	vCoexp cur_coexp;

	iEnv() {
		symtab = new Hashtable();
		keytab = new Hashtable();
		proctab = new Hashtable();
	}

	vDescriptor resolve(String s) {
		vDescriptor v = (vDescriptor) symtab.get(s);
		return v;
	}

	void declareGlobal(String s, vDescriptor x) {
		symtab.put(s, x);
	}

	vDescriptor resolveKey(String s) {
		vDescriptor v = (vDescriptor) keytab.get(s);
		if (v == null) {
			v = iNew.Null();
		}
		return v;
	}

	void declareKey(String s, vDescriptor k) {
		keytab.put(s, k);
	}

	vDescriptor resolveProc(String s) {
		vDescriptor v = (vDescriptor) proctab.get(s);
		if (v == null) {
			v = iNew.Null();
		}
		return v;
	}

	void declareProc(String s, vDescriptor k) {
		proctab.put(s, k);
	}
}

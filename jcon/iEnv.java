package rts;

import java.util.Hashtable;

public class iEnv {
	static Hashtable symtab = new Hashtable();
	static Hashtable keytab = new Hashtable();
	static Hashtable proctab = new Hashtable();

	public static vCoexp main;
	public static vCoexp cur_coexp;

	public static vDescriptor resolve(String s) {
		vDescriptor v = (vDescriptor) symtab.get(s);
		return v;
	}

	public static void declareGlobal(String s, vDescriptor x) {
		symtab.put(s, x);
	}

	public static vDescriptor resolveKey(String s) {
		vDescriptor v = (vDescriptor) keytab.get(s);
		if (v == null) {
			v = iNew.Null();
			System.err.println(
				"warning: keyword not found: &" + s);
		}
		return v;
	}

	public static void declareKey(String s, vDescriptor k) {
		keytab.put(s, k);
	}

	public static vDescriptor resolveProc(String s) {
		vDescriptor v = (vDescriptor) proctab.get(s);
		if (v == null) {
			v = iNew.Null();
			//#%#%#% temp for better diagnosis of missing oprs:
			char c = s.charAt(0);
			if (c != '_' && ! Character.isLetter(c)) {
				System.err.println(
					"warning: operation not found: " + s);
			}
		}
		return v;
	}

	public static void declareProc(String s, vDescriptor k) {
		proctab.put(s, k);
	}
}

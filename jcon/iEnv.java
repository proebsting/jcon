package rts;

import java.util.Hashtable;

public class iEnv {
	static Hashtable symtab = new Hashtable();
	static Hashtable keytab = new Hashtable();
	static Hashtable[] proctab = new Hashtable[3];
	static {
		proctab[0] = new Hashtable();
		proctab[1] = new Hashtable();
		proctab[2] = new Hashtable();
	}

	public static vCoexp main;
	public static vCoexp cur_coexp;

	public static vVariable resolve(String s) {
		vVariable v = (vVariable) symtab.get(s);
		return v;
	}

	public static void declareGlobal(String s, vVariable x) {
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

	public static vDescriptor resolveProc(String s, int args) {
		if (args < 1 || args > 3) {
			iRuntime.error(901);
		}
		vDescriptor v = (vDescriptor) proctab[args-1].get(s);
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

	public static void declareProc(String s, int args, vDescriptor k) {
		proctab[args-1].put(s, k);
	}
}

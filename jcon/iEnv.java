package rts;

import java.util.Hashtable;

public class iEnv {
	static boolean invokeAll = false;
	static Hashtable invoke = new Hashtable();
	static Hashtable symtab = new Hashtable();
	static Hashtable keytab = new Hashtable();
	static Hashtable builtintab = new Hashtable();
	static Hashtable[] proctab = new Hashtable[3];
	static {
		proctab[0] = new Hashtable();
		proctab[1] = new Hashtable();
		proctab[2] = new Hashtable();
	}

	public static vCoexp main;
	public static vCoexp cur_coexp;

	public static void declareInvoke(String s) {
		invoke.put(s,s);
	}

	public static void declareInvokeAll() {
		invokeAll = true;
	}

	public static void undeclared(String s) {
		invoke.put(s,s);
	}

	public static vVariable resolve(String s) {
		vVariable v = (vVariable) symtab.get(s);
		return v;
	}

	static void global(String s, vVariable x) {
		if (invokeAll || invoke.containsKey(s)) {
			symtab.put(s, x);
		}
	}

	public static void declareGlobal(String s) {
		if (!symtab.containsKey(s)) {
			global(s, iNew.SimpleVar(s));
		}
	}

	public static void declareGlobalInit(String s, vVariable x) {
		if (symtab.containsKey(s)) {
			if (!(((vVariable)symtab.get(s)).deref() instanceof vNull)) {
				System.err.println("\"" + s + "\": inconsistent redeclaration");
				System.exit(1);
			}
		}
		global(s,x);
	}

	public static void declareProcedure(String name, String classname, int arity) {
		try {
			declareGlobalInit(name, iNew.SimpleVar(name, iNew.Proc(Class.forName(classname), arity)));
		} catch (ClassNotFoundException e) {
			System.err.println(name + ": procedure not found");
			System.exit(1);
		}
	}

	public static void declareRecord(String name, String[] fields) {
		declareGlobalInit(name, iNew.SimpleVar(name, iNew.RecordProc(name, fields)));
	}

	public static vValue resolveBuiltin(String s) {
		vValue v = (vValue) builtintab.get(s);
		return v;
	}

	public static void declareBuiltin(String s, vValue x) {
		builtintab.put(s, x);
		if (!symtab.containsKey(s)) {
			iEnv.declareGlobalInit(s, iNew.SimpleVar(s, x));	// %#%##% invocables affect ?
		}
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

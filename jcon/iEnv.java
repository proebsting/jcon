package rts;

import java.util.Hashtable;

public class iEnv {



static boolean invokeAll = false;
static boolean error_conversion = true;
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



public static void declareNoErrorConversion() {
    error_conversion = false;
}

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
	global(s, vSimpleVar.New(s));
    }
}

public static void declareGlobalInit(String s, vVariable x) {
    if (symtab.containsKey(s)) {
	vValue val = ((vVariable)symtab.get(s)).Deref();
	if (!val.isnull()) {
	    vDescriptor bval = (vDescriptor) builtintab.get(s);
	    if (bval == null || bval.Deref() != val) {
		System.err.println("\"" + s + "\": inconsistent redeclaration");
		System.exit(1);
	    }
	}
    }
    global(s,x);
}

public static void declareKey(String name, vProc p) {
    p.img = vString.New("&" + name);
    p.args = 0;
    keytab.put(name, p);
}

public static void declareBuiltin(String name, vProc p, int arity) {
    boolean b;

    p.img = vString.New("function " + name);
    p.args = arity;

    switch (arity) {	// sanity check on arity
	case 0:  b = p instanceof vProc0;  break;
	case 1:  b = p instanceof vProc1;  break;
	case 2:  b = p instanceof vProc2;  break;
	case 3:  b = p instanceof vProc3;  break;
	case 4:  b = p instanceof vProc4;  break;
	case 5:  b = p instanceof vProc5;  break;
	case 6:  b = p instanceof vProc6;  break;
	case 7:  b = p instanceof vProc7;  break;
	case 8:  b = p instanceof vProc8;  break;
	case 9:  b = p instanceof vProc9;  break;
	default: b = p instanceof vProcV;  break;
    }
    if (!b) {
	System.err.println("unexpected arity: function " + name);
    }

    builtintab.put(name, p);
    if (!symtab.containsKey(name)) {
	declareGlobalInit(name, vSimpleVar.New(name, p));
    }
}

public static void declareProcedure(String name, String classname, int arity) {
    declareGlobalInit(name,
	vSimpleVar.New(name, vProc.New(classname, "procedure " + name, arity)));
}

public static void declareRecord(String name, String[] fields) {
    declareGlobalInit(name, vSimpleVar.New(name, vRecordProc.New(name,fields)));
}

public static vValue resolveBuiltin(String s) {
    vValue v = (vValue) builtintab.get(s);
    return v;
}

public static vDescriptor resolveKey(String s) {
    vDescriptor v = (vDescriptor) keytab.get(s);
    if (v == null) {
	iRuntime.bomb("keyword not found: &" + s);
    }
    return v;
}

public static vDescriptor resolveProc(String s, int args) {
    if (args < 1 || args > 3) {
	iRuntime.error(902);
    }
    vDescriptor v = (vDescriptor) proctab[args-1].get(s);
    if (v == null) {
	v = vNull.New();
	// diagnose missing operators (implementation bug):
	char c = s.charAt(0);
	if (c != '_' && ! Character.isLetter(c)) {
	    iRuntime.bomb("operation not found: " + s);
	}
    }
    return v;
}

public static void declareProc(String s, int args, vProc p) {
    p.img = vString.New("function " + s);
    proctab[args-1].put(s, p);
}



} // class iEnv

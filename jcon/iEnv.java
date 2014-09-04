package jcon;

import java.util.*;

public final class iEnv {



static boolean invokeAll = false;
static boolean debugging = false;
static Hashtable<String,String> invoke = new Hashtable<String,String>();
static Hashtable<String,vVariable> symtab = new Hashtable<String,vVariable>();

public static vCoexp cur_coexp;		// currently executing co-expression



static Object instantiate(String classname) {
    try {
	String s = iConfig.PackageName + "." + classname;
	return Class.forName(s).newInstance();
    } catch (Throwable t) {
	iRuntime.bomb("can't instantiate " + classname, t);
	return null;
    }
}

static Hashtable<String,iInstantiate> builtin_instantiator =
	new Hashtable<String,iInstantiate>();

public static vProc instantiate_builtin(String classname) {
    iInstantiate i = builtin_instantiator.get(classname);
    if (i == null) {
	return null;
    }
    return i.instantiate(classname);
}

public static void declareDebugging() {
    debugging = true;
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

public static void undeclared(String name, String proc, String file) {
    if (symtab.get(name) == null) {
	System.err.println(file + ": \"" + name +
			   "\": undeclared identifier, in procedure " + proc);
    }
}

public static vVariable resolve(String s) {
    vVariable v = symtab.get(s);
    return v;
}

static void global(String s, vVariable x) {
    if (invokeAll || invoke.containsKey(s)) {
	symtab.put(s, x);
    }
}

public static void declareGlobal(String s) {
    if (!symtab.containsKey(s)) {
	undeclared(s);			// always enter declared globals
	global(s, vSimpleVar.New(s));
    }
}

public static void declareGlobalInit(String s, vVariable x) {
    if (! symtab.containsKey(s)) {
	global(s, x);			// first definition
	return;
    }
    if (x.isnull()) {
	return;				// innocuous redeclaration
    }
    vVariable var = symtab.get(s);
    if (var instanceof vFuncVar) {	// if built-in function
	global(s, x);			// okay to replace
	return;
    }
    if (var.isnull()) {			// if not initialized before
	global(s, x);			// okay to replace
	return;
    }
    System.err.println("\"" + s + "\": inconsistent redeclaration");
    System.exit(1);
}

public static void declareProcedure(String name, vProc p, int arity) {
    p.img = vString.New("procedure " + name);
    p.args = arity;
    p.traceable = true;
    declareGlobalInit(name, vSimpleVar.New(name, p));
}

public static void declareRecord(String name, String[] fields) {
    declareGlobalInit(name, vSimpleVar.New(name, vRecordProc.New(name,fields)));
}



//  built-in functions

private static Hashtable<String,vFuncVar> builtintab =
	new Hashtable<String,vFuncVar>();

static void declareBuiltin(String name, int arity, iInstantiate i) {
    vFuncVar f = new vFuncVar(
	name, "function " + name, iConfig.FunctionPrefix + name, arity);
    builtintab.put(name, f);
    if (i != null) {
        builtin_instantiator.put(iConfig.FunctionPrefix + name, i);
    }
    if (!symtab.containsKey(name)) {
	declareGlobalInit(name, f);
    }
}

static vString[] listBuiltins() {
    vString[] v = new vString[builtintab.size()];
    Enumeration<String> e = builtintab.keys();
    for (int i = 0; e.hasMoreElements(); i++) {
	v[i] = vString.New(e.nextElement());
    }
    return v;
}

static vProc getBuiltin(String s) {
    vVariable v = (vVariable) builtintab.get(s);
    if (v == null) {
	return null;
    } else {
	return (vProc) v.Deref();
    }
}



//  keywords

private static Hashtable<String,vProc> keytab = new Hashtable<String,vProc>();

static vProc declareKey(String name, vProc p) {
    p.img = vString.New("&" + name);
    p.args = 0;
    keytab.put(name, p);
    return p;
}

static vVariable getKeyVar(String s) {
    vProc p = keytab.get(s);
    if (p == null) {
	return null;
    }
    vDescriptor d = p.Call();
    if (d instanceof vVariable) {
	return (vVariable) d;
    } else {
	return null;
    }
}



//  operators (registered for use via string invocation)

private static Object[] oname = {
    new Hashtable<vString,String>(),
    new Hashtable<vString,String>(),
    new Hashtable<vString,String>(),
};

private static Object[] oproc = {
    new Hashtable<vString,vProc>(),
    new Hashtable<vString,vProc>(),
    new Hashtable<vString,vProc>(),
};

@SuppressWarnings("unchecked")
static void declareOpr(String repr, int arity, String classname) {
    Hashtable<vString,String> nh = (Hashtable<vString,String>)oname[arity-1];
    nh.put(vString.New(repr), classname);
}

@SuppressWarnings("unchecked")
static vProc getOpr(vString repr, long arity) {
    if (arity < 1 || arity > 3) {
        return null;
    }
    int i = (int) arity - 1;
    Hashtable<vString,String> nh = (Hashtable<vString,String>)oname[i];
    Hashtable<vString,vProc> ph = (Hashtable<vString,vProc>)oproc[i];
    vProc v = ph.get(repr);			// check if already created
    if (v != null) {
	return v;				// found it
    }
    String classname = nh.get(repr);
    if (classname == null) {
	return null;				// unknown operation
    }
    ph.put(repr, v = iOperators.instantiate(classname));
    v.img = repr.surround("function ", "");
    return v;					// return new instance
}

public static vDescriptor resolveProc(String s, int args) {
    if (args < 1 || args > 3) {
	iRuntime.error(902);
    }
    vDescriptor v = (vDescriptor) getOpr(vString.New(s), args);
    if (v == null) {
	v = vNull.New();
    }
    return v;
}



} // class iEnv

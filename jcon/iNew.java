//  iNew.java -- "the factory" for producing new objects indirectly


package rts;


public class iNew {

	private static vNull TheNull = new vNull();



// for construction of Icon types

public static vNull Null() 			{ return TheNull; }

public static vInteger Integer(long x)		{ return new vInteger(x); }
public static vInteger Integer(double x)	{ return new vInteger(x); }
public static vInteger Integer(String x)	{ return new vInteger(x); }

public static vReal Real(double x) 		{ return new vReal(x); }

public static vCset Cset(String x) 		{ return new vCset(x); }
public static vCset Cset(int low, int high)	{ return new vCset(low, high); }

public static vString String(String x) 		{ return new vString(x); }
public static vString String(char c) 		{ return new vString(c); }

public static vList List(int n, vValue x)	{ return new vList(n, x); }
public static vList List(vDescriptor[] elements)
						{ return new vList(elements); }

public static vTable Table(vValue x)		{ return new vTable(x); }

public static vSet Set(vValue x)		{ return new vSet(x); }

public static vProc Proc(Class c, iEnv e)	{ return new vProc(c, e); }

public static vRecordProc RecordProc(String name, String[] fields)
					{ return new vRecordProc(name, fields);}


// for construction of internal types

public static iEnv Env()			{ return new iEnv(); }

public static vSimpleVar SimpleVar()		{ return new vSimpleVar(); }

public static vSimpleVar SimpleVar(vDescriptor x)
					{ return new vSimpleVar(x.deref()); }

public static vSubstring Substring(vVariable v, int i1, int i2)
					{ return new vSubstring(v, i1, i2); }

public static vSubstring Substring(vSubstring v, int i1, int i2)
					{ return new vSubstring(v, i1, i2); }

} // class iNew

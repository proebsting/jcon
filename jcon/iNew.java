//  iNew.java -- "the factory" for producing new objects indirectly



class iNew {

	private static vNull TheNull = new vNull();



// for construction of Icon types

static vNull Null() 			{ return TheNull; }

static vInteger Integer(long x)		{ return new vInteger(x); }
static vInteger Integer(double x)	{ return new vInteger(x); }
static vInteger Integer(String x)	{ return new vInteger(x); }

static vReal Real(double x) 		{ return new vReal(x); }

static vCset Cset(String x) 		{ return new vCset(x); }

static vString String(String x) 	{ return new vString(x); }

static vDescriptor List(int n, vValue x)	{ return new vList(n, x); }
static vDescriptor List(vDescriptor[] elements)	{ return new vList(elements); }

static vDescriptor Table(vValue x)	{ return new vTable(x); }

static vDescriptor Set(vValue x)	{ return new vSet(x); }

static vProc Proc(Class c, iEnv e)	{ return new vProc(c, e); }

static vRecordProc RecordProc(String name, String[] fields)
					{ return new vRecordProc(name, fields);}

// for construction of internal types

static iEnv Env()			{ return new iEnv(); }

static vSimpleVar SimpleVar()		{ return new vSimpleVar(); }

static vSimpleVar SimpleVar(vDescriptor x) { return new vSimpleVar(x.deref()); }

static vSubstring Substring(vVariable v, int i1, int i2)
					{ return new vSubstring(v, i1, i2); }

static vSubstring Substring(vSubstring v, int i1, int i2)
					{ return new vSubstring(v, i1, i2); }

} // class iNew

//  iNew.java -- #%#% factory under deconstruction

package rts;

import java.io.*;
import java.util.*;

public class iNew {



public static vNull Null()			{ return vNull.New(); }

public static vInteger Integer(double x)	{ return vInteger.New(x); }
public static vInteger Integer(String x)	{ return vInteger.New(x); }
public static vInteger Integer(long x)		{ return vInteger.New(x); }

public static vReal Real(double x)		{ return vReal.New(x); }
public static vReal Real(String x)		{ return vReal.New(x); }

public static vString String()			{ return vString.New(); }
public static vString String(char c)		{ return vString.New(c); }
public static vString String(String s)		{ return vString.New(s); }
public static vString String(byte[] b)		{ return vString.New(b); }
public static vString String(vString s,int i,int j) {return vString.New(s,i,j);}
public static vString String(vString s, int i, int j, vString t)
						{ return vString.New(s,i,j,t); }

public static vCset Cset(int c)			{ return vCset.New(c); }
public static vCset Cset(int low, int high)	{ return vCset.New(low, high); }
public static vCset Cset(String s)		{ return vCset.New(s); }
public static vCset Cset(vString s)		{ return vCset.New(s); }

public static vList List(int n, vValue x)	{ return vList.New(n, x); }
public static vList List(vDescriptor[] elems)	{ return vList.New(elems); }
public static vList List(Vector v)		{ return vList.New(v); }

public static vSet Set(vValue x)		{ return vSet.New(x); }

public static vTable Table(vValue x)		{ return vTable.New(x); }

public static vProc Proc(String s, String cnam, int args) {
    return vProc.New(s, cnam, args);
}

public static vRecordProc RecordProc(String name, String[] fields) {
    return vRecordProc.New(name, fields);
}

public static vSimpleVar SimpleVar(String name)
				{ return vSimpleVar.New(name); }

public static vSimpleVar SimpleVar(String name, vDescriptor x)
				{ return vSimpleVar.New(name, x.deref()); }

public static vSubstring Substring(vVariable v, int i1, int i2)
				{ return vSubstring.New(v, i1, i2); }

public static vSubstring Substring(vSubstring v, int i1, int i2)
				{ return vSubstring.New(v, i1, i2); }

public static vFile File(String kw, DataInput i, DataOutput o)
				{ return vFile.New (kw, i, o); }	

public static vFile File(String filename, String mode)
				{ return vFile.New(filename, mode, null); }


public static vDescriptor[] ArgArray(int i)  { return vDescriptor.ArgArray(i); }

public static void FreeArgs(vDescriptor[] a) { vDescriptor.FreeArgs(a); }



} // class iNew

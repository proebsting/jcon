//  iNew.java -- "the factory" for producing new objects indirectly


package rts;

import java.io.*;
import java.util.*;



public class iNew {




// for construction of Icon types

private static vNull TheNull = new vNull();
public static vNull Null() 			{ return TheNull; }	// reuse

public static vReal Real(double x) 		{ return new vReal(x); }
public static vReal Real(String x) 		{ return new vReal(x); }

public static vCset Cset(String x) 		{ return new vCset(x); }
public static vCset Cset(int low, int high)	{ return new vCset(low, high); }

public static vList List(int n, vValue x)	{ return new vList(n, x); }
public static vList List(vDescriptor[] elements){ return new vList(elements); }
public static vList List(Vector v)		{ return new vList(v); }

public static vTable Table(vValue x)		{ return new vTable(x); }

public static vSet Set(vValue x)		{ return new vSet(x); }

public static vProc Proc(String s, String classname, int args) {
	return new vProc(s, classname, args);
}

public static vRecordProc RecordProc(String name, String[] fields)
					{ return new vRecordProc(name, fields);}



//  for integers, preallocate some static values

private static vInteger intlist[] = 
    new vInteger[iConfig.MaxPrebuiltInt + 1 - iConfig.MinPrebuiltInt];

static {
    for (int i = iConfig.MinPrebuiltInt; i <= iConfig.MaxPrebuiltInt; i++) {
	intlist[i - iConfig.MinPrebuiltInt] = new vInteger(i);
    }
}

public static vInteger Integer(double x) {	// int from real
    return Integer((long) x);			//#%#% what about overflow?
}

public static vInteger Integer(String x) {	// int from string
    return Integer(Long.parseLong(x)); 		// can throw exception
}

public static vInteger Integer(long x) {
    if (x <= iConfig.MaxPrebuiltInt && x >= iConfig.MinPrebuiltInt) {
	return intlist[(int) x - iConfig.MinPrebuiltInt];
    } else {
	return new vInteger(x);
    }
}



//  for strings, preallocate empty string and one-character strings

private static vString nullstring = new vString("");
private static vString strlist[] = new vString[vCset.MAX_VALUE + 1];

static {
    for (int i = 0; i < strlist.length; i++) {
	strlist[i] = new vString((char) i + "");
    }
}

public static vString String(char c) {
    return strlist[c];
}

public static vString String(String x) {
    int n = x.length();
    if (n < 1) {
	return nullstring;
    } else if (n == 1) {
	return strlist[x.charAt(0)];
    } else {
	return new vString(x);
    }
}


//  for files, the actual type depends on the flags passed to open()

public static vFile File(String kw, InputStream i) { return new vTFile(kw, i); }
public static vFile File(String kw, PrintStream p) { return new vTFile(kw, p); }
public static vFile File(String filename, String mode) {
    try {
	if (iRuntime.upto("uU", mode)) {
	    return new vBFile(filename, mode);	// binary (untranslated) file
	} else {
	    return new vTFile(filename, mode);	// text (translated) file
	}
    } catch (IOException e) {
    	return null; /*FAIL*/
    }
}

public static vWindow Window(String name, String mode, vDescriptor args[]) {
    return vWindow.open(name, mode, args);
}



// for construction of internal types

public static vSimpleVar SimpleVar(String name)
				{ return new vSimpleVar(name); }

public static vSimpleVar SimpleVar(String name, vDescriptor x)
				{ return new vSimpleVar(name, x.deref()); }

public static vSubstring Substring(vVariable v, int i1, int i2)
				{ return new vSubstring(v, i1, i2); }

public static vSubstring Substring(vSubstring v, int i1, int i2)
				{ return new vSubstring(v, i1, i2); }

} // class iNew

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



//  for integers, cache some values

private static vInteger intlist[] = 
    new vInteger[iConfig.MaxCachedInt + 1 - iConfig.MinCachedInt];

public static vInteger Integer(double x) {	// int from real
    return Integer((long) x);			//#%#% what about overflow?
}

public static vInteger Integer(String x) {	// int from string
    return Integer(Long.parseLong(x)); 		// can throw exception
}

public static vInteger Integer(long x) {
    if (x > iConfig.MaxCachedInt || x < iConfig.MinCachedInt) {
	return new vInteger(x);
    }
    int i = (int)x - iConfig.MinCachedInt;
    vInteger v = intlist[i];
    if (v != null) {
	return v;
    }
    return intlist[i] = new vInteger(x);
}



//  for strings, preallocate empty string and one-character strings;
//  also cache strings corresponding to small integers

private static vString nullstring = new vString();
private static vString strlist[] = new vString[vCset.MAX_VALUE + 1];
private static vString intstrs[] = 
    new vString[iConfig.MaxCachedIntStr + 1 - iConfig.MinCachedIntStr];

static {
    for (int i = 0; i < strlist.length; i++) {
	strlist[i] = new vString((char) i);
    }
}

public static vString String() {
    return nullstring;
}

public static vString String(char c) {
    return strlist[c];
}

public static vString String(String s) {
    int len = s.length();
    if (len < 1) {
	return nullstring;
    } else if (len == 1) {
	return strlist[s.charAt(0)];
    } else {
	byte[] b = new byte[len];
	for (int i = 0; i < len; i++)
	    b[i] = (byte) s.charAt(i);
	return new vString(b);
    }
}

public static vString String(byte[] b) {
    int len = b.length;
    if (len < 1) {
	return nullstring;
    } else if (len == 1) {
	return strlist[b[0]];
    } else {
	return new vString(b);
    }
}

public static vString String(vString s, int i, int j) {     // s[i:j], both > 0
    int len = j - i;
    if (len == s.length()) {		// if extracting entire string
	return s;			// reuse it
    } else if (len == 0) {
	return nullstring;
    } else if (len == 1) {
	return strlist[s.charAt(i - 1)];
    } else {
	return new vString(s, i, j);
    }
}

public static vString String(vString s, int i, int j, vString t) {
    return new vString(s, i, j, t);
}

public static vString String(long x) {
    if (x > iConfig.MaxCachedIntStr || x < iConfig.MinCachedIntStr) {
	return iNew.String(Long.toString(x));
    }
    int i = (int)x - iConfig.MinCachedIntStr;
    vString v = intstrs[i];
    if (v != null) {
	return v;
    }
    return intstrs[i] = new vString(Long.toString(x).getBytes());
}



//  for csets, preallocate and reuse empty and one-character csets.

private static vCset zcset = new vCset();
private static vCset cslist[] = new vCset[vCset.MAX_VALUE];

static {
    for (int i = 0; i < cslist.length; i++) {
	cslist[i] = new vCset(i, i);
    }
}

public static vCset Cset(int c) {
    return cslist[c];
}

public static vCset Cset(int low, int high) {
    if (low == high) {
	return cslist[low];
    } else {
	return new vCset(low, high);
    }
}

public static vCset Cset(String s) {
    int len = s.length();
    if (len < 1) {
	return zcset;
    } else if (len == 1) {
	return cslist[s.charAt(0)];
    } else {
	return new vCset(s);
    }
}

public static vCset Cset(vString s) {
    int len = s.length();
    if (len < 1) {
	return zcset;
    } else if (len == 1) {
	return cslist[s.charAt(0)];
    } else {
	return new vCset(s);
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



static vDescriptor[][][] argCache = new vDescriptor[20][20][];
static int[] argCacheSP = new int[20];
public static vDescriptor[] ArgArray(int i) {
	if (i < 20 && argCacheSP[i] > 0) {
		return argCache[i][--argCacheSP[i]];
	} else {
		return new vDescriptor[i];
	}
}

public static void FreeArgs(vDescriptor[] a) {
	if (a != null) {
		int len = a.length;
		if (len < 20 && argCacheSP[len] < 20) {
			argCache[len][argCacheSP[len]++] = a;
		}
	}
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

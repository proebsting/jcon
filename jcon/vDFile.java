//  vDFile.java -- Icon directory files

package jcon;

import java.io.*;



public final class vDFile extends vFile {

    String[] entries;		// list of directory entries
    int index;			// index of next entry (-2 for ".", -1 for "..")



// for Unix we must add entries for "." and ".." 

private static String nlstring = System.getProperty("line.separator");
private static boolean isUnix = nlstring.equals("\n");	// crude but effective

private static vString dot = vString.New(".");
private static vString dotdot = vString.New("..");



//  constructor:  new vDFile(name, mode)  [mode is ignored; already checked]

vDFile(String name, String mode) throws IOException {
    super();
    entries = (new File(name)).list();
    index = isUnix ? -2 : 0;
}



//  many calls are trivial for directory files

public vFile seek(long n)	{ return null; /*FAIL*/ }
public vFile where(long n)	{ return null; /*FAIL*/ }

public void writes(vString s)	{ iRuntime.error(213, this); }
public void newline()		{ iRuntime.error(213, this); }

public vFile flush()		{ return this; }

public vFile close() {
    index = entries.length;
    openfiles.remove(this);
    return this;
}



//  read() -- return the next directory entry

public vString read() {
    if (index < -1) {
	index++;
    	return dot;
    } else if (index == -1) {
	index++;
    	return dotdot;
    } else if (index < entries.length) {
    	return vString.New(entries[index++]);
    } else {
    	return null; /*FAIL*/
    }
}



//  reads(n) -- return the next directory entry, limited to n bytes

public vString reads(long n) {
    vString s = read();
    if (s.length() <= n) {
    	return s;
    } else {
    	return vString.New(s, 1, (int)n + 1);
    }
}



} // class vDFile

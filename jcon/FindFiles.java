//  FindFiles -- generate list of .zip files needed
//
//  (A standalone utility used by the jcont script; not used at runtime.)
//
//  usage: FindFiles [-t] $IPATH file.zip ...
//
//	-t:  trace actions on stderr
//
//  FindFiles scans the named .zip files and (recursively) any others
//  referenced by "link" directives, writing the combined list to stdout.
//  A full, absolute path is written for each file.



package rts;

import java.io.*;
import java.util.*;
import java.util.zip.*;

final class FindFiles {



static String Usage = "usage: FindFiles [-t] $IPATH file.zip ...";

static char IconPathSeparator = ' ';	// allowed as alt to File.pathSeparator
static String ipath[];			// list of IPATH component directories

static boolean tracing = false;		// trace actions?
static int errorcount = 0;		// number of errors encountered

static Stack needed = new Stack();	   // names still needed
static Hashtable found = new Hashtable();  // names already processed

static Vector files = new Vector();	// list of files to link



public static void main(String a[]) {

    int nextarg = 0;

    //  process -t option

    if (a.length > nextarg && a[nextarg].equals("-t")) {
	nextarg++;
	tracing = true;
    }

    //  two or more positional arguments are required

    if (a.length < nextarg + 2) {
	System.err.println(Usage);
	System.exit(1);
    }

    //  process IPATH

    trace("IPATH: " + a[nextarg]);

    ipath = crackpath(a[nextarg++]);

    for (int i = 0; i < ipath.length; i++) {
	trace("   dir: " + ipath[i]);
    }

    //  process named files

    for (; nextarg < a.length; nextarg++) {
	addfile(a[nextarg]);
    }

    //  find needed link files
    while (! needed.empty()) {		// while more to do on list
	String s = (String) needed.pop();
	if (! found.contains(s)) {
	    found.put(s, s);		// in case of problems, try only once
	    String fname = findfile(s);	// find .zip file
	    if (fname == null) {
		error("cannot find \"" + s + "\" for linking");
	    } else {
		addfile(fname);		// add to list and scan for more links
	    }
	}
    }

    //  output the list of files

    for (int i = 0; i < files.size(); i++) {
	System.out.println((String) files.elementAt(i));
    }

    //  check for error exit

    if (errorcount > 0) {
	System.exit(1);
    }
}



//  trace(s) -- output tracing information to stderr, if enabled

static void trace(String s) {
    if (tracing) {
	System.err.println(s);
    }
}



//  error(s) -- report fatal error

static void error(String s) {
    System.err.println(s);
    errorcount++;
}



//  crackpath(s) -- break IPATH into list of directories
//
//  allows path components to be separated either by spaces
//  or by the standard pathSeparator character.

static String[] crackpath(String s) {
    Vector v = new Vector();

    s = s.replace(IconPathSeparator, File.pathSeparatorChar)
	+ File.pathSeparatorChar;

    v.addElement(System.getProperty("user.dir"));	// always first

    int i = 0;
    int j;
    while ((j = s.indexOf(File.pathSeparatorChar, i)) >= 0) {
	String dir = s.substring(i, j);
	if (dir.length() != 0 && ! dir.equals(v.lastElement())) {
	    v.addElement(dir);
	}
	i = j + 1;
    }

    String a[] = new String[v.size()];
    v.copyInto(a);
    return a;
}



//  findfile(name) -- find .zip file on IPATH

static String findfile(String name) {
    for (int i = 0; i < ipath.length; i++) {
	File f = new File(ipath[i], name + ".zip");
	if (f.canRead()) {
	    return f.getPath();
	}
    }
    return null;
}



//  addfile(fname) -- add file to list
//
//  adds the file fname to the list of files to link
//  adds the basename to the list of found names
//  scans the file for additional names to link

static void addfile(String fname) {

    // add file name to list
    trace("file: " + fname);
    files.addElement(new File(fname).getAbsolutePath());

    // add basename to found
    int i = fname.lastIndexOf(File.separatorChar) + 1;
    int j = fname.lastIndexOf('.');
    if (j == -1) {
	j = fname.length();
    }
    String s = fname.substring(i, j);
    found.put(s, s);

    // extract link commands
    readlinks(fname);
}



//  readlinks(fname) -- read links from .zip file and add to list

static void readlinks(String fname) {
    try {
	ZipFile f = new ZipFile(fname);
	ZipEntry e = f.getEntry("links");
	if (e == null) {
	    error(fname + ": linkage information missing");
	} else {
	    BufferedReader d = new BufferedReader(
		new InputStreamReader(f.getInputStream(e)));
	    String s;
	    while ((s = d.readLine()) != null) {
		trace("   links: " + s);
		needed.push(s);
	    }
	}
	f.close();
    } catch (Exception e) {
	// ignore EOFException, which happens with some Zip files
	//	(fixed in JDK 1.2b4; Sun bug #4040920)
	if (! (e instanceof EOFException)) {
	    error(fname + ": " + e);
	}
    }
}



} // class FindFiles

package rts;

import java.io.*;
import java.util.*;
import java.util.zip.*;



//  loadfunc(library, funcname)
//
//  Loads a class (which must extend rts.vProc[0-9V]) from a Zip (or Jar)
//  library and returns a procedure.  Loadfunc selects the first class
//  named either "funcname.class" or "p_l$<anything>$funcname.class".
//  For the latter case (Icon code) it invokes the Icon resolver.

final class f$loadfunc extends vProc2 {				// loadfunc(s,s)
    private Hashtable loaders = new Hashtable();

    private static final String linkhead = "l$";
    private static final String prochead = "p_l$";

    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	String filename = a.mkString().toString();
	String funcname = b.mkString().toString();
	String exact = funcname + ".class";
	String tail = "$" + funcname + ".class";
	try {
	    ZipFile zf = new ZipFile(filename);
	    ZipEntry ze;
	    String s;
	    Enumeration e = zf.entries();
	    while (true) {
		if (! e.hasMoreElements()) {
		    iRuntime.error(216, b);
		    return null;
		}
		ze = (ZipEntry) e.nextElement();
		s = ze.getName();
		if (s.equals(exact)
		|| (s.startsWith(prochead) && s.endsWith(tail))) {
		    break;
		}
	    }
	    String classname = s.substring(0, s.length() - 6);

	    DynamicLoader dl = (DynamicLoader) loaders.get(filename);
	    if (dl == null) {			// if first time for this file
		loaders.put(filename, dl = new DynamicLoader(zf));
		if (s.startsWith(prochead)) {	// if Icon procedure file
		    preload(dl, zf, s);		// init globals, resolve refs
		}
	    } else {
		zf.close();
	    }

	    Class c = dl.loadClass(classname);
	    vProc p = (vProc) c.newInstance();
	    p.img = vString.New("procedure " + funcname);

	    if (p instanceof vProcV)		p.args = -1;
	    else if (p instanceof vProc0)	p.args = 0;
	    else if (p instanceof vProc1)	p.args = 1;
	    else if (p instanceof vProc2)	p.args = 2;
	    else if (p instanceof vProc3)	p.args = 3;
	    else if (p instanceof vProc4)	p.args = 4;
	    else if (p instanceof vProc5)	p.args = 5;
	    else if (p instanceof vProc6)	p.args = 6;
	    else if (p instanceof vProc7)	p.args = 7;
	    else if (p instanceof vProc8)	p.args = 8;
	    else if (p instanceof vProc9)	p.args = 9;

	    return p;

	} catch (Throwable t) {
	    // as in Icon v9, print additional info before raising error
	    System.err.println(
		"loadfunc(\"" + filename + "\",\"" + funcname + "\"): " + t);
	    iRuntime.error(216);
	    return null;
	}
    }

    private void preload(ClassLoader dl, ZipFile zf, String s) throws Exception{
	String linkname = s.substring(2, s.lastIndexOf('$'));
	Class c = dl.loadClass(linkname);
	iFile r = (iFile) c.newInstance();
	//#%#%#%# need code mod first:	 	 r.declare();
	r.resolve();
    }
}



final class DynamicLoader extends ClassLoader {
    private ZipFile zipfile;
    private Hashtable cache = new Hashtable();

    DynamicLoader(ZipFile z) {				// constructor
	zipfile = z;	// stash zipfile
	z.getName();	// ensure non-null
    }

    public Class loadClass(String name, boolean resolve)
    throws ClassNotFoundException {
	Class c = (Class) cache.get(name);
	if (c == null) {
	    ZipEntry ze = zipfile.getEntry(name + ".class");
	    if (ze == null) {
		c = findSystemClass(name);
	    } else {
		byte[] zb = new byte [(int) ze.getSize()];
		try {
		    DataInputStream i = 
			new DataInputStream(zipfile.getInputStream(ze));
		    i.readFully(zb);
		    i.close();
		} catch (IOException e) {
		    throw new ClassNotFoundException(name);
		}
		c = defineClass(name, zb, 0, zb.length);
		if (resolve && c != null) {
		    resolveClass(c);
		}
	    }
	}
	if (c == null) {
	    throw new ClassNotFoundException(name);
	}
	return c;
    }
}

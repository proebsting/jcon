package rts;

import java.io.*;
import java.util.*;
import java.util.zip.*;



//  loadfunc(library, funcname)
//
//  Loads a class (which must extend rts.vProc[0-9V]) from a Zip (or Jar)
//  library and returns a procedure.  The class chosen is
//	1.  the first *$*$funcname.class, if any, else
//	2.  the first *$funcname.class, if any, else
//	3.  funcname.class

final class f$loadfunc extends vProc2 {				// loadfunc(s,s)
    private Hashtable loaders = new Hashtable();

    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	String filename = a.mkString().toString();
	String funcname = b.mkString().toString();
	String class1 = null;		// matching classname *$*$funcname
	String class2 = null;		// matching classname *$funcname
	String class3 = null;		// matching classname funcname
	String classtail = funcname + ".class";
	try {
	    ZipFile zf = new ZipFile(filename);
	    Enumeration e = zf.entries();
	    while (e.hasMoreElements()) {
		ZipEntry ze = (ZipEntry) e.nextElement();
		String s = ze.getName();
		if (s.endsWith(classtail)
		&& (s.length() == s.lastIndexOf('$') + 1 + classtail.length())){
		    int i1 = s.indexOf('$');
		    int i2 = s.lastIndexOf('$');
		    if (i1 != i2) {			// *$*$funcname.class
			if (class1 == null) {
			    class1 = s;
			}
		    } else if (i1 >= 0) {		// *$funcname.class
			if (class2 == null) {
			    class2 = s;
			}
		    } else {				// funcname.class
			if (class3 == null) {
			    class3 = s;
			}
		    }
		}
	    }
	    String entry = 
		(class1 != null) ? class1 : (class2 != null ? class2 : class3);
	    if (entry == null) {
		iRuntime.error(216, b);
	    }
	    String classname = entry.substring(0, entry.length() - 6);

	    DynamicLoader dl = (DynamicLoader) loaders.get(filename);
	    if (dl == null) {
		loaders.put(filename, dl = new DynamicLoader(zf));
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
		    InputStream i = zipfile.getInputStream(ze);
		    i.read(zb);
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

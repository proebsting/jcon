//  fLoad.java -- the loadfunc() function

package rts;

import java.io.*;
import java.util.*;
import java.util.zip.*;

final class fLoad extends iInstantiate {
    public static fLoad self = new fLoad();
    public vProc instantiate(String name) {
        if (name.equals("f$loadfunc")) return new f$loadfunc();
        return null;
    } // vProc instantiate(String)
}


//  loadfunc(library, funcname)
//
//  Loads a class (which must extend rts.vProc[0-9V]) from a Zip (or Jar)
//  library and returns a procedure.  Loadfunc selects the first class
//  named either "funcname.class" (assumed to be a Java procedure) or
//  "p_l$<anything>$funcname.class" (an Icon procedure).  For an Icon
//  library, all procedures are linked in and the specified one is returned.

final class f$loadfunc extends vProc2 {				// loadfunc(s,s)
    private static final String linkhead = "l$";
    private static final String prochead = "p_l$";
    private static Hashtable ilinked = new Hashtable();  // icnfiles prev linked

    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	String filename = a.mkString().toString();
	String funcname = b.mkString().toString();
	String exact = funcname + ".class";
	String tail = "$" + funcname + ".class";
	try {
	    ZipFile zf = new ZipFile(filename);
	    Enumeration e = zf.entries();
	    while (e.hasMoreElements()) {
		ZipEntry ze = (ZipEntry) e.nextElement();
		String s = ze.getName();
		if (s.startsWith(prochead) && s.endsWith(tail)) {
		    zf.close();
		    return loadIconCode(filename, funcname, s);
		} else if (s.equals(exact)) {
		    zf.close();
		    return loadJavaCode(filename, funcname);
		}
	    }
	    zf.close();
	    iRuntime.error(216, b);		// not found in library
	    return null;

	} catch (Throwable t) {
	    // as in Icon v9, print additional info before raising error
	    System.err.print(
		"loadfunc(\"" + filename + "\",\"" + funcname + "\"): " + t);
	    iRuntime.error(216);
	    return null;
	}
    }

    // loadJavaCode(filename, funcname) -- load Java function
    private static vProc loadJavaCode(
    String filename, String funcname) throws Exception {

	DynamicLoader dl = DynamicLoader.get(filename);

	Class c = dl.loadClass(funcname);
	vProc p = (vProc) c.newInstance();
	initProc(p, "function " + funcname);
	return p;
    }

    // loadIconCode(filename, procname, entryname) -- load Icon procedure
    private static vProc loadIconCode(
    String filename, String procname, String classentry) throws Exception {

	String classname = classentry.substring(0, classentry.length() - 6);
	String linkname = classname.substring(2, classname.lastIndexOf('$'));

	DynamicLoader dl = DynamicLoader.get(filename);
	Class c;

	if (ilinked.get(linkname) == null) {
	    c = dl.loadClass(linkname);		// get link director for file
	    iFile r = (iFile) c.newInstance();	// create instance
	    r.unresolved();			// create new globals 
	    iBuiltins.announce();		// init with builtins
	    r.declare();			// declare procedures
	    r.resolve();			// resolve references
	    ilinked.put(linkname, linkname);	// record as linked
	}

	c = dl.loadClass(classname);		// now find procedure class
	vProc p = (vProc) c.newInstance();	// create procedure
	initProc(p, "procedure " + procname);	// init the proc instance
	return p;				// and return it
    }

    // initProc(p, s) -- initialize p.img and p.args
    private static void initProc(vProc p, String s) {
	p.img = vString.New(s);
	if (p instanceof vProcV)	p.args = -1;
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
    }



} // class f$loadfunc



//  DynamicLoader -- simple loader for loading from a named Zip file

final class DynamicLoader extends ClassLoader {
    private ZipFile zf;					// library ZipFile
    private Hashtable classcache = new Hashtable();	// classes loaded


    // cache of known loaders
    private static Hashtable zcache = new Hashtable();

    // lookup or create loader for a particular library
    static DynamicLoader get(String libname) throws IOException {
	DynamicLoader dl = (DynamicLoader) zcache.get(libname);
	if (dl == null) {
	    zcache.put(libname, dl = new DynamicLoader(libname));
	}
	return dl;
    }


    // constructor
    DynamicLoader(String libname) throws IOException {
	zf = new ZipFile(libname);
    }


    // standard ClassLoader method
    public Class loadClass(String name, boolean resolve)
    throws ClassNotFoundException {
	Class c = (Class) classcache.get(name);
	if (c == null) {
	    ZipEntry ze = zf.getEntry(name + ".class");
	    if (ze == null) {
		c = findSystemClass(name);
	    } else {
		byte[] zb = new byte [(int) ze.getSize()];
		try {
		    DataInputStream i = 
			new DataInputStream(zf.getInputStream(ze));
		    i.readFully(zb);
		    i.close();
		} catch (IOException e) {
		    throw new ClassNotFoundException(name);
		}
		c = defineClass(name, zb, 0, zb.length);
		if (c != null) { 
		    classcache.put(name, c);
		    if (resolve) {
			resolveClass(c);
		    }
		}
	    }
	}
	if (c == null) {
	    throw new ClassNotFoundException(name);
	}
	return c;
    }

} // class DynamicLoader

package rts;

public class vProc extends vValue {

    vString img;	// image for printing
    Class proc;		// class that implements proc
    String classname;
    int args;		// number of args

    iFunctionClosure functionclosure;	// cached closure for pure funcs
    iClosure cachedclosure;		// cached closure



// constructors

public static vProc New(String s, String classname, int args) {
    return new vProc(s, classname, args);
}

private vProc(String img, String classname, int args) {
    this.img = vString.New(img);
    this.classname = classname;
    this.args = args;
}



vString image()		{ return img; }

static vString typestring = vString.New("procedure");
public vString Type()	{ return typestring; }

public vInteger Args()	{ return vInteger.New(args); }

vValue getproc()	{ return this; }



int rank()		{ return 80; }	// procedures sort after co-expressions
int compareTo(vValue v) { return compareLastWord(img, v.image()); }

//  compareLastWord(s1, s2) -- compare procedure types
//
//  compares images of procedures, functions, and record types
//  (which all sort together) based on the last word of each
//  argument, which should be the name.
static int compareLastWord(vString s1, vString s2) {
    int len1 = s1.length();
    int len2 = s2.length();
    int i1 = 0;
    int i2 = 0;
    for (int j = 0; j < len1; j++) {
	if (s1.charAt(j) == ' ') {
	    i1 = j + 1;
	}
    }
    for (int j = 0; j < len2; j++) {
	if (s2.charAt(j) == ' ') {
	    i2 = j + 1;
	}
    }
    while (i1 < len1 && i2 < len2) {
	int d = s1.charAt(i1++) - s2.charAt(i2++);
	if (d != 0) {
	    return d;
	}
    }
    return (len1 - i1) - (len2 - i2);
}



public vDescriptor ProcessArgs(vDescriptor x) {
    // unnecessary Deref() on following line
    // prevents sun.tools.java.CompilerError from JDK 1.1.6 (SGI)
    final vProc p = (vProc) this.Deref();
    final vDescriptor[] a = x.mkArray(126);
    return new vClosure() {
	iClosure func = iInterface.Instantiate(p, a, null);
			//#%#% no parent for Instantiate?
	public vDescriptor resume() {
	    this.retval = func.nextval();
	    if (retval == null) {
		return null;
	    } else {
		return this;
	    }
	}
    }.resume();
}



iClosure getClosure() {
    iClosure c = null;

    if (functionclosure != null) {
	return functionclosure;
    }
    if (cachedclosure != null) {
	iClosure tmp = cachedclosure;
	cachedclosure = null;
	return tmp;
    }
    try {
	try {
	    c = (iClosure) proc.newInstance();
	} catch (NullPointerException e) {
	    try {
		this.proc = Class.forName(classname);
	    } catch (ClassNotFoundException e1) {
		iRuntime.bomb("cannot load " + img);
	    }
	    c = (iClosure) proc.newInstance();
	    if (c instanceof iFunctionClosure) {
		functionclosure = (iFunctionClosure) c;
	    }
	}
    } catch (InstantiationException e) {
	iRuntime.bomb(e);
    } catch (IllegalAccessException e) {
	iRuntime.bomb(e);
    }
    c.vproc = this;
    return c;
}

public iClosure instantiate(vDescriptor[] args, iClosure parent) {
    iClosure c = getClosure();
    c.closure(args, parent);
    return c;
}

public iClosure instantiate(
	vDescriptor arg0, vDescriptor arg1, vDescriptor arg2, iClosure parent) {
    iClosure c = getClosure();
    c.closure(arg0, arg1, arg2, parent);
    return c;
}

public iClosure instantiate(vDescriptor arg0,vDescriptor arg1,iClosure parent) {
    iClosure c = getClosure();
    c.closure(arg0, arg1, parent);
    return c;
}

public iClosure instantiate(vDescriptor arg0, iClosure parent) {
    iClosure c = getClosure();
    c.closure(arg0, parent);
    return c;
}



} // class vProc

//  vProc is the parent class of all Icon and built-in procedures.
//  Every procedure extends one of these abstract subclasses:
//
//	vProc0	for procedures having no parameters
//	vProc1	for procedures having one parameter
//	  ...
//	vProc9	for procedures having nine parameters
//	vProcV	for varargs procedures and procedures with >9 fixed parameters



package rts;

public abstract class vProc extends vValue {

    vString img;	// image for printing
    int args;		// number of args



vString image()   { return (img == null) ? vString.New(this.toString()) : img; }

static vString typestring = vString.New("procedure");
public vString Type()		{ return typestring; }

public vInteger Args()		{ return vInteger.New(args); }

vProc mkProc(int i)		{ return this; }



//  New(classname, image, nargs) -- create vProc and initialize

static vProc New(String classname, String img, int nargs) {
    vProc p;

    try {
	p = (vProc) Class.forName(classname).newInstance();
    } catch (Exception e) {
	iRuntime.bomb("can't create instance of vProc class " + classname);
	p = null;
    }
    p.img = vString.New(img);
    p.args = nargs;
    return p;
}



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
    return this.Call(x.mkArray(126));
}



} // class vProc

//  iKeywords.java -- Icon keywords

//  This file implements most of the Icon keywords.  However,
//  three keywords are implemented entirely in the compiler:
//	&file
//	&line


package rts;

import java.io.*;
import java.text.*;
import java.util.*;



public class iKeywords extends iFile {

    void announce() {
	vCset lcase = vCset.New('a', 'z');
	vCset ucase = vCset.New('A', 'Z');

	declare("allocated");
	declare("ascii", vCset.New(0, 127));
	declare("clock");
	declare("col");
	declare("collections");
	declare("control");
	declare("cset", vCset.New(0, vCset.MAX_VALUE));
	declare("current");
	declare("date");
	declare("dateline");
	declare("digits", vCset.New('0', '9'));
	declare("dump");
	declare("e", vReal.New(Math.E));
	declare("error");
	declare("errornumber");
	declare("errortext");
	declare("errorvalue");
	declare("errout");
	declare("fail");
	declare("features");
	declare("host");
	declare("input");
	declare("interval");
	declare("lcase", lcase);
	declare("ldrag", vInteger.New(wEvent.LDrag));
	declare("letters", lcase.Union(ucase));
	declare("level", vInteger.New(0));	//#%#%
	declare("lpress", vInteger.New(wEvent.LPress));
	declare("lrelease", vInteger.New(wEvent.LRelease));
	declare("main");
	declare("mdrag", vInteger.New(wEvent.MDrag));
	declare("meta");
	declare("mpress", vInteger.New(wEvent.MPress));
	declare("mrelease", vInteger.New(wEvent.MRelease));
	declare("null", vNull.New());
	declare("output");
	declare("phi", vReal.New((1.0 + Math.sqrt(5.0)) / 2.0));
	declare("pi", vReal.New(Math.PI));
	declare("pos");
	declare("progname");
	declare("random");
	declare("rdrag", vInteger.New(wEvent.RDrag));
	declare("regions");
	declare("resize", vInteger.New(wEvent.Resize));
	declare("row");
	declare("rpress", vInteger.New(wEvent.RPress));
	declare("rrelease", vInteger.New(wEvent.RRelease));
	declare("shift");
	declare("source");
	declare("storage");
	declare("subject");
	declare("time");
	declare("trace");
	declare("ucase", ucase);
	declare("version", vString.New(iConfig.Version));
	declare("window");
	declare("x");
	declare("y");
    }

    static void declare(String name) {
	iEnv.declareKey(name, iConfig.KeywordPrefix + name);
    }

    static void declare(String name, vValue v) {
	iEnv.declareKey(name, new kConstant(name, v));
    }
}



//  constant-valued keywords (those initialized above) are kConstant instances

class kConstant extends vProc0 {
    vValue value;

    kConstant(String name, vValue v) {			// constructor
	img = vString.New("k$" + name);
	args = 0;
	value = v;
    }

    public vDescriptor Call()	{ return value; }	// reference
}



//  simple read-only keywords are assigned by rts calls to k$keyword.self.set().
//  assigning a Java null makes the keyword fail.

abstract class kReadOnly extends vProc0 {
    vValue value;			// may be null for failure
    public void set(vValue v)		{ value = v; }
    public vDescriptor Call()		{ return value; }
}

class k$progname extends kReadOnly {				// &progname
    static k$progname self;
    k$progname()		{ self = this; }
}

class k$errornumber extends kReadOnly {				// &errornumber
    static k$errornumber self;
    k$errornumber()		{ self = this; }
}

class k$errortext extends kReadOnly {				// &errortext
    static k$errortext self;
    k$errortext()		{ self = this; }
}

class k$errorvalue extends kReadOnly {				// &errorvalue
    static k$errorvalue self;
    k$errorvalue()		{ self = this; }
}

class k$control extends kReadOnly {				// &control
    static k$control self;
    k$control()			{ self = this; }
}

class k$meta extends kReadOnly {				// &meta
    static k$meta self;
    k$meta()			{ self = this; }
}

class k$shift extends kReadOnly {				// &shift
    static k$shift self;
    k$shift()			{ self = this; }
}

class k$interval extends kReadOnly {				// &interval
    static k$interval self;
    k$interval()		{ self = this; }
}



//  Decrementing counters (&trace, &error, &dump) are subclasses of kCounter.
//  Integer values may be assigned.
//  k$kwname.self.check() succeeds, and decrements, if the counter is nonzero.

abstract class kCounter extends vProc0 {
    private long count = 0;
    private vSimpleVar kwvar;

    kCounter(String name) {	// constructor

	kwvar = new vSimpleVar(name, vInteger.New(0)) {

	    public vVariable Assign(vDescriptor v) {
		count = v.mkInteger().value;
		return this;
	    }

	    public vValue Deref() {
		return vInteger.New(count);
	    }
	};
    }

    public vDescriptor Call() {
	return kwvar;		// returns assignable vSimpleVar
    }

    public boolean check() {
	if (count == 0) {
	    return false; 
	} else {
	    count--;
	    return true;
	}
    }
}

class k$trace extends kCounter {
    static k$trace self;
    k$trace()		{ super("&trace"); self = this; }
}

class k$error extends kCounter {
    static k$error self;
    k$error()		{ super("&error"); self = this; }
}

class k$dump extends kCounter {
    static k$dump self;
    k$dump()		{ super("&dump"); self = this; }
}



class k$fail extends vProc0 {					// &fail
    public vDescriptor Call() {
	return null; /*FAIL*/
    }
}



class k$features extends vProc0 {				// &features

    //  The features list is hard-wired.
    //  (And it's not completely clear what we should report.)
    private static vString[] flist = {
	vString.New("UNIX"),
	vString.New("Java"), 
	vString.New("ASCII"),
	vString.New("co-expressions"),
	vString.New("environment variables"),
	vString.New("pipes"),
	vString.New("system function"),
    };

    public vDescriptor Call() {
	return new vClosure() {
	    int posn = 0;
	    public vDescriptor Resume() {
		if (posn < flist.length) {
		    return flist[posn++];
		} else {
		    return null;
		}
	    }
	}.Resume();
    }
}



class k$current extends vProc0 {				// &current
    public vDescriptor Call() {
	return iEnv.cur_coexp;
    }
}

class k$main extends vProc0 {					// &main
    public vDescriptor Call() {
	return iEnv.main;
    }
}

class k$source extends vProc0 {					// &source
    public vDescriptor Call() {
	if (iEnv.cur_coexp.callers.empty()) {
	    return iEnv.main;
	}
	return (vCoexp) iEnv.cur_coexp.callers.peek();
    }
}



class k$clock extends vProc0 {					// &clock

    static SimpleDateFormat formatter;

    public vDescriptor Call() {
	if (formatter == null) {
	    formatter = new SimpleDateFormat("HH:mm:ss");
	    formatter.setTimeZone(TimeZone.getDefault());
	}
	Date d = new Date();
	String s = formatter.format(d);
	return vString.New(s);
    }
}



class k$date extends vProc0 {					// &date

    static SimpleDateFormat formatter;

    public vDescriptor Call() {
	if (formatter == null) {
	    formatter = new SimpleDateFormat("yyyy/MM/dd");
	    formatter.setTimeZone(TimeZone.getDefault());
	}
	Date d = new Date();
	String s = formatter.format(d);
	return vString.New(s);
    }
}



class k$time extends vProc0 {					// &time

    private static long tbase;

    public static void reset() {		// reset to zero
	tbase = System.currentTimeMillis();
    }

    public vDescriptor Call() {			// read value
	return vInteger.New(System.currentTimeMillis() - tbase);
    }
}



class k$dateline extends vProc0 {				// &dateline

    static SimpleDateFormat formatter;

    public vDescriptor Call() {
	if (formatter == null) {
	    formatter =
	    new SimpleDateFormat("EEEEEE, MMMM d, yyyy  h:mm aa");
	    formatter.setTimeZone(TimeZone.getDefault());
	}
	Date d = new Date();
	String s = formatter.format(d);
	int n = s.length() - 2;		// beginning of AM/PM
	s = s.substring(0, n) + s.substring(n).toLowerCase();
	return vString.New(s);
    }
}



class k$host extends vProc0 {					// &host

    private static vString hostname;

    public vDescriptor Call() {
	if (hostname != null) {
	    return hostname;
	}
	// warning: ugly unixisms follow
	try {
	    Process p = Runtime.getRuntime().exec("uname -n");
	    hostname = vString.New(
		new BufferedReader(
		new InputStreamReader(p.getInputStream()))
		.readLine().trim());
	    p.destroy();
	    hostname.charAt(0);		// ensure not empty
	} catch (Exception e1) {
	    try {
		hostname = vString.New(System.getProperty("os.name"));
	    } catch (Exception e2) {
		hostname = vString.New("Jcon");
	    }
	}
	return hostname;
    }

}



class k$input extends vProc0 {					// &input

    static vFile file =		// referenced externally
	vFile.New("&input",
	    new DataInputStream(new BufferedInputStream(System.in)), null);
	    //#%#% why is this buffered if we're piling our own buffering atop?

    public vDescriptor Call() {
	return file;
    }
}



class k$output extends vProc0 {					// &output

    static vFile file =		// referenced externally
	vFile.New("&output", null,
	    new DataOutputStream(new BufferedOutputStream(System.out)));

    public vDescriptor Call() {
	return file;
    }
}



class k$errout extends vProc0 {					// &errout

    static vFile file = 		// referenced externally
	vFile.New("&errout", null, new DataOutputStream(System.err));

    public vDescriptor Call() {
	return file;
    }
}



class k$subject extends vProc0 {				// &subject

    static vSimpleVar self = new vSimpleVar("&subject", vString.New()) {
	public vVariable Assign(vDescriptor v) {
	    value = v.mkString();			// &subject := s
	    k$pos.set(1);				// &pos := 1
	    return this;
	}
    };

    public vDescriptor Call() {
	return self;
    }
}



class k$pos extends vProc0 {				// &pos

    static vSimpleVar self = new vSimpleVar("&pos", vInteger.New(1)) {
	public vVariable Assign(vDescriptor v) {
	    vInteger p = v.mkInteger();
	    int n = ((vString)k$subject.self.value).posEq(p.value);
	    if (n > 0) {	// if valid
		value = p;
		return this;
	    } else {
		return null; /*FAIL*/
	    }
	}
    };

    public vDescriptor Call() {
	return self;
    }

    public static void set(long i) {	// assign value known to be valid
	self.value = vInteger.New(i);
    }
}




//  this random number generator is compatible with Icon v9
//  see Icon Analyst 38 (October, 1996) for an extensive analysis

class k$random extends vProc0 {				// &random

    private static long randval;			// current value

    private static final long RandA = 1103515245;
    private static final long RandC = 453816694;
    private static final double RanScale = 4.65661286e-10;

    private static vSimpleVar vrandom = new vSimpleVar("&random") {

	public vVariable Assign(vDescriptor v) {	// assign
	    randval = v.mkInteger().value;
	    return this;
	}

	public vValue Deref() {			// dereference
	    return vInteger.New(randval);
	}
    };

    public static double nextVal() {		// gen val in [0.0, 1.0)
	randval = (RandA * randval + RandC) & 0x7fffffff;
	return RanScale * randval;
    }

    public static long choose(long limit) {	// gen val in [0, limit)
	return (long) (limit * nextVal());
    }

    public vDescriptor Call() {
	return vrandom;
    }
}



//  allocation keywords just generate three or four zero values

class kZeroes extends vClosure {
    static vInteger zero = vInteger.New(0);

    int count;			// remaining count

    kZeroes(int n) {		// constructor
	retval = zero;
	count = n - 1;
    }

    public vDescriptor Resume() {
	if (count-- > 0) {
	    return retval; // another integer zero
	} else {
	    return null; /*FAIL*/
	}
    }
}

class k$allocated extends vProc0 {			// &allocated
    public vDescriptor Call() {
	return new kZeroes(4);
    }
}

class k$collections extends vProc0 {			// &collections
    public vDescriptor Call() {
	return new kZeroes(4);
    }
}

class k$regions extends vProc0 {			// &regions
    public vDescriptor Call() {
	return new kZeroes(3);
    }
}

class k$storage extends vProc0 {			// &storage
    public vDescriptor Call() {
	return new kZeroes(3);
    }
}



class k$window extends vProc0 {				// &window

    private static vSimpleVar kwindow = new vSimpleVar("&window") {

	public vVariable Assign(vDescriptor v) {	// assign
	    vValue w = v.Deref();
	    if ((w instanceof vWindow) || w.isnull()) {
		value = w;
		vWindow.setCurrent((vWindow) value);
		return this;
	    } else {
		iRuntime.error(140, w);
		return null;
	    }
	}

    };

    public static vWindow getWindow() {		// get &window, must be !null
	if (kwindow.isnull()) {
	    iRuntime.error(140);
	} 
	return (vWindow) kwindow.value.Deref();
    }

    public vDescriptor Call() {
	return kwindow;
    }
}



abstract class kMirrored extends vProc0 {	// super for "mirrored" int kwds

    private vSimpleVar kwvar;			// trapped variable

    kMirrored(String name) {			// constructor 
	kwvar = new vSimpleVar(name, vInteger.New(0)) {
	    public vVariable Assign(vDescriptor v) {
		vInteger i = v.mkInteger();	// must be integer
		newValue(i.value);		// call subclass (may give err)
		value = i;			// if no error, set value
		return this;
	    }
	};
    }

    abstract void newValue(long v);		// propagate side-effects

    void set(long v) {				// assign without side-effects
	kwvar.value = vInteger.New(v);
    }

    public vDescriptor Call() {
	return kwvar;
    }
}

class k$x extends kMirrored {				// &x
    static kMirrored self;
    k$x()			{ super("&x"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	k$col.self.set(1 + i / 12);		// #%#% should depend on font
    }
}

class k$y extends kMirrored {				// &y
    static kMirrored self;
    k$y()			{ super("&y"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	k$row.self.set(1 + i / 7);		// #%#% should depend on font
    }
}

class k$row extends kMirrored {				// &row
    static kMirrored self;
    k$row()			{ super("&row"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	k$y.self.set(12 * i);			// #%#% should depend on font
    }
}

class k$col extends kMirrored {				// &col
    static kMirrored self;
    k$col()			{ super("&col"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	k$x.self.set(7 * i);			// #%#% should depend on font
    }
}

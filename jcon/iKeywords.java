//  iKeywords.java -- Icon keywords

//  This file implements most of the Icon keywords.  However,
//  three keywords are implemented entirely in the compiler:
//	&fail
//	&file
//	&line
//
//  Also note that "functional" keywords (those that extend iValueClosure,
//  usually because they can fail) must be registered in ../tran/key.icn.

//  #%#%#% To do: unify methods for global references to keyword values



package rts;

import java.io.*;
import java.text.*;
import java.util.*;



public class iKeywords extends iFile {

    void announce() {

	// constants
	iEnv.declareKey("null", vNull.New());
	iEnv.declareKey("e", vReal.New(Math.E));
	iEnv.declareKey("phi", vReal.New((1.0 + Math.sqrt(5.0)) / 2.0));
	iEnv.declareKey("pi", vReal.New(Math.PI));
	iEnv.declareKey("version", vString.New(iConfig.Version));

	// cset constants
	vCset lcase, ucase;
	iEnv.declareKey("digits", vCset.New('0', '9'));
	iEnv.declareKey("lcase", lcase = vCset.New('a','z'));
	iEnv.declareKey("ucase", ucase = vCset.New('A','Z'));
	iEnv.declareKey("letters", lcase.Union(ucase));
	iEnv.declareKey("ascii", vCset.New(0, 127));
	iEnv.declareKey("cset", vCset.New(0, vCset.MAX_VALUE));

	// read-only, but variable
	iEnv.declareKey("clock", new k$clock());
	iEnv.declareKey("current", new k$current());
	iEnv.declareKey("date", new k$date());
	iEnv.declareKey("dateline", new k$dateline());
	iEnv.declareKey("host", new k$host());
	iEnv.declareKey("main", new k$main());
	iEnv.declareKey("progname", new k$progname());
	iEnv.declareKey("source", new k$source());
	iEnv.declareKey("time", new k$time());

	// files
	iEnv.declareKey("input",  new k$input());
	iEnv.declareKey("output", new k$output());
	iEnv.declareKey("errout", new k$errout());

	//incestuous
	k$subject s = new k$subject();		// &subject
	k$pos p = new k$pos();			// &pos
	s.pos = p;
	p.subject = s;
	s.Assign(vString.New());
	iEnv.declareKey("subject", s);
	iEnv.declareKey("pos", p);

	// error-related
	iEnv.declareKey("error", new k$error());
	iEnv.declareKey("errornumber", vProc.New(
	    "&errornumber", "rts.k$errornumber", 0));
	iEnv.declareKey("errortext", vProc.New(
	    "&errortext", "rts.k$errortext", 0));
	iEnv.declareKey("errorvalue", vProc.New(
	    "&errorvalue", "rts.k$errorvalue", 0));

	iEnv.declareKey("dump", new k$dump());

	// generators
	iEnv.declareKey("features", vProc.New("&features","rts.k$features",0));
	iEnv.declareKey("level", vProc.New("&level", "rts.k$level", 0));

	// bogus generators
	vProc proc = vProc.New("k$gen4zeroes", "rts.k$gen4zeros", 0);
	iEnv.declareKey("allocated", proc);
	iEnv.declareKey("collections", proc);
	proc = vProc.New("k$gen3zeroes", "rts.k$gen3zeros", 0);
	iEnv.declareKey("regions", proc);
	iEnv.declareKey("storage", proc);

	// special behavior
	iEnv.declareKey("trace", new k$trace());
	iEnv.declareKey("random", new k$random());

	// graphics constants
	iEnv.declareKey("lpress", vInteger.New(wEvent.LPress));
	iEnv.declareKey("mpress", vInteger.New(wEvent.MPress));
	iEnv.declareKey("rpress", vInteger.New(wEvent.RPress));
	iEnv.declareKey("ldrag", vInteger.New(wEvent.LDrag));
	iEnv.declareKey("mdrag", vInteger.New(wEvent.MDrag));
	iEnv.declareKey("rdrag", vInteger.New(wEvent.RDrag));
	iEnv.declareKey("lrelease", vInteger.New(wEvent.LRelease));
	iEnv.declareKey("mrelease", vInteger.New(wEvent.MRelease));
	iEnv.declareKey("rrelease", vInteger.New(wEvent.RRelease));
	iEnv.declareKey("resize", vInteger.New(wEvent.Resize));

	// graphics variables
	iEnv.declareKey("window", new k$window());	// &window
	iEnv.declareKey("x", new k$x());		// &x
	iEnv.declareKey("y", new k$y());		// &y
	iEnv.declareKey("row", new k$row());		// &row
	iEnv.declareKey("col", new k$col());		// &col
	iEnv.declareKey("interval", new k$interval());	// &interval
	iEnv.declareKey("control", vProc.New("&control", "rts.k$control", 0));
	iEnv.declareKey("meta", vProc.New("&meta", "rts.k$meta", 0));
	iEnv.declareKey("shift", vProc.New("&shift", "rts.k$shift", 0));
    }
}



class k$features extends iClosure {				// &features

    //#%#%  The features list is hard-wired for now.
    //#%#%  It's not completely clear what we should report.

    static String[] flist = {
	"Java", "ASCII", "co-expressions",
	"environment variables", "pipes", "system function" };

    int posn = 0;

    public vDescriptor nextval() {
	if (PC == 1) {
	    posn = 0;
	    PC = 2;
	}
	if (posn < flist.length) {
	    return vString.New(flist[posn++]);
	} else {
	    return null;
	}
    }

    String tfmt() { return "&features"; }
}



abstract class k$Value extends vIndirect {	// super of read-only keywords

    public abstract vValue deref();		// must implement deref()

    public vVariable Assign(vValue x)
	{ iRuntime.error(111, this.deref()); return null;}

    vString Name()	{ iRuntime.error(111, this.deref()); return null; }
}



class k$current extends k$Value {				// &current

    public vValue deref() {
	return iEnv.cur_coexp;
    }
}

class k$main extends k$Value {					// &main

    public vValue deref() {
	return iEnv.main;
    }
}

class k$source extends k$Value {				// &source

    public vValue deref() {
	if (iEnv.cur_coexp.callers.empty()) {
	    return iEnv.main;
	}
	return (vCoexp) iEnv.cur_coexp.callers.peek();
    }
}

class k$clock extends k$Value {					// &clock

    static SimpleDateFormat formatter;

    public vValue deref() {
	if (formatter == null) {
	    formatter = new SimpleDateFormat("HH:mm:ss");
	    formatter.setTimeZone(TimeZone.getDefault());
	}
	Date d = new Date();
	String s = formatter.format(d);
	return vString.New(s);
    }
}



class k$date extends k$Value {					// &date

    static SimpleDateFormat formatter;

    public vValue deref() {
	if (formatter == null) {
	    formatter = new SimpleDateFormat("yyyy/MM/dd");
	    formatter.setTimeZone(TimeZone.getDefault());
	}
	Date d = new Date();
	String s = formatter.format(d);
	return vString.New(s);
    }
}



class k$time extends k$Value {					// &time

    private static long tbase;

    public static void reset() {		// reset to zero
	tbase = System.currentTimeMillis();
    }

    public vValue deref() {			// read value
	return vInteger.New(System.currentTimeMillis() - tbase);
    }
}



class k$dateline extends k$Value {				// &dateline

    static SimpleDateFormat formatter;

    public vValue deref() {
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



class k$host extends k$Value {					// &host

    static vString hostname;

    public vValue deref() {
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



class k$progname extends k$Value {				// &progname
    static String name;

    public vValue deref() {
	return vString.New(name);
    }
}



class k$input extends k$Value {					// &input
    static vFile file;		// referenced externally

    k$input() {
	file = vFile.New("&input",
	    new DataInputStream(new BufferedInputStream(System.in)), null);
    }

    public vValue deref()	{ return file; }
}

class k$output extends k$Value {				// &output
    static vFile file;		// referenced externally

    k$output() {
	file = vFile.New("&output", null,
	    new DataOutputStream(new BufferedOutputStream(System.out)));
    }

    public vValue deref()	{ return file; }
}

class k$errout extends k$Value {				// &errout
    static vFile file;		// referenced externally

    k$errout() {		// unbuffered
	file = vFile.New("&errout", null, new DataOutputStream(System.err));
    }

    public vValue deref()	{ return file; }
}



class k$subject extends vSimpleVar {				// &subject

    k$pos pos;		// associated &pos variable
    static k$subject self;

    k$subject() { super("&subject"); self = this; }

    public vVariable Assign(vValue s) {
	value = s.mkString();			// &subject := s
	pos.SafeAssign(vInteger.New(1));	// &pos := 1
	return this;
    }
}


class k$pos extends vSimpleVar {				// &pos

    k$subject subject;		// associated &subject variable
    static k$pos self;

    k$pos() { super("&pos"); self = this; }

    public vVariable Assign(vValue i) {
	i = i.mkInteger();
	int n = ((vString)subject.value).posEq(((vInteger)i).value);
	if (n == 0)
	    return null;	// fail: position out of range
	value = vInteger.New(n);
	return this;
    }
    public vVariable SafeAssign(vValue i) {
	value = i;
	return this;
    }
}



class k$trace extends vSimpleVar {				// &trace

    static long trace;		// referenced in iClosure

    k$trace() { super("&trace"); }

    public vVariable Assign(vValue i) {
	value = i.mkInteger();
	trace = ((vInteger)value).value;
	return this;
    }

    public vValue deref() {
	return value = vInteger.New(trace);
    }
}




//  this random number generator is compatible with Icon v9
//  see Icon Analyst 38 (October, 1996) for an extensive analysis

class k$random extends vSimpleVar {				// &random

private static long randval;			// current value

private static final long RandA = 1103515245;
private static final long RandC = 453816694;
private static final double RanScale = 4.65661286e-10;

    k$random() { super("&random"); }		// constructor

    public vVariable Assign(vValue i) {		// assign
	value = i.mkInteger();
	randval = ((vInteger)value).value;
	return this;
    }

    public vValue deref() {			// dereference
	return value = vInteger.New(randval);
    }

    public static double nextVal() {		// gen val in [0.0, 1.0)
	randval = (RandA * randval + RandC) & 0x7fffffff;
	return RanScale * randval;
    }

    public static long choose(long limit) {	// gen val in [0, limit)
	return (long) (limit * nextVal());
    }
}



class k$window extends vSimpleVar {				// &window

    private static vValue value = vNull.New();	// current value

    k$window() { super("&window"); }		// constructor

    public vVariable Assign(vValue w) {		// assign
	if ((w instanceof vWindow) || w.isNull()) {
	    value = w;
	    vWindow.setCurrent((vWindow) value);
	    return this;
	} else {
	    iRuntime.error(140, w);
	    return null;
	}
    }

    public vValue deref() {			// dereference
	return value;
    }

    public static vWindow getWindow() {		// get &window, must be !null
	if (! (value instanceof vWindow)) {
	    iRuntime.error(140);
	}
	return (vWindow) value;
    }
}

class k$control extends iValueClosure {				// &control
    static vValue value;			// null value produces failure
    vDescriptor function(vDescriptor args[]) { return value; }
}

class k$meta extends iValueClosure {				// &meta
    static vValue value;			// null value produces failure
    vDescriptor function(vDescriptor args[]) { return value; }
}

class k$shift extends iValueClosure {				// &shift
    static vValue value;			// null value produces failure
    vDescriptor function(vDescriptor args[]) { return value; }
}



abstract class k$intWatcher extends vVariable { // super for "watched" int kwds
    String name;
    long value;
    abstract void newValue(long i);		// subclass must implement this

    k$intWatcher(String name) {			// constructor just saves name
	this.name = name;
    }

    public vValue deref() {
	return vInteger.New(value);		// deref just returns value
    }

    public vVariable Assign(vValue v) {		// assignment
	long i = v.mkInteger().value;		// must be integer
	newValue(i);				// call subclass (may give err)
	value = i;				// if no error, set value
	return this;
    }

    public void set(long i)	{		// set without side effects
	value = i;
    }

    vString Name()				{ return vString.New(name); }

    vString report()	{ return vString.New("(" + name + " = " + value + ")");}
}

class k$interval extends k$intWatcher {				// &interval
    static k$interval self;
    k$interval()		{ super("k$interval"); self = this; }
    void newValue(long i) {}		// no side effects
}

class k$x extends k$intWatcher {				// &x
    static k$x self;
    k$x()			{ super("k$x"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	k$col.self.set(1 + i / 12);	// #%#% should depend on font
    }
}

class k$y extends k$intWatcher {				// &y
    static k$y self;
    k$y()			{ super("k$y"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	k$row.self.set(1 + i / 7);	// #%#% should depend on font
    }
}

class k$row extends k$intWatcher {				// &row
    static k$row self;
    k$row()			{ super("k$row"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	k$y.self.set(12 * i);		// #%#% should depend on font
    }
}

class k$col extends k$intWatcher {				// &col
    static k$col self;
    k$col()			{ super("k$col"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	k$x.self.set(7 * i);		// #%#% should depend on font
    }
}



class k$dump extends vSimpleVar {				// &dump

    public static long dump;	// referenced externally

    k$dump() { super("&dump"); }

    public vVariable Assign(vValue i) {
	value = i.mkInteger();
	dump = ((vInteger)value).value;
	return this;
    }

    public vValue deref() {
	return value = vInteger.New(dump);
    }
}

class k$error extends vSimpleVar {				// &trace

    static long error;		// referenced in iClosure

    k$error() { super("&error"); }

    public vVariable Assign(vValue i) {
	value = i.mkInteger();
	error = ((vInteger)value).value;
	if (error != 0 & !iEnv.error_conversion) {
	    error = 0;
	    value = vInteger.New(error);
	    iRuntime.error(904,this);
	}
	return this;
    }

    public vValue deref() {
	return value = vInteger.New(error);
    }
}

class k$errornumber extends iValueClosure {
    static vInteger number;

    vDescriptor function(vDescriptor args[]) {
	return number;
    }
}

class k$errortext extends iValueClosure {
    static vString text;

    vDescriptor function(vDescriptor args[]) {
	return text;
    }
}

class k$errorvalue extends iValueClosure {
    static vValue value;

    vDescriptor function(vDescriptor args[]) {
	return value;
    }
}

class k$level extends iValueClosure {				// &level
    vDescriptor function(vDescriptor args[]) {
	int i = 0;
	for (iClosure p = this.parent; p != null; p = p.parent) {
	    i++;
	}
	return vInteger.New(i);
    }

    String tfmt() { return "&level"; }
}

class k$gen4zeros extends iClosure {		// &allocated, &collections
    int i;
    public vDescriptor nextval() {
	if (PC == 1) {
	    i = 0;
	    PC = 2;
	}
	if (i < 4) {
	    i++;
	    return vInteger.New(0);
	} else {
	    return null;
	}
    }

    String tfmt() { return "&...."; }
}

class k$gen3zeros extends iClosure {		// &regions, &storage
    int i;
    public vDescriptor nextval() {
	if (PC == 1) {
	    i = 0;
	    PC = 2;
	}
	if (i < 3) {
	    i++;
	    return vInteger.New(0);
	} else {
	    return null;
	}
    }
    String tfmt() { return "&..."; }
}

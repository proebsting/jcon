//  iKeywords.java -- Icon keywords

//  This file implements most of the Icon keywords.  However,
//  three keywords are implemented entirely in the compiler:
//	&file
//	&line


package rts;

import java.io.*;
import java.text.*;
import java.util.*;



public final class iKeywords extends iFile {

static void announce() {
    vCset lcase = vCset.New('a', 'z');
    vCset ucase = vCset.New('A', 'Z');

    iEnv.declareKey("allocated", new k$allocated());
    iEnv.declareKey("ascii", new kConstant(vCset.New(0, 127)));
    iEnv.declareKey("clock", new k$clock());
    iEnv.declareKey("col", new k$col());
    iEnv.declareKey("collections", new k$collections());
    iEnv.declareKey("control", new k$control());
    iEnv.declareKey("cset", new kConstant(vCset.New(0, vCset.MAX_VALUE)));
    iEnv.declareKey("current", new k$current());
    iEnv.declareKey("date", new k$date());
    iEnv.declareKey("dateline", new k$dateline());
    iEnv.declareKey("digits", new kConstant(vCset.New('0', '9')));
    iEnv.declareKey("dump", new k$dump());
    iEnv.declareKey("e", new kConstant(vReal.New(Math.E)));
    iEnv.declareKey("error", new k$error());
    iEnv.declareKey("errornumber", new k$errornumber());
    iEnv.declareKey("errortext", new k$errortext());
    iEnv.declareKey("errorvalue", new k$errorvalue());
    iEnv.declareKey("errout", new k$errout());
    iEnv.declareKey("fail", new kConstant(null));	// always fails
    iEnv.declareKey("features", new k$features());
    iEnv.declareKey("host", new k$host());
    iEnv.declareKey("input", new k$input());
    iEnv.declareKey("interval", new k$interval());
    iEnv.declareKey("lcase", new kConstant(lcase));
    iEnv.declareKey("ldrag", new kConstant(vInteger.New(wEvent.LDrag)));
    iEnv.declareKey("letters", new kConstant(lcase.Union(ucase)));
    iEnv.declareKey("level", new k$level());
    iEnv.declareKey("lpress", new kConstant(vInteger.New(wEvent.LPress)));
    iEnv.declareKey("lrelease", new kConstant(vInteger.New(wEvent.LRelease)));
    iEnv.declareKey("main", new k$main());
    iEnv.declareKey("mdrag", new kConstant(vInteger.New(wEvent.MDrag)));
    iEnv.declareKey("meta", new k$meta());
    iEnv.declareKey("mpress", new kConstant(vInteger.New(wEvent.MPress)));
    iEnv.declareKey("mrelease", new kConstant(vInteger.New(wEvent.MRelease)));
    iEnv.declareKey("null", new kConstant(vNull.New()));
    iEnv.declareKey("output", new k$output());
    iEnv.declareKey("phi", new kConstant(vReal.New((1.0+Math.sqrt(5.0))/2.0)));
    iEnv.declareKey("pi", new kConstant(vReal.New(Math.PI)));
    iEnv.declareKey("pos", new k$pos());
    iEnv.declareKey("progname", new k$progname());
    iEnv.declareKey("random", new k$random());
    iEnv.declareKey("rdrag", new kConstant(vInteger.New(wEvent.RDrag)));
    iEnv.declareKey("regions", new k$regions());
    iEnv.declareKey("resize", new kConstant(vInteger.New(wEvent.Resize)));
    iEnv.declareKey("row", new k$row());
    iEnv.declareKey("rpress", new kConstant(vInteger.New(wEvent.RPress)));
    iEnv.declareKey("rrelease", new kConstant(vInteger.New(wEvent.RRelease)));
    iEnv.declareKey("shift", new k$shift());
    iEnv.declareKey("source", new k$source());
    iEnv.declareKey("storage", new k$storage());
    iEnv.declareKey("subject", new k$subject());
    iEnv.declareKey("time", new k$time());
    iEnv.declareKey("trace", new k$trace());
    iEnv.declareKey("ucase", new kConstant(ucase));
    iEnv.declareKey("version", new kConstant(vString.New(iConfig.Version)));
    iEnv.declareKey("window", new k$window());
    iEnv.declareKey("x", new k$x());
    iEnv.declareKey("y", new k$y());
}

} // class iKeywords



//  common class used for constant-valued keywords, including &fail

final class kConstant extends vProc0 {
    vValue value;

    kConstant(vValue v)		{ value = v; }		// constructor
    public vDescriptor Call()	{ return value; }	// reference
}



//  These keywords are read-only from the Icon programmer's standpoint
//  (but not necessarily constant).  They are assigned by rts calls to
//  k$keyword.self.set().  Setting a Java null makes the keyword fail.

abstract class kReadOnly extends vProc0 {
    vValue value;			// may be null for failure
    public void set(vValue v)		{ value = v; }
    public vDescriptor Call()		{ return value; }
}

final class k$progname extends kReadOnly {			// &progname
    static k$progname self;
    k$progname()		{ self = this; }
}

final class k$errornumber extends kReadOnly {			// &errornumber
    static k$errornumber self;
    k$errornumber()		{ self = this; }
}

final class k$errortext extends kReadOnly {			// &errortext
    static k$errortext self;
    k$errortext()		{ self = this; }
}

final class k$errorvalue extends kReadOnly {			// &errorvalue
    static k$errorvalue self;
    k$errorvalue()		{ self = this; }
}

final class k$control extends kReadOnly {			// &control
    static k$control self;
    k$control()			{ self = this; }
}

final class k$meta extends kReadOnly {				// &meta
    static k$meta self;
    k$meta()			{ self = this; }
}

final class k$shift extends kReadOnly {				// &shift
    static k$shift self;
    k$shift()			{ self = this; }
}

final class k$interval extends kReadOnly {			// &interval
    static k$interval self;
    k$interval()		{ self = this; }
}



//  Decrementing counters (&trace, &error, &dump) are subclasses of kCounter.
//  Integer values may be assigned.
//  k$kwname.self.check() succeeds, and decrements, if the counter is nonzero.

abstract class kCounter extends vProc0 {
    public long count = 0;	// MS JVM cannot handle this being private
    public vSimpleVar kwvar;	// MS JVM cannot handle this being private

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

final class k$trace extends kCounter {
    static k$trace self;
    k$trace()		{ super("&trace"); self = this; }
}

final class k$error extends kCounter {
    static k$error self;
    k$error()		{ super("&error"); self = this; }
}

final class k$dump extends kCounter {
    static k$dump self;
    k$dump()		{ super("&dump"); self = this; }
}



final class k$features extends vProc0 {				// &features

    //  The features list is hard-wired.
    //  (And it's not completely clear what we should report.)
    private static vString[] flist = {
	vString.New("UNIX"),
	vString.New("Java"), 
	vString.New("ASCII"),
	vString.New("co-expressions"),
	vString.New("environment variables"),
	vString.New("large integers"),
	vString.New("pipes"),
	vString.New("system function"),
    };

    public vDescriptor Call() {
	return new vClosure() {
	    int posn = 0;
	    public vDescriptor Resume() {
		if (posn < flist.length) {
		    retval = flist[posn++];
		    return this;
		} else {
		    return null;
		}
	    }
	}.Resume();
    }
}



final class k$current extends vProc0 {				// &current
    public vDescriptor Call() {
	return iEnv.cur_coexp;
    }
}

final class k$main extends vProc0 {				// &main
    public vDescriptor Call() {
	return iEnv.main;
    }
}

final class k$source extends vProc0 {				// &source
    public vDescriptor Call() {
	if (iEnv.cur_coexp.callers.empty()) {
	    return iEnv.main;
	}
	return (vCoexp) iEnv.cur_coexp.callers.peek();
    }
}

final class k$level extends vProc0 {				// &level
    public vDescriptor Call() {
	StringWriter w = new StringWriter();
	PrintWriter p = new PrintWriter(w);
	(new Exception()).printStackTrace(p);
	LineNumberReader r =
	    new LineNumberReader(new StringReader(w.toString()));
	String s;
	int n = 0;
	try {
	    while ((s = r.readLine()) != null) {
		if (s.indexOf("p_l$") >= 0) {
		    n++;
		}
	    }
	} catch (IOException e) {
	}
	return vInteger.New(n);
    }
}



final class k$clock extends vProc0 {				// &clock

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



final class k$date extends vProc0 {				// &date

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



final class k$time extends vProc0 {				// &time

    private static long tbase;

    public static void reset() {		// reset to zero
	tbase = System.currentTimeMillis();
    }

    public vDescriptor Call() {			// read value
	return vInteger.New(System.currentTimeMillis() - tbase);
    }
}



final class k$dateline extends vProc0 {				// &dateline

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



final class k$host extends vProc0 {				// &host

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



final class k$input extends vProc0 {				// &input

    static vFile file =		// referenced externally
	vFile.New("&input", System.in);	

    public vDescriptor Call() {
	return file;
    }
}



final class k$output extends vProc0 {				// &output

    static vFile file =		// referenced externally
	vFile.New("&output", System.out, true);

    public vDescriptor Call() {
	return file;
    }
}



final class k$errout extends vProc0 {				// &errout

    static vFile file = 		// referenced externally
	vFile.New("&errout", System.err, false);

    public vDescriptor Call() {
	return file;
    }
}



final class k$subject extends vProc0 {				// &subject

    private static vSimpleVar self = new vSimpleVar("&subject", vString.New()) {
	public vVariable Assign(vDescriptor v) {
	    value = v.mkString();			// &subject := s
	    k$pos.set(1);				// &pos := 1
	    return this;
	}
    };

    public vDescriptor Call() {
	return self;
    }

    public static vString get() {
	return (vString) self.value;
    }
}



final class k$pos extends vProc0 {				// &pos

    private static vSimpleVar self = new vSimpleVar("&pos", vInteger.New(1)) {
	public vVariable Assign(vDescriptor v) {
	    vInteger p = v.mkInteger();
	    int n = k$subject.get().posEq(p.value);
	    if (n > 0) {	// if valid
		value = vInteger.New(n);	// assign positive equivalent
		return this;
	    } else {
		return null; /*FAIL*/
	    }
	}
    };

    public vDescriptor Call() {
	return self;
    }

    public static vInteger get() {		// retrieve value
	return (vInteger) self.value;
    }

    public static void set(long i) {	// assign value known to be valid
	self.value = vInteger.New(i);
    }
}




//  this random number generator is compatible with Icon v9
//  see Icon Analyst 38 (October, 1996) for an extensive analysis

final class k$random extends vProc0 {				// &random

    private static long randval;			// current value

    private static final long RandA = 1103515245;
    private static final long RandC = 453816694;
    private static final double RanScale = 4.65661286e-10;

    private static vSimpleVar vrandom = new vSimpleVar("&random", vNull.New()) {

	public vVariable Assign(vDescriptor v) {	// assign
	    randval = v.mkInteger().value;
	    return this;
	}

	public vValue Deref() {			// dereference
	    return vInteger.New(randval);
	}
    };

    public static long get() {			// get current seed value
	return randval;
    }

    public static double nextVal() {		// gen val in [0.0, 1.0)
	randval = (RandA * randval + RandC) & 0x7fffffff;
	return RanScale * randval;
    }

    public static long choose(long limit) {	// gen val in [0, limit)
	return (long) (limit * nextVal());
    }

    public vDescriptor Call() {			// return &random as variable
	return vrandom;
    }
}



//  allocation keywords just generate three or four zero values

final class kZeroes extends vClosure {
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

final class k$allocated extends vProc0 {			// &allocated
    public vDescriptor Call() {
	return new kZeroes(4);
    }
}

final class k$collections extends vProc0 {			// &collections
    public vDescriptor Call() {
	return new kZeroes(4);
    }
}

final class k$regions extends vProc0 {				// &regions
    public vDescriptor Call() {
	return new kZeroes(3);
    }
}

final class k$storage extends vProc0 {				// &storage
    public vDescriptor Call() {
	return new kZeroes(3);
    }
}



final class k$window extends vProc0 {				// &window

    private static vSimpleVar kwindow = new vSimpleVar("&window", vNull.New()) {

	public vVariable Assign(vDescriptor v) {	// assign
	    vValue w = v.Deref();
	    if (w.iswin() || w.isnull()) {
		value = w;
		vWindow.setCurrent((vWindow) value);
		return this;
	    } else {
		iRuntime.error(140, w);
		return null;
	    }
	}

    };

    public static vWindow getWindow() {		// get non-null &window value
	if (kwindow.value.isnull()) {
	    iRuntime.error(140);
	} 
	return (vWindow) kwindow.value;
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

    abstract void newValue(long v);		// set with side-effects

    void set(long v) {				// set without side-effects
	kwvar.value = vInteger.New(v);
    }

    public vDescriptor Call() {
	return kwvar;
    }
}

final class k$x extends kMirrored {				// &x
    static kMirrored self;
    k$x()			{ super("&x"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	k$col.self.set(1 + i / 12);		// #%#% should depend on font
    }
}

final class k$y extends kMirrored {				// &y
    static kMirrored self;
    k$y()			{ super("&y"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	k$row.self.set(1 + i / 7);		// #%#% should depend on font
    }
}

final class k$row extends kMirrored {				// &row
    static kMirrored self;
    k$row()			{ super("&row"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	k$y.self.set(12 * i);			// #%#% should depend on font
    }
}

final class k$col extends kMirrored {				// &col
    static kMirrored self;
    k$col()			{ super("&col"); self = this; }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	k$x.self.set(7 * i);			// #%#% should depend on font
    }
}

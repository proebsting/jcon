//  iKeyword.java -- Icon keywords

//  This file implements most of the Icon keywords.  However,
//  two keywords are implemented entirely in the compiler:
//	&file
//	&line


package rts;

import java.io.*;
import java.text.*;
import java.util.*;



public final class iKeyword {

static private vCset lcset = vCset.New('a', 'z');
static private vCset ucset = vCset.New('A', 'Z');

static vProc allocated =
    iEnv.declareKey("allocated", new kZeroes(4));
static vProc ascii =
    iEnv.declareKey("ascii", new kReadOnly(vCset.New(0, 127)));
static vProc clock =
    iEnv.declareKey("clock", new k$clock());
static kMirrored col = (kMirrored)
    iEnv.declareKey("col", new k$col());
static vProc collections =
    iEnv.declareKey("collections", new kZeroes(4));
static kReadOnly control = (kReadOnly)
    iEnv.declareKey("control", new kReadOnly());
static vProc cset =
    iEnv.declareKey("cset", new kReadOnly(vCset.New(0, vCset.MAX_VALUE)));
static vProc current =
    iEnv.declareKey("current", new k$current());
static vProc date =
    iEnv.declareKey("date", new k$date());
static vProc dateline =
    iEnv.declareKey("dateline", new k$dateline());
static vProc digits =
    iEnv.declareKey("digits", new kReadOnly(vCset.New('0', '9')));
static kCounter dump = (kCounter)
    iEnv.declareKey("dump", new kCounter("&dump"));
static vProc e =
    iEnv.declareKey("e", new kReadOnly(vReal.New(Math.E)));
static kCounter error = (kCounter)
    iEnv.declareKey("error", new kCounter("&error"));
static kReadOnly errornumber = (kReadOnly)
    iEnv.declareKey("errornumber", new kReadOnly());
static kReadOnly errortext = (kReadOnly)
    iEnv.declareKey("errortext", new kReadOnly());
static kReadOnly errorvalue = (kReadOnly)
    iEnv.declareKey("errorvalue", new kReadOnly());
static kReadOnly errout = (kReadOnly)
    iEnv.declareKey("errout",
	new kReadOnly(vFile.New("&errout", System.err, false)));
static vProc fail =
    iEnv.declareKey("fail", new kReadOnly(null));
static vProc features =
    iEnv.declareKey("features", new k$features());
static vProc host =
    iEnv.declareKey("host", new k$host());
static kReadOnly input = (kReadOnly)
    iEnv.declareKey("input", new kReadOnly(vFile.New("&input", System.in)));	
static kReadOnly interval = (kReadOnly)
    iEnv.declareKey("interval", new kReadOnly());
static vProc lcase =
    iEnv.declareKey("lcase", new kReadOnly(lcset));
static vProc ldrag =
    iEnv.declareKey("ldrag", new kReadOnly(vInteger.New(wEvent.LDrag)));
static vProc letters =
    iEnv.declareKey("letters", new kReadOnly(lcset.Union(ucset)));
static vProc level =
    iEnv.declareKey("level", new k$level());
static vProc lpress =
    iEnv.declareKey("lpress", new kReadOnly(vInteger.New(wEvent.LPress)));
static vProc lrelease =
    iEnv.declareKey("lrelease", new kReadOnly(vInteger.New(wEvent.LRelease)));
static vProc main =
    iEnv.declareKey("main", new k$main());
static vProc mdrag =
    iEnv.declareKey("mdrag", new kReadOnly(vInteger.New(wEvent.MDrag)));
static kReadOnly meta = (kReadOnly)
    iEnv.declareKey("meta", new kReadOnly());
static vProc mpress =
    iEnv.declareKey("mpress", new kReadOnly(vInteger.New(wEvent.MPress)));
static vProc mrelease =
    iEnv.declareKey("mrelease", new kReadOnly(vInteger.New(wEvent.MRelease)));
static vProc nulll =
    iEnv.declareKey("null", new kReadOnly(vNull.New()));
static kReadOnly output = (kReadOnly)
    iEnv.declareKey("output",
	new kReadOnly(vFile.New("&output", System.out, true)));
static vProc phi =
    iEnv.declareKey("phi", new kReadOnly(vReal.New((1.0+Math.sqrt(5.0))/2.0)));
static vProc pi =
    iEnv.declareKey("pi", new kReadOnly(vReal.New(Math.PI)));
static k$pos pos = (k$pos)
    iEnv.declareKey("pos", new k$pos());
static kReadOnly progname = (kReadOnly)
    iEnv.declareKey("progname", new kReadOnly());
static k$random random = (k$random)
    iEnv.declareKey("random", new k$random());
static vProc rdrag =
    iEnv.declareKey("rdrag", new kReadOnly(vInteger.New(wEvent.RDrag)));
static vProc regions =
    iEnv.declareKey("regions", new kZeroes(3));
static vProc resize =
    iEnv.declareKey("resize", new kReadOnly(vInteger.New(wEvent.Resize)));
static kMirrored row = (kMirrored)
    iEnv.declareKey("row", new k$row());
static vProc rpress =
    iEnv.declareKey("rpress", new kReadOnly(vInteger.New(wEvent.RPress)));
static vProc rrelease =
    iEnv.declareKey("rrelease", new kReadOnly(vInteger.New(wEvent.RRelease)));
static kReadOnly shift = (kReadOnly)
    iEnv.declareKey("shift", new kReadOnly());
static vProc source =
    iEnv.declareKey("source", new k$source());
static vProc storage =
    iEnv.declareKey("storage", new kZeroes(3));
static k$subject subject = (k$subject)
    iEnv.declareKey("subject", new k$subject());
static k$time time = (k$time)
    iEnv.declareKey("time", new k$time());
static kCounter trace = (kCounter)
    iEnv.declareKey("trace", new kCounter("&trace"));
static vProc ucase =
    iEnv.declareKey("ucase", new kReadOnly(ucset));
static vProc version =
    iEnv.declareKey("version", new kReadOnly(vString.New(iConfig.Version)));
static k$window window = (k$window)
    iEnv.declareKey("window", new k$window());
static kMirrored x = (kMirrored)
    iEnv.declareKey("x", new k$x());
static kMirrored y = (kMirrored)
    iEnv.declareKey("y", new k$y());

static void announce() {}	// nothing to do

} // class iKeyword



//  Many keywords are read-only from the Icon programmer's standpoint
//  (but not necessarily constant).  They are assigned by rts calls to
//  iKeyword.keywordname.set().  Setting a Java null makes the keyword fail.

final class kReadOnly extends vProc0 {

    vValue value;		// may be null for failure

    kReadOnly()			{ value = null; }	// null constructor
    kReadOnly(vValue v)		{ value = v; }		// initing constructor

    public vValue get()		{ return value; }	// get value
    public vFile file()		{ return (vFile)value;}	// get file value
    public void set(vValue v)	{ value = v; }		// set value
    public vDescriptor Call()	{ return value; }	// reference keyword
}



//  Decrementing counters (&trace, &error, &dump) are instances of kCounter.
//  Integer values may be assigned.  iKeyword.keywordname.check() succeeds,
//  and decrements, if the counter is nonzero.

final class kCounter extends vProc0 {
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

    void set(long n) {
	count = n;
    }

    boolean check() {
	if (count == 0) {
	    return false; 
	} else {
	    count--;
	    return true;
	}
    }
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

    SimpleDateFormat formatter;

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

    SimpleDateFormat formatter;

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

    private long tbase;

    void reset() {				// reset to zero
	tbase = System.currentTimeMillis();
    }

    public vDescriptor Call() {			// read value
	return vInteger.New(System.currentTimeMillis() - tbase);
    }
}



final class k$dateline extends vProc0 {				// &dateline

    SimpleDateFormat formatter;

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

    private vString hostname;

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



final class k$subject extends vProc0 {				// &subject

    private vSimpleVar subj = new vSimpleVar("&subject", vString.New()) {
	public vVariable Assign(vDescriptor v) {
	    value = v.mkString();			// &subject := s
	    iKeyword.pos.set(1);			// &pos := 1
	    return this;
	}
    };

    public vDescriptor Call() {			// return vVariable
	return subj;
    }

    vString get() {				// return vString
	return (vString) subj.value;
    }
}



final class k$pos extends vProc0 {				// &pos

    private vSimpleVar pos = new vSimpleVar("&pos", vInteger.New(1)) {
	public vVariable Assign(vDescriptor v) {
	    vInteger p = v.mkInteger();
	    int n = iKeyword.subject.get().posEq(p.value);
	    if (n > 0) {	// if valid
		value = vInteger.New(n);	// assign positive equivalent
		return this;
	    } else {
		return null; /*FAIL*/
	    }
	}
    };

    public vDescriptor Call() {			// return vVarible
	return pos;
    }

    vInteger get() {				// return vInteger
	return (vInteger) pos.value;
    }

    void set(long i) {				// set long value known valid
	pos.value = vInteger.New(i);
    }
}




//  this random number generator is compatible with Icon v9
//  see Icon Analyst 38 (October, 1996) for an extensive analysis

final class k$random extends vProc0 {				// &random

    private static final long RandA = 1103515245;
    private static final long RandC = 453816694;
    private static final double RanScale = 4.65661286e-10;
 
    private long randval;				// current value

    private vSimpleVar vrandom = new vSimpleVar("&random", vNull.New()) {

	public vVariable Assign(vDescriptor v) {	// assign
	    randval = v.mkInteger().value;
	    return this;
	}

	public vValue Deref() {			// dereference
	    return vInteger.New(randval);
	}
    };

    long get() {				// get current seed value
	return randval;
    }

    double nextVal() {				// gen val in [0.0, 1.0)
	randval = (RandA * randval + RandC) & 0x7fffffff;
	return RanScale * randval;
    }

    long choose(long limit) {			// gen val in [0, limit)
	return (long) (limit * nextVal());
    }

    public vDescriptor Call() {			// return &random as variable
	return vrandom;
    }
}



//  allocation keywords just generate three or four zero values

final class kZeroes extends vProc0 {
    static vInteger zero = vInteger.New(0);
    int count;				// number of zeroes to generate

    kZeroes(int n) { count = n; }	// constructor

    public vDescriptor Call() {		// generator
	return new vClosure() {
	    { retval = zero; }
	    int n = count - 1;
	    public vDescriptor Resume() {
		if (n-- > 0) {
		    return retval;
		} else {
		    return null; /*FAIL*/
		}
	    }
	};
    }
}



final class k$window extends vProc0 {				// &window

    private vSimpleVar kwindow = new vSimpleVar("&window", vNull.New()) {

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

    public vDescriptor Call() {			// return variable
	return kwindow;
    }

    vWindow getWindow() {			// get non-null &window value
	if (kwindow.value.isnull()) {
	    iRuntime.error(140);
	} 
	return (vWindow) kwindow.value;
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
    k$x()			{ super("&x"); }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	iKeyword.col.set(1 + i / 12);		// #%#% should depend on font
    }
}

final class k$y extends kMirrored {				// &y
    k$y()			{ super("&y"); }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	iKeyword.row.set(1 + i / 7);		// #%#% should depend on font
    }
}

final class k$row extends kMirrored {				// &row
    k$row()			{ super("&row"); }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	iKeyword.y.set(12 * i);			// #%#% should depend on font
    }
}

final class k$col extends kMirrored {				// &col
    k$col()			{ super("&col"); }

    void newValue(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	iKeyword.x.set(7 * i);			// #%#% should depend on font
    }
}

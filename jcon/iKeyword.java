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

// NOTE: changing the declared type of any keyword below 
//       also requires a corresponding change in ../tran/interfacegen.icn.

public static kZeroes  allocated = new kZeroes(4);
public static kValue   ascii	= new kValue(vCset.New(0, 127));
public static vProc    clock	= new k$clock();
public static kPaired  col	= new k$col();
public static kZeroes  collections = new kZeroes(4);
public static kValue   control	= new kValue();
public static kValue   cset	= new kValue(vCset.New(0, vCset.MAX_VALUE));
public static kValue   current	= new kValue();
public static vProc    date	= new k$date();
public static vProc    dateline	= new k$dateline();
public static kValue   digits	= new kValue(vCset.New('0', '9'));
public static kCounter dump	= new kCounter("&dump");
public static kValue   e	= new kValue(vReal.New(Math.E));
public static kCounter error	= new kCounter("&error");
public static kValue   errornumber = new kValue();
public static kValue   errortext = new kValue();
public static kValue   errorvalue = new kValue();
public static kValue   errout	= new kValue(vFile.New("&errout",System.err,false));
public static kValue   fail	= new kValue(null);
public static vProc    features	= new k$features();
public static vProc    host	= new k$host();
public static kValue   input	= new kValue(vFile.New("&input", System.in));	
public static kValue   interval	= new kValue();
public static kValue   lcase	= new kValue(lcset);
public static kValue   ldrag	= new kValue(vInteger.New(wEvent.LDrag));
public static kValue   letters	= new kValue(lcset.Union(ucset));
public static vProc    level	= new k$level();
public static kValue   lpress	= new kValue(vInteger.New(wEvent.LPress));
public static kValue   lrelease	= new kValue(vInteger.New(wEvent.LRelease));
public static kValue   main	= new kValue();
public static kValue   mdrag	= new kValue(vInteger.New(wEvent.MDrag));
public static kValue   meta	= new kValue();
public static kValue   mpress	= new kValue(vInteger.New(wEvent.MPress));
public static kValue   mrelease	= new kValue(vInteger.New(wEvent.MRelease));
public static kValue   nulll	= new kValue(vNull.New());
public static kValue   output	= new kValue(vFile.New("&output",System.out,true));
public static kValue   phi	= new kValue(vReal.New((1+Math.sqrt(5.0))/2));
public static kValue   pi	= new kValue(vReal.New(Math.PI));
public static k$pos    pos	= new k$pos();
public static kValue   progname	= new kValue();
public static k$random random	= new k$random();
public static kValue   rdrag	= new kValue(vInteger.New(wEvent.RDrag));
public static kZeroes  regions	= new kZeroes(3);
public static kValue   resize	= new kValue(vInteger.New(wEvent.Resize));
public static kPaired  row	= new k$row();
public static kValue   rpress	= new kValue(vInteger.New(wEvent.RPress));
public static kValue   rrelease	= new kValue(vInteger.New(wEvent.RRelease));
public static kValue   shift	= new kValue();
public static vProc    source	= new k$source();
public static kZeroes  storage	= new kZeroes(3);
public static k$subject subject	= new k$subject();
public static k$time   time	= new k$time();
public static kCounter trace	= new kCounter("&trace");
public static kValue   ucase	= new kValue(ucset);
public static kValue   version	= new kValue(vString.New(iConfig.Version));
public static k$window window	= new k$window();
public static kPaired  x	= new k$x();
public static kPaired  y	= new k$y();

static void announce() {
    iEnv.declareKey("allocated", allocated);
    iEnv.declareKey("ascii", ascii);
    iEnv.declareKey("clock", clock);
    iEnv.declareKey("col", col);
    iEnv.declareKey("collections", collections);
    iEnv.declareKey("control", control);
    iEnv.declareKey("cset", cset);
    iEnv.declareKey("current", current);
    iEnv.declareKey("date", date);
    iEnv.declareKey("dateline", dateline);
    iEnv.declareKey("digits", digits);
    iEnv.declareKey("dump", dump);
    iEnv.declareKey("e", e);
    iEnv.declareKey("error", error);
    iEnv.declareKey("errornumber", errornumber);
    iEnv.declareKey("errortext", errortext);
    iEnv.declareKey("errorvalue", errorvalue);
    iEnv.declareKey("errout", errout);
    iEnv.declareKey("fail", fail);
    iEnv.declareKey("features", features);
    iEnv.declareKey("host", host);
    iEnv.declareKey("input", input);
    iEnv.declareKey("interval", interval);
    iEnv.declareKey("lcase", lcase);
    iEnv.declareKey("ldrag", ldrag);
    iEnv.declareKey("letters", letters);
    iEnv.declareKey("level", level);
    iEnv.declareKey("lpress", lpress);
    iEnv.declareKey("lrelease", lrelease);
    iEnv.declareKey("main", main);
    iEnv.declareKey("mdrag", mdrag);
    iEnv.declareKey("meta", meta);
    iEnv.declareKey("mpress", mpress);
    iEnv.declareKey("mrelease", mrelease);
    iEnv.declareKey("null", nulll);
    iEnv.declareKey("output", output);
    iEnv.declareKey("phi", phi);
    iEnv.declareKey("pi", pi);
    iEnv.declareKey("pos", pos);
    iEnv.declareKey("progname", progname);
    iEnv.declareKey("random", random);
    iEnv.declareKey("rdrag", rdrag);
    iEnv.declareKey("regions", regions);
    iEnv.declareKey("resize", resize);
    iEnv.declareKey("row", row);
    iEnv.declareKey("rpress", rpress);
    iEnv.declareKey("rrelease", rrelease);
    iEnv.declareKey("shift", shift);
    iEnv.declareKey("source", source);
    iEnv.declareKey("storage", storage);
    iEnv.declareKey("subject", subject);
    iEnv.declareKey("time", time);
    iEnv.declareKey("trace", trace);
    iEnv.declareKey("ucase", ucase);
    iEnv.declareKey("version", version);
    iEnv.declareKey("window", window);
    iEnv.declareKey("x", x);
    iEnv.declareKey("y", y);
}

} // class iKeyword



//  kValue -- read-only keyword class
//
//  Many keywords are read-only from the Icon programmer's standpoint
//  (but not necessarily constant).  They are assigned by rts calls to
//  iKeyword.keywordname.set().  Setting a Java null makes the keyword fail.

final class kValue extends vProc0 {

    vValue value;		// may be null for failure

    kValue()			{ value = null; }	// null constructor
    kValue(vValue v)		{ value = v; }		// initing constructor

    public vValue get()		{ return value; }	// get value
    public vFile file()		{ return (vFile)value;}	// get file value
    public void set(vValue v)	{ value = v; }		// set value

    public vDescriptor Call()	{ return value; }	// reference keyword
}



//  kCounter -- decrementing counter class
//
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

    //  the features list is hard-wired
    private static vString[] flist = {
	vString.New("UNIX"),
	vString.New("Java"), 
	vString.New("ASCII"),
	vString.New("co-expressions"),
	vString.New("dynamic loading"),
	vString.New("environment variables"),
	vString.New("large integers"),
	vString.New("pipes"),
	vString.New("system function"),
	vString.New("graphics"),
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



final class k$source extends vProc0 {				// &source
    public vDescriptor Call() {
	vCoexp current = (vCoexp) iKeyword.current.get();
	if (current.callers.empty()) {
	    return iKeyword.main.get();
	}
	return (vCoexp) current.callers.peek();
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
 
    long randval;				// current value

    private vSimpleVar vrandom = new vSimpleVar("&random", vNull.New()) {

	public vVariable Assign(vDescriptor v){ // assign
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
	    if (w.iswin()) {
		vWindow.setCurrent((vWindow) w);
	    } else if (w.isnull()) {
		vWindow.setCurrent(null);
	    } else {
		iRuntime.error(140, w);
	    }
	    value = w;
	    return this;
	}

    };

    public vDescriptor Call() {			// return variable
	return kwindow;
    }

    vWindow getWindow() {			// get non-null &window value
	if (! kwindow.value.iswin()) {
	    iRuntime.error(140);
	} 
	return (vWindow) kwindow.value;
    }
}



abstract class kPaired extends vProc0 {		// super for paired integer kwds

    private vSimpleVar kwvar;			// trapped variable

    kPaired(String name) {			// constructor 
	kwvar = new vSimpleVar(name, vInteger.New(0)) {
	    public vVariable Assign(vDescriptor v) {
		vInteger i = v.mkInteger();	// must be integer
		assign(i.value);		// call subclass (may give err)
		value = i;			// if no error, set value
		return this;
	    }
	};
    }

    abstract void assign(long v);		// set with side-effects

    void set(long v) {				// set without side-effects
	kwvar.value = vInteger.New(v);
    }

    public vDescriptor Call() {
	return kwvar;
    }
}

final class k$x extends kPaired {				// &x
    k$x()			{ super("&x"); }

    void assign(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	iKeyword.col.set(1 + i / win.Fwidth());
    }
}

final class k$y extends kPaired {				// &y
    k$y()			{ super("&y"); }

    void assign(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	iKeyword.row.set(1 + i / win.Leading()); 
    }
}

final class k$row extends kPaired {				// &row
    k$row()			{ super("&row"); }

    void assign(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	iKeyword.y.set(i * win.Leading());
    }
}

final class k$col extends kPaired {				// &col
    k$col()			{ super("&col"); }

    void assign(long i) {
	vWindow win = vWindow.getCurrent();
	set(i);
	iKeyword.x.set(i * win.Fwidth());
    }
}

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
		iEnv.declareKey("null", iNew.Null());
		iEnv.declareKey("e", iNew.Real(Math.E));
		iEnv.declareKey("phi", iNew.Real((1.0 + Math.sqrt(5.0)) / 2.0));
		iEnv.declareKey("pi", iNew.Real(Math.PI));
		iEnv.declareKey("version", 
			iNew.String("Jcon Version 0.6.0, November 4, 1997"));

		// constant for lack of a better solution
		iEnv.declareKey("time", iNew.Integer(0));

	    	// cset constants
		vCset lcase, ucase;
		iEnv.declareKey("digits", iNew.Cset('0', '9'));
		iEnv.declareKey("lcase", lcase = iNew.Cset('a','z'));
		iEnv.declareKey("ucase", ucase = iNew.Cset('A','Z'));
		iEnv.declareKey("letters", lcase.Union(ucase));
		iEnv.declareKey("ascii", iNew.Cset(0, 127));
		iEnv.declareKey("cset", iNew.Cset(0, vCset.MAX_VALUE));

		// read-only, but variable
		iEnv.declareKey("clock", new k$clock());
		iEnv.declareKey("current", new k$current());
		iEnv.declareKey("date", new k$date());
		iEnv.declareKey("dateline", new k$dateline());
		iEnv.declareKey("host", new k$host());
		iEnv.declareKey("main", new k$main());
		iEnv.declareKey("source", new k$source());
		iEnv.declareKey("progname", new k$progname());

		// files
		iEnv.declareKey("input",  new k$input());
		iEnv.declareKey("output", new k$output());
		iEnv.declareKey("errout", new k$errout());

		//incestuous
		k$subject s = new k$subject();		// &subject
		k$pos p = new k$pos();			// &pos
		s.pos = p;
		p.subject = s;
		s.Assign(iNew.String(""));
		iEnv.declareKey("subject", s);
		iEnv.declareKey("pos", p);

		// error-related
		iEnv.declareKey("error", new k$error());
		iEnv.declareKey("errornumber", iNew.Proc(
			"&errornumber", "rts.k$errornumber", 0));
		iEnv.declareKey("errortext", iNew.Proc(
			"&errortext", "rts.k$errortext", 0));
		iEnv.declareKey("errorvalue", iNew.Proc(
			"&errorvalue", "rts.k$errorvalue", 0));

		iEnv.declareKey("dump", new k$dump());

		// generators
		iEnv.declareKey("features", iNew.Proc(
			"&features", "rts.k$features", 0));
		iEnv.declareKey("level", iNew.Proc(
			"&level", "rts.k$level", 0));

		// bogus generators
		vProc proc = iNew.Proc(
			"k$gen4zeroes", "rts.k$gen4zeros", 0);
		iEnv.declareKey("allocated", proc);
		iEnv.declareKey("collections", proc);
		proc = iNew.Proc(
			"k$gen3zeroes", "rts.k$gen3zeros", 0);
		iEnv.declareKey("regions", proc);
		iEnv.declareKey("storage", proc);

		// special behavior
		iEnv.declareKey("trace", new k$trace());
		iEnv.declareKey("random", new k$random());

		// graphics constants
		iEnv.declareKey("lpress", iNew.Integer(wEvent.LPress));
		iEnv.declareKey("mpress", iNew.Integer(wEvent.MPress));
		iEnv.declareKey("rpress", iNew.Integer(wEvent.RPress));
		iEnv.declareKey("ldrag", iNew.Integer(wEvent.LDrag));
		iEnv.declareKey("mdrag", iNew.Integer(wEvent.MDrag));
		iEnv.declareKey("rdrag", iNew.Integer(wEvent.RDrag));
		iEnv.declareKey("lrelease", iNew.Integer(wEvent.LRelease));
		iEnv.declareKey("mrelease", iNew.Integer(wEvent.MRelease));
		iEnv.declareKey("rrelease", iNew.Integer(wEvent.RRelease));
		iEnv.declareKey("resize", iNew.Integer(wEvent.Resize));

		// graphics variables
		iEnv.declareKey("window", new k$window());	// &window
		iEnv.declareKey("x", new k$x());		// &x
		iEnv.declareKey("y", new k$y());		// &y
		iEnv.declareKey("row", new k$row());		// &row
		iEnv.declareKey("col", new k$col());		// &col
		iEnv.declareKey("interval", new k$interval());	// &interval
		iEnv.declareKey(
			"control", iNew.Proc("&control", "rts.k$control", 0));
		iEnv.declareKey(
			"meta", iNew.Proc("&meta", "rts.k$meta", 0));
		iEnv.declareKey(
			"shift", iNew.Proc("&shift", "rts.k$shift", 0));
	}
}



class k$features extends iClosure {		// &features

	//#%#%  The features list is hard-wired for now.
	//#%#%  It's not completely clear what we should report.

	static String[] flist = { 
		"Java", "ASCII", "co-expressions",
		"environment variables", "system function" };

	int posn = 0;

	public void nextval() {
		if (posn < flist.length) {
			retvalue = iNew.String(flist[posn++]);
		} else {
			retvalue = null;
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



class k$current extends k$Value {		// &current

	public vValue deref() {
		return iEnv.cur_coexp;
	}
}

class k$main extends k$Value {			// &main

	public vValue deref() {
		return iEnv.main;
	}
}

class k$source extends k$Value {		// &source

	public vValue deref() {
		if (iEnv.cur_coexp.callers.empty()) {
			return iEnv.main;
		}
		return (vCoexp) iEnv.cur_coexp.callers.peek();
	}
}

class k$clock extends k$Value {			// &clock

	static SimpleDateFormat formatter =
		new SimpleDateFormat("HH:mm:ss");
	static { formatter.setTimeZone(TimeZone.getDefault()); }

	public vValue deref() {
		Date d = new Date();
		String s = formatter.format(d);
		return iNew.String(s);
	}
}



class k$date extends k$Value {			// &date

	static SimpleDateFormat formatter =
		new SimpleDateFormat("yyyy/MM/dd");
	static { formatter.setTimeZone(TimeZone.getDefault()); }

	public vValue deref() {
		Date d = new Date();
		String s = formatter.format(d);
		return iNew.String(s);
	}
}



class k$dateline extends k$Value {		// &dateline

	static SimpleDateFormat formatter =
		new SimpleDateFormat("EEEEEE, MMMM d, yyyy  h:mm aa");
	static { formatter.setTimeZone(TimeZone.getDefault()); }

	public vValue deref() {
		Date d = new Date();
		String s = formatter.format(d);
		int n = s.length() - 2;		// beginning of AM/PM
		s = s.substring(0, n) + s.substring(n).toLowerCase();
		return iNew.String(s);
	}
}



class k$host extends k$Value {			// &host

	static vString hostname;

	public vValue deref() {
		if (hostname == null) {
			inithost();
		}
		return hostname;
	}

	//#%#% warning: ugly unixisms follow

	static void inithost() {
	    try {
		Process p = Runtime.getRuntime().exec("uname -n");
		hostname = iNew.String(
		    new BufferedReader(
		    new InputStreamReader(p.getInputStream()))
		    .readLine().trim());
		p.destroy();
		hostname.value.charAt(0);	// ensure not empty
	    } catch (Exception e1) {
		try {
		    hostname = iNew.String(System.getProperty("os.name"));
		} catch (Exception e2) {
		    hostname = iNew.String("Jcon");
		}
	    }
	}

}



class k$progname extends k$Value {		// &progname
	static String name;

	public vValue deref() {
		return iNew.String(name);
	}
}



class k$input extends k$Value {			// &input
	static vFile file;	//#%#% ref'd in fIO.java

	k$input()		{ file = iNew.File("&input", System.in); }
	public vValue deref()	{ return file; }
}

class k$output extends k$Value {		// &output
	static vFile file;	//#%#% ref'd in fIO.java

	k$output()		{ file = iNew.File("&output", System.out); }
	public vValue deref()	{ return file; }
}

class k$errout extends k$Value {		// &errout
	static vFile file;	//#%#% ref'd in fIO.java

	k$errout()		{ file = iNew.File("&errout", System.err); }
	public vValue deref()	{ return file; }
}



class k$subject extends vSimpleVar {		// &subject
	
	k$pos pos;		// associated &pos variable
	static k$subject self;

	k$subject() { super("&subject"); self = this; }

	public vVariable Assign(vValue s) {
		value = s.mkString();			// &subject := s
		pos.Assign(iNew.Integer(1));		// &pos := 1
		return this;
	}

}


class k$pos extends vSimpleVar {		// &pos

	k$subject subject;		// associated &subject variable
	static k$pos self;

	k$pos() { super("&pos"); self = this; }

	public vVariable Assign(vValue i) {
		i = i.mkInteger();
		int n = ((vString)subject.value).posEq(((vInteger)i).value);
		if (n == 0)
			return null;	// fail: position out of range
		value = iNew.Integer(n);
		return this;
	}
}



class k$trace extends vSimpleVar {		// &trace

	static long trace;		// #%#%#% referenced in iClosure

	k$trace() { super("&trace"); }

	public vVariable Assign(vValue i) {
		value = i.mkInteger();
		trace = ((vInteger)value).value;
		return this;
	}

	public vValue deref() {
		return value = iNew.Integer(trace);
	}
}




//  this random number generator is compatible with Icon v9
//  see Icon Analyst 38 (October, 1996) for an extensive analysis

class k$random extends vSimpleVar {		// &random

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

	public vValue deref() {				// dereference
		return value = iNew.Integer(randval);
	}

	public static double nextVal() {		// gen val in [0.0, 1.0)
		randval = (RandA * randval + RandC) & 0x7fffffff;
		return RanScale * randval;
	}

	public static long choose(long limit) {		// gen val in [0, limit)
		return (long) (limit * nextVal());
	}
}



class k$window extends vSimpleVar {		// &window

	private static vValue value = iNew.Null();	// current value

	k$window() { super("&window"); }	// constructor

	public vVariable Assign(vValue w) {	// assign
		if (! (w instanceof vWindow) && ! (w instanceof vNull)) {
			iRuntime.error(140, w);
		}
		value = w;
		vWindow.setCurrent((vWindow) value);
		return this;
	}

	public vValue deref() {			// dereference
		return value;
	}

	public static vWindow getWindow() {	// get &window, must be !null
		if (! (value instanceof vWindow)) {
			iRuntime.error(140);
		} 
		return (vWindow) value;
	}
}

class k$control extends iValueClosure {		// &control
	static vValue value;			// null value produces failure
	vDescriptor function(vDescriptor args[]) { return value; }
}

class k$meta extends iValueClosure {		// &meta
	static vValue value;			// null value produces failure
	vDescriptor function(vDescriptor args[]) { return value; }
}

class k$shift extends iValueClosure {		// &shift
	static vValue value;			// null value produces failure
	vDescriptor function(vDescriptor args[]) { return value; }
}



abstract class k$intWatcher extends vVariable { // super for "watched" int kwds
	String name;
	long value;
	abstract void newValue(long i);		// subclass must impl this

	k$intWatcher(String name) {		// constructor just saves name
		this.name = name;
	}

	public vValue deref() {
		return iNew.Integer(value);	// deref just returns value
	}

	public vVariable Assign(vValue v) {	// assignment
		long i = v.mkInteger().value;	// must be integer
		newValue(i);			// call subclass (may give err)
		value = i;			// if no error, set value
		return this;
	}

	public void set(long i)	{		// set without side effects
		value = i;
	}

	vString Name()		{ return iNew.String(name); }

	String report()		{ return "(" + name + " = " + value + ")"; }
}

class k$interval extends k$intWatcher {		// &interval
	static k$interval self;
	k$interval()		{ super("k$interval"); self = this; }
	void newValue(long i) {} // no side effects 
}

class k$x extends k$intWatcher {		// &x
	static k$x self;
	k$x()			{ super("k$x"); self = this; }

	void newValue(long i) {
		vWindow win = vWindow.getCurrent();
		k$col.self.set(1 + i / 12);	//#%#%#%#%# bogus
	}
}

class k$y extends k$intWatcher {		// &y
	static k$y self;
	k$y()			{ super("k$y"); self = this; }

	void newValue(long i) {
		vWindow win = vWindow.getCurrent();
		k$row.self.set(1 + i / 7);	//#%#%#%#%# bogus
	}
}

class k$row extends k$intWatcher {		// &row
	static k$row self;
	k$row()			{ super("k$row"); self = this; }

	void newValue(long i) {
		vWindow win = vWindow.getCurrent();
		k$y.self.set(12 * i);		//#%#%#%#%# bogus
	}
}

class k$col extends k$intWatcher {		// &col
	static k$col self;
	k$col()			{ super("k$col"); self = this; }

	void newValue(long i) {
		vWindow win = vWindow.getCurrent();
		k$x.self.set(7 * i);		//#%#%#%#%# bogus
	}
}



class k$dump extends vSimpleVar {		// &dump

	public static long dump;	// #%#%#% referenced externally

	k$dump() { super("&dump"); }

	public vVariable Assign(vValue i) {
		value = i.mkInteger();
		dump = ((vInteger)value).value;
		return this;
	}

	public vValue deref() {
		return value = iNew.Integer(dump);
	}
}

class k$error extends vSimpleVar {		// &trace

	static long error;		// #%#%#% referenced in iClosure

	k$error() { super("&error"); }

	public vVariable Assign(vValue i) {
		value = i.mkInteger();
		error = ((vInteger)value).value;
		return this;
	}

	public vValue deref() {
		return value = iNew.Integer(error);
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

class k$level extends iValueClosure {	// &level
	vDescriptor function(vDescriptor args[]) {
		int i = 0;
		for (iClosure p = this.parent; p != null; p = p.parent) {
			i++;
		}
		return iNew.Integer(i);
	}

	String tfmt() { return "&level"; }
}

class k$gen4zeros extends iClosure {		// &allocated, &collections
	int i;
	public void nextval() {
		if (i < 4) {
			i++;
			retvalue = iNew.Integer(0);
		} else {
			retvalue = null;
		}
	}

	String tfmt() { return "&...."; }
}

class k$gen3zeros extends iClosure {		// &regions, &storage
	int i;
	public void nextval() {
		if (i < 3) {
			i++;
			retvalue = iNew.Integer(0);
		} else {
			retvalue = null;
		}
	}
	String tfmt() { return "&..."; }
}

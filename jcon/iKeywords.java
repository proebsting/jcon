//  iKeywords.java -- Icon keywords

package rts;

import java.io.*;
import java.text.*;
import java.util.*;


public class iKeywords extends iFile {

	void announce() {

		// implemented in the compiler:
		//	&fail
		//	&file
		//	&line
		// (preserve that line format for the documentation extractor)
		
		// constants
		iEnv.declareKey("null", iNew.Null());
		iEnv.declareKey("e", iNew.Real(Math.E));
		iEnv.declareKey("phi", iNew.Real((1.0 + Math.sqrt(5.0)) / 2.0));
		iEnv.declareKey("pi", iNew.Real(Math.PI));
		iEnv.declareKey("version", 
			    iNew.String("Jcon Version 0.4, Summer, 1997"));

	    	// cset constants
		vCset lcase, ucase;
		iEnv.declareKey("digits", iNew.Cset('0', '9'));
		iEnv.declareKey("lcase", lcase = iNew.Cset('a','z'));
		iEnv.declareKey("ucase", ucase = iNew.Cset('A','Z'));
		iEnv.declareKey("letters", lcase.Union(ucase));
		iEnv.declareKey("ascii", iNew.Cset(0, 127));
		//#%#% not clear that this is right, but 65K size is REAL slow:
		iEnv.declareKey("cset", iNew.Cset(0, 255));

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
		iEnv.declareKey("errornumber", 
			iNew.Proc((new k$errornumber()).getClass(), 0));
		iEnv.declareKey("errortext", 
			iNew.Proc((new k$errortext()).getClass(), 0));
		iEnv.declareKey("errorvalue", 
			iNew.Proc((new k$errorvalue()).getClass(), 0));

		iEnv.declareKey("dump", new k$dump());

		// generators
		iEnv.declareKey("features", 
			iNew.Proc((new k$features()).getClass(), 0));
		iEnv.declareKey("level", 
			iNew.Proc((new k$level()).getClass(), 0));

		// bogus generators
		vProc proc = iNew.Proc((new k$gen4zeros()).getClass(), 0);
		iEnv.declareKey("allocated", proc);
		iEnv.declareKey("collections", proc);
		proc = iNew.Proc((new k$gen3zeros()).getClass(), 0);
		iEnv.declareKey("regions", proc);
		iEnv.declareKey("storage", proc);

		// special behavior
		iEnv.declareKey("trace", new k$trace());
		iEnv.declareKey("random", new k$random());
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



abstract class k$Value extends vValue {		// super of read-only keywords

	public abstract vValue deref();		// must implement deref()

	String image()	{ return deref().image(); }
	String type()	{ return deref().type(); }
	int rank()	{ return -1; }		// should not appear in sorting
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

	public vValue deref() {
		Date d = new Date();
		String s = formatter.format(d);
		return iNew.String(s);
	}
}



class k$date extends k$Value {			// &date

	static SimpleDateFormat formatter =
		new SimpleDateFormat("yyyy/MM/dd");

	public vValue deref() {
		Date d = new Date();
		String s = formatter.format(d);
		return iNew.String(s);
	}
}



class k$dateline extends k$Value {		// &dateline

	static SimpleDateFormat formatter =
		new SimpleDateFormat("EEEEEE, MMMM d, yyyy  h:mm aa");

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
		Process p = Runtime.getRuntime().exec("/bin/uname -n");
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

class k$errornumber extends iFunctionClosure {
	static vInteger number;

	vDescriptor function(vDescriptor args[]) {
		return number;
	}
}

class k$errortext extends iFunctionClosure {
	static vString text;

	vDescriptor function(vDescriptor args[]) {
		return text;
	}
}

class k$errorvalue extends iFunctionClosure {
	static vValue value;

	vDescriptor function(vDescriptor args[]) {
		return value;
	}
}

class k$level extends iFunctionClosure {	// &level
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

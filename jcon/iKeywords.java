//  iKeywords.java -- Icon keywords

import java.util.*;


class iKeywords extends iFile {

	void announce(iEnv env) {
		
		// constants
		env.declareKey("null", iNew.Null());
		env.declareKey("e", iNew.Real(Math.E));
		env.declareKey("phi", iNew.Real((1.0 + Math.sqrt(5.0)) / 2.0));
		env.declareKey("pi", iNew.Real(Math.PI));
		env.declareKey("version", 
			    iNew.String("Jcon Version 0.0, Spring, 1997"));

	    	// cset constants
		vCset lcase, ucase;
		env.declareKey("digits", iNew.Cset('0', '9'));
		env.declareKey("lcase", lcase = iNew.Cset('a','z'));
		env.declareKey("ucase", ucase = iNew.Cset('A','Z'));
		env.declareKey("letters", lcase.Union(ucase));
		env.declareKey("ascii", iNew.Cset(0, 127));
//#%#% slow	env.declareKey("cset", iNew.Cset(0, Character.MAX_VALUE));

		// read-only, but variable
		env.declareKey("clock", new k$clock());
		env.declareKey("date", new k$date());
		env.declareKey("dateline", new k$dateline());

		//incestuous
		k$subject s = new k$subject();		// &subject
		k$pos p = new k$pos();			// &pos
		s.pos = p;
		p.subject = s;
		s.Assign(iNew.String(""));
		env.declareKey("subject", s);
		env.declareKey("pos", p);

		// generators
		env.declareKey("features", 
			iNew.Proc((new k$features()).getClass(), env));

		// special behavior
		env.declareKey("trace", new k$trace());
	}
}



class k$features extends iClosure {		// &features

	//#%#%  The features list is hard-wired for now.
	//#%#%  It's not completely clear what we should report.

	static String[] flist = { "Java", "ASCII", "co-expressions" };

	int posn = 0;

	void nextval() {
		if (posn < flist.length) {
			retvalue = iNew.String(flist[posn++]);
		} else {
			retvalue = null;
		}
	}
}



abstract class k$Value extends vValue {		// super of read-only keywords

	abstract vValue deref();		// must implement deref()

	String image()	{ return deref().image(); }
	String type()	{ return deref().type(); }
}



class k$clock extends k$Value {			// &clock

	vValue deref() {
		return iNew.String((new Date()).toString().substring(11,19));
	}
}



class k$date extends k$Value {			// &date

	vValue deref() {
		Date d = new Date();
		StringBuffer b = new StringBuffer(10);
		b.append(d.getYear() + 1900);
		b.append('/');
		if (d.getMonth() < 9) {		// getMonth returns 0 - 11
		    b.append('0');
		}
		b.append(d.getMonth() + 1);
		b.append('/');
		if (d.getDate() < 10) {		// but getDate returns 1 - 31
		    b.append('0');
		}
		b.append(d.getDate());
		return iNew.String(b.toString());
	}
}



class k$dateline extends k$Value {			// &dateline

	static String[] wkdays = { "Sunday, ", "Monday, ", "Tuesday, ",
		"Wednesday, ", "Thursday, ", "Friday, ", "Saturday, " };
	static String[] months = { "January ", "February ", "March ",
		"April ", "May ", "June ", "July ", "August ",
		"September ", "October ", "November ", "December " };

	vValue deref() {
		Date d = new Date();
		StringBuffer b = new StringBuffer(10);
		b.append(wkdays[d.getDay()]);
		b.append(months[d.getMonth()]);
		b.append(d.getDate());
		b.append(", ");
		b.append(d.getYear() + 1900);
		b.append("  ");
		b.append(((d.getHours() + 11) % 12) + 1);  // 12, 1, 2, .. 11
		b.append(d.toString().substring(13, 16));  // :mm
		b.append((d.getHours() >= 12) ? " pm" : " am");
		return iNew.String(b.toString());
	}
}



class k$subject extends vSimpleVar {		// &subject
	
	k$pos pos;		// associated &pos variable

	vVariable Assign(vValue s) {
		value = s.mkString();			// &subject := s
		pos.Assign(iNew.Integer(1));		// &pos := 1
		return this;
	}

}


class k$pos extends vSimpleVar {		// &pos

	k$subject subject;		// associated &subject variable

	vVariable Assign(vValue i) {
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

	vVariable Assign(vValue i) {
		value = i.mkInteger();
		trace = ((vInteger)value).value;
		return this;
	}

	vValue deref() {
		return value = iNew.Integer(trace);
	}
}

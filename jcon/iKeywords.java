//  iKeywords.java -- Icon keywords

import java.util.*;


class iKeywords extends iFile {

	void announce(iEnv env) {

		k$subject s = new k$subject();
		k$pos p = new k$pos();
		s.pos = p;
		p.subject = s;
		s.Assign(iNew.String(""));
		env.declareKey("subject", s);
		env.declareKey("pos", p);

		env.declareKey("clock", new k$clock());
		env.declareKey("date", new k$date());
		env.declareKey("dateline", new k$dateline());	//#%#% imperfect

		env.declareKey("null", iNew.Null());

		env.declareKey("e", iNew.Real(Math.E));
		env.declareKey("phi", iNew.Real((1.0 + Math.sqrt(5.0)) / 2.0));
		env.declareKey("pi", iNew.Real(Math.PI));

		env.declareKey("version", 
			    iNew.String("Jcon Version 0.0, Spring, 1997"));
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
		b.append(d.toString().substring(16, 19));  // :mm
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

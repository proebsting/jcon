//  iKeywords.java -- Icon keywords

class iKeywords extends iFile {

	void announce(iEnv env) {

		k$subject s = new k$subject();
		k$pos p = new k$pos();
		s.pos = p;
		p.subject = s;
		s.Assign(iNew.String(""));
		env.declareKey("subject", s);
		env.declareKey("pos", p);

		env.declareKey("null", iNew.Null());

		env.declareKey("fail", iNew.Null()); //#%#% BOGUS, avoids msgs

		env.declareKey("e", iNew.Real(Math.E));
		env.declareKey("phi", iNew.Real((1.0 + Math.sqrt(5.0)) / 2.0));
		env.declareKey("pi", iNew.Real(Math.PI));

		env.declareKey("version", 
			    iNew.String("Jcon Version 0.0, Spring, 1997"));
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

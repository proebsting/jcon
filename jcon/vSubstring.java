class vSubstring extends vVarExpr {

    int start, end;	// substring bounds, in Icon terms


    // constructors assume valid indices (in positive form)

    vSubstring(vSimpleVar v, int i1, int i2) {	// construct from String
	super(v);
	start = i1;
	end = i2;
    }

    vSubstring(vSubstring v, int i1, int i2) {	// construct from Substring
	super(v.var);
	start = v.start - 1 + i1;
	end = v.start - 1 + i2;
    }


    vValue deref() {
	vString s = var.mkString();		// deref and validate type
	if (end > s.value.length() + 1) {	// if index now out of range
	    return null; /*FAIL*/
	}
	return iNew.String(s.value.substring(start - 1, end - 1));
    }


    vVariable Assign(vValue x) {

	String xs = x.mkString().value;		// coerce assigned value

	String s = var.mkString().value;	// deref and validate base strg
	if (end > s.length() + 1) {		// if index now out of range
	    return null; /*FAIL*/
	}

	var.Assign(iNew.String(
	    new StringBuffer()
	    .append(s.substring(0, start - 1))
	    .append(xs)
	    .append(s.substring(end - 1))
	    .toString()));
	    
	return iNew.Substring(this, start, start + xs.length());
    }



    vDescriptor isNull()	{ return null; /*FAIL*/ }
    vDescriptor isntNull()	{ return this; }

    vDescriptor Select() {				// ?s
	if (start == end) {
	    return null; /*FAIL*/
	}
    	int offset = (int) iRuntime.random(end - start) + 1;
	return iNew.Substring(this, offset, offset + 1);
    }

    vDescriptor Bang(iClosure c) {			// !s
	int n;
	if (c.o == null) {
	    c.o = new Integer(n = start);
	} else {
	    c.o = new Integer(n = ((Integer)c.o).intValue() + 1);
	}
	if (n >= end) {
	    return null; /*FAIL*/
	} else {
	    return iNew.Substring(this, n, n + 1);
	}
    }

}

class vSubstring extends vVariable {

    vString sval;	// underlying string
    int start, end;	// substring bounds (in Icon terms)

    vSubstring(vSimpleVar v, int i1, int i2) {
    	sval = (vString) v.value;
	start = i1;
	end = i2;
    }

    vSubstring(vSubstring v, int i1, int i2) {
        sval = v.sval;
	start = v.start - 1 + i1;
	end = v.start - 1 + i2;
    }

    vValue deref(){
	//#%#%# should check that indices are still in range
	return iNew.String(sval.value.substring(start - 1, end - 1));
    }

    vVariable Assign(vValue x) {
    	iRuntime.error(901, this);	//#%#%#% substring assignment NYI
	return this;
    }


    vDescriptor isNull()	{ return null; /*FAIL*/ }
    vDescriptor isntNull()	{ return this; }

    vDescriptor Select() {		// ?s
	if (start == end) {
	    return null; /*FAIL*/
	}
    	int offset = (int) iRuntime.random(end - start) + 1;
	return iNew.Substring(this, offset, offset + 1);
    }

    vDescriptor Bang(iClosure c) {	// !s
	int n;
	if (c.o == null) {
	    c.o = new Integer(n = this.start);
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

package rts;

public class vSubstring extends vVariable {

    vVariable var;

    int start, end;	// substring bounds, in Icon terms



//  constructors assume valid, ordered indices in positive form

public static vSubstring New(vVariable v, int i1, int i2)
				{ return new vSubstring(v, i1, i2); }

private vSubstring(vVariable v, int i1, int i2) {	// construct from String
    var = v;
    start = i1;
    end = i2;
}



boolean isNull()			{ return false; }

vString Name() {
    String vname = var.Name().toString();
    return vString.New(vname +"[" + start + ":" + end + "]");
}



//  ss.strval() -- return underlying vString value.
//
//  Verifies that the underlying variable is still a string
//  and that the indices are still in range, and return String.

vString strval() {
    vDescriptor v = var.deref();
    if (! (v instanceof vString)) {
	iRuntime.error(205);
    }
    vString s = (vString) v;
    if (end > s.length() + 1)  {
	iRuntime.error(103, var);
    }
    return s;
}



//  ss.posEq(n) -- return positive equivalent of position n in substring ss,
//		   or zero if out of bounds

int posEq(long n) {
    int len = end - start;
    if (n <= 0) {
	n += len + 1;
    }
    if (n > 0 && n <= len + 1) {
	return (int)(start + n - 1);
    } else {
	return 0;
    }
}



//  internal methods

public vValue deref() {
    return vString.New(this.strval(), start, end);
}

vString report() {
    if (start + 1 == end) {
	return vString.New(var.deref().report() + "[" + start + "]");
    } else {
	return vString.New(var.deref().report() + "[" + start +":"+ end + "]");
    }
}



//  operators

public vVariable Assign(vValue x) {
    vString s = x.mkString();		// coerce assigned value
    var.Assign(vString.New(this.strval(), start, end, s));
    return vSubstring.New(this, start, start + s.length());
}

vDescriptor Index(vValue i) {			// s[i]
    this.strval();	// validate
    int m = this.posEq(i.mkInteger().value);
    if (m == 0) {
	return null; /*FAIL*/
    }
    return vSubstring.New(var, m, m+1);
}

vDescriptor Section(int i, int j) {		// s[i:j]
    this.strval();	// validate
    int m = this.posEq(i);
    int n = this.posEq(j);
    if (m == 0 || n == 0) {
	return null; /*FAIL*/
    }
    if (m > n) {
	return vSubstring.New(var, n, m);
    } else {
	return vSubstring.New(var, m, n);
    }
}

vDescriptor Select() {				// ?s
    if (start == end) {
	return null; /*FAIL*/
    }
    int offset = (int) k$random.choose(end - start) + 1;
    return vSubstring.New(this, offset, offset + 1);
}

vDescriptor Bang(iClosure c) {			// !s
    int n;			//#%#% use c.oint
    if (c.PC == 1) {
	c.o = new Integer(n = start);
	c.PC = 2;
    } else {
	c.o = new Integer(n = ((Integer)c.o).intValue() + 1);
    }
    if (n >= end) {
	return null; /*FAIL*/
    } else {
	return vSubstring.New(this, n, n + 1);
    }
}



} // class vSubstring

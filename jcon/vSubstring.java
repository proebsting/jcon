package rts;

public class vSubstring extends vVariable {

    vVariable var;

    int start, end;	// substring bounds, in Icon terms



//  constructors assume valid, ordered indices in positive form

vSubstring(vVariable v, int i1, int i2) {	// construct from String
    var = v;
    start = i1;
    end = i2;
}

vString Name() {
    String vname = var.Name().value;
    return iNew.String(vname +"[" + start + ":" + end + "]");
}

//  ss.strval() -- return underlying java.lang.String value.
//
//  Verifies that the underlying variable is still a string
//  and that the indices are still in range, and return String.

String strval() 
{
    vDescriptor v = var.deref();
    if (! (v instanceof vString)) {
    	iRuntime.error(205);
    }
    String s = ((vString)v).value;
    if (end > s.length() + 1)  {
    	iRuntime.error(103, var);
    }
    return s;
}



//  ss.posEq(n) -- return positive equivalent of position n in substring ss,
//		   or zero if out of bounds

int posEq(long n)
{
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
    return iNew.String(this.strval().substring(start - 1, end - 1));
}

String report() {
    if (start + 1 == end) {
	return var.deref().report() + "[" + start + "]";
    } else {
	return var.deref().report() + "[" + start + ":" + end + "]";
    }
}



//  operators


public vVariable Assign(vValue x) {

    String xs = x.mkString().value;		// coerce assigned value
    String s = this.strval();			// get original String

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



vDescriptor Index(vValue i) {			// s[i]
    this.strval();	// validate
    int m = this.posEq(i.mkInteger().value);
    if (m == 0) {
    	return null; /*FAIL*/
    }
    return iNew.Substring(var, m, m+1);
}

vDescriptor Section(vValue i, vValue j) {	// s[i:j]
    this.strval();	// validate
    int m = this.posEq(i.mkInteger().value);
    int n = this.posEq(j.mkInteger().value);
    if (m == 0 || n == 0) {
    	return null; /*FAIL*/
    }
    if (m > n) {
	return iNew.Substring(var, n, m);
    } else {
	return iNew.Substring(var, m, n);
    }
}



vDescriptor Select() {				// ?s
    if (start == end) {
	return null; /*FAIL*/
    }
    int offset = (int) k$random.choose(end - start) + 1;
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



} // class vSubstring

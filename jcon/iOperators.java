//  iOperators.java -- Icon operators

//#%#% tfmt() methods need to be double-checked vs. v9 Icon

package rts;

public class iOperators extends iFile {


void announce() {

    declare("#", 1, "oLimit");		// check L in (E\L)

    declare(":=", 2, "oAssign");
    declare("<-", 2, "oRevAssign");
    declare("<->", 2, "oRevSwap");
    declare(":=:", 2, "oSwap");
    declare(":?", 2, "oSubjAssign");	// assign for `s ? e'

    declare("...", 3, "oToBy");

    declare(".", 2, "oField");
    declare("[]", 2, "oIndex");
    declare("[:]", 3, "oSection");
    declare("[+:]", 3, "oSectPlus");
    declare("[-:]", 3, "oSectMinus");

    declare(".", 1, "oDeref");
    declare("/", 1, "oIsNull");
    declare("\\", 1, "oIsntNull");
    declare("*", 1, "oSize");
    declare("?", 1, "oSelect");
    declare("!", 1, "oBang");

    declare("+", 1, "oNumerate");
    declare("-", 1, "oNegate");

    declare("&", 2, "oConjunction");

    declare("^", 2, "oPower");
    declare("+", 2, "oAdd");
    declare("-", 2, "oSub");
    declare("*", 2, "oMul");
    declare("/", 2, "oDiv");
    declare("%", 2, "oMod");

    declare("~", 1,  "oComplement");
    declare("**", 2, "oIntersect");
    declare("++", 2, "oUnion");
    declare("--", 2, "oDiff");

    declare("<", 2, "oNLess");
    declare("<=", 2, "oNLessEq");
    declare("=", 2, "oNEqual");
    declare("~=", 2, "oNUnequal");
    declare(">=", 2, "oNGreaterEq");
    declare(">", 2, "oNGreater");

    declare("<<", 2, "oLLess");
    declare("<<=", 2, "oLLessEq");
    declare("==", 2, "oLEqual");
    declare("~==", 2, "oLUnequal");
    declare(">>=", 2, "oLGreaterEq");
    declare(">>", 2, "oLGreater");

    declare("===", 2, "oVEqual");
    declare("~===", 2, "oVUnequal");

    declare("|||", 2, "oListConcat");

    declare("||", 2, "oConcat");
    declare("=", 1, "oTabMatch");

    declare("@", 2, "oActivate");
    declare("^", 1, "oRefresh");

    declare("!", 2, "oProcessArgs");
}


static void declare(String opr, int args, String name)
{
	iEnv.declareProc(opr, args,
	    iNew.Proc("function " + opr, "rts." + name, args));
}


} // class iOperators



//------------------------------------------  individual operators follow


public class oLimit extends iUnaryValueClosure {		//  #x
	public static iUnaryClosure instance = new oLimit();
	vDescriptor function(vDescriptor arg) {
		vInteger i = arg.mkInteger();
		if (i.value > 0) {
			return i;
		}
		return null;
	}
	String tfmt() { return "limit counter: $1"; }
}

public class oAssign extends iBinaryRefClosure {		// x1 := x2
	public static iBinaryClosure instance = new oAssign();
	vDescriptor function() {
		return argument0.Assign(argument1.deref());
	}
	String tfmt() { return "{$1 := $2}"; }
}

public class oSwap extends iBinaryRefClosure {			// x1 :=: x2
	public static iBinaryClosure instance = new oSwap();
	vDescriptor function() {
		vValue tmp = argument1.deref();
		argument1.Assign(argument0.deref());
		return argument0.Assign(tmp);
	}
	String tfmt() { return "{$1 :=: $2}"; }
}

public class oRevAssign extends iClosure {			// x1 <- x2
	vValue old;

	public vDescriptor nextval() {
		if (old == null) {
			old = arguments[0].deref();
			return arguments[0].Assign(arguments[1].deref());
		} else {
			arguments[0].Assign(old);
			return null;
		}
	}
	String tfmt() { return "{$1 <- $2}"; }
}

public class oRevSwap extends iClosure {			// x1 <-> x2
	vValue oldleft;
	vValue oldright;

	public vDescriptor nextval() {
		if (oldleft == null) {
			oldleft = arguments[0].deref();
			oldright = arguments[1].deref();
			arguments[1].Assign(oldleft);
			return arguments[0].Assign(oldright);
		} else {
			arguments[0].Assign(oldleft);
			arguments[1].Assign(oldright);
			return null;
		}
	}
	String tfmt() { return "{$1 <-> $2}"; }
}


//  special assignment operator used to assign &subject for string scanning
//  (exactly the same as := except for the error message)

public class oSubjAssign extends iBinaryRefClosure {		// s ? e assign
	public static iBinaryClosure instance = new oSubjAssign();

	vDescriptor function() {
		return argument0.Assign(argument1.deref());
	}
	String tfmt() { return "{$2 ? ..}"; }
}



public class oConjunction extends iBinaryRefClosure {		// x1 & x2
	public static iBinaryClosure instance = new oConjunction();

	vDescriptor function() {
		argument0.deref();
		return argument1;
	}
	String tfmt() { return "{$1 & $2}"; }
}



public class oToBy extends iClosure {				// i1 to i2 by i3

	long i2, i3, ivar;

	public vDescriptor nextval() {
		long i1, iprev;

		if (PC == 1) {
			i1 = arguments[0].mkInteger().value;
			i2 = arguments[1].mkInteger().value;
			i3 = arguments[2].mkInteger().value;
			if (i3 == 0) {
				iRuntime.error(211, iNew.Integer(i3));
			}
			PC = 0;
			iprev = ivar = i1;
		} else {
			iprev = ivar;
			ivar += i3;
		}	

		if (i3 > 0) {		// ascending loop

		    // test for end of loop and for overflow
		    if (ivar > i2 || ivar < iprev) {
			    return null;
		    } else {
			    return iNew.Integer(ivar);
		    }

		} else {			// descending loop

		    // test for end of loop and for overflow
		    if (ivar < i2 || ivar > iprev) {
			    return null;
		    } else {
			    return iNew.Integer(ivar);
		    }

		}


	}

	String tfmt() { return "{$1 to $2 by $3}"; }
}



public class oField extends iBinaryRefClosure {			// R . s
	public static iBinaryClosure instance = new oField();
	String s;
	vDescriptor function() {
	    return argument0.field(s = argument1.mkString().value);
	}
	String tfmt() { return "{$1 . " + s + "}"; }
}

public class oIndex extends iBinaryRefClosure {			//  x1[x2]
	public static iBinaryClosure instance = new oIndex();
	vDescriptor function() {
		return argument0.Index(argument1.deref());
	}
	String tfmt() { return "{$1[$2]}"; }
}

public class oSection extends iRefClosure {			//  x1[x2:x3]
	vDescriptor function(vDescriptor[] args) {
		return args[0].Section(args[1].deref(), args[2].deref());
	}
	String tfmt() { return "{$1[$2:$3]}"; }
}

public class oSectPlus extends iRefClosure {			//  x1[x2+:x3]
	vDescriptor function(vDescriptor[] args) {
		return args[0].Section(args[1].deref(),
			args[1].mkInteger().Add(args[2].mkInteger()));
	}
	String tfmt() { return "{$1[$2+:$3]}"; }
}

public class oSectMinus extends iRefClosure {			//  x1[x2-:x3]
	vDescriptor function(vDescriptor[] args) {
		return args[0].Section(args[1].deref(),
			args[1].mkInteger().Sub(args[2].mkInteger()));
	}
	String tfmt() { return "{$1[$2-:$3]}"; }
}




//  miscellaneous unary operators

public class oDeref extends iUnaryValueClosure {		//  .x
	public static iUnaryClosure instance = new oDeref();
	vDescriptor function(vDescriptor arg) {
		return arg;		// deref'd by caller
	}
	String tfmt() { return "{. $1}"; }
}

public class oIsNull extends iUnaryRefClosure {			//  /x
	public static iUnaryClosure instance = new oIsNull();
	vDescriptor function(vDescriptor arg) {
		return arg.isNull();
	}
	String tfmt() { return "{/$1}"; }
}

public class oIsntNull extends iUnaryRefClosure {		//  \x
	public static iUnaryClosure instance = new oIsntNull();
	vDescriptor function(vDescriptor arg) {
		return arg.isntNull();
	}
	String tfmt() { return "{\\$1}"; }
}

public class oSize extends iUnaryValueClosure {			//  *x
	public static iUnaryClosure instance = new oSize();
	vDescriptor function(vDescriptor arg) {
		return arg.Size();
	}
	String tfmt() { return "{*$1}"; }
}

public class oSelect extends iUnaryRefClosure {			//  ?x
	public static iUnaryClosure instance = new oSelect();
	vDescriptor function(vDescriptor arg) {
		return arg.Select();
	}
	String tfmt() { return "{?$1}"; }
}

public class oBang extends iClosure {				//  !x
	public vDescriptor nextval() {
		return arguments[0].Bang(this);
	}
	String tfmt() { return "{!$1}"; }
}



//  arithmetic operators

public class oNumerate extends iUnaryValueClosure {		//  +n
	public static iUnaryClosure instance = new oNumerate();
	vDescriptor function(vDescriptor arg) {
		return arg.mkNumeric();
	}
	String tfmt() { return "{+$1}"; }
}

public class oNegate extends iUnaryValueClosure {		//  -n
	public static iUnaryClosure instance = new oNegate();
	vDescriptor function(vDescriptor arg) {
		return arg.Negate();
	}
	String tfmt() { return "{-$1}"; }
}

public class oAdd extends iBinaryValueClosure {			//  n1 + n2
	public static iBinaryClosure instance = new oAdd();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.Add(argument1);
	}
	String tfmt() { return "{$1 + $2}"; }
}

public class oSub extends iBinaryValueClosure {			//  n1 - n2
	public static iBinaryClosure instance = new oSub();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.Sub(argument1);
	}
	String tfmt() { return "{$1 - $2}"; }
}

public class oMul extends iBinaryValueClosure {			//  n1 * n2
	public static iBinaryClosure instance = new oMul();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.Mul(argument1);
	}
	String tfmt() { return "{$1 * $2}"; }
}

public class oDiv extends iBinaryValueClosure {			//  n1 / n2
	public static iBinaryClosure instance = new oDiv();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.Div(argument1);
	}
	String tfmt() { return "{$1 / $2}"; }
}

public class oMod extends iBinaryValueClosure {			//  n1 % n2
	public static iBinaryClosure instance = new oMod();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.Mod(argument1);
	}
	String tfmt() { return "{$1 % $2}"; }
}

public class oPower extends iBinaryValueClosure {		//  n1 ^ n2
	public static iBinaryClosure instance = new oPower();
	vDescriptor function() {
		// NOTE: usual coercion rules do not apply to n1 ^ n2
		argument0 = argument0.mkNumeric();
		argument1 = argument1.mkNumeric();
		return argument0.Power(argument1);
	}
	String tfmt() { return "{$1 ^ $2}"; }
}



// set operations

public class oComplement extends iUnaryValueClosure {		//  ~n
	public static iUnaryClosure instance = new oComplement();
	vDescriptor function(vDescriptor arg) {
		return arg.Complement();
	}
	String tfmt() { return "{~$1}"; }
}

public class oIntersect extends iBinaryValueClosure {		//  n1 ** n2
	public static iBinaryClosure instance = new oIntersect();
	vDescriptor function() {
		return argument0.Intersect(argument1);
	}
	String tfmt() { return "{$1 ** $2}"; }
}

public class oUnion extends iBinaryValueClosure {		//  n1 ++ n2
	public static iBinaryClosure instance = new oUnion();
	vDescriptor function() {
		return argument0.Union(argument1);
	}
	String tfmt() { return "{$1 ++ $2}"; }
}

public class oDiff extends iBinaryValueClosure {		//  n1 -- n2
	public static iBinaryClosure instance = new oDiff();
	vDescriptor function() {
		return argument0.Diff(argument1);
	}
	String tfmt() { return "{$1 -- $2}"; }
}



//  numeric comparison

public class oNLess extends iBinaryValueClosure {		//  n1 < n2
	public static iBinaryClosure instance = new oNLess();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.NLess(argument1);
	}
	String tfmt() { return "{$1 < $2}"; }
}

public class oNLessEq extends iBinaryValueClosure {		//  n1 <= n2
	public static iBinaryClosure instance = new oNLessEq();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.NLessEq(argument1);
	}
	String tfmt() { return "{$1 <= $2}"; }
}

public class oNEqual extends iBinaryValueClosure {		//  n1 = n2
	public static iBinaryClosure instance = new oNEqual();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.NEqual(argument1);
	}
	String tfmt() { return "{$1 = $2}"; }
}

public class oNUnequal extends iBinaryValueClosure {		//  n1 ~= n2
	public static iBinaryClosure instance = new oNUnequal();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.NUnequal(argument1);
	}
	String tfmt() { return "{$1 ~= $2}"; }
}

public class oNGreaterEq extends iBinaryValueClosure {		//  n1 >= n2
	public static iBinaryClosure instance = new oNGreaterEq();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.NGreaterEq(argument1);
	}
	String tfmt() { return "{$1 >= $2}"; }
}

public class oNGreater extends iBinaryValueClosure {		//  n1 > n2
	public static iBinaryClosure instance = new oNGreater();
	vDescriptor function() {
		vNumeric.Coerce(this);
		return argument0.NGreater(argument1);
	}
	String tfmt() { return "{$1 > $2}"; }
}



//  lexical comparison

public class oLLess extends iBinaryValueClosure {		//  n1 << n2
	public static iBinaryClosure instance = new oLLess();
	vDescriptor function() {
		argument0 = argument0.mkString();
		argument1 = argument1.mkString();
		return argument0.LLess(argument1);
	}
	String tfmt() { return "{$1 << $2}"; }
}

public class oLLessEq extends iBinaryValueClosure {		//  n1 <<= n2
	public static iBinaryClosure instance = new oLLessEq();
	vDescriptor function() {
		argument0 = argument0.mkString();
		argument1 = argument1.mkString();
		return argument0.LLessEq(argument1);
	}
	String tfmt() { return "{$1 <<= $2}"; }
}

public class oLEqual extends iBinaryValueClosure {		//  n1 == n2
	public static iBinaryClosure instance = new oLEqual();
	vDescriptor function() {
		argument0 = argument0.mkString();
		argument1 = argument1.mkString();
		return argument0.LEqual(argument1);
	}
	String tfmt() { return "{$1 == $2}"; }
}

public class oLUnequal extends iBinaryValueClosure {		//  n1 ~== n2
	public static iBinaryClosure instance = new oLUnequal();
	vDescriptor function() {
		argument0 = argument0.mkString();
		argument1 = argument1.mkString();
		return argument0.LUnequal(argument1);
	}
	String tfmt() { return "{$1 ~== $2}"; }
}

public class oLGreaterEq extends iBinaryValueClosure {		//  n1 >>= n2
	public static iBinaryClosure instance = new oLGreaterEq();
	vDescriptor function() {
		argument0 = argument0.mkString();
		argument1 = argument1.mkString();
		return argument0.LGreaterEq(argument1);
	}
	String tfmt() { return "{$1 >>= $2}"; }
}

public class oLGreater extends iBinaryValueClosure {		//  n1 >> n2
	public static iBinaryClosure instance = new oLGreater();
	vDescriptor function() {
		argument0 = argument0.mkString();
		argument1 = argument1.mkString();
		return argument0.LGreater(argument1);
	}
	String tfmt() { return "{$1 >> $2}"; }
}



// value comparison

public class oVEqual extends iBinaryValueClosure {		//  n1 === n2
	public static iBinaryClosure instance = new oVEqual();
	vDescriptor function() {
		return (argument0.equals(argument1)) ? argument1 : null;
	}
	String tfmt() { return "{$1 === $2}"; }
}

public class oVUnequal extends iBinaryValueClosure {		//  n1 ~=== n2
	public static iBinaryClosure instance = new oVUnequal();
	vDescriptor function() {
		return (argument0.equals(argument1)) ? null : argument1;
	}
	String tfmt() { return "{$1 ~=== $2}"; }
}



//  miscellaneous binary operators

public class oListConcat extends iBinaryValueClosure {		//  L1 ||| L2
	public static iBinaryClosure instance = new oListConcat();
	vDescriptor function() {
		return argument0.ListConcat(argument1);
	}
	String tfmt() { return "{$1 ||| $2}"; }
}

public class oConcat extends iBinaryValueClosure {		//  s1 || s2
	public static iBinaryClosure instance = new oConcat();
	vDescriptor function() {
		return argument0.mkString().Concat(argument1.mkString());
	}
	String tfmt() { return "{$1 || $2}"; }
}

public class oRefresh extends iUnaryValueClosure {		//  ^x
	public static iUnaryClosure instance = new oRefresh();
	vDescriptor function(vDescriptor arg) {
		return arg.Refresh();
	}
	String tfmt() { return "{ ^$1 }"; }
}

public class oActivate extends iBinaryValueClosure {		//  x @ C
	public static iBinaryClosure instance = new oActivate();
	vDescriptor function() {
		if (!(argument1 instanceof vCoexp)) {
			iRuntime.error(118, argument1);
		}
		return iEnv.cur_coexp.activate(argument0, argument1);
	}
	String tfmt() { return "{$1 @ $2}"; }
}

public class oTabMatch extends iClosure {			// =s
    iClosure tab;

    public vDescriptor nextval() {
	if (tab == null) {
	    iClosure match = new f$match();
	    vDescriptor[] args = { arguments[0].mkString() };
	    match.closure(args, this);
	    vDescriptor v = match.resume();
	    if (v == null) {
		return null;
	    }
	    args[0] = v;
	    tab = new f$tab();
	    tab.closure(args, this);
	}
	return tab.resume();
    }
}

public class oProcessArgs extends iClosure {			//  x ! y
    iClosure func;
    public vDescriptor nextval() {
        if (func == null) {
            arguments[0] = arguments[0].deref();
            vDescriptor[] a = arguments[1].mkArgs();
            func = iInterface.Instantiate(arguments[0], a, parent);
        }
        vDescriptor v = func.resume();
        this.returned = func.returned;
        return v;
    }
    String tfmt() { return "{$1 ! $2}"; }
}

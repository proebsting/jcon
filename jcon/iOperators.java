//  iOperators.java -- Icon operators

//#%#% tfmt() methods need to be double-checked vs. v9 Icon

package rts;

public class iOperators extends iFile {


void announce() {

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



public class oAssign extends iBinaryRefClosure {		// x1 := x2
	public static iClosure instance = new oAssign();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return arg0.Assign(arg1.deref());
	}
	String tfmt() { return "{$1 := $2}"; }
}

public class oSwap extends iBinaryRefClosure {			// x1 :=: x2
	public static iClosure instance = new oSwap();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vValue tmp = arg1.deref();
		arg1.Assign(arg0.deref());
		return arg0.Assign(tmp);
	}
	String tfmt() { return "{$1 :=: $2}"; }
}

public class oRevAssign extends iClosure {			// x1 <- x2
	vValue old;

	public void nextval() {
		if (old == null) {
			old = arguments[0].deref();
			retvalue = arguments[0].Assign(arguments[1].deref());
		} else {
			arguments[0].Assign(old);
			retvalue = null;
		}
	}
	String tfmt() { return "{$1 <- $2}"; }
}

public class oRevSwap extends iClosure {			// x1 <-> x2
	vValue oldleft;
	vValue oldright;

	public void nextval() {
		if (oldleft == null) {
			oldleft = arguments[0].deref();
			oldright = arguments[1].deref();
			arguments[1].Assign(oldleft);
			retvalue = arguments[0].Assign(oldright);
		} else {
			arguments[0].Assign(oldleft);
			arguments[1].Assign(oldright);
			retvalue = null;
		}
	}
	String tfmt() { return "{$1 <-> $2}"; }
}


//  special assignment operator used to assign &subject for string scanning
//  (exactly the same as := except for the error message)

public class oSubjAssign extends iBinaryRefClosure {		// s ? e assign
	public static iClosure instance = new oSubjAssign();

	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return arg0.Assign(arg1.deref());
	}
	String tfmt() { return "{$2 ? ..}"; }
}



public class oConjunction extends iBinaryRefClosure {		// x1 & x2
	public static iClosure instance = new oConjunction();

	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		arg0.deref();
		return arg1;
	}
	String tfmt() { return "{$1 & $2}"; }
}



public class oToBy extends iClosure {				// i1 to i2 by i3

	vInteger i1, i2, i3, iprev, ivar;

	public void nextval() {
		if (PC == 1) {
			for (int i = 0; i < arguments.length; i++) {
				arguments[i] = arguments[i].deref();
			}
			i1 = arguments[0].mkInteger();
			i2 = arguments[1].mkInteger();
			i3 = arguments[2].mkInteger();
			if (i3.value == 0) {
				iRuntime.error(211, i3);
			}
			PC = 0;
			iprev = ivar = i1;
		} else {
			iprev = ivar;
			ivar = iNew.Integer(ivar.value + i3.value);
		}	

		if (i3.value > 0) {		// ascending loop

		    // test for end of loop and for overflow
		    if (ivar.value > i2.value || ivar.value < iprev.value) {
			    retvalue = null;
		    } else {
			    retvalue = ivar;
		    }

		} else {			// descending loop

		    // test for end of loop and for overflow
		    if (ivar.value < i2.value || ivar.value > iprev.value) {
			    retvalue = null;
		    } else {
			    retvalue = ivar;
		    }

		}


	}

	String tfmt() { return "{$1 to $2 by $3}"; }
}



public class oField extends iBinaryRefClosure {			// R . s
	public static iClosure instance = new oField();
	String s;
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
	    return arg0.field(s = arg1.mkString().value);
	}
	String tfmt() { return "{$1 . " + s + "}"; }
}

public class oIndex extends iBinaryRefClosure {			//  x1[x2]
	public static iClosure instance = new oIndex();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return arg0.Index(arg1.deref());
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
	public static iClosure instance = new oDeref();
	vDescriptor function(vDescriptor arg) {
		return arg;		// deref'd by caller
	}
	String tfmt() { return "{. $1}"; }
}

public class oIsNull extends iUnaryRefClosure {			//  /x
	public static iClosure instance = new oIsNull();
	vDescriptor function(vDescriptor arg) {
		return arg.isNull();
	}
	String tfmt() { return "{/$1}"; }
}

public class oIsntNull extends iUnaryRefClosure {		//  \x
	public static iClosure instance = new oIsntNull();
	vDescriptor function(vDescriptor arg) {
		return arg.isntNull();
	}
	String tfmt() { return "{\\$1}"; }
}

public class oSize extends iUnaryValueClosure {			//  *x
	public static iClosure instance = new oSize();
	vDescriptor function(vDescriptor arg) {
		return arg.Size();
	}
	String tfmt() { return "{*$1}"; }
}

public class oSelect extends iUnaryRefClosure {			//  ?x
	public static iClosure instance = new oSelect();
	vDescriptor function(vDescriptor arg) {
		return arg.Select();
	}
	String tfmt() { return "{?$1}"; }
}

public class oBang extends iClosure {				//  !x
	public void nextval() {
		retvalue = arguments[0].Bang(this);
	}
	String tfmt() { return "{!$1}"; }
}



//  arithmetic operators

public class oNumerate extends iUnaryValueClosure {		//  +n
	public static iClosure instance = new oNumerate();
	vDescriptor function(vDescriptor arg) {
		return arg.mkNumeric();
	}
	String tfmt() { return "{+$1}"; }
}

public class oNegate extends iUnaryValueClosure {		//  -n
	public static iClosure instance = new oNegate();
	vDescriptor function(vDescriptor arg) {
		return arg.Negate();
	}
	String tfmt() { return "{-$1}"; }
}

public class oAdd extends iBinaryValueClosure {			//  n1 + n2
	public static iClosure instance = new oAdd();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.Add(argument1);
	}
	String tfmt() { return "{$1 + $2}"; }
}

public class oSub extends iBinaryValueClosure {			//  n1 - n2
	public static iClosure instance = new oSub();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.Sub(argument1);
	}
	String tfmt() { return "{$1 - $2}"; }
}

public class oMul extends iBinaryValueClosure {			//  n1 * n2
	public static iClosure instance = new oMul();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.Mul(argument1);
	}
	String tfmt() { return "{$1 * $2}"; }
}

public class oDiv extends iBinaryValueClosure {			//  n1 / n2
	public static iClosure instance = new oDiv();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.Div(argument1);
	}
	String tfmt() { return "{$1 / $2}"; }
}

public class oMod extends iBinaryValueClosure {			//  n1 % n2
	public static iClosure instance = new oMod();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.Mod(argument1);
	}
	String tfmt() { return "{$1 % $2}"; }
}

public class oPower extends iBinaryValueClosure {		//  n1 ^ n2
	public static iClosure instance = new oPower();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		// NOTE: usual coercion rules do not apply to n1 ^ n2
		arg0 = arg0.mkNumeric();
		arg1 = arg1.mkNumeric();
		return arg0.Power(arg1);
	}
	String tfmt() { return "{$1 ^ $2}"; }
}



// set operations

public class oComplement extends iUnaryValueClosure {		//  ~n
	public static iClosure instance = new oComplement();
	vDescriptor function(vDescriptor arg) {
		return arg.Complement();
	}
	String tfmt() { return "{~$1}"; }
}

public class oIntersect extends iBinaryValueClosure {		//  n1 ** n2
	public static iClosure instance = new oIntersect();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return arg0.Intersect(arg1);
	}
	String tfmt() { return "{$1 ** $2}"; }
}

public class oUnion extends iBinaryValueClosure {		//  n1 ++ n2
	public static iClosure instance = new oUnion();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return arg0.Union(arg1);
	}
	String tfmt() { return "{$1 ++ $2}"; }
}

public class oDiff extends iBinaryValueClosure {		//  n1 -- n2
	public static iClosure instance = new oDiff();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return arg0.Diff(arg1);
	}
	String tfmt() { return "{$1 -- $2}"; }
}



//  numeric comparison

public class oNLess extends iBinaryValueClosure {		//  n1 < n2
	public static iClosure instance = new oNLess();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.NLess(argument1);
	}
	String tfmt() { return "{$1 < $2}"; }
}

public class oNLessEq extends iBinaryValueClosure {		//  n1 <= n2
	public static iClosure instance = new oNLessEq();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.NLessEq(argument1);
	}
	String tfmt() { return "{$1 <= $2}"; }
}

public class oNEqual extends iBinaryValueClosure {		//  n1 = n2
	public static iClosure instance = new oNEqual();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.NEqual(argument1);
	}
	String tfmt() { return "{$1 = $2}"; }
}

public class oNUnequal extends iBinaryValueClosure {		//  n1 ~= n2
	public static iClosure instance = new oNUnequal();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.NUnequal(argument1);
	}
	String tfmt() { return "{$1 ~= $2}"; }
}

public class oNGreaterEq extends iBinaryValueClosure {		//  n1 >= n2
	public static iClosure instance = new oNGreaterEq();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.NGreaterEq(argument1);
	}
	String tfmt() { return "{$1 >= $2}"; }
}

public class oNGreater extends iBinaryValueClosure {		//  n1 > n2
	public static iClosure instance = new oNGreater();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		vNumeric.Coerce(this);
		return argument0.NGreater(argument1);
	}
	String tfmt() { return "{$1 > $2}"; }
}



//  lexical comparison

public class oLLess extends iBinaryValueClosure {		//  n1 << n2
	public static iClosure instance = new oLLess();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		arg0 = arg0.mkString();
		arg1 = arg1.mkString();
		return arg0.LLess(arg1);
	}
	String tfmt() { return "{$1 << $2}"; }
}

public class oLLessEq extends iBinaryValueClosure {		//  n1 <<= n2
	public static iClosure instance = new oLLessEq();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		arg0 = arg0.mkString();
		arg1 = arg1.mkString();
		return arg0.LLessEq(arg1);
	}
	String tfmt() { return "{$1 <<= $2}"; }
}

public class oLEqual extends iBinaryValueClosure {		//  n1 == n2
	public static iClosure instance = new oLEqual();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		arg0 = arg0.mkString();
		arg1 = arg1.mkString();
		return arg0.LEqual(arg1);
	}
	String tfmt() { return "{$1 == $2}"; }
}

public class oLUnequal extends iBinaryValueClosure {		//  n1 ~== n2
	public static iClosure instance = new oLUnequal();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		arg0 = arg0.mkString();
		arg1 = arg1.mkString();
		return arg0.LUnequal(arg1);
	}
	String tfmt() { return "{$1 ~== $2}"; }
}

public class oLGreaterEq extends iBinaryValueClosure {		//  n1 >>= n2
	public static iClosure instance = new oLGreaterEq();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		arg0 = arg0.mkString();
		arg1 = arg1.mkString();
		return arg0.LGreaterEq(arg1);
	}
	String tfmt() { return "{$1 >>= $2}"; }
}

public class oLGreater extends iBinaryValueClosure {		//  n1 >> n2
	public static iClosure instance = new oLGreater();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		arg0 = arg0.mkString();
		arg1 = arg1.mkString();
		return arg0.LGreater(arg1);
	}
	String tfmt() { return "{$1 >> $2}"; }
}



// value comparison

public class oVEqual extends iBinaryValueClosure {		//  n1 === n2
	public static iClosure instance = new oVEqual();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return (arg0.equals(arg1)) ? arg1 : null;
	}
	String tfmt() { return "{$1 === $2}"; }
}

public class oVUnequal extends iBinaryValueClosure {		//  n1 ~=== n2
	public static iClosure instance = new oVUnequal();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return (arg0.equals(arg1)) ? null : arg1;
	}
	String tfmt() { return "{$1 ~=== $2}"; }
}



//  miscellaneous binary operators

public class oListConcat extends iBinaryValueClosure {		//  L1 ||| L2
	public static iClosure instance = new oListConcat();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return arg0.ListConcat(arg1);
	}
	String tfmt() { return "{$1 ||| $2}"; }
}

public class oConcat extends iBinaryValueClosure {		//  s1 || s2
	public static iClosure instance = new oConcat();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		return arg0.mkString().Concat(arg1.mkString());
	}
	String tfmt() { return "{$1 || $2}"; }
}

public class oRefresh extends iUnaryValueClosure {		//  ^x
	public static iClosure instance = new oRefresh();
	vDescriptor function(vDescriptor arg) {
		return arg.Refresh();
	}
	String tfmt() { return "{ ^$1 }"; }
}

public class oActivate extends iBinaryValueClosure {		//  x @ C
	public static iClosure instance = new oActivate();
	vDescriptor function(vDescriptor arg0, vDescriptor arg1) {
		if (!(arg1 instanceof vCoexp)) {
			iRuntime.error(118, arg1);
		}
		return iEnv.cur_coexp.activate(arg0, arg1);
	}
	String tfmt() { return "{$1 @ $2}"; }
}

public class oTabMatch extends iClosure {			// =s
    iClosure tab;

    public void nextval() {
	if (tab == null) {
	    iClosure match = new f$match();
	    vDescriptor[] args = { arguments[0].mkString() };
	    match.closure(args, this);
	    match.resume();
	    if (match.retvalue == null) {
		this.retvalue = null;
		return;
	    }
	    args[0] = match.retvalue;
	    tab = new f$tab();
	    tab.closure(args, this);
	}
	tab.resume();
	this.retvalue = tab.retvalue;
    }
}

public class oProcessArgs extends iClosure {			//  x ! y
    iClosure func;
    public void nextval() {
        if (func == null) {
            arguments[0] = arguments[0].deref();
            vDescriptor[] a = arguments[1].mkArgs();
            func = iInterface.Instantiate(arguments[0], a, parent);
        }
        func.resume();
        this.retvalue = func.retvalue;
        this.returned = func.returned;
    }
    String tfmt() { return "{$1 ! $2}"; }
}

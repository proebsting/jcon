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

    declare("||", 2, "oConcat");
    declare("=", 1, "oTabMatch");

    declare("@", 2, "oActivate");
    declare("^", 1, "oRefresh");

    declare("!", 2, "oProcessArgs");
}


static void declare(String opr, int args, String name)
{
    try {
	iEnv.declareProc(opr, args, iNew.Proc(Class.forName("rts." + name), args));
    } catch (ClassNotFoundException e) {
	iRuntime.bomb("cannot declare opr " + opr + " using class " + name);
    }
}


} // class iOperators



//------------------------------------------  individual operators follow



class oAssign extends iRefClosure {			// x1 := x2
	vDescriptor function(vDescriptor[] args) {
		return args[0].Assign(args[1].deref());
	}
	String tfmt() { return "{$1 := $2}"; }
}

class oSwap extends iRefClosure {			// x1 :=: x2
	vDescriptor function(vDescriptor[] args) {
		vValue tmp = args[1].deref();
		args[1].Assign(args[0].deref());
		return args[0].Assign(tmp);
	}
	String tfmt() { return "{$1 :=: $2}"; }
}

class oRevAssign extends iClosure {			// x1 <- x2
	vValue old;

	void nextval() {
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

class oRevSwap extends iClosure {			// x1 <-> x2
	vValue oldleft;
	vValue oldright;

	void nextval() {
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

class oSubjAssign extends iRefClosure {			// s ? e assignment

	vDescriptor function(vDescriptor[] args) {
		return args[0].Assign(args[1].deref());
	}
	String tfmt() { return "{$2 ? ..}"; }
}



class oConjunction extends iRefClosure {		// x1 & x2

	vDescriptor function(vDescriptor[] args) {
		args[0].deref(); //#%#% is this correct??
		return args[1];
	}
	String tfmt() { return "{$1 & $2}"; }
}



class oToBy extends iClosure {				// i1 to i2 by i3

	vInteger i1, i2, i3, iprev, ivar;

	void nextval() {
		if (PC == 1) {
			for (int i = 0; i < arguments.length; i++) {
				arguments[i] = arguments[i].deref();
			}
			i1 = arguments[0].mkInteger();
			i2 = arguments[1].mkInteger();
			i3 = arguments[2].mkInteger();
			if (i3.value == 0) {
				iRuntime.error(211);
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



class oField extends iRefClosure {			// R . s
	vDescriptor function(vDescriptor[] args) {
	    return args[0].field((String)(this.o = args[1].mkString().value));
	}
	String tfmt() { return "{$1 . " + (String)this.o + "}"; }
}

class oIndex extends iRefClosure {			//  x1[x2]
	vDescriptor function(vDescriptor[] args) {
		return args[0].Index(args[1].deref());
	}
	String tfmt() { return "{$1[$2]}"; }
}

class oSection extends iRefClosure {			//  x1[x2:x3]
	vDescriptor function(vDescriptor[] args) {
		return args[0].Section(args[1].deref(), args[2].deref());
	}
	String tfmt() { return "{$1[$2:$3]}"; }
}

class oSectPlus extends iRefClosure {			//  x1[x2+:x3]
	vDescriptor function(vDescriptor[] args) {
		return args[0].Section(args[1].deref(),
			args[1].mkInteger().Add(args[2].mkInteger()));
	}
	String tfmt() { return "{$1[$2+:$3]}"; }
}

class oSectMinus extends iRefClosure {			//  x1[x2-:x3]
	vDescriptor function(vDescriptor[] args) {
		return args[0].Section(args[1].deref(),
			args[1].mkInteger().Sub(args[2].mkInteger()));
	}
	String tfmt() { return "{$1[$2-:$3]}"; }
}




//  miscellaneous unary operators

class oDeref extends iFunctionClosure {			//  .x
	vDescriptor function(vDescriptor[] args) {
		return args[0];		// deref'd by caller
	}
	String tfmt() { return "{.$1}"; }
}

class oIsNull extends iRefClosure {			//  /x
	vDescriptor function(vDescriptor[] args) {
		return args[0].isNull();
	}
	String tfmt() { return "{/$1}"; }
}

class oIsntNull extends iRefClosure {			//  \x
	vDescriptor function(vDescriptor[] args) {
		return args[0].isntNull();
	}
	String tfmt() { return "{\\$1}"; }
}

class oSize extends iFunctionClosure {			//  ?x
	vDescriptor function(vDescriptor[] args) {
		return args[0].Size();
	}
	String tfmt() { return "{*$1}"; }
}

class oSelect extends iRefClosure {			//  ?x
	vDescriptor function(vDescriptor[] args) {
		return args[0].Select();
	}
	String tfmt() { return "{?$1}"; }
}

class oBang extends iClosure {				//  !x
	void nextval() {
		retvalue = arguments[0].Bang(this);
	}
	String tfmt() { return "{!$1}"; }
}



//  arithmetic operators

class oNumerate extends iFunctionClosure {		//  +n
	vDescriptor function(vDescriptor[] args) {
		return args[0].mkNumeric();
	}
	String tfmt() { return "{+$1}"; }
}

class oNegate extends iFunctionClosure {		//  -n
	vDescriptor function(vDescriptor[] args) {
		return args[0].Negate();
	}
	String tfmt() { return "{-$1}"; }
}

class oPower extends iFunctionClosure {			//  n1 ^ n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Power(args[1]);
	}
	String tfmt() { return "{$1 ^ $2}"; }
}

class oAdd extends iFunctionClosure {			//  n1 + n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Add(args[1]);
	}
	String tfmt() { return "{$1 + $2}"; }
}

class oSub extends iFunctionClosure {			//  n1 - n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Sub(args[1]);
	}
	String tfmt() { return "{$1 - $2}"; }
}

class oMul extends iFunctionClosure {			//  n1 * n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Mul(args[1]);
	}
	String tfmt() { return "{$1 * $2}"; }
}

class oDiv extends iFunctionClosure {			//  n1 / n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Div(args[1]);
	}
	String tfmt() { return "{$1 / $2}"; }
}

class oMod extends iFunctionClosure {			//  n1 % n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Mod(args[1]);
	}
	String tfmt() { return "{$1 % $2}"; }
}

// set operations
class oComplement extends iFunctionClosure {		//  ~n
	vDescriptor function(vDescriptor[] args) {
		return args[0].Complement();
	}
	String tfmt() { return "{~$1}"; }
}

class oIntersect extends iFunctionClosure {		//  n1 ** n2
	vDescriptor function(vDescriptor[] args) {
		return args[0].Intersect(args[1]);
	}
	String tfmt() { return "{$1 ** $2}"; }
}

class oUnion extends iFunctionClosure {			//  n1 ++ n2
	vDescriptor function(vDescriptor[] args) {
		return args[0].Union(args[1]);
	}
	String tfmt() { return "{$1 ++ $2}"; }
}

class oDiff extends iFunctionClosure {			//  n1 -- n2
	vDescriptor function(vDescriptor[] args) {
		return args[0].Diff(args[1]);
	}
	String tfmt() { return "{$1 -- $2}"; }
}



//  numeric comparison

class oNLess extends iFunctionClosure {			//  n1 < n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NLess(args[1]);
	}
	String tfmt() { return "{$1 < $2}"; }
}

class oNLessEq extends iFunctionClosure {		//  n1 <= n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NLessEq(args[1]);
	}
	String tfmt() { return "{$1 <= $2}"; }
}

class oNEqual extends iFunctionClosure {		//  n1 = n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NEqual(args[1]);
	}
	String tfmt() { return "{$1 = $2}"; }
}

class oNUnequal extends iFunctionClosure {		//  n1 ~= n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NUnequal(args[1]);
	}
	String tfmt() { return "{$1 ~= $2}"; }
}

class oNGreaterEq extends iFunctionClosure {		//  n1 >= n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NGreaterEq(args[1]);
	}
	String tfmt() { return "{$1 >= $2}"; }
}

class oNGreater extends iFunctionClosure {		//  n1 > n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NGreater(args[1]);
	}
	String tfmt() { return "{$1 > $2}"; }
}

//  lexical comparison

class oLLess extends iFunctionClosure {			//  n1 << n2
	vDescriptor function(vDescriptor[] args) {
		args[0] = args[0].mkString();
		args[1] = args[1].mkString();
		return args[0].LLess(args[1]);
	}
	String tfmt() { return "{$1 << $2}"; }
}

class oLLessEq extends iFunctionClosure {		//  n1 <<= n2
	vDescriptor function(vDescriptor[] args) {
		args[0] = args[0].mkString();
		args[1] = args[1].mkString();
		return args[0].LLessEq(args[1]);
	}
	String tfmt() { return "{$1 <<= $2}"; }
}

class oLEqual extends iFunctionClosure {		//  n1 == n2
	vDescriptor function(vDescriptor[] args) {
		args[0] = args[0].mkString();
		args[1] = args[1].mkString();
		return args[0].LEqual(args[1]);
	}
	String tfmt() { return "{$1 == $2}"; }
}

class oLUnequal extends iFunctionClosure {		//  n1 ~== n2
	vDescriptor function(vDescriptor[] args) {
		args[0] = args[0].mkString();
		args[1] = args[1].mkString();
		return args[0].LUnequal(args[1]);
	}
	String tfmt() { return "{$1 ~== $2}"; }
}

class oLGreaterEq extends iFunctionClosure {		//  n1 >>= n2
	vDescriptor function(vDescriptor[] args) {
		args[0] = args[0].mkString();
		args[1] = args[1].mkString();
		return args[0].LGreaterEq(args[1]);
	}
	String tfmt() { return "{$1 >>= $2}"; }
}

class oLGreater extends iFunctionClosure {		//  n1 >> n2
	vDescriptor function(vDescriptor[] args) {
		args[0] = args[0].mkString();
		args[1] = args[1].mkString();
		return args[0].LGreater(args[1]);
	}
	String tfmt() { return "{$1 >> $2}"; }
}

// value comparison
class oVEqual extends iFunctionClosure {		//  n1 === n2
	vDescriptor function(vDescriptor[] args) {
		return (args[0].equals(args[1].deref())) ? args[1] : null;
	}
	String tfmt() { return "{$1 === $2}"; }
}

class oVUnequal extends iFunctionClosure {		//  n1 ~=== n2
	vDescriptor function(vDescriptor[] args) {
		return (args[0].equals(args[1].deref())) ? null : args[1];
	}
	String tfmt() { return "{$1 ~=== $2}"; }
}


//  miscellaneous binary operators

class oConcat extends iFunctionClosure {		//  s1 || s2
	vDescriptor function(vDescriptor[] args) {
		return args[0].mkString().Concat(args[1].mkString());
	}
	String tfmt() { return "{$1 || $2}"; }
}

class oRefresh extends iFunctionClosure {		//  ^x
	vDescriptor function(vDescriptor[] args) {
		return args[0].Refresh();
	}
	String tfmt() { return "{ ^$1 }"; }
}

class oActivate extends iFunctionClosure {		//  x @ C
	vDescriptor function(vDescriptor[] args) {
		return iEnv.cur_coexp.activate(args[0], args[1]);
	}
	String tfmt() { return "{$1 @ $2}"; }
}

class oTabMatch extends iClosure {			// =s
    iClosure tab;

    void nextval() {
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

class oProcessArgs extends iClosure {			//  x ! y
    iClosure func;
    void nextval() {
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

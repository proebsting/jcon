//  iOperators.java -- Icon operators

//#%#% tfmt() methods need to be double-checked vs. v9 Icon

class iOperators extends iFile {


void announce(iEnv env) {

    declare(env, ":=,2", "oAssign");
    declare(env, ":?,2", "oSubjAssign");	// assign for `s ? e'

    declare(env, "...,3", "oToBy");

    declare(env, ".,2", "oField");
    declare(env, "[],2", "oIndex");
    declare(env, "[:],3", "oSection");
    declare(env, "[+:],3", "oSectPlus");
    declare(env, "[-:],3", "oSectMinus");

    declare(env, ".,1", "oDeref");
    declare(env, "/,1", "oIsNull");
    declare(env, "\\,1", "oIsntNull");
    declare(env, "*,1", "oSize");
    declare(env, "?,1", "oSelect");
    declare(env, "!,1", "oBang");

    declare(env, "+,1", "oNumerate");
    declare(env, "-,1", "oNegate");

    declare(env, "&,2", "oConjunction");

    declare(env, "+,2", "oAdd");
    declare(env, "-,2", "oSub");
    declare(env, "*,2", "oMul");
    declare(env, "/,2", "oDiv");
    declare(env, "%,2", "oMod");

    declare(env, "~,1",  "oComplement");
    declare(env, "**,2", "oIntersect");
    declare(env, "++,2", "oUnion");
    declare(env, "--,2", "oDiff");

    declare(env, "<,2", "oNLess");
    declare(env, "<=,2", "oNLessEq");
    declare(env, "=,2", "oNEqual");
    declare(env, "~=,2", "oNUnequal");
    declare(env, ">=,2", "oNGreaterEq");
    declare(env, ">,2", "oNGreater");

    declare(env, "===,2", "oVEqual");
    declare(env, "~===,2", "oVUnequal");

    declare(env, "||,2", "oConcat");

    declare(env, "@,2", "oActivate");
}


static void declare(iEnv env, String opr, String name)
{
    try {
	env.declareProc(opr, iNew.Proc(Class.forName(name), env));
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

class oIsNull extends iFunctionClosure {		//  /x
	vDescriptor function(vDescriptor[] args) {
		return args[0].isNull();
	}
	String tfmt() { return "{/$1}"; }
}

class oIsntNull extends iFunctionClosure {		//  \x
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
		return args[0].Concat(args[1]);
	}
	String tfmt() { return "{$1 || $2}"; }
}

class oActivate extends iFunctionClosure {		//  x @ C
	vDescriptor function(vDescriptor[] args) {
		return env.cur_coexp.activate(args[0], args[1]);
	}
	String tfmt() { return "{$1 @ $2}"; }
}

//  iOperators.java -- Icon operators

class iOperators extends iFile {


void announce(iEnv env) {

    declare(env, ":=,2", "oAssign");
    declare(env, "&,2", "oConjunction");

    declare(env, "...,3", "oToBy");

    declare(env, "/,1", "oIsNull");
    declare(env, "\\,1", "oIsntNull");
    declare(env, "*,1", "oSize");
    declare(env, "?,1", "oSelect");
    declare(env, "!,1", "oBang");

    declare(env, "+,1", "oNumerate");
    declare(env, "-,1", "oNegate");


    declare(env, "+,2", "oAdd");
    declare(env, "-,2", "oSub");
    declare(env, "*,2", "oMul");
    declare(env, "/,2", "oDiv");
    declare(env, "%,2", "oMod");

    declare(env, "<,2", "oNLess");
    declare(env, "<=,2", "oNLessEq");
    declare(env, "=,2", "oNEqual");
    declare(env, "~=,2", "oNUnequal");
    declare(env, ">=,2", "oNGreaterEq");
    declare(env, ">,2", "oNGreater");

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
}



class oConjunction extends iRefClosure {		// x1 & x2

	vDescriptor function(vDescriptor[] args) {
		args[0].deref();
		return args[1];
	}
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
}



//  miscellaneous unary operators

class oIsNull extends iFunctionClosure {		//  /x
	vDescriptor function(vDescriptor[] args) {
		return args[0].isNull();
	}
}

class oIsntNull extends iFunctionClosure {		//  \x
	vDescriptor function(vDescriptor[] args) {
		return args[0].isntNull();
	}
}

class oSize extends iFunctionClosure {		//  ?x
	vDescriptor function(vDescriptor[] args) {
		return args[0].Size();
	}
}

class oSelect extends iFunctionClosure {		//  ?x
	vDescriptor function(vDescriptor[] args) {
		return args[0].Select();
	}
}

class oBang extends iClosure {		//  !x
	void nextval() {
		retvalue = arguments[0].deref().Bang(this);
	}
}



//  arithmetic operators

class oNumerate extends iFunctionClosure {		//  +n
	vDescriptor function(vDescriptor[] args) {
		return args[0].mkNumeric();
	}
}

class oNegate extends iFunctionClosure {		//  -n
	vDescriptor function(vDescriptor[] args) {
		return args[0].Negate();
	}
}

class oAdd extends iFunctionClosure {			//  n1 + n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Add(args[1]);
	}
}

class oSub extends iFunctionClosure {			//  n1 - n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Sub(args[1]);
	}
}

class oMul extends iFunctionClosure {			//  n1 * n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Mul(args[1]);
	}
}

class oDiv extends iFunctionClosure {			//  n1 / n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Div(args[1]);
	}
}

class oMod extends iFunctionClosure {			//  n1 % n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].Mod(args[1]);
	}
}



//  numeric comparison

class oNLess extends iFunctionClosure {			//  n1 < n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NLess(args[1]);
	}
}

class oNLessEq extends iFunctionClosure {		//  n1 <= n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NLessEq(args[1]);
	}
}

class oNEqual extends iFunctionClosure {		//  n1 = n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NEqual(args[1]);
	}
}

class oNUnequal extends iFunctionClosure {		//  n1 ~= n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NUnequal(args[1]);
	}
}

class oNGreaterEq extends iFunctionClosure {		//  n1 >= n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NGreaterEq(args[1]);
	}
}

class oNGreater extends iFunctionClosure {		//  n1 > n2
	vDescriptor function(vDescriptor[] args) {
		vNumeric.Coerce(args);
		return args[0].NGreater(args[1]);
	}
}

class oActivate extends iFunctionClosure {		//  n1 @ n2
	vDescriptor function(vDescriptor[] args) {
		return env.cur_coexp.activate(args[0], args[1]);
	}
}

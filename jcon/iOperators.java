//  iOperators.java -- string invodation of Icon operators

package rts;



public class iOperators {

static void announce() {
    iEnv.declareOpr(".",   1, "oDeref");
    iEnv.declareOpr(":=",  2, "oAssign");
    iEnv.declareOpr("<-",  2, "oRevAssign");
    iEnv.declareOpr(":=:", 2, "oSwap");
    iEnv.declareOpr("<->", 2, "oRevSwap");
    iEnv.declareOpr("&",   2, "oConjunction");
    iEnv.declareOpr("!",   2, "oProcessArgs");
    iEnv.declareOpr("@",   2, "oActivate");
    iEnv.declareOpr("...", 3, "oToBy");
    iEnv.declareOpr("+",   1, "oNumerate");
    iEnv.declareOpr("-",   1, "oNegate");
    iEnv.declareOpr("*",   1, "oSize");
    iEnv.declareOpr("~",   1, "oComplement");
    iEnv.declareOpr("^",   1, "oRefresh");
    iEnv.declareOpr("=",   1, "oTabMatch");
    iEnv.declareOpr("/",   1, "oIsNull");
    iEnv.declareOpr("\\",  1, "oIsntNull");
    iEnv.declareOpr("?",   1, "oSelect");
    iEnv.declareOpr("!",   1, "oBang");
    iEnv.declareOpr(".",   2, "oField");
    iEnv.declareOpr("[]",  2, "oIndex");
    iEnv.declareOpr("[:]", 3, "oSection");
    iEnv.declareOpr("[+:]",3, "oSectPlus");
    iEnv.declareOpr("[-:]",3, "oSectMinus");
    iEnv.declareOpr("+",   2, "oAdd");
    iEnv.declareOpr("-",   2, "oSub");
    iEnv.declareOpr("*",   2, "oMul");
    iEnv.declareOpr("/",   2, "oDiv");
    iEnv.declareOpr("%",   2, "oMod");
    iEnv.declareOpr("^",   2, "oPower");
    iEnv.declareOpr("<",   2, "oNLess");
    iEnv.declareOpr("<=",  2, "oNLessEq");
    iEnv.declareOpr("=",   2, "oNEqual");
    iEnv.declareOpr("~=",  2, "oNUnequal");
    iEnv.declareOpr(">=",  2, "oNGreaterEq");
    iEnv.declareOpr(">",   2, "oNGreater");
    iEnv.declareOpr("<<",  2, "oLLess");
    iEnv.declareOpr("<<=", 2, "oLLessEq");
    iEnv.declareOpr("==",  2, "oLEqual");
    iEnv.declareOpr("~==", 2, "oLUnequal");
    iEnv.declareOpr(">>=", 2, "oLGreaterEq");
    iEnv.declareOpr(">>",  2, "oLGreater");
    iEnv.declareOpr("===", 2, "oVEqual");
    iEnv.declareOpr("~===",2, "oVUnequal");
    iEnv.declareOpr("||",  2, "oConcat");
    iEnv.declareOpr("|||", 2, "oListConcat");
    iEnv.declareOpr("**",  2, "oIntersect");
    iEnv.declareOpr("++",  2, "oUnion");
    iEnv.declareOpr("--",  2, "oDiff");
}

} // class iOperators



class oDeref extends vProc1 {					//  .x
    public vDescriptor Call(vDescriptor a) {
	return a.Deref();
    }
}

class oAssign extends vProc2 {					//  v := x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Assign(b);
    }
}

class oRevAssign extends vProc2 {				//  v <- x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.RevAssign(b);
    }
}

class oSwap extends vProc2 {					//  v :=: v
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Swap(b);
    }
}

class oRevSwap extends vProc2 {					//  v <-> v
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.RevSwap(b);
    }
}

class oConjunction extends vProc2 {				//  e1 & e2
    public vDescriptor Call(vDescriptor a,vDescriptor b) {
	return a.Conjunction(b);
    }
}

class oProcessArgs extends vProc2 {				//  p ! L
    public vDescriptor Call(vDescriptor a,vDescriptor b) {
	return a.ProcessArgs(b);
    }
}

class oActivate extends vProc2 {				//  v @ C
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return b.Activate(a);	// note reversal of arg order for a @ b
    }
}

class oToBy extends vProc3 {					//  i to j by k
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.ToBy(b, c);
    }
}

class oNumerate extends vProc1 {				//  +n
    public vDescriptor Call(vDescriptor a) {
	return a.Numerate();
    }
}

class oNegate extends vProc1 {					//  -n
    public vDescriptor Call(vDescriptor a) {
	return a.Negate();
    }
}

class oSize extends vProc1 {					//  *x
    public vDescriptor Call(vDescriptor a) {
	return a.Size();
    }
}

class oComplement extends vProc1 {				//  ~x
    public vDescriptor Call(vDescriptor a) {
	return a.Complement();
    }
}

class oRefresh extends vProc1 {					//  ^C
    public vDescriptor Call(vDescriptor a) {
	return a.Refresh();
    }
}

class oTabMatch extends vProc1 {				//  =s
    public vDescriptor Call(vDescriptor a) {
	return a.TabMatch();
    }
}

class oIsNull extends vProc1 {					//  /x
    public vDescriptor Call(vDescriptor a) {
	return a.IsNull();
    }
}

class oIsntNull extends vProc1 {				//  \x
    public vDescriptor Call(vDescriptor a) {
	return a.IsntNull();
    }
}

class oSelect extends vProc1 {					//  ?x
    public vDescriptor Call(vDescriptor a) {
	return a.Select();
    }
}

class oBang extends vProc1 {					//  ~x
    public vDescriptor Call(vDescriptor a) {
	return a.Bang();
    }
}

class oField extends vProc2 {					//  R . f
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!(a.Deref() instanceof vRecord)) {  // check before converting b
	    iRuntime.error(107, a);
	}
	return a.Field(b.mkString().toString());
    }
}

class oIndex extends vProc2 {					//  x[v]
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Index(b);
    }
}

class oSection extends vProc3 {					//  x[i:j]
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.Section(b, c);
    }
}

class oSectPlus extends vProc3 {				//  x[i+:j]
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.SectPlus(b, c);
    }
}

class oSectMinus extends vProc3 {				//  x[i-:j]
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.SectMinus(b, c);
    }
}

class oAdd extends vProc2 {					//  n1 + n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Add(b);
    }
}

class oSub extends vProc2 {					//  n1 - n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Sub(b);
    }
}

class oMul extends vProc2 {					//  n1 * n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Mul(b);
    }
}

class oDiv extends vProc2 {					//  n1 / n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Div(b);
    }
}

class oMod extends vProc2 {					//  n1 % n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Mod(b);
    }
}

class oPower extends vProc2 {					//  n1 ^ n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Power(b);
    }
}

class oNLess extends vProc2 {					//  n1 < n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NLess(b);
    }
}

class oNLessEq extends vProc2 {					//  n1 <= n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NLessEq(b);
    }
}

class oNEqual extends vProc2 {					//  n1 = n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NEqual(b);
    }
}

class oNUnequal extends vProc2 {				//  n1 ~= n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NUnequal(b);
    }
}

class oNGreaterEq extends vProc2 {				//  n1 >= n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NGreaterEq(b);
    }
}

class oNGreater extends vProc2 {				//  n1 > n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NGreater(b);
    }
}

class oLLess extends vProc2 {					//  s1 << s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LLess(b);
    }
}

class oLLessEq extends vProc2 {					//  s1 <<= s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LLessEq(b);
    }
}

class oLEqual extends vProc2 {					//  s1 == s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LEqual(b);
    }
}

class oLUnequal extends vProc2 {				//  s1 ~== s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LUnequal(b);
    }
}

class oLGreaterEq extends vProc2 {				//  s1 >>= s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LGreaterEq(b);
    }
}

class oLGreater extends vProc2 {				//  s1 >> s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LGreater(b);
    }
}

class oVEqual extends vProc2 {					//  s1 === s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.VEqual(b);
    }
}

class oVUnequal extends vProc2 {				//  v1 ~=== v2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.VUnequal(b);
    }
}

class oConcat extends vProc2 {					//  s1 || s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Concat(b);
    }
}

class oListConcat extends vProc2 {				//  L1 ||| L2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.ListConcat(b);
    }
}

class oIntersect extends vProc2 {				//  x ** x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Intersect(b);
    }
}

class oUnion extends vProc2 {					//  x ++ x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Union(b);
    }
}

class oDiff extends vProc2 {					//  x -- x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Diff(b);
    }
}

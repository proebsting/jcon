//  iOperators.java -- string invodation of Icon operators

package rts;



public final class iOperators {

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

public static vProc instantiate(String name) {
	if (name.compareTo( "oDeref" ) == 0) return new oDeref();
	if (name.compareTo( "oAssign" ) == 0) return new oAssign();
	if (name.compareTo( "oRevAssign" ) == 0) return new oRevAssign();
	if (name.compareTo( "oSwap" ) == 0) return new oSwap();
	if (name.compareTo( "oRevSwap" ) == 0) return new oRevSwap();
	if (name.compareTo( "oConjunction" ) == 0) return new oConjunction();
	if (name.compareTo( "oProcessArgs" ) == 0) return new oProcessArgs();
	if (name.compareTo( "oActivate" ) == 0) return new oActivate();
	if (name.compareTo( "oToBy" ) == 0) return new oToBy();
	if (name.compareTo( "oNumerate" ) == 0) return new oNumerate();
	if (name.compareTo( "oNegate" ) == 0) return new oNegate();
	if (name.compareTo( "oSize" ) == 0) return new oSize();
	if (name.compareTo( "oComplement" ) == 0) return new oComplement();
	if (name.compareTo( "oRefresh" ) == 0) return new oRefresh();
	if (name.compareTo( "oTabMatch" ) == 0) return new oTabMatch();
	if (name.compareTo( "oIsNull" ) == 0) return new oIsNull();
	if (name.compareTo( "oIsntNull" ) == 0) return new oIsntNull();
	if (name.compareTo( "oSelect" ) == 0) return new oSelect();
	if (name.compareTo( "oBang" ) == 0) return new oBang();
	if (name.compareTo( "oField" ) == 0) return new oField();
	if (name.compareTo( "oIndex" ) == 0) return new oIndex();
	if (name.compareTo( "oSection" ) == 0) return new oSection();
	if (name.compareTo( "oSectPlus" ) == 0) return new oSectPlus();
	if (name.compareTo( "oSectMinus" ) == 0) return new oSectMinus();
	if (name.compareTo( "oAdd" ) == 0) return new oAdd();
	if (name.compareTo( "oSub" ) == 0) return new oSub();
	if (name.compareTo( "oMul" ) == 0) return new oMul();
	if (name.compareTo( "oDiv" ) == 0) return new oDiv();
	if (name.compareTo( "oMod" ) == 0) return new oMod();
	if (name.compareTo( "oPower" ) == 0) return new oPower();
	if (name.compareTo( "oNLess" ) == 0) return new oNLess();
	if (name.compareTo( "oNLessEq" ) == 0) return new oNLessEq();
	if (name.compareTo( "oNEqual" ) == 0) return new oNEqual();
	if (name.compareTo( "oNUnequal" ) == 0) return new oNUnequal();
	if (name.compareTo( "oNGreaterEq" ) == 0) return new oNGreaterEq();
	if (name.compareTo( "oNGreater" ) == 0) return new oNGreater();
	if (name.compareTo( "oLLess" ) == 0) return new oLLess();
	if (name.compareTo( "oLLessEq" ) == 0) return new oLLessEq();
	if (name.compareTo( "oLEqual" ) == 0) return new oLEqual();
	if (name.compareTo( "oLUnequal" ) == 0) return new oLUnequal();
	if (name.compareTo( "oLGreaterEq" ) == 0) return new oLGreaterEq();
	if (name.compareTo( "oLGreater" ) == 0) return new oLGreater();
	if (name.compareTo( "oVEqual" ) == 0) return new oVEqual();
	if (name.compareTo( "oVUnequal" ) == 0) return new oVUnequal();
	if (name.compareTo( "oConcat" ) == 0) return new oConcat();
	if (name.compareTo( "oListConcat" ) == 0) return new oListConcat();
	if (name.compareTo( "oIntersect" ) == 0) return new oIntersect();
	if (name.compareTo( "oUnion" ) == 0) return new oUnion();
	if (name.compareTo( "oDiff" ) == 0) return new oDiff();
	return null;
} // vProc instantiate(String)

} // class iOperators



final class oDeref extends vProc1 {				//  .x
    public vDescriptor Call(vDescriptor a) {
	return a.Deref();
    }
}

final class oAssign extends vProc2 {				//  v := x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Assign(b);
    }
}

final class oRevAssign extends vProc2 {				//  v <- x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.RevAssign(b);
    }
}

final class oSwap extends vProc2 {				//  v :=: v
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Swap(b);
    }
}

final class oRevSwap extends vProc2 {				//  v <-> v
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.RevSwap(b);
    }
}

final class oConjunction extends vProc2 {			//  e1 & e2
    public vDescriptor Call(vDescriptor a,vDescriptor b) {
	return a.Conjunction(b);
    }
}

final class oProcessArgs extends vProc2 {			//  p ! L
    public vDescriptor Call(vDescriptor a,vDescriptor b) {
	return a.ProcessArgs(b);
    }
}

final class oActivate extends vProc2 {				//  v @ C
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return b.Activate(a);	// note reversal of arg order for a @ b
    }
}

final class oToBy extends vProc3 {				//  i to j by k
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.ToBy(b, c);
    }
}

final class oNumerate extends vProc1 {				//  +n
    public vDescriptor Call(vDescriptor a) {
	return a.Numerate();
    }
}

final class oNegate extends vProc1 {				//  -n
    public vDescriptor Call(vDescriptor a) {
	return a.Negate();
    }
}

final class oSize extends vProc1 {				//  *x
    public vDescriptor Call(vDescriptor a) {
	return a.Size();
    }
}

final class oComplement extends vProc1 {			//  ~x
    public vDescriptor Call(vDescriptor a) {
	return a.Complement();
    }
}

final class oRefresh extends vProc1 {				//  ^C
    public vDescriptor Call(vDescriptor a) {
	return a.Refresh();
    }
}

final class oTabMatch extends vProc1 {				//  =s
    public vDescriptor Call(vDescriptor a) {
	return a.TabMatch();
    }
}

final class oIsNull extends vProc1 {				//  /x
    public vDescriptor Call(vDescriptor a) {
	return a.IsNull();
    }
}

final class oIsntNull extends vProc1 {				//  \x
    public vDescriptor Call(vDescriptor a) {
	return a.IsntNull();
    }
}

final class oSelect extends vProc1 {				//  ?x
    public vDescriptor Call(vDescriptor a) {
	return a.Select();
    }
}

final class oBang extends vProc1 {				//  ~x
    public vDescriptor Call(vDescriptor a) {
	return a.Bang();
    }
}

final class oField extends vProc2 {				//  R . f
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	if (!(a.Deref() instanceof vRecord)) {  // check before converting b
	    iRuntime.error(107, a);
	}
	return a.Field(b.mkString().toString());
    }
}

final class oIndex extends vProc2 {				//  x[v]
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Index(b);
    }
}

final class oSection extends vProc3 {				//  x[i:j]
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.Section(b, c);
    }
}

final class oSectPlus extends vProc3 {				//  x[i+:j]
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.SectPlus(b, c);
    }
}

final class oSectMinus extends vProc3 {				//  x[i-:j]
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.SectMinus(b, c);
    }
}

final class oAdd extends vProc2 {				//  n1 + n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Add(b);
    }
}

final class oSub extends vProc2 {				//  n1 - n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Sub(b);
    }
}

final class oMul extends vProc2 {				//  n1 * n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Mul(b);
    }
}

final class oDiv extends vProc2 {				//  n1 / n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Div(b);
    }
}

final class oMod extends vProc2 {				//  n1 % n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Mod(b);
    }
}

final class oPower extends vProc2 {				//  n1 ^ n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Power(b);
    }
}

final class oNLess extends vProc2 {				//  n1 < n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NLess(b);
    }
}

final class oNLessEq extends vProc2 {				//  n1 <= n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NLessEq(b);
    }
}

final class oNEqual extends vProc2 {				//  n1 = n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NEqual(b);
    }
}

final class oNUnequal extends vProc2 {				//  n1 ~= n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NUnequal(b);
    }
}

final class oNGreaterEq extends vProc2 {			//  n1 >= n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NGreaterEq(b);
    }
}

final class oNGreater extends vProc2 {				//  n1 > n2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.NGreater(b);
    }
}

final class oLLess extends vProc2 {				//  s1 << s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LLess(b);
    }
}

final class oLLessEq extends vProc2 {				//  s1 <<= s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LLessEq(b);
    }
}

final class oLEqual extends vProc2 {				//  s1 == s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LEqual(b);
    }
}

final class oLUnequal extends vProc2 {				//  s1 ~== s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LUnequal(b);
    }
}

final class oLGreaterEq extends vProc2 {			//  s1 >>= s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LGreaterEq(b);
    }
}

final class oLGreater extends vProc2 {				//  s1 >> s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.LGreater(b);
    }
}

final class oVEqual extends vProc2 {				//  s1 === s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.VEqual(b);
    }
}

final class oVUnequal extends vProc2 {				//  v1 ~=== v2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.VUnequal(b);
    }
}

final class oConcat extends vProc2 {				//  s1 || s2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Concat(b);
    }
}

final class oListConcat extends vProc2 {			//  L1 ||| L2
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.ListConcat(b);
    }
}

final class oIntersect extends vProc2 {				//  x ** x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Intersect(b);
    }
}

final class oUnion extends vProc2 {				//  x ++ x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Union(b);
    }
}

final class oDiff extends vProc2 {				//  x -- x
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Diff(b);
    }
}

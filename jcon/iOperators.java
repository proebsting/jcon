//  iOperators.java -- string invodation of Icon operators

package rts;



public class iOperators extends iFile{



void announce() {

    declare(".", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.Deref();
	}
    });

    declare(":=", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Assign(b);
	}
    });

    declare("<=", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.RevAssign(b);
	}
    });

    declare(":=:", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Swap(b);
	}
    });

    declare("<->", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.RevSwap(b);
	}
    });

    declare("&", new vProc2() {
	public vDescriptor Call(vDescriptor a,vDescriptor b) {
	    return a.Conjunction(b);
	}
    });

    declare("!", new vProc2() {
        public vDescriptor Call(vDescriptor a,vDescriptor b) {
	    return a.ProcessArgs(b);
	}
    });

    declare("@", new vProc2() {	// note reversal of arg order for a @ b
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return b.Activate(a);
	}
    });

    declare("...", new vProc3() {
        public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	    return a.ToBy(b, c);
	}
    });

    declare("+", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.Numerate();
	}
    });

    declare("-", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.Negate();
	}
    });

    declare("*", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.Size();
	}
    });

    declare("~", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.Complement();
	}
    });

    declare("^", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.Refresh();
	}
    });

    declare("=", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.TabMatch();
	}
    });

    declare("/", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.IsNull();
	}
    });

    declare("\\", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.IsntNull();
	}
    });

    declare("?", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.Select();
	}
    });

    declare("!", new vProc1() {
	public vDescriptor Call(vDescriptor a) {
	    return a.Bang();
	}
    });

    declare(".", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    if (!(a.Deref() instanceof vRecord)) {  // check before converting b
		iRuntime.error(107, a);
	    }
	    return a.Field(b.mkString().toString());
	}
    });

    declare("[]", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Index(b);
	}
    });

    declare("[:]", new vProc3() {
        public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	    return a.Section(b, c);
	}
    });

    declare("[+:]", new vProc3() {
        public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	    return a.SectPlus(b, c);
	}
    });

    declare("[-:]", new vProc3() {
        public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	    return a.SectMinus(b, c);
	}
    });

    declare("+", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Add(b);
	}
    });

    declare("-", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Sub(b);
	}
    });

    declare("*", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Mul(b);
	}
    });

    declare("/", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Div(b);
	}
    });

    declare("%", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Mod(b);
	}
    });

    declare("^", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Power(b);
	}
    });

    declare("<", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.NLess(b);
	}
    });

    declare("<=", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.NLessEq(b);
	}
    });

    declare("=", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.NEqual(b);
	}
    });

    declare("~=", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.NUnequal(b);
	}
    });

    declare(">=", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.NGreaterEq(b);
	}
    });

    declare(">", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.NGreater(b);
	}
    });

    declare("<<", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.LLess(b);
	}
    });

    declare("<<=", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.LLessEq(b);
	}
    });

    declare("==", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.LEqual(b);
	}
    });

    declare("~==", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.LUnequal(b);
	}
    });

    declare(">>=", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.LGreaterEq(b);
	}
    });

    declare(">>", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.LGreater(b);
	}
    });

    declare("===", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.VEqual(b);
	}
    });

    declare("~===", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.VUnequal(b);
	}
    });

    declare("||", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Concat(b);
	}
    });

    declare("|||", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.ListConcat(b);
	}
    });

    declare("**", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Intersect(b);
	}
    });

    declare("++", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Union(b);
	}
    });

    declare("--", new vProc2() {
	public vDescriptor Call(vDescriptor a, vDescriptor b) {
	    return a.Diff(b);
	}
    });
}


static void declare(String s, vProc p) {

    int n;
    if (p instanceof vProc1) {
	n = 1;
    } else if (p instanceof vProc2) {
	n = 2;
    } else if (p instanceof vProc3) {
	n = 3;
    } else {
	iRuntime.bomb("bad iOperators class for " + s); 
	n = 0;
    }
    iEnv.declareProc(s, n, p);
}



} // class iOperators

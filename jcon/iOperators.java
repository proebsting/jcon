//  iOperator.java -- string invodation of Icon operators



package rts;

import java.util.*;


abstract class oOperator {
}

abstract class oUnary extends oOperator {
    abstract vDescriptor call(vDescriptor a);
}

abstract class oBinary extends oOperator {
    abstract vDescriptor call(vDescriptor a, vDescriptor b);
}

abstract class oTrinary extends oOperator {
    abstract vDescriptor call(vDescriptor a, vDescriptor b, vDescriptor c);
}



abstract class iOperators {

static Hashtable unary = new Hashtable();
static Hashtable binary = new Hashtable();
static Hashtable trinary = new Hashtable();

static {

    declare(".", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.Deref(); }
    });

    declare(":=", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Assign(b); }
    });

    declare("<=", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.RevAssign(b);}
    });

    declare(":=:", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Swap(b); }
    });

    declare("<->", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.RevSwap(b); }
    });

    declare("&", new oBinary() {
	vDescriptor call(vDescriptor a,vDescriptor b) {return a.Conjunction(b);}
    });

    declare("!", new oBinary() {
        vDescriptor call(vDescriptor a,vDescriptor b) {return a.ProcessArgs(b);}
    });

    declare("@", new oBinary() {	// note reversal of arg order for a @ b
	vDescriptor call(vDescriptor a, vDescriptor b) { return b.Activate(a); }
    });

    declare("...", new oTrinary() {
        vDescriptor call(vDescriptor a, vDescriptor b, vDescriptor c) {
	    return a.ToBy(b, c);
	}
    });

    declare("+", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.Numerate(); }
    });

    declare("-", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.Negate(); }
    });

    declare("*", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.Size(); }
    });

    declare("~", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.Complement(); }
    });

    declare("^", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.Refresh(); }
    });

    declare("=", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.TabMatch(); }
    });

    declare("/", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.IsNull(); }
    });

    declare("\\", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.IsntNull(); }
    });

    declare("?", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.Select(); }
    });

    declare("!", new oUnary() {
	vDescriptor call(vDescriptor a) { return a.Bang(); }
    });

    declare(".", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) {
	    if (!(a.Deref() instanceof vRecord)) {  // check before converting b
		iRuntime.error(107, a);
	    }
	    return a.Field(b.mkString().toString());
	}
    });

    declare("[]", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Index(b); }
    });

    declare("[:]", new oTrinary() {
        vDescriptor call(vDescriptor a, vDescriptor b, vDescriptor c) {
	    return a.Section(b, c);
	}
    });

    declare("[+:]", new oTrinary() {
        vDescriptor call(vDescriptor a, vDescriptor b, vDescriptor c) {
	    return a.SectPlus(b, c);
	}
    });

    declare("[-:]", new oTrinary() {
        vDescriptor call(vDescriptor a, vDescriptor b, vDescriptor c) {
	    return a.SectMinus(b, c);
	}
    });

    declare("+", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Add(b); }
    });

    declare("-", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Sub(b); }
    });

    declare("*", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Mul(b); }
    });

    declare("/", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Div(b); }
    });

    declare("%", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Mod(b); }
    });

    declare("^", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Power(b); }
    });

    declare("<", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.NLess(b); }
    });

    declare("<=", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.NLessEq(b); }
    });

    declare("=", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.NEqual(b); }
    });

    declare("~=", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.NUnequal(b); }
    });

    declare(">=", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) {return a.NGreaterEq(b);}
    });

    declare(">", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.NGreater(b); }
    });

    declare("<<", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.LLess(b); }
    });

    declare("<<=", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.LLessEq(b); }
    });

    declare("==", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.LEqual(b); }
    });

    declare("~==", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.LUnequal(b); }
    });

    declare(">>=", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) {return a.LGreaterEq(b);}
    });

    declare(">>", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.LGreater(b); }
    });

    declare("===", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.VEqual(b); }
    });

    declare("~===", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.VUnequal(b); }
    });

    declare("||", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Concat(b); }
    });

    declare("|||", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) {return a.ListConcat(b);}
    });

    declare("**", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Intersect(b);}
    });

    declare("++", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Union(b); }
    });

    declare("--", new oBinary() {
	vDescriptor call(vDescriptor a, vDescriptor b) { return a.Diff(b); }
    });
}


static void declare(String s, oOperator o) {
    Hashtable h;

    vString v = vString.New(s);
    if (o instanceof oUnary) {
	unary.put(v, o);
    } else if (o instanceof oBinary) {
	binary.put(v, o);
    } else if (o instanceof oTrinary) {
	trinary.put(v, o);
    } else {
	iRuntime.bomb("operator arity"); 
    }
}


static vDescriptor invoke(vString s, vDescriptor v) {
    return invoke(s, v.mkArray(126));
}

static vDescriptor invoke(vString s, vDescriptor[] a) {
    switch (a.length) {
	case 1: {
	    oUnary o = (oUnary) unary.get(s);
	    if (o == null) {
		break;
	    } else {
		return o.call(a[0]);
	    }
	}
	case 2: {
	    oBinary o = (oBinary) binary.get(s);
	    if (o == null) {
		break;
	    } else {
		return o.call(a[0], a[1]);
	    }
	}
	case 3: {
	    oTrinary o = (oTrinary) trinary.get(s);
	    if (o == null) {
		break;
	    } else {
		return o.call(a[0], a[1], a[2]);
	    }
	}
    }
    iRuntime.error(106, s);
    return null;
}



} // class iOperators

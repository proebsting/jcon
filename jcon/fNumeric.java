//  fNumeric.java -- functions operating on Icon integers and reals

package rts;

final class fNumeric extends iInstantiate {
    public static fNumeric self = new fNumeric();
    public vProc instantiate(String name) {
        if (name.equals("f$abs")) return new f$abs();
        if (name.equals("f$seq")) return new f$seq();
        if (name.equals("f$icom")) return new f$icom();
        if (name.equals("f$iand")) return new f$iand();
        if (name.equals("f$ior")) return new f$ior();
        if (name.equals("f$ixor")) return new f$ixor();
        if (name.equals("f$ishift")) return new f$ishift();
        if (name.equals("f$sqrt")) return new f$sqrt();
        if (name.equals("f$exp")) return new f$exp();
        if (name.equals("f$log")) return new f$log();
        if (name.equals("f$dtor")) return new f$dtor();
        if (name.equals("f$rtod")) return new f$rtod();
        if (name.equals("f$sin")) return new f$sin();
        if (name.equals("f$cos")) return new f$cos();
        if (name.equals("f$tan")) return new f$tan();
        if (name.equals("f$asin")) return new f$asin();
        if (name.equals("f$acos")) return new f$acos();
        if (name.equals("f$atan")) return new f$atan();
        return null;
    } // vProc instantiate(String)
}


final class f$abs extends vProc1 {				// abs(n)
    public vDescriptor Call(vDescriptor a) {
	return a.Abs();
    }
}



final class f$seq extends vProc2 {				// seq(i1,i2)
    static final vInteger vOne = vInteger.New(1);
    static final vInteger vZero = vInteger.New(0);
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	final vNumeric i1 = a.isnull() ? vOne : a.mkFixed();
	final vNumeric i2 = b.isnull() ? vOne : b.mkFixed();
	if (i2.NEqual(vZero) != null) {
	    iRuntime.error(211, b);
	}
	return new vClosure() {
	    { retval = i1; }
	    vNumeric n = i1;
	    public vDescriptor Resume() {
		return n = n.Add(i2);
	    }
	};
    }
}



//  bit-manipulation functions

final class f$icom extends vProc1 {				// icom(n)
    public vDescriptor Call(vDescriptor a) {
	return a.mkFixed().Compl();
    }
}



final class f$iand extends vProc2 {				// iand(m, n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.mkFixed().And(b.mkFixed());
    }
}



final class f$ior extends vProc2 {				// ior(m, n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.mkFixed().Or(b.mkFixed());
    }
}



final class f$ixor extends vProc2 {				// ixor(m, n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.mkFixed().Xor(b.mkFixed());
    }
}



final class f$ishift extends vProc2 {				// ishift(m, n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.mkFixed().Shift(b.mkInteger());
    }
}



//  mathematical functions

final class f$sqrt extends vProc1 {				// sqrt(r)
    public vDescriptor Call(vDescriptor a) {
	double d = a.mkReal().value;
	if (d >= 0) {
	    return vReal.New(Math.sqrt(d));
	} else {
	    iRuntime.error(205, a);
	    return null;
	}
    }
}

final class f$exp extends vProc1 {				// exp(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(Math.exp(a.mkReal().value));
    }
}

final class f$log extends vProc2 {				// log(r,b)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	double r = a.mkReal().value;
	double x = b.isnull() ? Math.E : b.mkReal().value;
	if (r <= 0.0) {
	    iRuntime.error(205, a);
	}
	if (x <= 1.0) {
	    iRuntime.error(205, b);
	}
	return vReal.New(Math.log(r) / Math.log(x));
    }
}

final class f$dtor extends vProc1 {				// dtor(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(a.mkReal().value * Math.PI / 180.0);
    }
}

final class f$rtod extends vProc1 {				// rtod(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(a.mkReal().value * 180.0 / Math.PI);
    }
}

final class f$sin extends vProc1 {				// sin(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(Math.sin(a.mkReal().value));
    }
}

final class f$cos extends vProc1 {				// cos(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(Math.cos(a.mkReal().value));
    }
}

final class f$tan extends vProc1 {				// tan(r)
    public vDescriptor Call(vDescriptor a) {
	double d = Math.tan(a.mkReal().value);
	if (Double.isInfinite(d)) {
	    iRuntime.error(204);
	}
	return vReal.New(d);
    }
}

final class f$asin extends vProc1 {				// asin(r)
    public vDescriptor Call(vDescriptor a) {
	double d = Math.asin(a.mkReal().value);
	if (Double.isNaN(d)) {
	    iRuntime.error(205, a);
	}
	return vReal.New(d);
    }
}

final class f$acos extends vProc1 {				// acos(r)
    public vDescriptor Call(vDescriptor a) {
	double d = Math.acos(a.mkReal().value);
	if (Double.isNaN(d)) {
	    iRuntime.error(205, a);
	}
	return vReal.New(d);
    }
}

final class f$atan extends vProc2 {				// atan(r1,r2)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	double r1 = a.mkReal().value;
	double r2 = b.isnull() ? 1.0 : b.mkReal().value;
	if (r2 == 0.0 && r1 == 0.0) {
	    return vReal.New(0.0);	// define as 0 and avoid "domain error"
	} else {
	    return vReal.New(Math.atan2(r1, r2));
	}
    }
}

//  fNumeric.java -- functions operating on Icon integers and reals

package rts;

class fNumeric {} // dummy



class f$abs extends vProc1 {					// abs(n)
    public vDescriptor Call(vDescriptor a) {
	return a.Abs();
    }
}



class f$seq extends vProc2 {					// seq(i1,i2)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	final long i1 = a.isnull() ? 1 : a.mkInteger().value;
	final long i2 = b.isnull() ? 1 : b.mkInteger().value;
	return new vClosure() {
	    { retval = vInteger.New(i1); }
	    long n = i1;
	    public vDescriptor Resume() {
		return vInteger.New(n += i2);
	    }
	};
    }
}



//  bit-manipulation functions

class f$icom extends vProc1 {					// icom(n)
    public vDescriptor Call(vDescriptor a) {
	return vInteger.New(~a.mkInteger().value);
    }
}



class f$iand extends vProc2 {					// iand(m, n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return vInteger.New(a.mkInteger().value & b.mkInteger().value);
    }
}



class f$ior extends vProc2 {					// ior(m, n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return vInteger.New(a.mkInteger().value | b.mkInteger().value);
    }
}



class f$ixor extends vProc2 {					// ixor(m, n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return vInteger.New(a.mkInteger().value ^ b.mkInteger().value);
    }
}



class f$ishift extends vProc2 {					// ishift(m, n)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	long v = a.mkInteger().value;
	long n = b.mkInteger().value;
	if (n >= 64) {
	    return vInteger.New(0);		//#%#% ignoring overflow
	} else if (n >= 0) {
	    return vInteger.New(v << n);	//#%#% ignoring overflow
	} else if (n > -64) {
	    return vInteger.New(v >> -n);
	} else {
	    return vInteger.New(v >> 63);	// fill with sign
	}
    }
}



//  mathematical functions

class f$sqrt extends vProc1 {					// sqrt(r)
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

class f$exp extends vProc1 {					// exp(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(Math.exp(a.mkReal().value));
    }
}

class f$log extends vProc2 {					// log(r,b)
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

class f$dtor extends vProc1 {					// dtor(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(a.mkReal().value * Math.PI / 180.0);
    }
}

class f$rtod extends vProc1 {					// rtod(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(a.mkReal().value * 180.0 / Math.PI);
    }
}

class f$sin extends vProc1 {					// sin(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(Math.sin(a.mkReal().value));
    }
}

class f$cos extends vProc1 {					// cos(r)
    public vDescriptor Call(vDescriptor a) {
	return vReal.New(Math.cos(a.mkReal().value));
    }
}

class f$tan extends vProc1 {					// tan(r)
    public vDescriptor Call(vDescriptor a) {
	double d = Math.tan(a.mkReal().value);
	if (Double.isInfinite(d)) {
	    iRuntime.error(204);
	}
	return vReal.New(d);
    }
}

class f$asin extends vProc1 {					// asin(r)
    public vDescriptor Call(vDescriptor a) {
	double d = Math.asin(a.mkReal().value);
	if (Double.isNaN(d)) {
	    iRuntime.error(205, a);
	}
	return vReal.New(d);
    }
}

class f$acos extends vProc1 {					// acos(r)
    public vDescriptor Call(vDescriptor a) {
	double d = Math.acos(a.mkReal().value);
	if (Double.isNaN(d)) {
	    iRuntime.error(205, a);
	}
	return vReal.New(d);
    }
}

class f$atan extends vProc2 {					// atan(r1,r2)
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

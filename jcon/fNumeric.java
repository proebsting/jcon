//  fNumeric.java -- functions operating on Icon integers and reals

package rts;

class fNumeric {} // dummy



class f$abs extends iValueClosure {				// abs(n)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args,0).mkNumeric().Abs();
    }
}



class f$seq extends iClosure {					// seq(i1,i2)
    long i1, i2;

    public vDescriptor nextval() {
	if (PC == 1) {
	    PC = 2;
	    i1 = vInteger.argVal(arguments, 0, 1);
	    i2 = vInteger.argVal(arguments, 1, 1);
	} else {
	    i1 += i2;
	}
	return iNew.Integer(i1);
    }
}



//  bit-manipulation functions

class f$icom extends iValueClosure {				// icom(n)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Integer(~vInteger.argVal(args, 0));
    }
}



class f$iand extends iValueClosure {				// iand(n)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Integer(
	    vInteger.argVal(args, 0) & vInteger.argVal(args, 1));
    }
}



class f$ior extends iValueClosure {				// ior(n)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Integer(
	    vInteger.argVal(args, 0) | vInteger.argVal(args, 1));
    }
}



class f$ixor extends iValueClosure {				// ixor(n)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Integer(
	    vInteger.argVal(args, 0) ^ vInteger.argVal(args, 1));
    }
}



class f$ishift extends iValueClosure {				// ishift(n)
    vDescriptor function(vDescriptor[] args) {
	long v = vInteger.argVal(args, 0);
	long n = vInteger.argVal(args, 1);
	if (n >= 64) {
	    return iNew.Integer(0);		//#%#% ignoring overflow
	} else if (n >= 0) {
	    return iNew.Integer(v << n);	//#%#% ignoring overflow
	} else if (n > -64) {
	    return iNew.Integer(v >> -n);
	} else {
	    return iNew.Integer(v >> 63);	// fill with sign
	}
    }
}



//  mathematical functions

class f$sqrt extends iValueClosure {				// sqrt(r)
    vDescriptor function(vDescriptor[] args) {
	double d = vReal.argVal(args, 0);
	if (d < 0) {
	    iRuntime.error(205, args[0]);
	}
	return iNew.Real(Math.sqrt(d));
    }
}

class f$exp extends iValueClosure {				// exp(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(Math.exp(vReal.argVal(args, 0)));
    }
}

class f$log extends iValueClosure {				// log(r,b)
    vDescriptor function(vDescriptor[] args) {
	double r = vReal.argVal(args, 0);
	double b = vReal.argVal(args, 1, Math.E);
	if (r <= 0.0) {
	    iRuntime.error(205, args[0]);
	}
	if (b <= 1.0) {
	    iRuntime.error(205, args[1]);
	}
	return iNew.Real(Math.log(r) / Math.log(b));
    }
}

class f$dtor extends iValueClosure {				// dtor(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(vReal.argVal(args, 0) * Math.PI / 180.0);
    }
}

class f$rtod extends iValueClosure {				// rtod(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(vReal.argVal(args, 0) * 180.0 / Math.PI);
    }
}

class f$sin extends iValueClosure {				// sin(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(Math.sin(vReal.argVal(args, 0)));
    }
}

class f$cos extends iValueClosure {				// cos(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(Math.cos(vReal.argVal(args, 0)));
    }
}

class f$tan extends iValueClosure {				// tan(r)
    vDescriptor function(vDescriptor[] args) {
	double d = Math.tan(vReal.argVal(args, 0));
	if (Double.isInfinite(d)) {
	    iRuntime.error(204);
	}
	return iNew.Real(d);
    }
}

class f$asin extends iValueClosure {				// asin(r)
    vDescriptor function(vDescriptor[] args) {
	double d = Math.asin(vReal.argVal(args, 0));
	if (Double.isNaN(d)) {
	    iRuntime.error(205, args[0]);
	}
	return iNew.Real(d);
    }
}

class f$acos extends iValueClosure {				// acos(r)
    vDescriptor function(vDescriptor[] args) {
	double d = Math.acos(vReal.argVal(args, 0));
	if (Double.isNaN(d)) {
	    iRuntime.error(205, args[0]);
	}
	return iNew.Real(d);
    }
}

class f$atan extends iValueClosure {				// atan(r1,r2)
    vDescriptor function(vDescriptor[] args) {
	double r1 = vReal.argVal(args, 0);
	double r2 = vReal.argVal(args, 1, 1.0);
	if (r2 == 0.0 && r1 == 0.0) {
	    return iNew.Real(0.0);	// define as 0 and avoid "domain error"
	} else {
	    return iNew.Real(Math.atan2(r1, r2));
	}
    }
}

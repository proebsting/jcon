//  fNumeric.java -- functions operating on Icon integers and reals

package rts;



class f$abs extends iFunctionClosure {				// abs(n)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args,0).mkNumeric().Abs();
    }
}



class f$icom extends iFunctionClosure {				// icom(n)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args,0).mkInteger().ICom();
    }
}



class f$iand extends iFunctionClosure {				// iand(n)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args,0).mkInteger().IAnd(
		iRuntime.argVal(args,1).mkInteger());
    }
}



class f$ior extends iFunctionClosure {				// ior(n)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args,0).mkInteger().IOr(
		iRuntime.argVal(args,1).mkInteger());
    }
}



class f$ixor extends iFunctionClosure {				// ixor(n)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args,0).mkInteger().IXor(
		iRuntime.argVal(args,1).mkInteger());
    }
}



class f$ishift extends iFunctionClosure {			// ishift(n)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args,0).mkInteger().IShift(
		iRuntime.argVal(args,1).mkInteger());
    }
}



//  mathematical functions

class f$sqrt extends iFunctionClosure {				// sqrt(r)
    vDescriptor function(vDescriptor[] args) {
	double d = vReal.argVal(args, 0);
	if (d < 0) {
	    iRuntime.error(205, args[0]);
	}
	return iNew.Real(Math.sqrt(d));
    }
}

class f$exp extends iFunctionClosure {				// exp(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(Math.exp(vReal.argVal(args, 0)));
    }
}

class f$log extends iFunctionClosure {				// log(r,b)
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

class f$dtor extends iFunctionClosure {				// dtor(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(vReal.argVal(args, 0) * Math.PI / 180.0); 
    }
}

class f$rtod extends iFunctionClosure {				// rtod(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(vReal.argVal(args, 0) * 180.0 / Math.PI); 
    }
}

class f$sin extends iFunctionClosure {				// sin(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(Math.sin(vReal.argVal(args, 0)));
    }
}

class f$cos extends iFunctionClosure {				// cos(r)
    vDescriptor function(vDescriptor[] args) {
	return iNew.Real(Math.cos(vReal.argVal(args, 0)));
    }
}

class f$tan extends iFunctionClosure {				// tan(r)
    vDescriptor function(vDescriptor[] args) {
	double d = Math.tan(vReal.argVal(args, 0));
	if (Double.isInfinite(d)) {
	    iRuntime.error(204);
	}
	return iNew.Real(d);
    }
}

class f$asin extends iFunctionClosure {				// asin(r)
    vDescriptor function(vDescriptor[] args) {
	double d = Math.asin(vReal.argVal(args, 0));
	if (Double.isNaN(d)) {
	    iRuntime.error(205, args[0]);
	}
	return iNew.Real(d);
    }
}

class f$acos extends iFunctionClosure {				// acos(r)
    vDescriptor function(vDescriptor[] args) {
	double d = Math.acos(vReal.argVal(args, 0));
	if (Double.isNaN(d)) {
	    iRuntime.error(205, args[0]);
	}
	return iNew.Real(d);
    }
}

class f$atan extends iFunctionClosure {				// atan(r1,r2)
    vDescriptor function(vDescriptor[] args) {
	double r1 = vReal.argVal(args, 0);
	double r2 = vReal.argVal(args, 1, 1.0);
	return iNew.Real(Math.atan2(r1, r2));
    }
}

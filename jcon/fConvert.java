//  fConvert.java -- type conversion functions

package rts;

class fConvert {} //dummy



class f$type extends iValueClosure {				// type(x)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args, 0).Type();
    }
}



class f$integer extends iValueClosure {				// integer(x)
    vDescriptor function(vDescriptor[] args) {
	try {
	    return iRuntime.argVal(args, 0).mkInteger();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



class f$numeric extends iValueClosure {				// numeric(x)
    vDescriptor function(vDescriptor[] args) {
	try {
	    return iRuntime.argVal(args, 0).Numerate();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



class f$real extends iValueClosure {				// real(x)
    vDescriptor function(vDescriptor[] args) {
	try {
	    return iRuntime.argVal(args, 0).mkReal();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



class f$string extends iValueClosure {				// string(x)
    vDescriptor function(vDescriptor[] args) {
	try {
	    return iRuntime.argVal(args, 0).mkString();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



class f$image extends iValueClosure {				// image(x)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args, 0).image();
    }
}



class f$cset extends iValueClosure {				// cset(x)
    vDescriptor function(vDescriptor[] args) {
	try {
	    return iRuntime.argVal(args, 0).mkCset();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}

//  fConvert.java -- type conversion functions

package rts;

class fConvert {} //dummy



class f$type extends vProc1 {					// type(x)
    public vDescriptor Call(vDescriptor a) {
	return a.Type();
    }
}



class f$image extends vProc1 {					// image(x)
    public vDescriptor Call(vDescriptor a) {
	return a.image();
    }
}



class f$integer extends vProc1 {				// integer(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.mkInteger();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



class f$numeric extends vProc1 {				// numeric(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.Numerate();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



class f$real extends vProc1 {					// real(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.mkReal();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



class f$string extends vProc1 {					// string(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.mkString();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



class f$cset extends vProc1 {					// cset(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.mkCset();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}

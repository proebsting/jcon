//  fConvert.java -- type conversion functions

package rts;

final class fConvert {} //dummy



final class f$type extends vProc1 {				// type(x)
    public vDescriptor Call(vDescriptor a) {
	return a.Type();
    }
}



final class f$image extends vProc1 {				// image(x)
    public vDescriptor Call(vDescriptor a) {
	return a.image();
    }
}



final class f$integer extends vProc1 {				// integer(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.mkInteger();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



final class f$numeric extends vProc1 {				// numeric(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.Numerate();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



final class f$real extends vProc1 {				// real(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.mkReal();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



final class f$string extends vProc1 {				// string(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.mkString();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}



final class f$cset extends vProc1 {				// cset(x)
    public vDescriptor Call(vDescriptor a) {
	try {
	    return a.mkCset();
	} catch (iError e) {
	    return null; /*FAIL*/
	}
    }
}

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

//  vProc5 -- parent class for procedures declared with 5 arguments

package rts;

public abstract class vProc5 extends vProc {

static vNull vnull = vNull.New();



public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e);



public vDescriptor Call(vDescriptor v[]) {
	vDescriptor a = (v.length > 0) ? v[0] : vnull;
	vDescriptor b = (v.length > 1) ? v[1] : vnull;
	vDescriptor c = (v.length > 2) ? v[2] : vnull;
	vDescriptor d = (v.length > 3) ? v[3] : vnull;
	vDescriptor e = (v.length > 4) ? v[4] : vnull;
	return Call(a, b, c, d, e);
}

public vDescriptor Call() {
	return Call(vnull, vnull, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a) {
	return Call(a, vnull, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return Call(a, b, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return Call(a, b, c, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	return Call(a, b, c, d, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return Call(a, b, c, d, e);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return Call(a, b, c, d, e);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	return Call(a, b, c, d, e);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return Call(a, b, c, d, e);
}



} // class vProc5

//  vProc8 -- parent class for procedures declared with 8 arguments

package rts;

public abstract class vProc8 extends vProc {

static vNull vnull = vNull.New();



public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h);



public vDescriptor Call(vDescriptor v[]) {
	vDescriptor a = (v.length > 0) ? v[0] : vnull;
	vDescriptor b = (v.length > 1) ? v[1] : vnull;
	vDescriptor c = (v.length > 2) ? v[2] : vnull;
	vDescriptor d = (v.length > 3) ? v[3] : vnull;
	vDescriptor e = (v.length > 4) ? v[4] : vnull;
	vDescriptor f = (v.length > 5) ? v[5] : vnull;
	vDescriptor g = (v.length > 6) ? v[6] : vnull;
	vDescriptor h = (v.length > 7) ? v[7] : vnull;
	return Call(a, b, c, d, e, f, g, h);
}

public vDescriptor Call() {
	return Call(vnull, vnull, vnull, vnull, vnull, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a) {
	return Call(a, vnull, vnull, vnull, vnull, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return Call(a, b, vnull, vnull, vnull, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return Call(a, b, c, vnull, vnull, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	return Call(a, b, c, d, vnull, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	return Call(a, b, c, d, e, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return Call(a, b, c, d, e, f, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return Call(a, b, c, d, e, f, g, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return Call(a, b, c, d, e, f, g, h);
}



} // class vProc8

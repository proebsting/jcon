//  vProc4 -- parent class for procedures declared with 4 arguments

package jcon;

public abstract class vProc4 extends vProc {

static vNull vnull = vNull.New();



public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d);



public vDescriptor Call(vDescriptor v[]) {
	vDescriptor a = (v.length > 0) ? v[0] : vnull;
	vDescriptor b = (v.length > 1) ? v[1] : vnull;
	vDescriptor c = (v.length > 2) ? v[2] : vnull;
	vDescriptor d = (v.length > 3) ? v[3] : vnull;
	return Call(a, b, c, d);
}

public vDescriptor Call() {
	return Call(vnull, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a) {
	return Call(a, vnull, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return Call(a, b, vnull, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return Call(a, b, c, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	return Call(a, b, c, d);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return Call(a, b, c, d);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return Call(a, b, c, d);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	return Call(a, b, c, d);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return Call(a, b, c, d);
}



} // class vProc4

//  vProc2 -- parent class for procedures declared with 2 arguments

package jcon;

public abstract class vProc2 extends vProc {

static vNull vnull = vNull.New();



public abstract vDescriptor Call(vDescriptor a, vDescriptor b);



public vDescriptor Call(vDescriptor v[]) {
	vDescriptor a = (v.length > 0) ? v[0] : vnull;
	vDescriptor b = (v.length > 1) ? v[1] : vnull;
	return Call(a, b);
}

public vDescriptor Call() {
	return Call(vnull, vnull);
}

public vDescriptor Call(vDescriptor a) {
	return Call(a, vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return Call(a, b);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	return Call(a, b);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	return Call(a, b);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return Call(a, b);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return Call(a, b);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	return Call(a, b);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return Call(a, b);
}



} // class vProc2

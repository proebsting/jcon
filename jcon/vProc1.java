//  vProc1 -- parent class for procedures declared with 1 argument

package rts;

public abstract class vProc1 extends vProc {

static vNull vnull = vNull.New();



public abstract vDescriptor Call(vDescriptor a);  // implemented by subclass



public vDescriptor Call(vDescriptor v[]) {
	vDescriptor a = (v.length > 0) ? v[0] : vnull;
	return Call(a);
}

public vDescriptor Call() {
	return Call(vnull);
}

public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return Call(a);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return Call(a);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	return Call(a);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	return Call(a);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return Call(a);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return Call(a);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	return Call(a);
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return Call(a);
}



} // class vProc1

//  vProcV -- parent class for procedures declared with variable arglists
//	      or with more than nine fixed arguments

package jcon;

public abstract class vProcV extends vProc {



public abstract vDescriptor Call(vDescriptor v[]);   // implemented by subclass



public vDescriptor Call() {
	return Call(new vDescriptor[] {});
}

public vDescriptor Call(vDescriptor a) {
	return Call(new vDescriptor[] {a});
}

public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return Call(new vDescriptor[] {a, b});
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return Call(new vDescriptor[] {a, b, c});
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d) {
	return Call(new vDescriptor[] {a, b, c, d});
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e) {
	return Call(new vDescriptor[] {a, b, c, d, e});
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f) {
	return Call(new vDescriptor[] {a, b, c, d, e, f});
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g) {
	return Call(new vDescriptor[] {a, b, c, d, e, f, g});
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h) {
	return Call(new vDescriptor[] {a, b, c, d, e, f, g, h});
}

public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i) {
	return Call(new vDescriptor[] {a, b, c, d, e, f, g, h, i});
}



} // class vProcV

// vProcClosure creates a closure from a vProc.  Used for &main.

package rts;

public final class vProcClosure extends vClosure {
    vDescriptor vproc;
    vDescriptor[] args;
    vDescriptor closure;

public vProcClosure(vDescriptor vproc, vDescriptor[] args) {
    this.vproc = vproc;
    this.args = args;
}

public vDescriptor Resume() {
    if (closure == null) {
	if (iEnv.debugging) {
	    closure = iTrampoline.Call(null, 0, vproc, args);
	} else {
	    closure = vproc.Call(args);
	}
	return closure;
    } else {
	if (iEnv.debugging) {
	    return iTrampoline.Resume(null, 0, closure);
	} else {
	    return closure.Resume();
	}
    }
}

public vClosure refreshcopy() {
    return new vProcClosure(this.vproc, this.args);
}

} // class vProcClosure

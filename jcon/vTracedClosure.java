package rts;

public final class vTracedClosure extends vClosure {
    vDescriptor tracedFn;
    vDescriptor[] tracedArgs;

public vTracedClosure(vDescriptor tracedFn, vDescriptor[] tracedArgs, vDescriptor tracedClosure) {
    this.tracedFn = tracedFn;
    this.tracedArgs = tracedArgs;
    retval = tracedClosure;
}

public static vTracedClosure New(vDescriptor tracedFn, vDescriptor[] tracedArgs, vDescriptor tracedClosure) {
    if (tracedClosure == null) {
	return null;
    } else {
        return new vTracedClosure(tracedFn, tracedArgs, tracedClosure);
    }
}

public vDescriptor Resume() {
    return retval.Resume();
}

} // class vTracedClosure

package jcon;

public final class vTracedClosure extends vClosure {
    vProc tracedProc;
    vDescriptor[] tracedArgs;

public vTracedClosure(vProc tracedProc, vDescriptor[] tracedArgs,
				vDescriptor tracedClosure) {
    this.tracedProc = tracedProc;
    this.tracedArgs = tracedArgs;
    retval = tracedClosure;
}

public static vDescriptor New(vProc tracedProc, vDescriptor[] tracedArgs,
				vDescriptor tracedResult) {
    if (tracedResult == null) {
	return null;
    } else if (tracedResult instanceof vClosure) {
        return new vTracedClosure(tracedProc, tracedArgs, tracedResult);
    } else {
	return tracedResult;
    }
}

public vDescriptor Resume() {
    return retval.Resume();
}

} // class vTracedClosure

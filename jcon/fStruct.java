//  fStruct.java -- functions dealing with sets, tables, structures in general

package rts;

final class fStruct {} // dummy



final class f$set extends vProc1 {				// set(x)
    public vDescriptor Call(vDescriptor a) {
	return vSet.New(a.Deref());
    }
}



final class f$table extends vProc1 {				// table(x)
    public vDescriptor Call(vDescriptor a) {
	return vTable.New(a.Deref());
    }
}



final class f$insert extends vProc3 {				// insert(X,x,y)
    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
	return a.Insert(b.Deref(), c.Deref());
    }
}



final class f$member extends vProc2 {				// member(X,x)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Member(b.Deref());
    }
}



final class f$delete extends vProc2 {				// delete(X,x)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Delete(b.Deref());
    }
}



final class f$key extends vProc1 {				//  key(T)
    public vDescriptor Call(vDescriptor a) {
	return a.Key();
    }
}



final class f$copy extends vProc1 {				// copy(x)
    public vDescriptor Call(vDescriptor a) {
	return a.Copy();
    }
}



final class f$serial extends vProc1 {				// serial(x)
    public vDescriptor Call(vDescriptor a) {
	return a.Serial();
    }
}

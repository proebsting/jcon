//  fStruct.java -- functions dealing with sets, tables, structures in general

package rts;

final class fStruct extends iInstantiate {
    public static fStruct self = new fStruct();
    public vProc instantiate(String name) {
        if (name.compareTo( "f$set" ) == 0) return new f$set();
        if (name.compareTo( "f$table" ) == 0) return new f$table();
        if (name.compareTo( "f$insert" ) == 0) return new f$insert();
        if (name.compareTo( "f$member" ) == 0) return new f$member();
        if (name.compareTo( "f$delete" ) == 0) return new f$delete();
        if (name.compareTo( "f$key" ) == 0) return new f$key();
        if (name.compareTo( "f$copy" ) == 0) return new f$copy();
        if (name.compareTo( "f$serial" ) == 0) return new f$serial();
        return null;
    } // vProc instantiate(String)
}


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

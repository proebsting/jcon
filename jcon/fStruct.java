//  fStruct.java -- functions dealing with sets, tables, structures in general

package rts;

class fStruct {} // dummy



class f$set extends iValueClosure {				// set(x)
    vDescriptor function(vDescriptor[] args) {
	return vSet.New(iRuntime.argVal(args, 0));
    }
}



class f$table extends iValueClosure {				// table(x)
    vDescriptor function(vDescriptor[] args) {
	return vTable.New(iRuntime.argVal(args, 0));
    }
}



class f$insert extends iValueClosure {				// insert(X,x,y)
    vDescriptor function(vDescriptor[] args) {
	vValue X = iRuntime.argVal(args, 0, 122);
	vValue x = iRuntime.argVal(args, 1);
	vValue y = iRuntime.argVal(args, 2);
	return X.Insert(x, y);
    }
}



class f$member extends iValueClosure {				// member(X,x)
    vDescriptor function(vDescriptor[] args) {
	vValue X = iRuntime.argVal(args, 0, 122);
	return X.Member(iRuntime.argVal(args, 1));
    }
}



class f$delete extends iValueClosure {				// delete(X,x)
    vDescriptor function(vDescriptor[] args) {
	vValue X = iRuntime.argVal(args, 0, 122);
	return X.Delete(iRuntime.argVal(args, 1));
    }
}



class f$key extends iClosure {					//  key(T)
    vDescriptor c;
    public vDescriptor nextval() {
        if (PC == 1) {
            if (arguments.length == 0) {
                iRuntime.error(124);
                return null;
            }
            c = arguments[0].Key();
            if (c != null) {
                PC = 2;
                return c.Deref();
            } else {
                return null;
            }
        } else {
            return c.resume();
        }
    }
}



class f$copy extends iValueClosure {				// copy(x)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args, 0).Copy();
    }
}



class f$serial extends iValueClosure {				// serial(x)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args, 0).Serial();
    }
}

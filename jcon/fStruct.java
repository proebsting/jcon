//  fStruct.java -- functions dealing with sets and tables

package rts;



class f$set extends iFunctionClosure {				// set(x)
	vDescriptor function(vDescriptor[] args) {
		return iNew.Set(iRuntime.argVal(args, 0));
	}
}



class f$table extends iFunctionClosure {			// table(x)
	vDescriptor function(vDescriptor[] args) {
		return iNew.Table(iRuntime.argVal(args, 0));
	}
}



class f$insert extends iFunctionClosure {			// insert(X,x,y)
	vDescriptor function(vDescriptor[] args) {
		vValue X = iRuntime.argVal(args, 0, 122);
		vValue x = iRuntime.argVal(args, 1);
		vValue y = iRuntime.argVal(args, 2);
		return X.Insert(x, y);
	}
}



class f$member extends iFunctionClosure {			// member(X,x)
	vDescriptor function(vDescriptor[] args) {
		vValue X = iRuntime.argVal(args, 0, 122);
		return X.Member(iRuntime.argVal(args, 1));
	}
}



class f$delete extends iFunctionClosure {			// delete(X,x)
	vDescriptor function(vDescriptor[] args) {
		vValue X = iRuntime.argVal(args, 0, 122);
		return X.Delete(iRuntime.argVal(args, 1));
	}
}



class f$key extends iClosure {					//  key(T)
	void nextval() {
		if (arguments.length == 0) {
			iRuntime.error(124);
		}
		retvalue = arguments[0].Key(this);
	}
}

//  fList.java -- functions operating on Icon lists

package rts;



class f$list extends iFunctionClosure {				// list(i, x)
	vDescriptor function(vDescriptor[] args) {
		int i = (int) vInteger.argVal(args, 0, 0);
		vValue x = iRuntime.argVal(args, 1);
		return iNew.List(i, x);
	}
}



class f$push extends iFunctionClosure {				// push(L, x...)
	vDescriptor function(vDescriptor[] args) {
		vValue L = iRuntime.argVal(args, 0, 108);
		L.Push(iRuntime.argVal(args, 1));		// at least one
		for (int i = 2; i < args.length; i++) {
			L.Push(args[i]);
		}
		return L;
	}
}



class f$pull extends iFunctionClosure {				// pull(L)
	vDescriptor function(vDescriptor[] args) {
		return iRuntime.argVal(args, 0, 108).Pull();
	}
}



class f$pop extends iFunctionClosure {				// pop(L)
	vDescriptor function(vDescriptor[] args) {
		return iRuntime.argVal(args, 0, 108).Pop();
	}
}



class f$get extends iFunctionClosure {				// get(L)
	vDescriptor function(vDescriptor[] args) {
		return iRuntime.argVal(args, 0, 108).Get();
	}
}



class f$put extends iFunctionClosure {				// put(L, x...)
	vDescriptor function(vDescriptor[] args) {
		vValue L = iRuntime.argVal(args, 0, 108);
		L.Put(iRuntime.argVal(args, 1));		// at least one
		for (int i = 2; i < args.length; i++) {
			L.Put(args[i]);
		}
		return L;
	}
}



//  sort() process several datatypes but always produces a list

class f$sort extends iFunctionClosure {				// sort(X,i)
	vDescriptor function(vDescriptor[] args) {
		vValue x = iRuntime.argVal(args, 0, 115);
		vInteger i;
		if (args.length > 1) {
			i = iRuntime.argVal(args, 1).mkInteger();
			if (i.value < 1 || i.value > 4) {
				iRuntime.error(205, i);
			}
		} else {
			i = iNew.Integer(1);
		}
		return x.Sort(i);
	}
}

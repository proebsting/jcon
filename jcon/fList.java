//  fList.java -- functions operating on Icon lists

package rts;

class fList {} //dummy



class f$list extends iValueClosure {				// list(i, x)
    vDescriptor function(vDescriptor[] args) {
	int i = (int) vInteger.argVal(args, 0, 0);
	vValue x = iRuntime.argVal(args, 1);
	return iNew.List(i, x);
    }
}



class f$push extends iValueClosure {				// push(L, x...)
    vDescriptor function(vDescriptor[] args) {
	vValue L = iRuntime.argVal(args, 0, 108);
	L.Push(iRuntime.argVal(args, 1));	// always push at least one val
	for (int i = 2; i < args.length; i++) {
	    L.Push(args[i]);
	}
	return L;
    }
}



class f$pull extends iValueClosure {				// pull(L)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args, 0, 108).Pull();
    }
}



class f$pop extends iValueClosure {				// pop(L)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args, 0, 108).Pop();
    }
}



class f$get extends iValueClosure {				// get(L)
    vDescriptor function(vDescriptor[] args) {
	return iRuntime.argVal(args, 0, 108).Get();
    }
}



class f$put extends iValueClosure {				// put(L, x...)
    vDescriptor function(vDescriptor[] args) {
	vValue L = iRuntime.argVal(args, 0, 108);
	L.Put(iRuntime.argVal(args, 1));	// always add at least one val
	for (int i = 2; i < args.length; i++) {
	    L.Put(args[i]);
	}
	return L;
    }
    //#%#% to do (graphics): guarantee that put(L,a,b,c) is an atomic action
}



//  sort() and sortf() process several datatypes but always produce a list

class f$sort extends iValueClosure {				// sort(X,i)
    vDescriptor function(vDescriptor[] args) {
	vValue x = iRuntime.argVal(args, 0, 115);
	long i = vInteger.argVal(args, 1, 1);
	if (i < 1 || i > 4) {
	    iRuntime.error(205, args[1]);
	}
	return x.Sort((int) i);
    }
}

class f$sortf extends iValueClosure {				// sortf(X,i)
    vDescriptor function(vDescriptor[] args) {
	vValue[] a = iRuntime.argVal(args, 0, 125).mkArray();
	vInteger i = iNew.Integer(vInteger.argVal(args, 1, 1));
	if (i.value == 0) {
	    iRuntime.error(205, i);
	}
	for (int j = 0; j < a.length; j++) {
	    a[j] = new vSortElem(a[j], i);
	}
	iUtil.sort(a);
	for (int j = 0; j < a.length; j++) {
	    a[j] = ((vSortElem)a[j]).value;
	}
	return iNew.List(a);
    }
}

class vSortElem extends vValue {		// key/value pair for sortf()

    vValue key;		// value x[i] used for sorting, if any
    vValue value;	// value x used if x[i] did not exist

    vSortElem(vValue x, vInteger i) {		// constructor
	value = x;
	if (x instanceof vList || x instanceof vRecord) {
	    vDescriptor d = value.Index(i);	// null on failure
	    if (d == null) {
		key = null;
	    } else {
		key = d.deref();
	    }
	}
    }

    int compareTo(vValue v)	{
	vSortElem e = (vSortElem) v;
	int d = this.value.rank() - e.value.rank();
	if (d != 0)
	    return d;
	if (this.key == null) {
	    if (e.key == null) {
		return iUtil.compare(this.value, e.value);
	    } else {
		return -1;
	    }
	} else /* this.key != null */ {
	    if (e.key == null) {
		return 1;
	    } else {
		d = iUtil.compare(this.key, e.key);
		if (d != 0) {
		    return d;
		} else {
		    return iUtil.compare(this.value, e.value);
		}
	    }
	}
    }

    vString image()	{ return value.image().surround("<", ">"); }

    static vString typestring = iNew.String("sortf");
    vString type()	{ return typestring; }	// shouldn't ever be used
    int rank()		{ return -1; }		// never compared to other types

} // class vSortElem

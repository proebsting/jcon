//  fList.java -- functions operating on Icon lists

package rts;

final class fList {} //dummy



final class f$list extends vProc2 {				// list(i, x)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	int i = a.isnull() ? 0 : (int) a.mkInteger().value;
	return vList.New(i, b.Deref());
    }
}



final class f$push extends vProcV {				// push(L, x...)

    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Push(b.Deref());
    }

    public vDescriptor Call(vDescriptor[] v) {
	vValue L = iRuntime.arg(v, 0).Deref();
	L.Push(iRuntime.arg(v, 1).Deref());	// always push at least one val
	for (int i = 2; i < v.length; i++) {
	    L.Push(v[i].Deref());
	}
	return L;
    }
}



final class f$pull extends vProc1 {				// pull(L)
    public vDescriptor Call(vDescriptor a) {
	return a.Pull();
    }
}



final class f$pop extends vProc1 {				// pop(L)
    public vDescriptor Call(vDescriptor a) {
	return a.Pop();
    }
}



final class f$get extends vProc1 {				// get(L)
    public vDescriptor Call(vDescriptor a) {
	return a.Get();
    }
}



final class f$put extends vProcV {				// put(L, x...)

    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	return a.Put(b.Deref());
    }

    public vDescriptor Call(vDescriptor[] v) {
	vValue L = iRuntime.arg(v, 0).Deref();
	L.Put(iRuntime.arg(v, 1).Deref());	// always add at least one val
	for (int i = 2; i < v.length; i++) {
	    L.Put(v[i].Deref());
	}
	return L;
    }
}



//  sort() and sortf() process several datatypes but always produce a list

final class f$sort extends vProc2 {				// sort(X,i)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	int i = b.isnull() ? 1 : (int) b.mkInteger().value;
	if (i < 1 || i > 4) {
	    iRuntime.error(205, b);
	}
	return a.Sort(i);
    }
}

final class f$sortf extends vProc2 {				// sortf(X,i)
    public vDescriptor Call(vDescriptor a, vDescriptor b) {
	vValue[] v = a.mkArray(125);
	vInteger i = b.isnull() ? vInteger.New(1) : b.mkInteger();
	if (i.value == 0) {
	    iRuntime.error(205, i);
	}
	for (int j = 0; j < v.length; j++) {
	    v[j] = new vSortElem(v[j], i);
	}
	iSort.sort(v);
	for (int j = 0; j < v.length; j++) {
	    v[j] = ((vSortElem)v[j]).value;
	}
	return vList.New(v);
    }
}

final class vSortElem extends vValue {		// key/value pair for sortf()

    vValue key;		// value x[i] used for sorting, if any
    vValue value;	// value x used if x[i] did not exist

    vSortElem(vValue x, vInteger i) {		// constructor
	value = x;
	if (x instanceof vList || x instanceof vRecord) {
	    vDescriptor d = value.Index(i);	// null on failure
	    if (d == null) {
		key = null;
	    } else {
		key = d.Deref();
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
		return iSort.compare(this.value, e.value);
	    } else {
		return -1;
	    }
	} else /* this.key != null */ {
	    if (e.key == null) {
		return 1;
	    } else {
		d = iSort.compare(this.key, e.key);
		if (d != 0) {
		    return d;
		} else {
		    return iSort.compare(this.value, e.value);
		}
	    }
	}
    }

    public vString image()	{ return value.image().surround("<", ">"); }

    static vString typestring = vString.New("sortf");
    public vString Type()	{ return typestring; }	// shouldn't be used
    int rank()			{ return -1; }		// never compared

} // class vSortElem

package rts;

public class vSimpleVar extends vVariable {

    vValue value;		// value of variable
    String name;

    private static vSimpleVar freeList;
    private vSimpleVar next;		// linked list


// constructors

private static vSimpleVar get(String name) {
    vValue v = vNull.New();
    if (freeList != null) {
	vSimpleVar sv = freeList;
	freeList = sv.next;
	sv.next = null;
	sv.value = v;
	sv.name = name;
	return sv;
    } else {
        return new vSimpleVar(name, v);
    }
}

private static vSimpleVar get(String name, vDescriptor x) {
    vValue v = x.Deref();
    if (freeList != null) {
	vSimpleVar sv = freeList;
	freeList = sv.next;
	sv.next = null;
	sv.value = v;
	sv.name = name;
	return sv;
    } else {
        return new vSimpleVar(name, v);
    }
}

public static vSimpleVar New(String name) {
    return get(name);
}

public static vSimpleVar New(String name, vDescriptor x) {
    return get(name, x);
}

public static void Free(vSimpleVar x) {
}

public static vSimpleVar NewLocal(String name) {
    vSimpleVar sv = get(name);
    sv.next = iEnv.cur_coexp.locals;
    iEnv.cur_coexp.locals = sv;
    return sv;
}

public static vSimpleVar NewLocal(String name, vDescriptor x) {
    vSimpleVar sv = get(name, x);
    sv.next = iEnv.cur_coexp.locals;
    iEnv.cur_coexp.locals = sv;
    return sv;
}

public static void FreeLocals(vSimpleVar x) {
    if (x == null) {
	iRuntime.bomb("unexpected null in FreeLocals");
    }
    vSimpleVar p = iEnv.cur_coexp.locals;
    iEnv.cur_coexp.locals = x.next;
    for (;;) {
	p.value = null;
	vSimpleVar t = p.next;
	p.next = freeList;
	freeList = p;
	if (p == x) {
	    break;
	}
	p = t;
    }
}

vSimpleVar(String name, vValue x) {	// new vSimpleVar(name, value)
    value = x;
    this.name = name;
}



public vValue Deref()			{ return value; }

public vVariable Assign(vDescriptor x)	{ value = x.Deref(); return this; }

public vString Name()			{ return vString.New(name); }

public vString report() {
    return vString.New("(variable = " + this.value.report() + ")");
}



} // class vSimpleVar

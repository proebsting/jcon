package jcon;

public class vLocalVar extends vSimpleVar {

    private static vLocalVar freeList;
    private vLocalVar next;		// linked list

// constructors

private final static vLocalVar get(String name, vValue v) {
    if (freeList != null) {
	vLocalVar sv = freeList;
	freeList = sv.next;
	sv.next = null;
	sv.value = v;
	sv.name = name;
	return sv;
    } else {
        return new vLocalVar(name, v);
    }
}

public static vLocalVar NewLocal(String name) {
    vLocalVar lv = get(name, vNull.New());
    lv.next = iEnv.cur_coexp.locals;
    iEnv.cur_coexp.locals = lv;
    return lv;
}

public static vLocalVar NewLocal(String name, vDescriptor x) {
    vLocalVar lv = get(name, x.Deref());
    lv.next = iEnv.cur_coexp.locals;
    iEnv.cur_coexp.locals = lv;
    return lv;
}

public static vLocalVar NewLocalUnstacked(String name, vDescriptor x) {
    vLocalVar lv = get(name, x.Deref());
    return lv;
}

public static void Free(vLocalVar x) {
    vLocalVar p;
    for (p = iEnv.cur_coexp.locals; p != x; p = p.next) {
	p.value = null;
    }
    p.value = null;

    p = x.next;
    x.next = freeList;
    freeList = iEnv.cur_coexp.locals;
    iEnv.cur_coexp.locals = p;
}

vLocalVar(String name, vValue x) {	// new vLocalVar(name, value)
    super(name, x);
}

public vDescriptor DerefLocal()		{ return value; }	// a local.
public vDescriptor Return()		{ return value; }	// a local.

} // class vLocalVar

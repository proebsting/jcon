//  vDescriptor -- master abstract class.
//
//  A descriptor is the basic object of Icon operation.
//  Descriptors are used for method arguments, intermediate results, etc.
//
//  Descriptors come in three flavors (q.v.):
//	vValue		simple dereferenced value such as 3, "x", list L
//	vIndirect	active or assignable object such as &subject, s[i:j]
//	vClosure	suspended generator, including latest suspended value

package rts;

public abstract class vDescriptor {



// methods declared here must be implemented for all descriptor classes

// Type checking and conversion
abstract boolean isnull();			// runtime check for null
abstract vString mkString();			// convert to Icon string
abstract vInteger mkInteger();			// convert to Icon integer
abstract vReal mkReal();			// convert to Icon real
abstract vCset mkCset();			// convert to Icon numeric
abstract vValue[] mkArray(int errno);		// convert to array of vValues

// special-purpose string conversions
abstract vString write();			// convert for use in write()
abstract vString image();			// convert for use in image()
abstract vString report();			// convert for error, traceback

// dereferencing and assignment
public abstract vValue Deref();				// . x
public abstract vVariable Assign(vDescriptor x);	// v := x
public abstract vVariable SubjAssign(vDescriptor x);	// &subject := x  

// control-structure-like operations
public abstract vDescriptor Resume();			// Resume vClosure
public abstract vInteger Limit();			// ... \ n
public abstract vDescriptor Conjunction(vDescriptor x);	// v & x
public abstract vDescriptor ProcessArgs(vDescriptor x);	// v ! x
public abstract vDescriptor Activate(vDescriptor x);	// v @ C
public abstract vDescriptor ToBy(vDescriptor j, vDescriptor k);	// i to j by k

// simple value-returning unary operators
public abstract vNumeric Numerate();			//  +n
public abstract vNumeric Negate();			//  -n
public abstract vInteger Size();			//  *x
public abstract vValue Complement();			//  ~x
public abstract vCoexp Refresh();			//  ^x
public abstract vDescriptor TabMatch();			//  =s
public abstract vNumeric Abs();				//  abs(x)
public abstract vValue Copy();				//  copy(x)
public abstract vString Type();				//  type(x)

// other unary operators
public abstract vDescriptor IsNull();			//  /x
public abstract vDescriptor IsntNull();			//  \x
public abstract vDescriptor Select();			//  ?x
public abstract vDescriptor Bang();			//  !x
public abstract vDescriptor Key();			//  key(T)

// variable operations
public abstract vString Name();				//  name(v)
public abstract vInteger Args();			//  args(p)
public abstract vValue Proc(long i);			//  proc(s, i)

// element access
public abstract vVariable Field(String s);		//  R . s
public abstract vDescriptor Index(vDescriptor v);	//  x[v]
public abstract vDescriptor Section(vDescriptor i, vDescriptor j);   //  x[i:j]
public abstract vDescriptor SectPlus(vDescriptor i, vDescriptor j);  //  x[i+:j]
public abstract vDescriptor SectMinus(vDescriptor i, vDescriptor j); //  x[i-:j]

// simple binary operators
public abstract vNumeric Add(vDescriptor v);		//  n1 + n2
public abstract vNumeric Sub(vDescriptor v);		//  n1 - n2
public abstract vNumeric Mul(vDescriptor v);		//  n1 * n2
public abstract vNumeric Div(vDescriptor v);		//  n1 / n2
public abstract vNumeric Mod(vDescriptor v);		//  n1 % n2
public abstract vNumeric Power(vDescriptor v);		//  n1 ^ n2

public abstract vNumeric NLess(vDescriptor v);		//  n1 < n2
public abstract vNumeric NLessEq(vDescriptor v);	//  n1 <= n2
public abstract vNumeric NEqual(vDescriptor v);		//  n1 = n2
public abstract vNumeric NUnequal(vDescriptor v);	//  n1 ~= n2
public abstract vNumeric NGreaterEq(vDescriptor v);	//  n1 >= n2
public abstract vNumeric NGreater(vDescriptor v);	//  n1 > n2

public abstract vString LLess(vDescriptor v);		//  s1 << s2
public abstract vString LLessEq(vDescriptor v);		//  s1 <<= s2
public abstract vString LEqual(vDescriptor v);		//  s1 == s2
public abstract vString LUnequal(vDescriptor v);	//  s1 ~== s2
public abstract vString LGreaterEq(vDescriptor v);	//  s1 >>= s2
public abstract vString LGreater(vDescriptor v);	//  s1 >> s2

public abstract vValue VEqual(vDescriptor v);		//  v1 === v2
public abstract vValue VUnequal(vDescriptor v);		//  v1 ~=== v2

public abstract vString Concat(vDescriptor v);		//  s1 || s2
public abstract vList ListConcat(vDescriptor v);	//  L1 ||| L2

// list operations
public abstract vList Push(vDescriptor v);		//  push(L, x)
public abstract vValue Pull();				//  pull(L)
public abstract vValue Pop();				//  pop(L)
public abstract vValue Get();				//  get(L)
public abstract vList Put(vDescriptor v);		//  put(L, x)

// table/set operations
public abstract vValue Member(vDescriptor i);		//  member(X, x)
public abstract vValue Delete(vDescriptor i);		//  delete(X, x)
public abstract vValue Insert(vDescriptor i, vDescriptor val);  //  insert(X, x)

// set/cset operations
public abstract vValue Intersect(vDescriptor i);	//  x ** x
public abstract vValue Union(vDescriptor i);		//  x ++ x
public abstract vValue Diff(vDescriptor i);		//  x -- x

// structure operations
public abstract vInteger Serial();			//  serial(x)
public abstract vList Sort(int i);			//  sort(X, i)

// swapped-argument operations

abstract vNumeric AddInto(vInteger a);		// a + b ==> b.AddInto(a)
abstract vNumeric SubFrom(vInteger a);		// a - b ==> b.SubFrom(a)
abstract vNumeric MulInto(vInteger a);		// a * b ==> b.MulInto(a)
abstract vNumeric DivInto(vInteger a);		// a / b ==> b.DivInto(a)
abstract vNumeric ModInto(vInteger a);		// a % b ==> b.ModInto(a)
abstract vNumeric PowerOf(vInteger a);		// a ^ b ==> b.PowerOf(a)

abstract vNumeric BkwLess(vInteger a);		// a < b  ==> b.BkwLess(a)
abstract vNumeric BkwLessEq(vInteger a);	// a <= b ==> b.BkwLessEq(a)
abstract vNumeric BkwEqual(vInteger a);		// a = b  ==> b.BkwEqual(a)
abstract vNumeric BkwUnequal(vInteger a);	// a ~= b ==> b.BkwUnequal(a)
abstract vNumeric BkwGreaterEq(vInteger a);	// a < b  ==> b.BkwGreaterEq(a)
abstract vNumeric BkwGreater(vInteger a);	// a >= b ==> b.BkwGreaterEq(a)

abstract vNumeric AddInto(vReal a);		// a + b ==> b.AddInto(a)
abstract vNumeric SubFrom(vReal a);		// a - b ==> b.SubFrom(a)
abstract vNumeric MulInto(vReal a);		// a * b ==> b.MolInto(a)
abstract vNumeric DivInto(vReal a);		// a / b ==> b.DivInto(a)
abstract vNumeric ModInto(vReal a);		// a % b ==> b.ModInto(a)
abstract vNumeric PowerOf(vReal a);		// a ^ b ==> b.PowerOf(a)

abstract vNumeric BkwLess(vReal a);		// a < b  ==> b.BkwLess(a)
abstract vNumeric BkwLessEq(vReal a);		// a <= b ==> b.BkwLessEq(a)
abstract vNumeric BkwEqual(vReal a);		// a = b  ==> b.BkwEqual(a)
abstract vNumeric BkwUnequal(vReal a);		// a ~= b ==> b.BkwUnequal(a)
abstract vNumeric BkwGreaterEq(vReal a);	// a < b  ==> b.BkwGreaterEq(a)
abstract vNumeric BkwGreater(vReal a);		// a >= b ==> b.BkwGreaterEq(a)

// procedure calls for varargs and 0 to 9 args

public abstract vDescriptor Call(vDescriptor v[]);
public abstract vDescriptor Call();
public abstract vDescriptor Call(vDescriptor a);
public abstract vDescriptor Call(vDescriptor a, vDescriptor b);
public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c);
public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d);
public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e);
public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f);
public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g);
public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h);
public abstract vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c,
    vDescriptor d, vDescriptor e, vDescriptor f, vDescriptor g, vDescriptor h,
    vDescriptor i);



//  swapping and reversible assignment are handled here by calling Assign()

public vVariable Swap(vDescriptor v) {		// a :=: b
    vValue a = this.Deref();
    vValue b = v.Deref();
    vVariable rv;
    if ((rv = this.Assign(b)) == null || v.Assign(a) == null) {
	return null; /*FAIL*/
    }
    return rv;
}

public vDescriptor RevSwap(final vDescriptor v) {
    final vValue a = this.Deref();
    final vValue b = v.Deref();
    final vVariable rv;
    // must check for failure; order matters, too
    if ((rv = this.Assign(b)) == null || v.Assign(a) == null) {
	return null; /*FAIL*/
    }
    return new vClosure() {
	{ retval = rv; }
	public vDescriptor Resume() {
	    if (rv.Assign(a) != null) {
		v.Assign(b);
	    }
	    return null;
	}
    };
}

public vDescriptor RevAssign(vDescriptor v) {
    final vValue oldval = this.Deref();
    final vVariable rv = this.Assign(v.Deref());
    if (rv == null) {
	return null; /*FAIL*/
    }
    return new vClosure() {
	{ retval = rv; }
	public vDescriptor Resume() {
	    rv.Assign(oldval);
	    return null;
	}
    };
}



//  general instantiate() method is abstract, requiring subclass implementation

public abstract iClosure instantiate(vDescriptor[] args,iClosure parent);//#%#%



//  shortcuts with fixed-size arglists call that general method

public iClosure instantiate(
	vDescriptor arg0, vDescriptor arg1, vDescriptor arg2, iClosure parent) {
    vDescriptor[] args = { arg0, arg1, arg2 };
    return this.instantiate(args, parent);
}

public iClosure instantiate(vDescriptor arg0,vDescriptor arg1,iClosure parent) {
    vDescriptor[] args = { arg0, arg1 };
    return this.instantiate(args, parent);
}

public iClosure instantiate(vDescriptor arg0, iClosure parent) {
    vDescriptor[] args = { arg0 };
    return this.instantiate(args, parent);
}



} // class vDescriptor

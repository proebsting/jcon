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
abstract boolean iswin();			// runtime check for window
abstract vString mkString();			// convert to vString
abstract vInteger mkInteger();			// convert to vInteger
abstract vNumeric mkFixed();			// convert to vInteger/vBigInt
abstract vReal mkReal();			// convert to vReal
abstract vCset mkCset();			// convert to vCset
abstract vProc mkProc(int i);			// convert to vProc
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
public abstract vInteger Limit();			// ... \ n
public abstract vDescriptor Conjunction(vDescriptor x);	// e1 & e2
public abstract vDescriptor ProcessArgs(vDescriptor x);	// p ! L
public abstract vDescriptor Activate(vDescriptor x);	// v @ C
public abstract vDescriptor ToBy(vDescriptor j, vDescriptor k);	// i to j by k

// simple value-returning unary operators
public abstract vNumeric Numerate();			//  +n
public abstract vNumeric Negate();			//  -n
public abstract vInteger Size();			//  *x
public abstract vValue Complement();			//  ~x
public abstract vCoexp Refresh();			//  ^C
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

// element access
public abstract vVariable Field(String s);		//  R . f
public abstract vDescriptor Index(vDescriptor v);	//  x[v]
public abstract vDescriptor Section(vDescriptor i, vDescriptor j);   //  x[i:j]

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

abstract vNumeric AddInto(vBigInt a);		// a + b ==> b.AddInto(a)
abstract vNumeric SubFrom(vBigInt a);		// a - b ==> b.SubFrom(a)
abstract vNumeric MulInto(vBigInt a);		// a * b ==> b.MulInto(a)
abstract vNumeric DivInto(vBigInt a);		// a / b ==> b.DivInto(a)
abstract vNumeric ModInto(vBigInt a);		// a % b ==> b.ModInto(a)
abstract vNumeric PowerOf(vBigInt a);		// a ^ b ==> b.PowerOf(a)

abstract vNumeric BkwLess(vBigInt a);		// a < b  ==> b.BkwLess(a)
abstract vNumeric BkwLessEq(vBigInt a);		// a <= b ==> b.BkwLessEq(a)
abstract vNumeric BkwEqual(vBigInt a);		// a = b  ==> b.BkwEqual(a)
abstract vNumeric BkwUnequal(vBigInt a);	// a ~= b ==> b.BkwUnequal(a)
abstract vNumeric BkwGreaterEq(vBigInt a);	// a < b  ==> b.BkwGreaterEq(a)
abstract vNumeric BkwGreater(vBigInt a);	// a >= b ==> b.BkwGreaterEq(a)

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

// only vClosures are resumable; all other vDescriptors fail on resumption

public vDescriptor Resume()			{ return null; /*FAIL*/ }



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



//  SectPlus and SectMinus are turned into Section calls here.
//  Wraparound is detected and produces failure.
//
//  There are two kinds of position values in Icon
//	from start:  1,  2,  3, ...
//	from end:    0, -1, -2, ...
//  Wraparound occurs when (i) and (i +/- j) are of opposite type.  It is
//  checked by negating the two values, which makes the sign bit serve as
//  a classifier, and then checking for sign bit differences.
//
//  #%#% Should still give error (not failure) if object is not subscriptable.

public vDescriptor SectPlus(vDescriptor a, vDescriptor b) {	// s[i+:j]
    vInteger ai = a.mkInteger();
    long i = ai.value;
    long j = i + b.mkInteger().value;
    if ((-i ^ -j) < 0) {	// if wraparound
	return null; /*FAIL*/
    }
    return Section(ai, vInteger.New(j));
}

public vDescriptor SectMinus(vDescriptor a, vDescriptor b) {	// s[i-:j]
    vInteger ai = a.mkInteger();
    long i = ai.value;
    long j = i - b.mkInteger().value;
    if ((-i ^ -j) < 0) {	// if wraparound
	return null; /*FAIL*/
    }
    return Section(ai, vInteger.New(j));
}



} // class vDescriptor

//#%#%#% later merge with iInterface

class iRuntime {



// error(n, d) -- diagnose runtime error.
//
// never returns.
// to keep Java happy, follow error() calls with "return null" in caller.

static void error(int n)  { error(n, null); }

static void error(int n, vDescriptor d)
{
    throw new iError(n,d);
}



//  bomb(s) -- abort with message due to internal error

static void bomb(String s)
{
    System.out.flush();
    System.err.println("Runtime malfunction: " + s);
    System.exit(1);
}



//  argVal(args[], i)    -- return arg i, defaulting to &null
//  argVal(args[], i, e) -- return arg i, signalling error e if missing
//
//  (both assume that arg arrays have already been dereferenced.)

static vValue argVal(vDescriptor[] args, int index)
{
    if (args.length <= index) {
	return iNew.Null();
    } else {
    	return (vValue) args[index];
    }
}

static vValue argVal(vDescriptor[] args, int index, int errcode)
{
    if (args.length <= index) {
    	iRuntime.error(errcode);
	return null;
    } else {
    	return (vValue) args[index];
    }
}



// #%#%#% redo later to use Icon's &random etc.

static double random() {	// return double value "between 0 and 1"
    return Math.random();
}


static long random(long limit) {	// return long value in [0, limit)
    return (long) (limit * Math.random());
}




} // class iRuntime

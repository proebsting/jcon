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





// #%#%#% redo later to use Icon's &random etc.

static double random()	{ return Math.random(); }

static long random(long limit) { return (long) (limit * Math.random()); }




} // class iRuntime

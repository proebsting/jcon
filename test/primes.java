//  primes(n) generates the primes through n
//
//  (part of the loadfunc test)

import jcon.*;

public class primes extends vProc1 {

    public vDescriptor Call(vDescriptor a) {
        final int n = (int) a.mkInteger().value;
	final boolean b[] = new boolean[n+1];	// b[i] true => i is composite
	int k = (int) Math.sqrt(n);
	for (int i = 2; i <= k; i++) {
	    if (!b[i]) {
	    	for (int j = 2 * i; j <= n; j += i) {
		    b[j] = true;
		}
	    }
	}
        return new vClosure() {
            int i = 1;
            public vDescriptor Resume() {
                while (++i <= n) {
                    if (!b[i]) {
                         retval = vInteger.New(i);
                         return this;
                    }
                }
                return null; /*FAIL*/
            }
        }.Resume();
    }

}

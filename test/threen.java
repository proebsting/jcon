// threen(n) generates the 3n+1 sequence (n/2 if n even) until n < 2
//
// (part of the loadfunc test)

import rts.*;

public class threen extends vProc1 {
    public vDescriptor Call(vDescriptor a) {
	final long arg = a.mkInteger().value;
	if (arg < 2) {
	    return null; /*FAIL*/
	}
	return new vClosure() {
	    long n = arg;
	    public vDescriptor Resume() {
		if (n < 2) {
		    return null; /*FAIL*/
		} else if (n % 2 == 0) {
		    retval = vInteger.New(n = n / 2);
		} else {
		    retval = vInteger.New(n = 3 * n + 1);
		}
		return this;
	    }
	}.Resume();
    }
}

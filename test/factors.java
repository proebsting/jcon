//  factors(n) generates the factors of n
//
//  (part of the loadfunc test)

import jcon.*;

public class factors extends vProc1 {

    public vDescriptor Call(vDescriptor a) {
        final long arg = a.mkInteger().value;
        return new vClosure() {
            long n = 0;
            public vDescriptor Resume() {
                while (++n <= arg) {
                    if (arg % n == 0) {
                         retval = vInteger.New(n);
                         return this;
                    }
                }
                return null; /*FAIL*/
            }
        }.Resume();
    }

}

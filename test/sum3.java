//  sum3(i,j,k) returns the sum of three integers
//
//  (part of the loadfunc test)

import rts.*;

public class sum3 extends vProc3 {

    public vDescriptor Call(vDescriptor a, vDescriptor b, vDescriptor c) {
        vInteger i = a.mkInteger();
        vInteger j = b.mkInteger();
        vInteger k = c.mkInteger();
        return vInteger.New(i.value + j.value + k.value);
    }

}

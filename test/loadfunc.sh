#!/bin/sh
#
#  special test of loadfunc()

set -e
cat <<==EOF== >myfunc.java

	import rts.*;

	// myfunc(n) generates the 3n+1 sequence (n/2 if n even) until n < 2
	public class myfunc extends vProc1 {
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

==EOF==
CLASSPATH=../bin/rts.zip javac myfunc.java
jar cfM myfunc.zip myfunc*.class
$JCONT -s loadfunc.icn
./loadfunc >loadfunc.out
cmp loadfunc.std loadfunc.out
rm myfunc.zip myfunc.java myfunc.class

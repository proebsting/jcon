package rts;

public class Semaphore {

    int value = 0;



Semaphore()			{value = 0;}		// new Semaphore()

Semaphore(int initial)		{value = initial;}	// new Semaphore(n)

public synchronized void P() throws InterruptedException {
    value--;
    while (value < 0) {
	try {
	    wait();
	} catch (InterruptedException e) {
	    value++;
	    throw e;
	}
    }
}

public synchronized void V() {	// this technique prevents
    value++;			// barging since any caller of
    if (value <= 0) notify();	// P() will wait even if it
}				// enters before signaled thread



} // class Semaphore

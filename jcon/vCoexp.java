package rts;

public class vCoexp extends vValue implements Runnable {

	int originalPC;
	Thread thread;
	iClosure closure;
	iClosure refreshCopy;
	java.util.Stack callers;
	vDescriptor incomingValue;
	Semaphore lock;
	int snum;	// serial number


	static int nextsn = 1;		// next serial number

	int rank()	{ return 70; }	// co-expressions sort after files

	//#%#% incorp. into first constructor, call that from other one?
	void setup(Thread thread, iClosure closure) {
		this.closure = closure;
		this.thread = thread;
		this.originalPC = closure.PC;
		this.snum = nextsn++;
		callers = new java.util.Stack();
		lock = new Semaphore();
	}

	vCoexp(Thread thread, iClosure closure) {
		setup(thread, closure);
	}

	vCoexp(iClosure closure) {
		setup(new Thread(this), closure);
	}

	public void run() {
		try {
			lock.P();
		} catch (InterruptedException e) {
			iRuntime.error(500);
		}
		closure.resume();  // should never return;
	}

	public void coret(vDescriptor retValue) {
		vCoexp caller = (vCoexp) callers.pop();
		transfer(retValue, caller);
	}

	public void cofail() {
		for (;;) {
			vCoexp caller = (vCoexp) callers.pop();
			transfer(null, caller);
		}
	}

	public vDescriptor activate(vDescriptor value, vDescriptor coexp) {
		vCoexp target = (vCoexp)coexp;
		target.callers.push(this);
		transfer(value, target);
		return this.incomingValue;
	}

	void transfer(vDescriptor value, vCoexp target) {
		target.incomingValue = value;
		target.closure.env.cur_coexp = target;
		target.lock.V();
		try {
			lock.P();
		} catch (InterruptedException e) {
			iRuntime.error(500);
		}
	}

	public void create() {
		thread.start();
		this.refreshCopy = closure.copy(this.closure.PC);
	}

	vValue Refresh() {
		vCoexp c = new vCoexp(refreshCopy.copy(this.originalPC));
		c.refreshCopy = refreshCopy;
		c.thread.start();
		return c;
	}

	String type()	{ return "co-expression";}

	String image()  {			//#%#% count is bogus in image
		return "co-expression_" + snum + "(" + "0" + ")";
	}
}

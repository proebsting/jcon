class vCoexp extends vValue implements Runnable {
	Thread thread;
	iClosure closure;
	java.util.Stack callers;
	vDescriptor incomingValue;
	Semaphore lock;

	void setup(Thread thread, iClosure closure) {
		this.closure = closure;
		this.thread = thread;
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
	}

	String image()	{ return "co-expression";}	//#%#% incomplete image

	String type()	{ return "co-expression";}
}

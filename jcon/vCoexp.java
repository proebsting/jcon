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
    int resultCount;

static int nextsn = 1;			// next serial number



vCoexp(Thread thread, iClosure closure) {
    setup(thread, closure);
}

vCoexp(iClosure closure) {
    setup(new Thread(this), closure);
}

// can't fold into constructor: must construct before "new Thread(this)" call
void setup(Thread thread, iClosure closure) {
    this.closure = closure;
    this.thread = thread;
    this.originalPC = closure.PC;
    this.snum = nextsn++;
    callers = new java.util.Stack();
    lock = new Semaphore();
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
    resultCount++;
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
    iEnv.cur_coexp = target;
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



vInteger Size()		{ return iNew.Integer(resultCount); }

static vString typestring = iNew.String("co-expression");
vString type()		{ return typestring;}

vString image()		{ return iNew.String(
			    "co-expression_" + snum + "(" + resultCount + ")");
}

vInteger Serial()	{ return iNew.Integer(snum); }

int rank()		{ return 70; }	// co-expressns sort after files

int compareTo(vValue v)	{ return this.snum - ((vCoexp)v).snum; }



} // class vCoexp

//  vCoexp -- coexpressions	#%#% TO BE DONE

package rts;

public class vCoexp extends vValue implements Runnable {

    Thread thread;
    java.util.Stack callers;
    vDescriptor incomingValue;
    Semaphore lock;
    int snum;		// serial number
    int resultCount;

static int nextsn = 1;			// next serial number



//#%#%	vCoexp(Thread thread, iClosure closure) {
//#%#%	    setup(thread, closure);
//#%#%	}
//#%#%	
//#%#%	vCoexp(iClosure closure) {
//#%#%	    setup(new Thread(this), closure);
//#%#%	}
//#%#%	
//#%#%	// can't fold into constructor: must construct before "new Thread(this)" call
//#%#%	void setup(Thread thread, iClosure closure) {
//#%#%	    this.closure = closure;
//#%#%	    this.thread = thread;
//#%#%	    this.originalPC = closure.PC;
//#%#%	    this.snum = nextsn++;
//#%#%	    callers = new java.util.Stack();
//#%#%	    lock = new Semaphore();
//#%#%	}



public void run() {
    try {
	lock.P();
    } catch (InterruptedException e) {
	iRuntime.error(902);
    }
//#%#%	    closure.Resume();  // should never return;
}

//#%#%	public void coret(vDescriptor retValue) {
//#%#%	    vCoexp caller = (vCoexp) callers.pop();
//#%#%	    resultCount++;
//#%#%	    transfer(retValue, caller);
//#%#%	}
//#%#%	
//#%#%	public void cofail() {
//#%#%	    for (;;) {
//#%#%		vCoexp caller = (vCoexp) callers.pop();
//#%#%		transfer(null, caller);
//#%#%	    }
//#%#%	}
//#%#%	
//#%#%	public vDescriptor activate(vDescriptor value, vDescriptor coexp) {
//#%#%	    vCoexp target = (vCoexp)coexp;
//#%#%	    target.callers.push(this);
//#%#%	    transfer(value, target);
//#%#%	    return this.incomingValue;
//#%#%	}
//#%#%	
//#%#%	void transfer(vDescriptor value, vCoexp target) {
//#%#%	    target.incomingValue = value;
//#%#%	    iEnv.cur_coexp = target;
//#%#%	    target.lock.V();
//#%#%	    try {
//#%#%		lock.P();
//#%#%	    } catch (InterruptedException e) {
//#%#%		iRuntime.error(902);
//#%#%	    }
//#%#%	}
//#%#%	
//#%#%	public void create() {
//#%#%	    thread.start();
//#%#%	    this.refreshCopy = closure.copy(this.closure.PC);
//#%#%	}



// vDescriptor methods



public vInteger Size()	{ return vInteger.New(resultCount); }

static vString typestring = vString.New("co-expression");
public vString Type()	{ return typestring;}

vString image()
  { return vString.New("co-expression_" + snum + "(" + resultCount + ")"); }

public vInteger Serial(){ return vInteger.New(snum); }

int rank()		{ return 70; }	// co-expressions sort after files
int compareTo(vValue v)	{ return this.snum - ((vCoexp)v).snum; }


//#%#%	public vCoexp Refresh() {
//#%#%	    vCoexp c = new vCoexp(refreshCopy.copy(this.originalPC));
//#%#%	    c.refreshCopy = refreshCopy;
//#%#%	    c.thread.start();
//#%#%	    return c;
//#%#%	}
//#%#%	
//#%#%	public vDescriptor Activate(vDescriptor v) {
//#%#%	    return iEnv.cur_coexp.activate(v.Deref(), this);
//#%#%	}



} // class vCoexp

package rts;

public class vProc extends vValue {
	
	String img;	// image for printing
	Class proc;	// class that implements proc
	int args;	// number of args

	vProc(String s, Class c, int args) {
		img = s;
		proc = c;
		this.args = args;
	}

	public vValue deref() {
		return this;
	}

	String image()	{ return img; }

	String type()	{ return "procedure"; }

	int rank()	{ return 80; }	// procedures sort after co-expressions

	int compareTo(vValue v) {	// must handle procs, funcs, rec constrs
		String s1 = this.image();
		String s2 = v.image();
		s1 = s1.substring(s1.lastIndexOf(' ') + 1);
		s2 = s2.substring(s2.lastIndexOf(' ') + 1);
		return s1.compareTo(s2);
	}

	iClosure getClosure() {
		iClosure c = null;

		try {
			c = (iClosure) proc.newInstance();
		} catch (InstantiationException e) {
			iRuntime.bomb(e);
		} catch (IllegalAccessException e) {
			iRuntime.bomb(e);
		}
		return c;
	}

	public iClosure instantiate(vDescriptor[] args, iClosure parent) {
		iClosure c = getClosure();
		c.closure(args, parent);
		return c;
	}
	public iClosure instantiate(vDescriptor arg0, vDescriptor arg1, vDescriptor arg2, iClosure parent) {
		iClosure c = getClosure();
		c.closure(arg0, arg1, arg2, parent);
		return c;
	}
	public iClosure instantiate(vDescriptor arg0, vDescriptor arg1, iClosure parent) {
		iClosure c = getClosure();
		c.closure(arg0, arg1, parent);
		return c;
	}
	public iClosure instantiate(vDescriptor arg0, iClosure parent) {
		iClosure c = getClosure();
		c.closure(arg0, parent);
		return c;
	}

	vInteger Args()		{ return iNew.Integer(args); }

	vValue getproc()	{ return this; }

}

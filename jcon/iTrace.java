//  iTrace.java -- support of tracing and traceback



package rts;

public final class iTrace {



public static vDescriptor Call(String fname, int lineno,
			vDescriptor a, vDescriptor[] args) {

    vProc p = a.mkProc(args.length > 0 ? args.length : -1);
    if (! p.traceable) {
	return vTracedClosure.New(p, args, p.Call(args));
    }

    if (iKeyword.trace.check()) {
	StringBuffer b = new StringBuffer(50);
	b.append('(');
	for (int i = 0; i < args.length; i++) {
	    if (i > 0) {
		b.append(',');
	    }
	    b.append(args[i].report().toString());
	}
	b.append(')');
	trace(fname, lineno, p, b.toString());
    }

    vDescriptor result = vTracedClosure.New(p, args, p.Call(args));

    if (iKeyword.trace.check()) {
	if (result instanceof vClosure) {
	    trace(fname, lineno, p, " suspended", result);
	} else {
	    trace(fname, lineno, p, " returned", result);
	}
    }

    return result;
}



public static vDescriptor Resume(String fname, int lineno, vDescriptor object) {
    if (! (object instanceof vTracedClosure)) {		// don't trace to-by etc
	return object.Resume();
    }
    if (iKeyword.trace.check()) {
	trace(fname, lineno, object, " resumed");
    }
    vDescriptor v = object.Resume();
    if (iKeyword.trace.check()) {
	trace(fname, lineno, object, " suspended", v);
    }
    return v;
}



//  trace(file, line, obj, string, value) -- output suspend/return trace
//  trace(file, line, obj, string) -- output general trace message

static void trace(String fn, int ln, vDescriptor o, String s, vDescriptor v) {
    if (v == null) {
	trace(fn, ln, o, " failed");
    } else {
	trace(fn, ln, o, s + " " + v.report());
    }
}

static void trace(String fname, int lineno, vDescriptor obj, String s) {
    StringBuffer out = new StringBuffer(80);

    out.append(fname);
    while (out.length() < 12) {
	    out.append(" ");
    }
    out.append(" : ");

    String ln = String.valueOf(lineno);
    for (int i = ln.length(); i <= 4; i++) {
	    out.append(" ");
    }
    out.append(ln).append(" ");

    out.append("|...| ");	//#%#%#%#

    if (obj == null) {
	out.append("?");
    } else if (obj instanceof vTracedClosure) {
	String procImage = ((vTracedClosure)obj).tracedProc.report().toString();
	out.append(procImage.substring(procImage.lastIndexOf(' ') + 1));
    } else {
	String procImage = obj.report().toString();
	out.append(procImage.substring(procImage.lastIndexOf(' ') + 1));
    }

    out.append(s);
    System.err.println(out);
}



} // class iTrace

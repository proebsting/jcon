//  iTrace.java -- support of tracing and traceback



package rts;

public final class iTrace {



//  location of "return" or "suspend"
//  (set by generated code when compiled in trace mode)

public static String file;	// file name
public static int line;		// line number



//  iTrace.Call -- called by trampolines to issue tracing messages

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
	    b.append(args[i].Deref().report().toString());
	}
	b.append(')');
	trace(fname, lineno, p, b.toString());
    }

    file = null;		// clear for called procedure to set (or not)
    line = 0;
    vDescriptor result = vTracedClosure.New(p, args, p.Call(args));

    if (iKeyword.trace.check()) {
	if (result instanceof vClosure) {
	    trace(file, line, p, " suspended", result);
	} else {
	    trace(file, line, p, " returned", result);
	}
    }

    file = null;		// prevent confusion by caller's caller
    line = 0;
    return result;		// return/suspend result
}



//  iTrace.Resume -- called by trampolines to issue tracing messages

public static vDescriptor Resume(String fname, int lineno, vDescriptor object) {
    if (! (object instanceof vTracedClosure)) {		// don't trace to-by etc
	return object.Resume();
    }
    iEnv.cur_coexp.depth++;
    if (iKeyword.trace.check()) {
	trace(fname, lineno, object, " resumed");
    }

    file = null;		// clear for suspended procedure to set (or not)
    line = 0;
    vDescriptor result;

    try {
	result = object.Resume();	// resume procedure
    } catch (iError e) {
	iEnv.cur_coexp.depth--;
	file = null;		// prevent confusion upstream
	line = 0;
    	throw e;
    }

    if (iKeyword.trace.check()) {
	trace(file, line, object, " suspended", result);
    }

    iEnv.cur_coexp.depth--;
    file = null;		// prevent confusion by caller's caller
    line = 0;
    return result;		// return/suspend result
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

    if (fname == null || lineno == 0) {
	out.append("                     ");
    } else {
	out.append(fname);
	while (out.length() < 12) {
		out.append(" ");
	}
	out.append(" :");
	String ln = String.valueOf(lineno);
	for (int i = ln.length(); i < 5; i++) {
		out.append(" ");
	}
	out.append(ln).append("  ");
    }

    for (int n = iEnv.cur_coexp.depth - 1; n > 0; n--) {
	out.append("| ");
    }

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

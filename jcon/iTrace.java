//  iTrace.java -- support of tracing and traceback



package jcon;

public final class iTrace {



//  location of "return" or "suspend"
//  (set by generated code when compiled in trace mode)

public static String file;	// file name
public static int line;		// line number (negative for suspend)



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
	    if (line < 0) {	// if true suspend
		trace(file, -line, p, " suspended", result);
	    } else {
		trace(file, line, p, " returned", result);
		result = ((vClosure)result).retval;
	    }
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
	if (line < 0) {
	    line = -line;	// just ensure positive; can't really return 
	}
	trace(file, line, object, " suspended", result);
    }

    iEnv.cur_coexp.depth--;
    file = null;		// prevent confusion by caller's caller
    line = 0;
    return result;		// return/suspend result
}



//  iTrace.coret -- called by trampolines to issue tracing messages

public static void coret(String fname, int lineno, String caller,
vCoexp a, vDescriptor val) {
    if (iKeyword.trace.check()) {
	String s = caller + "; " + iEnv.cur_coexp.report() +
	    " returned " + val.report() + " to ";
	try {
	    s += ((vCoexp) a.callers.peek()).report();
	} catch (java.util.EmptyStackException e) {
	    iRuntime.error(900);
	}
	trace(fname, lineno, s);
    }
    iEnv.cur_coexp.depth--;
    a.coret(val);
}



//  iTrace.cofail -- called by trampolines to issue tracing messages

public static void cofail(String fname, int lineno, String caller, vCoexp a) {
    if (iKeyword.trace.check()) {
	String s = caller + "; " + iEnv.cur_coexp.report() + " failed to ";
	try {
	    s += ((vCoexp) a.callers.peek()).report();
	} catch (java.util.EmptyStackException e) {
	    iRuntime.error(900);
	}
	trace(fname, lineno, s);
    }
    iEnv.cur_coexp.depth--;
    a.cofail();
}



//  iTrace.Activate -- called by trampolines to issue tracing messages

public static vDescriptor Activate(String fname, int lineno, String caller,
					vDescriptor a1, vDescriptor a2) {
    iEnv.cur_coexp.depth++;
    if (iKeyword.trace.check()) {
	trace(fname, lineno, caller + "; " + iEnv.cur_coexp.report() +
	    " : " + a2.Deref().report() + " @ " + a1.Deref().report());
    }
    return a1.Activate(a2);
}



//  trace(file, line, obj, string, value) -- output suspend/return trace
//  trace(file, line, obj, string) -- output general trace message
//  trace(file, line, string) -- output general trace message

static void trace(String fn, int ln, vDescriptor o, String s, vDescriptor v) {
    if (v == null) {
	trace(fn, ln, o, " failed");
    } else {
	trace(fn, ln, o, s + " " + v.report());
    }
}

static void trace(String fname, int lineno, vDescriptor obj, String s) {
    StringBuffer out = new StringBuffer(80);

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
    trace(fname, lineno, out.toString());
}

static void trace(String fname, int lineno, String s) {
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

    out.append(s);
    System.err.println(out);
}



} // class iTrace

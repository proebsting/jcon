//  ftab.java -- entab and detab functions

package rts;



final class fTab extends iInstantiate {
    public static fTab self = new fTab();
    public vProc instantiate(String name) {
        if (name.compareTo( "f$detab" ) == 0) return new f$detab();
        if (name.compareTo( "f$entab" ) == 0) return new f$entab();
        return null;
    } // vProc instantiate(String)

static int[] deftabs = {1, 9};

static int[] stops(vDescriptor[] args) {
    if (args.length < 2) {
	return deftabs;
    }
    int[] ret = new int[args.length];
    ret[0] = 1;
    for (int i = 1; i < args.length; i++) {
	ret[i] = (int) args[i].mkInteger().value;
	if (ret[i] <= ret[i - 1]) {
	    iRuntime.error(210, args[i]);
	}
    }
    return ret;
}

static int nextTab(int[] stops, int col) {
    int n = stops.length;
    for (int i = 1; i < n; i++) {
	if (stops[i] > col) {
	    return stops[i];
	}
    }
    int interval = stops[n - 1] - stops[n - 2];
    return col + interval - (col - stops[n - 1]) % interval;
}

} // class fTab



// detab(s,i,...) - replace tabs with spaces, with stops at columns indicated

final class f$detab extends vProcV {
    public vDescriptor Call(vDescriptor[] v) {
	byte[] s = iRuntime.arg(v, 0).mkString().getBytes();
	int stops[] = fTab.stops(v);
	vByteBuffer b = new vByteBuffer(s.length + 40);
	int col = 1;

	for (int i = 0; i < s.length; i++) {
	    char c = (char) (s[i] & 0xFF);
	    switch (c) {
		case '\b':				// backspace
		    b.append(c);
		    col--;
		    break;
		case '\n':				// newline or return
		case '\r':
		    b.append(c);
		    col = 1;
		    break;
		case '\t':				// tab
		    int target = fTab.nextTab(stops, col);
		    while (col < target) {
			b.append(' ');
			col++;
		    }
		    break;
		default:				// other character
		    b.append(c);
		    if (c >= ' ' && c != '\177') {	// if not control char
			col++;
		    }
		    break;
	    }
	}

	return b.mkString();
    }

} // class f$detab



// entab(s,i,...) - replace spaces with tabs, with stops at columns indicated

final class f$entab extends vProcV {
    public vDescriptor Call(vDescriptor[] v) {
	byte[] s = iRuntime.arg(v, 0).mkString().getBytes();
	int stops[] = fTab.stops(v);
	vByteBuffer b = new vByteBuffer(s.length);
	int col = 1;

	for (int i = 0; i < s.length; i++) {
	    char c = (char) (s[i] & 0xFF);
	    switch (c) {
		case '\b':				// backspace
		    b.append(c);
		    col--;
		    break;
		case '\n':				// newline or return
		case '\r':
		    b.append(c);
		    col = 1;
		    break;
		case '\t':				// tab
		    b.append(c);
		    col = fTab.nextTab(stops, col);
		    break;
		case ' ':				// one or more spaces
		    int nspaces = 1;
		    while (i + 1 < s.length && s[i+1] == ' ') {
			nspaces++;
			i++;
		    }
		    if (nspaces == 1) {		// lone space never becomes tab
			b.append(c);
			col++;
			break;
		    }
		    int nxtstop;
		    // following outer test avoids one-char tab before spaces
		    if (fTab.nextTab(stops, col + 1) <= col + nspaces) {
			while((nxtstop=fTab.nextTab(stops,col)) <= col+nspaces){
			    b.append('\t');
			    nspaces -= (nxtstop - col);
			    col = nxtstop;
			}
		    }
		    while (nspaces-- > 0) {
			b.append(' ');
			col++;
		    }
		    break;
		default:				// other character
		    b.append(c);
		    if (c >= ' ' && c != '\177') {	// if not control char
			col++;
		    }
		    break;
	    }
	}
	return b.mkString();
    }

} // class f$entab

//  vFile.java -- Icon Files

package rts;

import java.io.*;



public class vFile extends vValue {

    String img;			// string for image() and for sorting

    DataInput instream;		// input stream, if readable
    DataOutput outstream;	// output stream, if writable
    RandomAccessFile randfile;	// random handle, if seekable



String type()			{ return "file"; }
String image()			{ return this.img; }
int rank()			{ return 60; }	// files sort after csets
int compareTo(vValue v)		{ return this.img.compareTo(((vFile) v).img); }
vDescriptor Bang(iClosure c)	{ return this.read(); }



vFile(String kwname, InputStream i) {		// new vFile(&input, System.in)
    instream = new DataInputStream(i);
    img = kwname;
}

vFile(String kwname, PrintStream p) {		// new vFile(&output/&errout...)
    outstream = new DataOutputStream(p);
    img = kwname;
}



// new vFile(name, flags) constructor for open()
// throws IOException for failure
// #%#%# ignores t/u flags; disallows p flag

vFile(String name, String flags) throws IOException {

    String mode;

    if (any("pP", flags)) {
    	throw new IOException("can't open pipe"); /*FAIL*/	//%#%#%#
    }

    if (any("wabcWABC", flags)) {			// planning to write?
	mode = "rw";
	if (any("cC", flags) || ! any("abrABR", flags)) {
	    (new FileOutputStream(name)).close();	// truncate
	}
    } else {
	mode = "r";
    }

    randfile = new RandomAccessFile(name, mode);	// open file

    if (any("aA", flags)) {				// if append mode
	randfile.seek(randfile.length());
    }
    if (any("wabcWABC", flags)) {
	outstream = randfile;				// output side
	if (any("rbRB", flags)) {
	    instream = randfile;			// input side
	}
    } else {
	instream = randfile;				// input side
    }
}

static boolean any(String c, String s) {	// any(c,s) -- like Icon's
    for (int i = 0; i < c.length(); i++) {
    	if (s.indexOf(c.charAt(i)) >= 0) {
	    return true;
	}
    }
    return false;
}



//  static methods for argument processing and defaulting

static vFile argVal(vDescriptor[] args, int index)		// required arg
{
    if (index >= args.length || ! (args[index] instanceof vFile)) {
	iRuntime.error(105);	// file expected
	return null;
    } else {
	return (vFile) args[index];
    }
}

static vFile argVal(vDescriptor[] args, int index, vFile dflt)	// optional arg
{
    if (index >= args.length || args[index] instanceof vNull) {
	return dflt;
    } else if (args[index] instanceof vFile) {
	return (vFile) args[index];
    } else {
	iRuntime.error(105);	// file expected
	return null;
    }
}



// --------------------------- Icon I/O operations --------------------------



vFile flush() { 					// flush()
    // nothing to flush when there's no buffering...
    return this;
}



vFile close() {						// close()
    if (instream == null && outstream == null) {
    	return this;			// already closed
    }
    RandomAccessFile r = randfile;	// save random handle
    randfile = null;			// indicate file closed
    instream = null;
    outstream = null;
    if (r != null) {			// if not stdin/stdout/stderr
    	try {
	    r.close();			// try system close
	} catch (IOException e) {
	    iRuntime.error(214, this);	// I/O error
	}
    }
    return this;
}



vFile seek(long n) {					// seek(n)
    if (randfile == null) {		// if seekable
    	return null; /*FAIL*/
    }
    try {
    	if (n > 0) {
	    n--;			// remove Icon bias
	} else {
	    n = randfile.length() + n;	// distance from end
	}
	if (n < 0 || n > randfile.length()) {
	    return null; /*FAIL*/
	}
	randfile.seek(n);
	return this;
    } catch (IOException e) {
	return null; /*FAIL*/
    }
}



vInteger where() {					// where()
    if (randfile == null) {
    	return null; /*FAIL*/
    } 
    try {
	return iNew.Integer(1 + randfile.getFilePointer());
    } catch (IOException e) {
    	return null; /*FAIL*/
    }
}



vString read() {					// read()
    StringBuffer b = new StringBuffer(100);

    if (instream == null) {
	iRuntime.error(212, this);	// not open for reading
    }
    try {
	byte c;
	while ((c = instream.readByte()) != '\n') {	//#%#% assumes Unix
	    b.append((char) c);
	}
    } catch (EOFException e) {
    	if (b.length() == 0)
	    return null; /*FAIL*/
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
	return null;
    } 
    return iNew.String(b.toString());
}



vString reads(long n) {					// reads(n)
    StringBuffer b = new StringBuffer((int) n);

    if (instream == null) {
	iRuntime.error(212, this);	// not open for reading
    }
    try {
	while (b.length() < n) {
	    b.append((char) instream.readByte());
	}
    } catch (EOFException e) {
    	if (b.length() == 0)
	    return null; /*FAIL*/
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
	return null;
    } 
    return iNew.String(b.toString());
}



void writes(String s) {				// writes(s)
    if (outstream == null) {
	iRuntime.error(213, this);	// not open for writing
    }
    try {
	outstream.writeBytes(s);
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
    }
}


void newline() {				// write newline
    if (outstream == null) {
	iRuntime.error(213, this);	// not open for writing
    }
    try {
	outstream.writeByte('\n');	//#%#% assumes Unix
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
    }
}



} // class vFile

//  vFile.java -- Icon Files

//  vFile is an abstract class that is subclassed by
//  vTFile for text files or vBFile for binary files.
//  vFile.New() examines flags to know which Type to construct.
//
//  many common operations are implemented here, including read().
//  reads(), writes(), and newline() are class-specific.

package rts;

import java.io.*;
import java.util.*;



public abstract class vFile extends vValue {

    vString img;		// string for image() and for sorting

    DataInput instream;		// input stream, if readable
    DataOutput outstream;	// output stream, if writable
    RandomAccessFile randfile;	// random handle, if seekable
    Process pipe;		// pipe process, if any

    byte[] ibuf;		// input buffer, if read-only random file
    int inext;			// offset to next buffered character
    int icount;			// number of remaining buffered chars
    long ifpos;			// input file position (only valid mode "r")
    long iflen;			// input file length (only valid random "r")

    char lastCharRead = '\0';	// last char seen by read()



final static int BufferSize = 4096;		// input buffer size

static Hashtable openfiles = new Hashtable();	// set of open files



// Window synchronization: The graphics code sets winToSync to register
// a vWindow file that needs to be synchronized (by winToSync.flush())
// before reading from &input.  This is here and not in vWindow because we
// don't want to initialize the vWindow class in a non-graphics execution.

static vFile winToSync;		// window to sync, if non-null



abstract vString reads(long n);		// read n bytes
abstract void writes(vString s);	// write without appending newline
abstract void newline();		// write newline



//  vDescriptor methods

static vString typestring = vString.New("file");
public vString Type()		{ return typestring; }

public vString image()		{ return this.img; }
int rank()			{ return 60; }	// files sort after windows
int compareTo(vValue v)		{ return this.img.compareTo(((vFile) v).img); }

public vDescriptor Bang() {
    final vString s = this.read();
    if (s == null) {
	return null;
    }
    return new vClosure() {
	{ retval = s; }
	public vDescriptor Resume() {
	    return vFile.this.read();
	}
    };
}



//  shutdown() -- handle files for program termination.
//
//  This method must be called when the progam exits.  It flushes
//  stdout/stderr and closes other files.  This is especially needed
//  to kill any processes spawned by opening pipes.

public static void shutdown() {
    for (Enumeration e = openfiles.elements(); e.hasMoreElements(); ) {
	((vFile) e.nextElement()).close();
    }
}



// factory methods

public static vFile New(String kwname, InputStream i) {		// &input
    return new vTFile(kwname, new DataInputStream(i), null);
}

public static vFile New(String kwname, PrintStream o, boolean buffered) {
							// &output, errout
    DataOutputStream ostream;
    if (buffered) {
	ostream = new DataOutputStream(new BufferedOutputStream(o));
    } else {
	ostream = new DataOutputStream(o);
    }
    return new vTFile(kwname, null, ostream);
}

public static vFile New(String filename, String mode, vDescriptor args[]) {
    try {
	if (upto("gGxX", mode)) {
	    return new vWindow(filename, mode, args);
	} else if (upto("uU", mode)) {
	    return new vBFile(filename, mode);	// binary (untranslated) file
	} else {
	    return new vTFile(filename, mode);	// text (translated) file
	}
    } catch (IOException e) {
	return null; /*FAIL*/
    }
}



// new vFile() -- degenerate constructor for vWindow subclass

vFile() {}



// new vFile(kwname, instream, outstream) -- constructor for keyword files

vFile(String kwname, DataInput i, DataOutput o) {
    img = vString.New(kwname);

    if (i != null) {			// if input file
	instream = i;
	ibuf = new byte[BufferSize];	// allocate input buffer
	inext = icount = 0;
    } else {
	outstream = o;
    }
    openfiles.put(this, this);				// remember open file
}



// new vFile(name, flags) -- constructor for open()
// throws IOException for failure

vFile(String name, String flags) throws IOException {

    String mode;

    img = vString.New("file(" + name + ")");		// save image

    if (upto("pP", flags)) {
	newpipe(name, flags);				// open pipe (or throw)
	return;
    }

    if (upto("wabcWABC", flags)) {			// planning to write?
	mode = "rw";
	if (upto("cC", flags) || ! upto("abrABR", flags)) {
	    (new FileOutputStream(name)).close();	// truncate
	}
    } else {
	mode = "r";
    }

    randfile = new RandomAccessFile(name, mode);	// open file

    if (upto("aA", flags)) {				// if append mode
	randfile.seek(randfile.length());
    }
    if (upto("wabcWABC", flags)) {
	outstream = randfile;				// output side
	if (upto("rbRB", flags)) {
	    instream = randfile;			// input side
	}
    } else {
	instream = randfile;				// input side
    }

    if (instream != null && outstream == null) {	// if only for input
	ibuf = new byte[BufferSize];			// allocate input buffer
	inext = icount = 0;
	iflen = randfile.length();			// record length
    }

    openfiles.put(this, this);				// remember open file
}



// newpipe(name, flags) -- open a pipe
// throws IOException for failure

void newpipe(String name, String flags) throws IOException {

    String argv[] = { "sh", "-c", name.toString() };
    pipe = Runtime.getRuntime().exec(argv);

    if (upto("wabcWABC", flags)) {

	// open pipe for writing
	if (upto("rbRB", flags)) {
	    throw new IOException();		// cannot open bidirectionally
	}
	outstream = new DataOutputStream(
	    new BufferedOutputStream(pipe.getOutputStream()));

    } else {
	// open pipe for reading
	instream = new DataInputStream(
	    new BufferedInputStream(pipe.getInputStream()));
	pipe.getOutputStream().close();		// close the pipe's input
    }

    openfiles.put(this, this);			// remember open file
}



//  static methods for argument processing and defaulting

static vFile arg(vDescriptor v) {		// required arg
    vValue vv = v.Deref();
    if (vv instanceof vFile) {
	return (vFile) vv; 
    } else {
	iRuntime.error(105, v);		// file expected
	return null;
    }
}

static vFile arg(vDescriptor v, vFile dflt) {	// optional arg
    vValue vv = v.Deref();
    if (vv instanceof vFile) {
	return (vFile) vv; 
    } else if (vv.isnull()) {
	return dflt;
    } else {
	iRuntime.error(105, v);		// file expected
	return null;
    }
}



//  upto(c, s) -- is any char of c contained in s?  (like Icon's upto())

static boolean upto(String c, String s) {
    for (int i = 0; i < c.length(); i++) {
	if (s.indexOf(c.charAt(i)) >= 0) {
	    return true;
	}
    }
    return false;
}



// ------------- input buffering: MUST USE THIS FOR ALL INPUT ------------



//  rchar() -- read one character

char rchar() throws IOException, EOFException {
    if (icount > 0) {			// if buffer is not empty
	icount--;
	return (char) (ibuf[inext++] & 0xFF);
    }

    if (ibuf == null) {			// if not buffered by us
	return (char) (instream.readByte() & 0xFF);
    }

    if (this == iKeyword.input.file()){ // flush output before reading &input
	if (winToSync != null) {
	    winToSync.flush();		// flush pending graphics output
	}
	iKeyword.output.file().flush();	// flush &output
    }

    int nbytes;
    if (randfile != null) {
	nbytes = randfile.read(ibuf);	// refill buffer
    } else {
	nbytes = ((InputStream)instream).read(ibuf);
    }
    if (nbytes <= 0) {
	throw new EOFException();
    }
    ifpos += nbytes;				// update file position
    inext = 1;				// point to second byte
    icount = nbytes - 1;
    return (char) (ibuf[0] & 0xFF);	// return first byte
}



// --------------------------- Icon I/O operations --------------------------



vFile flush() {							// flush()

    if (outstream != null && outstream instanceof OutputStream) {
	try {
	    ((OutputStream)outstream).flush();
	} catch (IOException e) {
	    iRuntime.error(214, this);		// I/O error
	}
    }
    inext = icount = 0;
    return this;
}



vFile close() {							// close()

    if (! openfiles.containsKey(this)) {
	return this;				// already closed
    }

    this.flush();				// flush pending output
    inext = icount = 0;				// clear input buffer
    openfiles.remove(this);			// remove from open file set

    try {
	if (pipe != null) {			// if pipe
	    this.closepipe();
	} else if (randfile != null) {		// if random file
	    randfile.close();
	}
    } catch (IOException e) {
	randfile = null;
	instream = null;
	outstream = null;
	pipe = null;
	iRuntime.error(214, this);		// I/O error
    }

    randfile = null;
    instream = null;
    outstream = null;
    pipe = null;
    return this;
}



void closepipe() throws IOException {

    if (outstream != null) {			// if output pipe
	((OutputStream)outstream).close();	// close output file
	try {
	    pipe.waitFor();			// wait for process to finish
	} catch (InterruptedException e) {
	    // nothing
	};
	copy(pipe.getInputStream(), iKeyword.output.file());	// copy stdout
    } else {
	iKeyword.output.file().flush();		// only need to flush stdout
    }

    copy(pipe.getErrorStream(), iKeyword.errout.file());	// copy stderr

    pipe.destroy();				// kill process
}



//  vFile.copy(instream, ofile) -- copy pipe output, and flush

static void copy(InputStream ifile, vFile ofile) throws IOException {
    byte b[] = new byte[BufferSize];
    int n;

    while ((n = ifile.read(b)) >= 0) {
	ofile.outstream.write(b, 0, n);
    }
    ofile.flush();
}



vFile seek(long n) {						// seek(n)
    if (randfile == null) {			// if not seekable
	return null; /*FAIL*/
    }
    try {
	long len;

	if (ibuf != null) {
	    len = iflen;			// length known if read-buffered
	} else {
	    len = randfile.length();		// others can change; must ask
	}

	if (n > 0) {
	    n--;				// remove Icon bias from address
	} else {
	    n = len + n;			// distance from end
	}

	if (n < 0 || n > len) {
	    return null; /*FAIL*/
	}

	if (ibuf != null) {			// if buffered, we know position
	    int bdata = inext + icount;		// valid data in buffer
	    long off = bdata - ifpos + n;	// offset to new posn in buffer
	    if (off >= 0 && off <= bdata) {
		// sought position is within current buffer
		inext = (int) off;
		icount = bdata - inext;
		return this;
	    }
	}

	// not buffered, or position is not in buffer
	randfile.seek(n);			// reposition file
	ifpos = n;				// record new position
	inext = icount = 0;			// clear input buffer
	return this;

    } catch (IOException e) {
	return null; /*FAIL*/
    }
}



vInteger where() {						// where()
    if (randfile == null) {
	return null; /*FAIL*/
    }
    if (ibuf != null) {		// if read-only & buffered, we know position
	return vInteger.New(1 + ifpos - icount);
    }
    try {
	return vInteger.New(1 + randfile.getFilePointer() - icount);
    } catch (IOException e) {
	return null; /*FAIL*/
    }
}



vString read() {						// read()
    if (instream == null) {
	iRuntime.error(212, this);	// not open for reading
    }

    vByteBuffer b = new vByteBuffer(100);
    char c = '\0';
    try {
	if (lastCharRead == '\r') {
	    if ((c = this.rchar()) != '\n') {
		b.append(c);
	    }
	}
	while ((c = this.rchar()) != '\n' && c != '\r') {
	    b.append(c);
	}
    } catch (EOFException e) {
	if (b.length() == 0)
	    return null; /*FAIL*/
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
	return null;
    }
    lastCharRead = c;
    return b.mkString();
}



// ------------------------ Internal I/O operations -----------------------


//  These involve conversion to vString, so avoid in anything that
//  needs to be fast.

void print(String s)	{ this.writes(vString.New(s)); }
void println(String s)	{ this.writes(vString.New(s)); this.newline(); }



} // class vFile

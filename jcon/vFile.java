//  vFile.java -- Icon Files

//  vFile is an abstract class that is subclassed by
//  vTFile for text files or vBFile for binary files.
//  iNew.vFile() examines flags to know which type to construct.
//
//  many common operations are implemented here, including read().
//  reads(), writes(), and newline() are class-specific.

package rts;

import java.io.*;



public abstract class vFile extends vValue {

    vString img;		// string for image() and for sorting

    DataInput instream;		// input stream, if readable
    DataOutput outstream;	// output stream, if writable
    RandomAccessFile randfile;	// random handle, if seekable

    byte[] ibuf;		// input buffer, if read-only random file
    int inext;			// offset to next buffered character
    int icount;			// number of remaining buffered chars
    long ifpos;			// input file position (only valid mode "r")
    long iflen;			// input file length (only valid mode "r")

    char lastCharRead = '\0';	// last char seen by read()



final static int BufferSize = 4096;	// input buffer size



abstract vString reads(long n);		// read n bytes
abstract void writes(vString s);	// write without appending newline
abstract void newline();		// write newline


static vString typestring = iNew.String("file");
vString type()			{ return typestring; }
vString image()			{ return this.img; }
int rank()			{ return 60; }	// files sort after windows
int compareTo(vValue v)		{ return this.img.compareTo(((vFile) v).img); }
vDescriptor Bang(iClosure c)	{ return this.read(); }



// synchronization: The graphics code sets fileToSync to register a vWindow
// file that needs to be synchronized before tty (actually dataInputStream)
// input.  This is here and not in vWindow because we don't want to
// initialize the vWindow class in a non-graphics execution.
// Synchronization is done by calling fileToSync.flush().

static vFile fileToSync;	// file to sync, if non-null



//  output flushing: this method must be called on exit!
//
//  If we start buffering other output files besides stdin/stderr,
//  we'll need to remember them, and flush them here.

public static void flushall() {
    k$output.file.flush();
    k$errout.file.flush();
}



// new vFile(kwname, instream, outstream) -- constructor for keyword files

vFile(String kwname, DataInput i, DataOutput o) {
    img = iNew.String(kwname);
    instream = i;
    outstream = o;
}



// new vFile(name, flags) -- constructor for open()
// throws IOException for failure

vFile(String name, String flags) throws IOException {

    String mode;

    img = iNew.String("file(" + name + ")");		// save image

    if (iRuntime.upto("wabcWABC", flags)) {		// planning to write?
	mode = "rw";
	if (iRuntime.upto("cC", flags) || ! iRuntime.upto("abrABR", flags)) {
	    (new FileOutputStream(name)).close();	// truncate
	}
    } else {
	mode = "r";
    }

    randfile = new RandomAccessFile(name, mode);	// open file

    if (iRuntime.upto("aA", flags)) {			// if append mode
	randfile.seek(randfile.length());
    }
    if (iRuntime.upto("wabcWABC", flags)) {
	outstream = randfile;				// output side
	if (iRuntime.upto("rbRB", flags)) {
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
}



// new vFile() -- degenerate constructor for vWindow subclass

vFile() {}



//  static methods for argument processing and defaulting

static vFile argVal(vDescriptor[] args, int index)		// required arg
{
    if (index >= args.length) {
	iRuntime.error(105);	// file expected
	return null;
    } else if (! (args[index] instanceof vFile)) {
	iRuntime.error(105, args[index]);	// file expected
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
	iRuntime.error(105, args[index]);	// file expected
	return null;
    }
}



// ------------- input buffering for random-access input files ------------



//  rchar() -- read one character.  MUST USE THIS FOR ALL INPUT.

char rchar() throws IOException, EOFException {
    if (icount > 0) {			// if buffer is not empty
	icount--;
	return (char) (ibuf[inext++] & 0xFF);
    }
    if (ibuf == null) {			// if not buffered by us
	return (char) (instream.readByte() & 0xFF);
    }

    int n = randfile.read(ibuf);	// refill buffer
    if (n <= 0) {
	throw new EOFException();
    }
    ifpos += n;				// update file position
    inext = 1;				// point to second byte
    icount = n - 1;
    return (char) (ibuf[0] & 0xFF);	// return first byte
}





// --------------------------- Icon I/O operations --------------------------



vFile flush() { 					// flush()
    if (outstream instanceof OutputStream) {
    	try {
    	    ((OutputStream)outstream).flush();
	} catch (IOException e) {
	    iRuntime.error(214, this);	// I/O error
	}
    }
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
    inext = icount = 0;
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
    if (randfile == null) {		// if not seekable
    	return null; /*FAIL*/
    }
    try {
	long len;

	if (ibuf != null) {
	    len = iflen;		// file length known if read-buffered
	} else {
	    len = randfile.length();	// otherwise can change; must ask
	}

    	if (n > 0) {
	    n--;			// remove Icon bias from seek address
	} else {
	    n = len + n;		// distance from end
	}

	if (n < 0 || n > len) {
	    return null; /*FAIL*/
	}

	if (ibuf != null) { 			// if buffered, we know position
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
	randfile.seek(n);		// reposition file
	ifpos = n;			// record new position
	inext = icount = 0;		// clear input buffer
	return this;

    } catch (IOException e) {
	return null; /*FAIL*/
    }
}



vInteger where() {					// where()
    if (randfile == null) {
    	return null; /*FAIL*/
    } 
    if (ibuf != null) {		// if read-only & buffered, we know position
	return iNew.Integer(1 + ifpos - icount);
    }
    try {
	return iNew.Integer(1 + randfile.getFilePointer() - icount);
    } catch (IOException e) {
    	return null; /*FAIL*/
    }
}



vString read() {					// read()
    if (instream == null) {
	iRuntime.error(212, this);	// not open for reading
    }

    if (fileToSync != null && instream == System.in) {
	fileToSync.flush();			// flush pending graphics output
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



} // class vFile

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

    byte lastCharRead = '\0';	// last char seen by read()



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
    if (instream == null) {
	iRuntime.error(212, this);	// not open for reading
    }

    if (instream instanceof InputStream) {		// if possibly tty
	if (fileToSync != null) {
	    fileToSync.flush();		// flush pending graphics output
	}
    }

    vByteBuffer b = new vByteBuffer(100);
    byte c = '\0';
    try {
	if (lastCharRead == '\r') {
	    if ((c = instream.readByte()) != '\n') {
		b.append((char) c);
	    }
	}
	while ((c = instream.readByte()) != '\n' && c != '\r') {
	    b.append((char) c);
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

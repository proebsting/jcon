//  vBFile.java -- Icon Binary ("untranslated") Files

package rts;

import java.io.*;



public class vBFile extends vFile {



vBFile(String name, String mode)		 // new vBFile(name, mode)
	throws IOException
{
    super(name, mode);
}



//  untranslated reads() just passes bytes, never checking for newlines.

vString reads(long n) {
    StringBuffer b = new StringBuffer((int) n);

    if (instream == null) {
	iRuntime.error(212, this);	// not open for reading
    }
    try {
	// suppress initial LF if previous call was to read()
	// and it consumed CR but not yet LF
	if (lastCharRead == '\r') {
	    byte c = instream.readByte();
	    if (c != '\n') {
		b.append((char) c);
	    }
	}
	// now read remaining bytes
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

    lastCharRead = '\0';		// reset in case read() follows

    return iNew.String(b.toString());	// return data as a string
}



//  untranslated writes() does not translate "\n" characters to system newlines

void writes(String s) {
    if (outstream == null) {
	iRuntime.error(213, this);	// not open for writing
    }
    try {
	outstream.writeBytes(s);
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
    }
}



//  untranslated newline() always writes a single '\n' character

void newline() {
    if (outstream == null) {
	iRuntime.error(213, this);	// not open for writing
    }
    try {
	outstream.writeByte('\n');	// always, regardless of system
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
    }
}



} // class vBFile

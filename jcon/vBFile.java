//  vBFile.java -- Icon Binary ("untranslated") Files

package rts;

import java.io.*;



public final class vBFile extends vFile {



vBFile(String name, String mode) throws IOException {	// new vBFile(nm, mode)
    super(name, mode);
}



//  untranslated reads() just passes bytes, never checking for newlines.

public vString reads(long n) {
    vByteBuffer b = new vByteBuffer((int) n);

    if (instream == null) {
	iRuntime.error(212, this);	// not open for reading
    }

    try {
	// suppress initial LF if previous call was to read()
	// and it consumed CR but not yet LF
	if (lastCharRead == '\r') {
	    char c = this.rchar();
	    if (c != '\n') {
		b.append(c);
	    }
	}
	// now read remaining bytes
	while (b.length() < n) {
	    b.append(this.rchar());
	}
    } catch (EOFException e) {
	if (b.length() == 0)
	    return null; /*FAIL*/
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
	return null;
    }

    lastCharRead = '\0';		// reset in case read() follows

    return b.mkString();		// return data as a string
}



//  untranslated writes() does not translate "\n" characters to system newlines

public void writes(vString s) {
    if (outstream == null) {
	iRuntime.error(213, this);	// not open for writing
    }
    try {
	outstream.write(s.getBytes());
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
    }
}



//  untranslated newline() always writes a single '\n' character

public void newline() {
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

//  vTFile.java -- Icon Text ("translated") Files

package rts;

import java.io.*;



public class vTFile extends vFile {

private static String nlstring = System.getProperty("line.separator");
private static boolean nleasy = nlstring.equals("\n");



vTFile(String kwname, DataInput i, DataOutput o) {	 // new keyword vTFile
    super(kwname, i, o);
}

vTFile(String name, String mode) throws IOException {	// new vTFile(name,mode)
    super(name, mode);
}



//  translated reads() maps any line terminator sequence to "\n"

vString reads(long n) {
    vByteBuffer b = new vByteBuffer((int) n);

    if (instream == null) {
	iRuntime.error(212, this);		// not open for reading
    }

    if (fileToSync != null && instream == System.in) {
	fileToSync.flush();			// flush pending graphics output
    }

    try {
	while (b.length() < n) {
	    char c = this.rchar();
	    if (lastCharRead != '\r' || c != '\n') {	// if not LF after CR
		if (c == '\r') {			// if new CR
		    b.append('\n');
		} else {			// other including LF
		    b.append(c);
		}
	    }
	    lastCharRead = c;
	}
    } catch (EOFException e) {
	if (b.length() == 0)
	    return null; /*FAIL*/
    } catch (IOException e) {
	iRuntime.error(214, this);		// I/O error
	return null;
    }
    return b.mkString();
}



//  translated writes() maps newline chars to system line terminators

void writes(vString s) {
    if (outstream == null) {
	iRuntime.error(213, this);		// not open for writing
    }
    byte b[] = s.getBytes();
    try {
	if (nleasy) {
	    outstream.write(b);
	} else {
	    int i = 0;
	    int j = 0;
	    for (; j < b.length; j++) {
		if (b[j] == '\n') {
		    if (i < j) {
			outstream.write(b, i, j - i);
			i = j + 1;
		    }
		    outstream.writeBytes(nlstring);
		}
	    }
	    if (i < j) {
		outstream.write(b, i, j - i);
	    }
	}
    } catch (IOException e) {
	iRuntime.error(214, this);		// I/O error
    }
}



//  translated newline() writes the system line terminator

void newline() {
    if (outstream == null) {
	iRuntime.error(213, this);		// not open for writing
    }
    try {
	outstream.writeBytes(nlstring);
    } catch (IOException e) {
	iRuntime.error(214, this);		// I/O error
    }
}



} // class vTFile

//  vTFile.java -- Icon Text ("translated") Files

package jcon;

import java.io.*;



public final class vTFile extends vFile {

private static String nlstring = System.getProperty("line.separator");
private static boolean nleasy = nlstring.equals("\n");



vTFile(String kwname, DataInput i, DataOutput o) {	 // new keyword vTFile
    super(kwname, i, o);
}

vTFile(String name, String mode) throws IOException {	// new vTFile(name,mode)
    super(name, mode);
}



//  translated writes() maps newline chars to system line terminators

public void writes(vString s) {
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

public void newline() {
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

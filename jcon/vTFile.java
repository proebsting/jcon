//  vTFile.java -- Icon Text ("translated") Files

package rts;

import java.io.*;



public class vTFile extends vFile {

private static String nlstring = System.getProperty("line.separator");



vTFile(String kwname, InputStream i) {		// new vTFile(&input, System.in)
    super(kwname, new DataInputStream(i), null);
}

vTFile(String kwname, PrintStream p) {		// new vTFile(&output/&errout..)
    super(kwname, null, new DataOutputStream(p));
}

vTFile(String name, String mode)		// new vTFile(name, mode)
	throws IOException
{
    super(name, mode);
}



//  translated reads() maps any line terminator sequence to "\n"

vString reads(long n) {
    StringBuffer b = new StringBuffer((int) n);

    if (instream == null) {
	iRuntime.error(212, this);	// not open for reading
    }

    vWindow.sync();			// flush pending graphics output
    	//#%#%##% should only sync graphics if input file is a tty

    try {
	while (b.length() < n) {
	    byte c = instream.readByte();
	    if (lastCharRead != '\r' || c != '\n') {	// if not LF after CR
		if (c == '\r') {			// if new CR
		    b.append('\n');
		} else {			// other including LF
		    b.append((char) c);
		}
	    }
	    lastCharRead = c;
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



//  translated writes() maps newline chars to system line terminators

void writes(String s) {
    if (outstream == null) {
	iRuntime.error(213, this);	// not open for writing
    }
    try {
	int i = 0;
	int j;
	while ((j = s.indexOf('\n', i)) >= 0) {		// find embedded '\n'
	    if (j > i) {
		outstream.writeBytes(s.substring(i, j));
	    }
	    outstream.writeBytes(nlstring);
	    i = j + 1;
	}
	if (i < s.length()) {
	    outstream.writeBytes(s.substring(i, s.length()));
	}
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
    }
}



//  translated newline() writes the system line terminator

void newline() {
    if (outstream == null) {
	iRuntime.error(213, this);	// not open for writing
    }
    try {
	outstream.writeBytes(nlstring);
    } catch (IOException e) {
	iRuntime.error(214, this);	// I/O error
    }
}



} // class vTFile

//  vBFile.java -- Icon Binary ("untranslated") Files

package jcon;

import java.io.*;



public final class vBFile extends vFile {



vBFile(String name, String mode) throws IOException {	// new vBFile(nm, mode)
    super(name, mode);
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

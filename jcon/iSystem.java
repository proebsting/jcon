//  iSystem.java -- potentially nonportable code

//  This file holds code containing known Unix dependencies.

package jcon;

import java.io.*;
import java.util.*;



public final class iSystem {



//  command(s) -- execute command, returning Process
//
//  must call "sh" to handle wildcarding and redirection
//  (Java's "exec(s)" is not the same as C's "system(s)".)

public static Process command(String s) throws IOException {
    String argv[] = { "sh", "-c", s };
    return Runtime.getRuntime().exec(argv);
}



//  getenv(s) -- get environment value

private static Hashtable<String,vString> etable;

public static vString getenv(String name) {

    if (etable == null) {		// init table on first call
	try {
	    etable = new Hashtable<String,vString>();
	    Process p = Runtime.getRuntime().exec("env");
	    BufferedReader d = new BufferedReader(
		new InputStreamReader(p.getInputStream()));
	    String s;
	    while ((s = d.readLine()) != null) {
		s = s.trim();
		int n = s.indexOf('=');
		if (n > 0) {
		    String key = s.substring(0, n);
		    String val = s.substring(n + 1);
		    etable.put(key, vString.New(val));
		}
	    }
	    p.destroy();
	} catch (Exception e1) {
	    // nothing; table remains empty, all calls fail
	}
    }

    return etable.get(name);
}



} // class iSystem

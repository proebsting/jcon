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



} // class iSystem

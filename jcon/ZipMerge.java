//  ZipMerge -- merge files into a single zip file.
//
//  (A standalone utility used by the jcont script; not used at runtime.)
//
//  usage: ZipMerge destination.zip files....
//
//  ZipMerge creates one zip file from many files.  When ZipMerge encounters
//  a zip file it opens it up and adds its contents.  Others are simply added.
//  ZipMerge ignores subsequent repeated files.  (This happens with "link"
//  files.)



package jcon;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ZipMerge {



static boolean verbose = false;



public static void main(String[] args) {
    int i = 0;
    if (args.length > 0 && args[0].equals("-v")) {
	verbose = true;
	i++;
    }
    if (i >= args.length) {
	System.err.println("Usage: ZipMerge [-v] destination sourcefiles....");
	System.exit(1);
    }
    String dst = args[i++];
    Vector v = new Vector();
    while (i < args.length) {
	v.addElement(args[i++]);
    }
    Enumeration enum = v.elements();
    compose(dst, enum);
}



static byte[] buffer = new byte[10000];

public static void addZipEntry(ZipEntry ze, ZipOutputStream zos,
				InputStream is) throws IOException {

    // need a new ZipEntry that echoes the input one
    // but does not have the "compressedSize" field set
    // (it may recompress to a slightly different size)
    ZipEntry ne = new ZipEntry(ze.getName());
    if (ze.getTime() != -1) 	{ ne.setTime(ze.getTime()); }
    if (ze.getSize() != -1) 	{ ne.setSize(ze.getSize()); }
    if (ze.getCrc() != -1) 	{ ne.setCrc(ze.getCrc()); }
    if (ze.getMethod() != -1) 	{ ne.setMethod(ze.getMethod()); }
    if (ze.getExtra() != null) 	{ ne.setExtra(ze.getExtra()); }
    if (ze.getComment()!=null)	{ ne.setComment(ze.getComment()); }

    try {
	zos.putNextEntry(ne);
    } catch (ZipException e) {
	return;	// duplicate entry, ignored.
    }
    try {
	int len = is.read(buffer, 0, buffer.length);
	while (len > 0) {
	    zos.write(buffer, 0, len);
	    len = is.read(buffer, 0, buffer.length);
	}
    } catch (EOFException e) {	// this happens with some files; ignore
				// (fixed in JDK 1.2b4; Sun bug #4040920)
    }

    zos.closeEntry();
}



public static ZipOutputStream newZipFile(String name) throws IOException {
    FileOutputStream os = new FileOutputStream(name);
    BufferedOutputStream bos = new BufferedOutputStream(os);
    ZipOutputStream zos = new ZipOutputStream(bos);
    return zos;
}



public static void compose(String dst, Enumeration files) {
    try {
	ZipOutputStream zos = newZipFile(dst);

	while (files.hasMoreElements()) {
	    String fname = (String) files.nextElement();
	    ZipFile zsrc;
	    try {
		zsrc = new ZipFile(fname);
	    } catch (Exception e) {
		// (most Javas throw ZipException, but 1.2b4 throws IOException)
		// handle regular file, not zip.
		if (verbose) {
		    System.err.println("   plainfile: " + fname);
		}
		FileInputStream fis = new FileInputStream(fname);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ZipEntry zf = new ZipEntry(fname);
		addZipEntry(zf, zos, bis);
		bis.close();
		continue;
	    }
	    if (verbose) {
		System.err.println("   zipfile:   " + fname);
	    }
	    Enumeration e = zsrc.entries();
	    while (e.hasMoreElements()) {
		ZipEntry ze = (ZipEntry) e.nextElement();
		if (verbose) {
		    System.err.println("      " + ze.getName());
		}
		InputStream zis = zsrc.getInputStream(ze);
		addZipEntry(ze, zos, zis);
		zis.close();
	    }
	}
	zos.close();
    } catch (IOException ex) {
	if (verbose) {
	    ex.printStackTrace();
	} else {
	    System.err.println(ex);
	}
	System.exit(1);
    }
}



} // class ZipMerge

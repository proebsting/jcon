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

package rts;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ZipMerge {
    public static void main(String[] args) {
	if (args.length == 0) {
	    System.err.println("Usage: ZipMerge destination sourcefiles....");
	    System.exit(1);
	}
	String dst = args[0];
	String[] files = new String[args.length - 1];
	for (int i = 1; i < args.length; i++) {
	    files[i-1] = args[i];
	}
        compose(dst, files);
    }

    static byte[] buffer = new byte[10000];
    public static void addZipEntry(ZipEntry ze, ZipOutputStream zos, InputStream is) throws IOException {
	try {
            zos.putNextEntry(ze);
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
	}

        zos.closeEntry();
    }

    public static ZipOutputStream newZipFile(String name) throws IOException {
        FileOutputStream os = new FileOutputStream(name);
	BufferedOutputStream bos = new BufferedOutputStream(os);
        ZipOutputStream zos = new ZipOutputStream(bos);
        return zos;
    }

    public static void closeZipFile(ZipOutputStream zos) throws IOException {
	zos.close();
    }

    public static void compose(String dst, String[] files) {
        try {
            ZipOutputStream zos = newZipFile(dst);

            for (int k = 0; k < files.length; k++) {
                ZipFile zsrc;
		try {
                    zsrc = new ZipFile(files[k]);
		} catch (ZipException ze) {
		    // handle regular file, not zip.
	            FileInputStream fis = new FileInputStream(files[k]);
		    BufferedInputStream bis = new BufferedInputStream(fis);
	            ZipEntry zf = new ZipEntry(files[k]);
	            addZipEntry(zf, zos, bis);
		    continue;
		}
                Enumeration e = zsrc.entries();
                while (e.hasMoreElements()) {
                    ZipEntry ze = (ZipEntry) e.nextElement();
                    InputStream zis = zsrc.getInputStream(ze);
		    addZipEntry(ze, zos, zis);
                }
            }
	    closeZipFile(zos);
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }
}

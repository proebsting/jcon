//  wImage.java -- image operations

package jcon;

import java.awt.*;
import java.awt.image.*;
import java.io.*;



public final class wImage {



//  wImage.load(window, filename) -- load image and wait for completion

public static Image load(vWindow win, String fname) {
    Image im = Toolkit.getDefaultToolkit().getImage(fname);
    MediaTracker t = new MediaTracker(win.getCanvas());
    t.addImage(im, 0);
    try {
	t.waitForAll();
    } catch (InterruptedException e) {
	return null;
    }
    if (t.isErrorAny()) {
	return null;
    } else {
	return im;
    }
}



//  wImage.decode(window, s) -- decode DrawImage string, returning Image

public static Image decode(vWindow win, vString s) {
    byte[] data = s.getBytes();
    int width = 0;
    int i = 0;
    int c;

    try {
	while (data[i] == ' ') {		// skip spaces
	    i++;
	}
	while ((c = data[i]) >= '0' && c <= '9') {
	    width = 10 * width + (c - '0');	// read width
	    i++;
	}
	while (data[i] == ' ') {		// skip spaces
	    i++;
	}
	if (data[i++] != ',' || width <= 0) {	// skip comma
	    return null; /*FAIL*/
	}
	while (data[i] == ' ') {		// skip spaces
	    i++;
	}
	if (i >= data.length) {
	    return null; /*FAIL*/
	}

	if (data[i] == '#' || data[i] == '~') {
	    return heximage(win, width, s, i);		// bilevel image
	} else {
	    return palimage(win, width, s, i);		// palette image
	}
    
    } catch (ArrayIndexOutOfBoundsException e) {
	return null;					// malformed spec
    }
}



//  palimage(win, width, s, i) -- decode palette image

private static Image palimage(vWindow win, int width, vString s, int off) {
    byte[] data = s.getBytes();

    // parse palette name and get palette
    int i = off;
    while (data[i] != ',' && data[i] != ' ') {
	i++;
    }
    cPalette p = cPalette.New(vString.New(s, off + 1, i + 1));
    if (p == null) {
	return null; /*FAIL*/
    }

    while (data[i] == ' ') {		// skip spaces
	i++;
    }
    if (data[i] != ',') {
	return null; /*FAIL*/
    }
    off = i + 1;			// beginning of data

    // prescan data to validate and count pixels
    int n = 0;
    for (i = off; i < data.length; i++) {
	char c = (char) (data[i] & 0xFF);
	cEntry e = p.entry[c];
	if (e.valid || e.transpt) {
	    n++;
	} else if (c != ' ' && c != ',') {
	    return null; /*FAIL*/
	}
    }
    if (n % width != 0) {
	return null; /*FAIL*/
    }
    int height = n / width;

    // build array of pixel indices
    byte[] pix = new byte[width * height];
    int j = 0;
    for (i = off; i < data.length; i++) {
	char c = (char) (data[i] & 0xFF);
	cEntry e = p.entry[c];
	if (e.valid || e.transpt) {
	    pix[j++] = (byte) c;
	}
    }

    // create color model
    byte[] r = new byte[256];
    byte[] g = new byte[256];
    byte[] b = new byte[256];
    byte[] a = new byte[256];
    double invg = 1.0 / win.gamma;
    for (i = 0; i < 256; i++) {
	cEntry e = p.entry[i];
	if (e.valid) {
	    r[i] = (byte) (255 * Math.pow(e.r / 65535.0, invg));
	    g[i] = (byte) (255 * Math.pow(e.g / 65535.0, invg));
	    b[i] = (byte) (255 * Math.pow(e.b / 65535.0, invg));
	    a[i] = (byte) 255;
	}
    }
    ColorModel m = new IndexColorModel(8, 256, r, g, b, a);

    // create image
    return win.getCanvas().createImage(
	new MemoryImageSource(width, height, m, pix, 0, width));
}



//  heximage(win, width, s, off) -- decode bi-level (hex-digit) image

private static Image heximage(vWindow win, int width, vString s, int off) {
    byte[] data = s.getBytes();		// array of data bytes
    byte flag = data[off++];		// bi-level flag: '#' or '~'
    int ldig = (width + 3) / 4;		// hex digits per scan line

    // prescan data to validate and count hex digits
    int n = 0;
    for (int i = off; i < data.length; i++) {
	int c = data[i];
	if (hexdigit(c) >= 0) {
	    n++;
	} else if (c != ' ' && c != ',') {
	    return null; /*FAIL*/
	}
    }
    if (n % ldig != 0) {
	return null; /*FAIL*/
    }
    int height = n / ldig;

    // build array of pixel indices (0 for fg, 1 for bg/transparent)
    byte[] pix = hexpix(width, height, data, off);

    // create color model
    byte[] r = new byte[] { (byte)win.bg.getRed(),   (byte)win.fg.getRed() };
    byte[] g = new byte[] { (byte)win.bg.getGreen(), (byte)win.fg.getGreen() };
    byte[] b = new byte[] { (byte)win.bg.getBlue(),  (byte)win.fg.getBlue() };
    byte[] a = new byte[] { (byte) ((flag == '~') ? 0 : 255), (byte)255 }; 
    ColorModel m = new IndexColorModel(8, 2, r, g, b, a);

    // create image
    return win.getCanvas().createImage(
	new MemoryImageSource(width, height, m, pix, 0, width));
}



//  hexpix(w, h, data, offset) -- build pixel index array from hex digits

private static byte[] hexpix(int width, int height, byte[] data, int off) {

    byte[] pix = new byte[width * height];

    int p = -width;
    for (int row = 0; row < height; row++) {
	p += 2 * width;

	int bits = 0;
	int nbits = width % 4;

	// load first hex digit if < 4 bits
	if (nbits != 0) {
	    do {
		bits = hexdigit(data[off++]);
	    } while (bits < 0);
	}

	for (int i = 0; i < width; i++) {
	    if (nbits == 0) {
		do {
		    bits = hexdigit(data[off++]);
		} while (bits < 0);
		nbits = 4;
	    }
	    nbits--;
	    pix[--p] = (byte) ((bits >> nbits) & 1);
	}
    }
    return pix;
}



//  hexdigit(c) -- convert character from hex digit to binary bits  (-1 if err)

private static int hexdigit(int c)
{
    if (c >= '0' && c <= '9') {
	return c - '0';
    } else if (c >= 'A' && c <= 'F') {
	return c - 'A' + 10;
    } else if (c >= 'a' && c <= 'f') {
	return c - 'a' + 10;
    } else {
	return -1;
    }
}



//  wImage.Write(window, fname, x, y, w, h, q) -- write image to file
//
//  If fname ends in .jpg or .jpeg (case insensitive), write JPEG file.
//  Otherwise, write GIF file.
//  JPEG writing fails on versions of Java earlier than 1.2.
//
//  q is a quality spec.  If q < 0, default settings are used:
//  q=0.0 for JPEG and q=256 for GIF.
//
//  If 0.0 <= q <= 1.0, quality is interpreted on a floating scale.
//  For JPEG files, the given value is passed to the encoder.
//  For GIF files, q >= 0.9 produces a 256-color GIF file.
//
//  If q > 1, quality is interpreted on an integer scale.  For GIF files,
//  q specifies the number of colors to be output, limited to 256.
//  For JPEG files, q = 256 is equivalent to q = 0.90.

public static vValue Write(vWindow win, String fname,
			    int x, int y, int w, int h, double q) {

    Rectangle r = limitBounds(win, x, y, w, h);
    final int[] data = Grab(win, r);
    if (data == null) {
    	return null; /*FAIL*/
    }

    String lfname = fname.toLowerCase();
    if (lfname.endsWith(".jpg") || lfname.endsWith(".jpeg")) {
	return writeJPEG(win, data, fname, r.width, r.height, q);
    } else {
	return writeGIF(win, data, fname, r.width, r.height, q);
    }
}



//  writeGIF(win, data, fname, w, h, q) -- write GIF file

public static vValue writeGIF(vWindow win, int[] data, String fname,
				int w, int h, double q) {

    int n = 256;
    if (q > 1 && q < 256) {
	// number of colors specified
	n = (int) q;
    } else if (q >= 0.0 && q <= 1.0) {
	// convert from JPEG quality value
	n = (int) (2 * Math.pow(167, Math.sqrt(q)));
	if (n > 256) {
	    n = 256;
	}
    }
    Quantize.toNcolors(data, n);	// reduce to max of n colors

    ImageProducer p = new MemoryImageSource(w, h, data, 0, w);
    OutputStream o;
    try {
	o = new BufferedOutputStream(new FileOutputStream(fname));
	new GifEncoder(p, o).encode();
	o.close();
	return win;
    } catch (IOException iox) {
	return null; /*FAIL*/
    }
}



//  writeJPEG(win, data, fname, w, h, q) -- write JPEG file

public static vValue writeJPEG(vWindow win, int[] data, String fname,
				int w, int h, double q) {

    if (q < 0.0) {
	q = 0.75;		// default quality
    } else if (q > 1.0) {
	// convert from GIF terms
	q = (Math.log(q) - Math.log(2)) / Math.log(167);
	q = q * q;
	if (q > 0.90) {		// equiv of 256 colors
	    q = 0.90;
	}
    }

    // this is all done very indirectly because the
    // com.sun.image.codec.jpeg.* class may not be available
    // (if not, writing fails)

    Integer ww = new Integer(w);
    Integer hh = new Integer(h);
    Integer zero = new Integer(0);
    FileOutputStream f = null;

    try {
	f = new FileOutputStream(fname);

	// int rgbtype = BufferedImage.TYPE_INT_RGB;
	int rgbtype = Reflect.field(
	    "java.awt.image.BufferedImage", "TYPE_INT_RGB").getInt(null);

	// BufferedImage bi = new BufferedImage(w, h, rgbtype);
	Object bi = Reflect.construct("java.awt.image.BufferedImage", 
	    new Class[] { int.class, int.class, int.class },
	    new Object[] { ww, hh, new Integer(1) }) ;

	// bi.setRGB(0, 0, w, h, data, 0, w);
	Reflect.call(bi, "setRGB",
	    new Class[] { int.class, int.class, int.class, int.class,
		int[].class, int.class, int.class },
	    new Object[] { zero, zero, ww, hh, data, zero, ww });

	// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(f);
	Object encoder = Reflect.call(
	    "com.sun.image.codec.jpeg.JPEGCodec", "createJPEGEncoder",
	    new Class[] { OutputStream.class },
	    new Object[] { f });

	// JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
	Object param = Reflect.call(encoder, "getDefaultJPEGEncodeParam",
	    new Class[] { bi.getClass() },
	    new Object[] { bi });

	// param.setQuality((float)q, false);
	Reflect.call(param, "setQuality",
	    new Class[] { float.class, boolean.class },
	    new Object[] { new Float(q), new Boolean(false) });

	// encoder.encode(bi, param);
	Reflect.call(encoder, "encode",
	    new Class[] { bi.getClass(),
		Class.forName("com.sun.image.codec.jpeg.JPEGEncodeParam") },
	    new Object[] { bi, param });

	f.close();
	return win;

    } catch (Exception e) {
	try {
	    f.close();
	} catch (Exception ee) {
	}
	return null; /*FAIL*/
    }
}



//  wImage.Pixel(window, x, y, w, h) -- generate pixels for Pixel()

public static vDescriptor Pixel(vWindow win, int x, int y, int w, int h) {
    final double gamma = win.gamma;
    final int[] data = Grab(win, limitBounds(win, x, y, w, h));
    if (data == null) {
    	return null; /*FAIL*/
    }

    return new vClosure() {
	int i = 0;
	int previous = 0x08000000; 	// a value we won't ever see

	public vDescriptor Resume() {
	    if (i >= data.length) {
		return null; /*FAIL*/
	    }
	    int rgb = data[i++];
	    if (rgb != previous) {
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >>  8) & 0xFF;
		int b = rgb & 0xFF;
		r = (int) (65535 * Math.pow(r / 255.0, gamma));
		g = (int) (65535 * Math.pow(g / 255.0, gamma));
		b = (int) (65535 * Math.pow(b / 255.0, gamma));
		retval = vString.New(r + "," + g + "," + b);
	    }
	    return this;
	}; 

    }.Resume();
}



//  limitBounds(win, x, y, w, h) -- return intersection of x,y,w,h with window
//
//  The resulting rectangle has been adjusted by dx/dy.

private static Rectangle limitBounds (vWindow win, int x, int y, int w, int h) {

    // adjust negative width and height
    if (w < 0) {
	x -= (w = -w);
    }
    if (h < 0) {
	y -= (h = -h);
    }

    // factor in dx/dy
    x += win.dx;
    y += win.dy;

    // trim to image bounds
    if (x < 0) {
	w += x;
	x = 0;
    }
    if (y < 0) {
	h += y;
	y = 0;
    }
    if (w > win.getWidth() - x) {
	w = win.getWidth() - x;
    }
    if (h > win.getHeight() - y) {
	h = win.getHeight() - y;
    }

    if (w <= 0 || h <= 0) {
	return new Rectangle(0, 0, 0, 0);
    } else {
        return new Rectangle(x, y, w, h);
    }
}



//  Grab(window, rectangle) -- grab pixels as array of RGB ints

private static int[] Grab(vWindow win, Rectangle r) {

    if (r.width == 0 || r.height == 0) {
    	return null;
    }
    Image im = win.getCanvas().i;
    final int[] data = new int[r.width * r.height];
    PixelGrabber pg =
        new PixelGrabber(im, r.x, r.y, r.width, r.height, data, 0, r.width);
    try {
	pg.grabPixels();
	return data;
    } catch (InterruptedException e) {
	return null; /*FAIL*/
    }
}



} // class wImage

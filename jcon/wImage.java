//  wImage.java -- image operations

package rts;

import java.awt.*;
import java.awt.image.*;



final class wImage {



//  wImage.load(window, filename) -- load image and wait for completion

static Image load(vWindow win, String fname) {
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

static Image decode(vWindow win, vString s) {
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
    } else if (c >= 'A' && c <= 'Z') {
	return c - 'A' + 10;
    } else if (c >= 'a' && c <= 'z') {
	return c - 'a' + 10;
    } else {
	return -1;
    }
}



//  wImage.Pixels(window, x, y, w, h) -- generate pixels for Pixel()

static vDescriptor Pixel(vWindow win, int x, int y, int w, int h) {

    Image im = win.getCanvas().i;

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

    // trim to image bounds	//#%#% should gen bg color instead??
    if (x < 0) {
	w += x;
	x = 0;
    }
    if (y < 0) {
	w += y;
	y = 0;
    }
    if (w > im.getWidth(null)) {
	w = im.getWidth(null);
    }
    if (h > im.getHeight(null)) {
	h = im.getHeight(null);
    }
    if (w <= 0 || h <= 0) {
	return null; /*FAIL*/
    }

    // get data
    final int[] data = new int[w * h];
    PixelGrabber pg = new PixelGrabber(im, x, y, w, h, data, 0, w);
    try {
	pg.grabPixels();
    } catch (InterruptedException e) {
	return null; /*FAIL*/
    }

    // generate values
    final double gamma = win.gamma;

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



} // class wImage

//  cColors.java -- regular color palettes (c2 through c6)

package rts;

import java.awt.*;



final class cColors extends cPalette {

    int dim;	// size of one dimension of cube



cColors(int n) {

    name = vString.New("c" + n);
    dim = n - 1;

    // choose the labeling characters
    switch (n) {
	case 2:
	    chars = vString.New("kbgcrmywx");
	    break;
	case 3:
	    chars = vString.New("@ABCDEFGHIJKLMNOPQRSTUVWXYZabcd");
	    break;
	case 4: 
	    chars = vString.New("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
				"abcdefghijklmnopqrstuvwxyz{}$%&*+-/?@");
	    break;
	case 5:
	    chars = vCset.New(0, 5 * 5 * 5 + 4 * 4 - 1).mkString();
	    break;
	case 6:
	    chars = vCset.New(0, 6 * 6 * 6 + 5 * 5 - 1).mkString();
	    break;
    }

    // install the n * n * n color cube
    int i = 0;
    for (int r = 0; r < n; r++) {
	double rr = r / (n - 1.0);
	for (int g = 0; g < n; g++) {
	    double gg = g / (n - 1.0);
	    for (int b = 0; b < n; b++) {
		double bb = b / (n - 1.0);
		char c = chars.charAt(i++);
		entry[c] = new cEntry(rr, gg, bb, true, false);
	    }
	}
    }

    // install the "extra" grayscale entries
    int nn = n * (n - 1);
    for (int g = 0; g < nn; g++) {
	if (g % n != 0) {
	    double gg = g / (double) nn;
	    char c = chars.charAt(i++);
	    entry[c] = new cEntry(gg, gg, gg, true, false);
	}
    }
}


vString Key(wColor k) {
    int r = (int) (k.r * (dim - 1) + 0.5);
    int g = (int) (k.g * (dim - 1) + 0.5);
    int b = (int) (k.b * (dim - 1) + 0.5);
    int n = dim * dim * r + dim * g + b;
    return vString.New(chars.charAt(n));
}



} // class cColors

//  cColors.java -- regular color palettes (c2 through c6)

package jcon;

import java.awt.*;



final class cColors extends cPalette {

    int dim;		// size of one dimension of cube
    String grays;	// string of grays, in order



cColors(int n) {

    name = vString.New("c" + n);
    dim = n;

    // choose the labeling characters
    switch (n) {
	case 2:
	    chars = vString.New("kbgcrmywx");
	    grays = "kxw";
	    break;
	case 3:
	    chars = vString.New("@ABCDEFGHIJKLMNOPQRSTUVWXYZabcd");
	    grays = "@abMcdZ";
	    break;
	case 4: 
	    chars = vString.New("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
				"abcdefghijklmnopqrstuvwxyz{}$%&*+-/?@");
	    grays = "0$%&L*+-g/?@}";
	    break;
	case 5:
	    chars = vCset.New(0, 5 * 5 * 5 + 4 * 4 - 1).mkString();
	    grays = "\0}~\177\200\37\201\202\203\204>" + 
		    "\205\206\207\210]\211\212\213\214|";
	    break;
	case 6:
	    chars = vCset.New(0, 6 * 6 * 6 + 5 * 5 - 1).mkString();
	    grays = "\0\330\331\332\333\334+\335\336\337\340\341V\342\343\344" +
		"\345\346\201\347\350\351\352\353\254\354\355\356\357\360\327";
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
    if (k.r == k.g && k.g == k.b) {
	// this is a shade of gray
	int n = dim * (dim - 1) + 1;		// number of grays present
	int i = (int) (k.g * (n - 1) + 0.5);	// index of best one
	return vString.New(grays.charAt(i));
    } else {
	// not a shade of gray
	int r = (int) (k.r * (dim - 1) + 0.5);	// closest red value
	int g = (int) (k.g * (dim - 1) + 0.5);	// closest green value
	int b = (int) (k.b * (dim - 1) + 0.5);	// closest blue value
	int i = dim * dim * r + dim * g + b;	// index into basic cube
	return vString.New(chars.charAt(i));
    }
}



} // class cColors

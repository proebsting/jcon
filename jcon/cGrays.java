//  cGrays.java -- regular monochrome palettes (g2 through g256)

package rts;

import java.awt.*;

final class cGrays extends cPalette {

static final String g64chars =
    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz{}$%&*+-/?@";


cGrays(int n) {
    vByteBuffer b = new vByteBuffer(256);

    name = vString.New("g" + n);
    for (int i = 0; i < n; i++) {
	char c = (n <= 64) ? g64chars.charAt(i) : (char)i;
	b.append(c);
	double g = i / (n - 1.0);
	entry[c] = new cEntry(g, g, g, true, false);
    }
    chars = b.mkString();
}


vString Key(wColor k) {
    double lum = 0.299 * k.r + 0.587 * k.g + 0.114 * k.b;
    return vString.New(chars.charAt((int) (lum * (chars.length() - 1) + 0.5)));
}


} // class cGrays

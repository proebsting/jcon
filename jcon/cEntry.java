//  cEntry.java -- a single color palette entry

package jcon;

final class cEntry {

    int r, g, b;		// linear rgb, 0 - 65535
    boolean valid;		// is this a valid (opaque) color?
    boolean transpt;		// is this a transparency indicator?

cEntry(double r, double g, double b, boolean valid, boolean transpt) {
    this.r = (int) (65535 * r);
    this.g = (int) (65535 * g);
    this.b = (int) (65535 * b);
    this.valid = valid;
    this.transpt = transpt;
}

} // class cEntry

//  cPalette.java -- an Icon color palette
//
//  superclass of cNamed, cColors, and cGrays

package rts;

import java.util.*;
import java.awt.*;



abstract class cPalette {
    vString name;	// name, e.g. "c2" or "g8"
    vString chars;	// chars, e.g. "rgbcmykwx" or "01234567"
    cEntry entry[];	// array of 256 entries, indexed by char

abstract vString Key(wColor k);		// PaletteKey(W, p, k)



static Hashtable cache = new Hashtable();
static cEntry nullentry = new cEntry(0, 0, 0, true, false);
static cEntry xptentry = new cEntry(0, 0, 0, false, true);



cPalette() {		// default constructor used by subclasses
    entry = new cEntry[256];
    for (int i = 0; i < 256; i++) {
	entry[i] = nullentry;
    }
    entry['~'] = xptentry;
    entry[0xFF] = xptentry;
}



static final cPalette New(vString name) {	// factory

    if (name.length() < 2) {
	return null;
    }
    Object o = cache.get(name);
    if (o != null) {
	return (cPalette) o;
    }

    char c = name.charAt(0);
    int n;
    try {
	n = Integer.parseInt(name.toString().substring(1));
    } catch (Exception e) {
	return null;
    }

    cPalette p;
    if (c == 'c' && n == 1) {
	p = new cNamed();
    } else if (c == 'c' && n >= 2 && n <= 6) {
	p = new cColors(n);
    } else if (c == 'g' && n >= 2 && n <= 256) {
	p = new cGrays(n);
    } else {
	return null;
    }
    cache.put(name, p);
    return p;
}



} // class cPalette

//  wFont.java -- font handling

package rts;

import java.awt.*;
import java.awt.event.*;
import java.util.*;



class wFont extends Font {

    vString spec;	// original spcification string

static final String DEFAULT_FONT = "fixed";	// initial font specification
static final int DEFAULT_SIZE = 14;		// default font size




//  new wFont(name, style, ptsize, spec) -- create new Icon font

wFont(String name, int style, int ptsize, vString spec) {
    super(name, style, ptsize);
    this.spec = spec;
}



//  parse(s) -- interpret Icon font spec and return wFont value (or null)

static wFont parse(vString spec) {
    int size = 0;
    int style = 0;
    String s = spec.value;

    // check for special-cased X name, and model (imperfectly)
    if (s.equals("fixed")) {
	s = "mono,10";
    }

    // set up string scanner
    StringTokenizer tkr = new StringTokenizer(s, ",");
    if (! tkr.hasMoreTokens()) {
    	return null;
    }

    // get family name (case sensitive)
    // map Icon standard names (recognized insensitively)
    String family = tkr.nextToken();
    String f = family.toLowerCase();
    if (f.equals("mono"))		family = "DialogInput";
    else if (f.equals("typewriter"))	family = "Monospaced";
    else if (f.equals("sans"))		family = "SansSerif";
    else if (f.equals("serif"))		family = "Serif";

    // process characteristics
    while (tkr.hasMoreTokens()) {
    	String tk = tkr.nextToken().toLowerCase();
	try {
	    // if numeric, it's a font size
	    size = Integer.parseInt(tk);
	    continue;
	} catch (Exception e) {
	    // anything else is a style characteristic
	    // unrecognized attributes are ignored
	    if (tk.equals("italic"))		style |= Font.ITALIC;
	    else if (tk.equals("oblique"))	style |= Font.ITALIC;
	    else if (tk.equals("bold"))		style |= Font.BOLD;
	    else if (tk.equals("demi"))		style |= Font.BOLD;
	    else if (tk.equals("demibold"))	style |= Font.BOLD;
	}
    }

    // set size if none was specified
    if (size == 0) {
    	size = DEFAULT_SIZE;
    }

    //#%#%#% convert pixels to points here
    //#%#%#% (doesn't seem to be needed?)

    return new wFont(family, style, size, spec);
}



} // class wFont

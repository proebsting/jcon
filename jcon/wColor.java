//  wColor.java -- color issues

package rts;

import java.awt.*;
import java.awt.event.*;



class wHue {		// table entry for named colors
    String name;
    String ish;
    float h, s, b;

    wHue(String name, String ish, int h, int s, int b) {
        this.name = name;
        this.ish = ish;
        this.h = (float)(h / 360.0);
        this.s = (float)(s / 100.0);
        this.b = (float)(b / 100.0);
    }
}



class wColor extends Color {

    vString spec;	// original spcification string

static wHue[] huelist = {
    //        color       ish-form     hue  sat  brt 
    new wHue("black",    "blackish",     0,   0,   0),
    new wHue("blue",     "bluish",     240, 100, 100),
    new wHue("brown",    "brownish",    30, 100,  50),	//*
    new wHue("cyan",     "cyanish",    180, 100, 100),
    new wHue("gray",     "grayish",      0,   0,  50),
    new wHue("green",    "greenish",   120, 100, 100),
    new wHue("grey",     "greyish",      0,   0,  50),
    new wHue("magenta",  "magentaish", 300, 100, 100),
    new wHue("orange",   "orangish",    15, 100, 100),
    new wHue("pink",     "pinkish",    345,  50, 100),	//*
    new wHue("purple",   "purplish",   270, 100, 100),
    new wHue("red",      "reddish",      0, 100, 100),
    new wHue("violet",   "violetish",  270,  50, 100),	//*
    new wHue("white",    "whitish",      0,   0, 100),
    new wHue("yellow",   "yellowish",   60, 100, 100),
};

static final wColor Black = wColor.parse(iNew.String("black"));
static final wColor White = wColor.parse(iNew.String("white"));



//  parse(s) -- interpret Icon color spec and return wColor value (or null)

static wColor parse(vString s) {

    wColor k = parseName(s);
    if (k == null) {
    	k = parseInts(s);
    }
    return k;
}



//  parseName(s) -- parse colors by name
//  #%#%#%# INCOMPLETE: handles just basic colors, no modifiers
//  #%#%#%# need to fold string to lower case before processing

private static wColor parseName(vString s) {
    for (int i = 0; i < huelist.length; i++) {
	wHue h = huelist[i];
    	if (s.value.equals(h.name)) {
	    return new wColor(s, Color.HSBtoRGB(h.h, h.s, h.b));
	}
    }
    return null;
}



//  parseInts(s) -- parse "rrrrr,ggggg,bbbbb" color spec

private static wColor parseInts(vString s) {
    return null;		//#%#%#%# TBD
}




wColor(vString s, int k) {
    super(k);
    spec = s;
}



} // class wColor

//  wColor.java -- color handling

package rts;

import java.awt.*;
import java.awt.event.*;
import java.util.*;



final class wHue {		// table entry for named colors
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



final class wColor extends Color {

    vString spec;	// original spcification string
    double gamma;	// gamma correction applied
    double r, g, b;	// linear (uncorrected) r/g/b values, 0 - 1



static Hashtable hueTable = new Hashtable();

static {
    //		      color       ish-form     hue  sat  brt
    install(new wHue("black",    "blackish",     0,   0,   0));
    install(new wHue("blue",     "bluish",     240, 100, 100));
    install(new wHue("brown",    "brownish",    30, 100,  50));
    install(new wHue("cyan",     "cyanish",    180, 100, 100));
    install(new wHue("gray",     "grayish",      0,   0,  50));
    install(new wHue("green",    "greenish",   120, 100, 100));
    install(new wHue("grey",     "greyish",      0,   0,  50));
    install(new wHue("magenta",  "magentaish", 300, 100, 100));
    install(new wHue("orange",   "orangish",    15, 100, 100));
    install(new wHue("pink",     "pinkish",    345,  50, 100));
    install(new wHue("purple",   "purplish",   270, 100, 100));
    install(new wHue("red",      "reddish",      0, 100, 100));
    install(new wHue("violet",   "violetish",  270,  50, 100));
    install(new wHue("white",    "whitish",      0,   0, 100));
    install(new wHue("yellow",   "yellowish",   60, 100, 100));
};

private static void install(wHue h) {
    hueTable.put(h.name, h);
    hueTable.put(h.ish, h);
}

//  these must follow hueTable initialization, with nohue first:
static final wHue nohue = new wHue("", "", 0, 0, 0);
static final wColor Black = New(vString.New("black"), iConfig.Gamma);
static final wColor White = New(vString.New("white"), iConfig.Gamma);




//  wColor.New(s, gamma) -- create new color, given an Icon color spec

static wColor New(vString s, double gamma) {
    wColor c;
    if (s.charAt(0) == '#') {
	c = newHexColor(s, gamma);
    } else {
	c = newIntColor(s, gamma);
    }
    if (c == null) {
       c = newNamedColor(s, gamma);
    }
    return c;	// null for failure
}

//  new wColor(s, gamma, r, g, b) -- internal wColor constructor

private wColor(vString s, double gamma, double r, double g, double b) {
    super(jrgb(gamma, r, g, b));
    this.spec = s;
    this.gamma = gamma;
    this.r = r;
    this.g = g;
    this.b = b;
}

//  jrgb(gamma, r, g, b) -- calculate Java RGB integer color spec

private static int jrgb(double gamma, double r, double g, double b) {
    double invg = 1.0 / gamma;
    int ir = (int) (255 * Math.pow(r, invg));
    int ig = (int) (255 * Math.pow(g, invg));
    int ib = (int) (255 * Math.pow(b, invg));
    return (ir << 16) + (ig << 8) + ib;
}



//  newIntColor(s, gamma) -- parse "rrrrr,ggggg,bbbbb" color spec

private static wColor newIntColor(vString s, double gamma) {
    StringTokenizer tkr = new StringTokenizer(s.toString(), ",");
    try {
	int r = Integer.parseInt(tkr.nextToken());
	int g = Integer.parseInt(tkr.nextToken());
	int b = Integer.parseInt(tkr.nextToken());
	if (tkr.hasMoreTokens() || r < 0 || g < 0 || b < 0
	    || r > 65535 || g > 65535 || b > 65535) {
		return null; /*FAIL*/
	    }
	return new wColor(s, gamma, r / 65535.0, g / 65535.0, b / 65535.0);
    } catch (Exception e) {
	return null; /*FAIL*/
    }
}



//  newHexColor(s, gamma) -- parse #rgb forms of color spec

private static wColor newHexColor(vString s, double gamma) {
    byte spec[] = s.getBytes();
    int n;
    double d;
    switch (spec.length) {
    	case 4:		n = 1;  d = 15.0;  break;
	case 7:		n = 2;  d = 255.0;  break;
	case 10:	n = 3;  d = 4095.0;  break;
	case 13:	n = 4;  d = 65535.0;  break;
	default:	return null; /*FAIL*/
    }
    int r = hex(spec, 1, n);
    int g = hex(spec, n + 1, n);
    int b = hex(spec, 2 * n + 1, n);
    if (r < 0 || g < 0 || b < 0) {
    	return null; /*FAIL*/
    }
    return new wColor(s, gamma, r / d, g / d, b / d);
}

private static int hex(byte[] b, int i, int n) {
    int r = 0;
    for (int j = 0; j < n; j++) {
	r = 16 * r;
    	char c = (char) b[i++];
	if ('0' <= c && c <= '9') {
	    r += c - '0';
	} else if ('a' <= c && c <= 'f') {
	    r += c - 'a' + 10;
	} else if ('A' <= c && c <= 'F') {
	    r += c - 'A' + 10;
	} else {
	    return -1;
	}
    }
    return r;
}



//  newNamedColor(s, gamma) -- parse named color

private static wColor newNamedColor(vString vs, double gamma) {
    String js = vs.toString().toLowerCase();
    StringTokenizer tkr = new StringTokenizer(js, " -");

    try {
	String t = tkr.nextToken();

	double lgtAdjust = parseLightness(t);	// lightness adjustment
	if (Double.isNaN(lgtAdjust)) {
	    lgtAdjust = 0.0;
	} else {
	    t = tkr.nextToken();
	}

	double satAdjust = parseSaturation(t);	// saturation adjustment
	if (Double.isNaN(satAdjust)) {
	    satAdjust = 1.0;
	} else {
	    t = tkr.nextToken();
	}

	wHue h1, h2;			// two (or possibly just one) named hues
	double weight;			// weight of first hue (25%, 50%, 100%)

	h1 = parseIsh(t);		// check for "ish" form

	if (h1 != null) {

	    t = tkr.nextToken();	// had an ish form
	    h2 = parseHue(t);		// must have a second hue
	    weight = 0.25;

	} else {

	    h1 = parseHue(t);		// must have a first hue
	    if (h1 == null) {
		return null; /*FAIL*/
	    }

	    if (tkr.hasMoreTokens()) {
		t = tkr.nextToken();
		h2 = parseHue(t);	// two equally weighted hues
		if (h2 == null) {
		    return null; /*FAIL*/
		}
		weight = 0.50;
	    } else {
		h2 = nohue;		// only one hue counts
		weight = 1.00;
	    }
	}

	if (tkr.hasMoreTokens()) {	// should be no more tokens
	    return null; /*FAIL*/
	}

	// at this point we have a valid parse; figure out what it means

	// start by creating a weighted mix of the two basic colors
	double d = h1.h - h2.h;
	double h;
	if (d > 0.5) {
	    h = weight * (h1.h - 1.0) + (1.0 - weight) * h2.h;
	} else if (d < -0.5) {
	    h = weight * h1.h + (1.0 - weight) * (h2.h - 1.0);
	} else {
	    h = weight * h1.h + (1.0 - weight) * h2.h;
	}
	if (h < 0.0) {
	    h += 1.0;
	}

	double s = weight * h1.s + (1 - weight) * h2.s;
	double b = weight * h1.b + (1 - weight) * h2.b;

	// now apply lightness and saturation adjustments

	if (lgtAdjust > 0.0) {
	    b = (1.0 + lgtAdjust) * b;
	    s = (1.0 - lgtAdjust) * s;
	    if (b > 1.0) {
		b = 1.0;
	    }
	} else /* lgtAdjust <= 0.0 */ {
	    b = (1.0 + lgtAdjust) * b;
	}

	b = satAdjust * b + (1.0 - satAdjust) * (b - 0.5 * b * s);
	s = satAdjust * s;

	int jrgb = Color.HSBtoRGB((float)h, (float)s, (float)b) & 0xFFFFFF;
	int ir = (jrgb >> 16) & 0xFF;
	int ig = (jrgb >>  8) & 0xFF;
	int ib = (jrgb >>  0) & 0xFF;
	return new wColor(vs, gamma, ir / 255.0, ig / 255.0, ib / 255.0);

    } catch (Exception e) {
	return null; /*FAIL*/
    }
}



private static double parseLightness(String s) {
    if (s.length() > 3) {
	switch (s.charAt(2)) {
	    case 'l':  if (s.equals("pale"))   { return +.666667; }
	    case 'g':  if (s.equals("light"))  { return +.333333; }
	    case 'd':  if (s.equals("medium")) { return 0.000000; }
	    case 'r':  if (s.equals("dark"))   { return -.333333; }
	    case 'e':  if (s.equals("deep"))   { return -.666667; }
	}
    }
    return 0.0 / 0.0;		// NaN signals failure
}



private static double parseSaturation(String s) {
    if (s.length() > 0) {
	switch (s.charAt(0)) {
	    case 'w':  if (s.equals("weak"))     { return 0.25; }
	    case 'm':  if (s.equals("moderate")) { return 0.50; }
	    case 's':  if (s.equals("strong"))   { return 0.75; }
	    case 'v':  if (s.equals("vivid"))    { return 1.00; }
	}
    }
    return 0.0 / 0.0;		// NaN signals failure
}



private static wHue parseIsh(String s) {
    if (s.endsWith("ish")) {
	return (wHue) hueTable.get(s);
    } else {
	return null;
    }
}



private static wHue parseHue(String s) {
    if (s.endsWith("ish")) {
	return null;
    } else {
	return (wHue) hueTable.get(s);
    }
}



} // class wColor

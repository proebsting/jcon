//  wTTY.java -- read() and write() support for graphics windows

package rts;

import java.awt.*;



class wTTY {

    int xloc, yloc;		// current text position

    boolean echo = true;	// echo characters on input?
    boolean cursor = false;	// show text cursor on input?

    static final vString ON = iNew.String("on");
    static final vString OFF = iNew.String("off");



//  new wTTY() -- create new tty structure

wTTY() {		
    xloc = 0;
    yloc = 20;	//#%#%#%#%#%#%#%#%#%##%#%# should depend on font 
}



//  read(win) -- read line from window

vString read(vWindow win) {
    char c;
    StringBuffer b = new StringBuffer();

    do {
	c = rchar(win, b);			// read character into b
    } while (c != '\n');			// until \n is seen

    b.setLength(b.length() - 1);		// remove \n from buffer
    return iNew.String(b.toString());		// return string
}



//  reads(win, n) -- read n characters from window

vString reads(vWindow win, long n) {
    StringBuffer b = new StringBuffer((int) n);

    while (b.length() < n) {			// until buffer is full
	rchar(win, b);				// read character into b
    }
    return iNew.String(b.toString());		// return string
}



//  rchar(win) -- read one character from window
//
//  handles cursor display and echoing
//
//  maps \r into \n
//  handles \b and \d

private static vString NEWLINE = iNew.String("\n");

private char rchar(vWindow win, StringBuffer b) {
    vValue e;
    FontMetrics m = win.getFontMetrics();

    if (cursor) {				// display cursor
    	win.FillRectangle(xloc, yloc, m.charWidth('W'), m.getDescent());
    }

    do {
	e = win.Event();
    } while (! (e instanceof vString));		// wait for character event

    if (cursor) {				// hide cursor
    	win.EraseArea(xloc, yloc, m.charWidth('W'), m.getDescent());
    }

    String s = ((vString) e).value;
    char c = s.charAt(0);

    if (c == '\b' || c == '\177') {		// if backspace or delete

	if (b.length() > 0) {			// if buffer is not empty
	    c = b.charAt(b.length() - 1);	// save deleted character
	    b.setLength(b.length() - 1);	// remove from buffer
	    if (echo) {
		int w = m.charWidth(c);
		xloc -= w;			// backspace text cursor
		win.EraseArea(xloc, yloc - m.getMaxAscent(), w, m.getHeight());
	    }
	}

    } else {

	if (c == '\r') {		// turn \r into \n
	    c = '\n';
	    s = "\n";
	}
	b.append(c);			// add to buffer
        if (echo) {
	    writes(win, s);		// echo to screen
	}
    }

    return c;
}



//  writes(win, s) -- write string to window

//#%#%#%##% need to handle \r and \t

void writes(vWindow win, String s) {
    int i = 0;
    int j;
    while ((j = s.indexOf('\n', i)) >= 0) {		// find embedded '\n'
	if (j > i) {
	    wstring(win, s.substring(i, j));
	}
	newline(win);
	i = j + 1;
    }
    if (i < s.length()) {
	wstring(win, s.substring(i, s.length()));
    }
}



//  wstring(win, s) -- write string to window

private void wstring(vWindow win, String s) {
    FontMetrics m = win.getFontMetrics();
    int w = m.stringWidth(s);
    int a = m.getMaxAscent();
    win.EraseArea(xloc, yloc - a, w, a + m.getMaxDescent());
    win.DrawString(xloc, yloc, s);
    xloc += w;
}



//  newline(win) -- write newline to window

void newline(vWindow win) {
    Dimension d = win.getCanvas().getSize();
    FontMetrics m = win.getFontMetrics();

    int leading = win.Leading();
    int limit = d.height - m.getMaxDescent() - m.getLeading();
	// shouldn't need getLeading, but even MaxDescent() sometimes lies 

    xloc = 0;			// set new text position
    yloc += leading;

    if (yloc > limit) {
	// need to scroll upward
	int shift = yloc - limit;
        win.CopyArea(0, shift, d.width, d.height, 0, 0);
    	yloc = limit;
    }
}


vValue Echo(String v) {
    if (v != null) {
	if (v.equals("on")) {
	    echo = true;
	} else if (v.equals("off")) {
	    echo = false;
	} else {
	    return null; /*FAIL*/
	}
    }
    return echo ? ON : OFF;
}

vValue Cursor(String v) {
    if (v != null) {
	if (v.equals("on")) {
	    cursor = true;
	} else if (v.equals("off")) {
	    cursor = false;
	} else {
	    return null; /*FAIL*/
	}
    }
    return cursor ? ON : OFF;
}

vValue X(String v) {
    if (v != null) try {
	xloc = Integer.parseInt(v);
    } catch (Exception e) {
	return null; /*FAIL*/
    }
    return iNew.Integer(xloc);
}

vValue Y(String v) {
    if (v != null) try {
	yloc = Integer.parseInt(v);
    } catch (Exception e) {
	return null; /*FAIL*/
    }
    return iNew.Integer(yloc);
}

vValue Row(vWindow win, String v) {
    FontMetrics m = win.getFontMetrics();
    int leading = win.Leading();
    int a = m.getMaxAscent();

    if (v != null) try {
	yloc = Integer.parseInt(v);
	yloc = (yloc - 1) * leading + a;
    } catch (Exception e) {
	return null; /*FAIL*/
    }
    if (leading == 0) {
    	iRuntime.error(204);	// this is what v9 does
    }
    return iNew.Integer((yloc - a) / leading + 1);
}

vValue Col(vWindow win, String v) {
    FontMetrics m = win.getFontMetrics();
    int a = m.getMaxAdvance();
    if (a <= 0) {
	a = m.charWidth('W');
    }
    if (v != null) try {
	xloc = Integer.parseInt(v);
	xloc = (xloc - 1) * a;
    } catch (Exception e) {
	return null; /*FAIL*/
    }
    return iNew.Integer(xloc / a + 1);
}



} // wTTY

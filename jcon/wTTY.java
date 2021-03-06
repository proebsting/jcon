//  wTTY.java -- read() and write() support for graphics windows

package jcon;

import java.awt.*;



public final class wTTY {

    private int xcur, ycur;		// current text position

    private boolean echo = true;	// echo characters on input?
    private boolean cursor = false;	// show text cursor on input?

    private static final vString ON = vString.New("on");
    private static final vString OFF = vString.New("off");



//  read(win) -- read line from window

public vString read(vWindow win) {
    char c;
    StringBuffer b = new StringBuffer();

    iKeyword.output.file().flush();
    win.flush();
    do {
	c = rchar(win, b);			// read character into b
    } while (c != '\n');			// until \n is seen

    b.setLength(b.length() - 1);		// remove \n from buffer
    return vString.New(b.toString());		// return string
}



//  reads(win, n) -- read n characters from window

public vString reads(vWindow win, long n) {
    StringBuffer b = new StringBuffer((int) n);

    iKeyword.output.file().flush();
    win.flush();
    while (b.length() < n) {			// until buffer is full
	rchar(win, b);				// read character into b
    }
    return vString.New(b.toString());		// return string
}



//  rchar(win) -- read one character from window
//
//  handles cursor display and echoing
//
//  maps \r into \n
//  handles \b and \d

private static vString NEWLINE = vString.New("\n");

private char rchar(vWindow win, StringBuffer b) {
    vValue e;
    FontMetrics m = win.getFontMetrics();

    if (cursor) {				// display cursor
	win.FillRectangle(xcur, ycur, m.charWidth('W'), m.getDescent());
    }

    do {
	e = win.Event();
    } while (! (e instanceof vString));		// wait for character event

    if (cursor) {				// hide cursor
	win.EraseArea(xcur, ycur, m.charWidth('W'), m.getDescent());
    }

    char c = ((vString) e).charAt(0);

    if (c == '\b' || c == '\177') {		// if backspace or delete

	if (b.length() > 0) {			// if buffer is not empty
	    c = b.charAt(b.length() - 1);	// save deleted character
	    b.setLength(b.length() - 1);	// remove from buffer
	    if (echo) {
		int w = m.charWidth(c);
		xcur -= w;			// backspace text cursor
		win.EraseArea(xcur, ycur - m.getMaxAscent(), w, m.getHeight());
	    }
	}

    } else {

	if (c == '\r') {			// turn \r into \n
	    c = '\n';
	}
	b.append(c);				// add to buffer
	if (echo) {
	    writes(win, vString.New(c));	// echo to screen
	}
    }

    return c;
}



//  writes(win, s) -- write string to window

public void writes(vWindow win, vString s) {
    FontMetrics m = win.getFontMetrics();
    byte[] b = s.getBytes();
    int i, j;

    for (i = j = 0; j < b.length; j++) {
	char c = (char) b[j];
	switch (c) {
	    case '\n':
		wchars(win, m, b, i, j);
		newline(win);
		break;
	    case '\r':
		wchars(win, m, b, i, j);
		xcur = 0;
		break;
	    case '\t':
		wchars(win, m, b, i, j);
		int tabw = 8 * win.Fwidth();
		xcur = xcur - (xcur % tabw) + tabw;
		break;
	    default:
		continue;
	}
	i = j + 1;
    }
    wchars(win, m, b, i, j);
}

//  wchars(win, m, b, i, j) -- write b[i:j] to window

private void wchars(vWindow win, FontMetrics m, byte[] b, int i, int j) {
    if (i >= j) {
	return;
    }
    String s = new String(b, i, j - i);
    int w = m.stringWidth(s);
    int a = m.getMaxAscent();
    win.EraseArea(xcur, ycur - a, w, win.Leading());
    win.DrawString(xcur, ycur, s);
    xcur += w;
}



//  newline(win) -- write newline to window

public void newline(vWindow win) {
    Dimension d = win.getCanvas().getSize();
    FontMetrics m = win.getFontMetrics();

    int leading = win.Leading();
    int limit = d.height - m.getMaxDescent() - m.getLeading();
	// shouldn't need getLeading, but even MaxDescent() sometimes lies

    xcur = 0;			// set new text position
    ycur += leading;

    if (ycur > limit) {
	// need to scroll upward
	int shift = ycur - limit;
	win.CopyArea(win, 0, shift, d.width, d.height, 0, 0);
	ycur = limit;
    }
}


public vValue Echo(String v) {
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

public vValue Cursor(String v) {
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

public vValue X(String v) {
    if (v != null) try {
	xcur = wAttrib.parseInt(v);
    } catch (Exception e) {
	return null; /*FAIL*/
    }
    return vInteger.New(xcur);
}

public vValue Y(String v) {
    if (v != null) try {
	ycur = wAttrib.parseInt(v);
    } catch (Exception e) {
	return null; /*FAIL*/
    }
    return vInteger.New(ycur);
}

public vValue Row(vWindow win, String v) {
    FontMetrics m = win.getFontMetrics();
    int leading = win.Leading();
    int a = m.getMaxAscent();

    if (v != null) try {
	ycur = wAttrib.parseInt(v);
	ycur = (ycur - 1) * leading + a;
    } catch (Exception e) {
	return null; /*FAIL*/
    }
    if (leading == 0) {
	iRuntime.error(204);	// this is what v9 does: real division by 0
    }
    return vInteger.New((ycur - a) / leading + 1);
}

public vValue Col(vWindow win, String v) {
    int a = win.Fwidth();
    if (v != null) try {
	xcur = wAttrib.parseInt(v);
	xcur = (xcur - 1) * a;
    } catch (Exception e) {
	return null; /*FAIL*/
    }
    return vInteger.New(xcur / a + 1);
}



public vValue PointerRow(vWindow win) {	// return row number of mouse
    FontMetrics m = win.getFontMetrics();
    int leading = win.Leading();
    int a = m.getMaxAscent();

    if (leading == 0) {
	iRuntime.error(204);	// this is what v9 does: real division by 0
    }
    return vInteger.New((win.getCanvas().yloc - a) / leading + 1);
}

public vValue PointerCol(vWindow win) {	// return column number of mouse
    return vInteger.New(win.getCanvas().xloc / win.Fwidth() + 1);
}



} // wTTY

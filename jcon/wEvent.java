//  wEvent.java -- event processing

package jcon;

import java.awt.*;
import java.awt.event.*;



public final class wEvent implements WindowListener, ComponentListener,
    KeyListener, MouseListener, MouseMotionListener
{
    wCanvas c;		// associated canvas



//  the following definitions are consistent with Icon v9

public static final int ShiftFlag   = 4 << 16;
public static final int MetaFlag    = 2 << 16;
public static final int ControlFlag = 1 << 16;
public static final int LeftmostFlag = 4 << 16;

public static final int LPress = -1;
public static final int MPress = -2;
public static final int RPress = -3;
public static final int LRelease = -4;
public static final int MRelease = -5;
public static final int RRelease = -6;
public static final int LDrag = -7;
public static final int MDrag = -8;
public static final int RDrag = -9;
public static final int Resize = -10;



public wEvent(wCanvas cv) {			// new wEvent(w)
    this.c = cv;
}



static void register(wCanvas cv) {		// register event handlers

    wEvent handler = new wEvent(cv);
    cv.f.addWindowListener(handler);
    cv.addKeyListener(handler);
    cv.addMouseListener(handler);
    cv.addMouseMotionListener(handler);
    cv.addComponentListener(handler);
}



//  enqueue(a, x, y, e) -- enqueue Icon event code a derived from Java event e

public void enqueue(vValue a, int x, int y, InputEvent e) {
    int flags = 0;
    long msec = 0;

    if (e != null) {
	msec = c.interval(e.getWhen());
	if (e.isControlDown()) {
	    flags += ControlFlag;
	}
	if (e.isShiftDown()) {
	    flags += ShiftFlag;
	}
	if (!(e instanceof MouseEvent) && (e.isMetaDown() || e.isAltDown())) {
	    flags += MetaFlag;
	}
    }

    int expo = 0;
    while (msec >= 0x1000) {
	msec >>= 4;
	expo++;
    }

    c.enqueue(a,
	vInteger.New(flags | (x & 0xFFFF)),
	vInteger.New((expo << 28) | (msec << 16) | (y & 0xFFFF)));
}



//  dequeue(canvas, dx, dy) -- get next event from a window

public static vValue dequeue(wCanvas c, int dx, int dy) {
    vValue a, xv, yv;

    // get first value; return null if queue is empty
    a = c.evq.Get();
    if (a == null) {
	return null;
    }

    //  get two more values, which must be integers
    xv = c.evq.Get();
    yv = c.evq.Get();
    if (xv == null || !(xv instanceof vInteger)
    ||  yv == null || !(yv instanceof vInteger)) {
	iRuntime.error(143);		// malformed queue
    }

    long x = ((vInteger)xv).value;
    long y = ((vInteger)yv).value;
    if (x < 0 || x >= (LeftmostFlag << 1) || y < 0) {
	iRuntime.error(143);		// malformed queue
    }

    iKeyword.control.set(((x & ControlFlag) != 0) ? vNull.New() : null);
    iKeyword.meta   .set(((x & MetaFlag) != 0)    ? vNull.New() : null);
    iKeyword.shift  .set(((x & ShiftFlag) != 0)   ? vNull.New() : null);

    long msec = (y >> 16) & 0xFFF;	// set &interval
    long expo = y >> 28;
    iKeyword.interval.set(vInteger.New(msec << (4 * expo)));

    x = (long)(short)x - dx;		// extract & translate signed coords
    y = (long)(short)y - dy;

    iKeyword.x.assign(x);		// also sets k$col
    iKeyword.y.assign(y);		// also sets k$row

    if (x == -1 && y == -1 && a instanceof vInteger
    			&& ((vInteger)a).value == Resize) {
	// this is a window-closed event
    	vWindow win = (vWindow) c.wlist.elementAt(0);
	if (win != null) {
	    win.close();		// mark as closed in Icon
	}
    }

    return a;				// return event code
}



public void windowOpened(WindowEvent e)		{}
public void windowClosed(WindowEvent e)		{}
public void windowActivated(WindowEvent e)	{}
public void windowDeactivated(WindowEvent e)	{}
public void windowIconified(WindowEvent e)	{}
public void windowDeiconified(WindowEvent e)	{}

public void windowClosing(WindowEvent e) {		// user closed window
    enqueue(vInteger.New(Resize), -1, -1, null);	// enqueue event
    							// (close when dequeued)
}



public void componentHidden(ComponentEvent e)	{}
public void componentMoved(ComponentEvent e)	{}
public void componentShown(ComponentEvent e)	{}

public void componentResized(ComponentEvent e) {
    Dimension d = e.getComponent().getSize();			// get new size
    if (d.width == c.width && d.height == c.height) {
	return;		// discard program-generated resize
    }
    if (d.width == c.width + 1 && d.height == c.height) {
	return;		// discard bogus initial resize from some Java impls
    }
    c.resize(null, d.width, d.height);				// resize image
    enqueue(vInteger.New(Resize), d.width, d.height, null);	// enqueue event
}



public void keyPressed(KeyEvent e)		{}
public void keyTyped(KeyEvent e)		{}

public void keyReleased(KeyEvent e)		{
    char ch = e.getKeyChar();
    if (ch == '\n' && ! e.isControlDown()) {	// if enter key
	ch = '\r';				// change to \r for v9 compat
    }
    if (ch != KeyEvent.CHAR_UNDEFINED) {
	enqueue(vString.New(ch), c.xloc, c.yloc, e);
    } else if (e.isActionKey()) {
	enqueue(vInteger.New(e.getKeyCode()), c.xloc, c.yloc, e);
    }
}



public void mouseClicked(MouseEvent e)	{}

public void mouseEntered(MouseEvent e)	{ c.xloc = e.getX(); c.yloc = e.getY();}
public void mouseMoved(MouseEvent e)	{ c.xloc = e.getX(); c.yloc = e.getY();}
public void mouseExited(MouseEvent e)	{ c.xloc = c.yloc = 0; }

public void mousePressed(MouseEvent e) {
    c.xloc = e.getX();
    c.yloc = e.getY();
    enqueue(vInteger.New(LPress + mouseMod(e)), c.xloc, c.yloc, e);
}

public void mouseReleased(MouseEvent e) {
    c.xloc = e.getX();
    c.yloc = e.getY();
    enqueue(vInteger.New(LRelease + mouseMod(e)), c.xloc, c.yloc, e);
}

public void mouseDragged(MouseEvent e) {
    c.xloc = e.getX();
    c.yloc = e.getY();
    enqueue(vInteger.New(LDrag + mouseMod(e)), c.xloc, c.yloc, e);
}

public static int mouseMod(MouseEvent e) {   // adjust event code for modifiers
    if (e.isMetaDown()) {
	return RPress - LPress;
    } else if (e.isAltDown()) {
	return MPress - LPress;
    } else {
	return 0;
    }
}



} // class wEvent

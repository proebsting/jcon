//  wEvent.java -- event processing

package rts;

import java.awt.*;
import java.awt.event.*;



class wEvent implements WindowListener,  //#%#% ComponentListener,
    KeyListener, MouseListener, MouseMotionListener
{
    wCanvas c;		// associated canvas
    int xloc;		// mouse x-coordinate
    int yloc;		// mouse y-coordinate



//  the following definitions are consistent with Icon v9

static final int ShiftFlag   = 4 << 16;
static final int MetaFlag    = 2 << 16;
static final int ControlFlag = 1 << 16;
static final int LeftmostFlag = 4 << 16;

static final int LPress = -1;
static final int MPress = -2;
static final int RPress = -3;
static final int LRelease = -4;
static final int MRelease = -5;
static final int RRelease = -6;
static final int LDrag = -7;
static final int MDrag = -8;
static final int RDrag = -9;
static final int Resize = -10;



wEvent(wCanvas cv) {				// new wEvent(w)
    this.c = cv;
}



static void register(wCanvas cv) {		// register event handlers

    wEvent handler = new wEvent(cv);
    cv.f.addWindowListener(handler);
    cv.addKeyListener(handler);
    cv.addMouseListener(handler);
    cv.addMouseMotionListener(handler);
}



//  enqueue(a, e) -- enqueue Icon event code a derived from Java event e

void enqueue(vValue a, InputEvent e) {

    int msec = 0;	//#%#%#% msec values not known yet
    int expo = 0;
    while (msec > 0x1000) {
	msec >>= 4;
	expo += 0x1000;
    }

    int flags = 0;
    if (e.isControlDown()) {
	flags += ControlFlag;
    }
    if (e.isShiftDown()) {
	flags += ShiftFlag;
    }
    if (!(e instanceof MouseEvent) && (e.isMetaDown() || e.isAltDown())) {
	flags += MetaFlag;
    }

    c.enqueue(a,
	vInteger.New(flags | (xloc & 0xFFFF)),
	vInteger.New((expo << 28) | (msec << 16) | (yloc & 0xFFFF)));
}



//  dequeue(list) -- get next event from a window

static vValue dequeue(vList evq) {
    vValue a, xv, yv;

    a = evq.Get();
    while (a == null) {		//#%#% is polling really the way to do this?
	try {
	    Thread.sleep(iConfig.PollDelay);
	} catch (InterruptedException e) {
	}
	a = evq.Get();
    }

    //  get two more values, which must be integers
    xv = evq.Get();
    yv = evq.Get();
    if (xv == null || !(xv instanceof vInteger)
    ||  yv == null || !(yv instanceof vInteger)) {
	iRuntime.error(143);		// malformed queue
    }

    long x = ((vInteger)xv).value;
    long y = ((vInteger)yv).value;
    if (x < 0 || x >= (LeftmostFlag << 1) || y < 0) {
	iRuntime.error(143);		// malformed queue
    }

    if ((x & ControlFlag) != 0) {	// check CONTROL flag
	k$control.value = vNull.New();	// succeed (null) if set
    } else {
	k$control.value = null;		// failure value
    }

    if ((x & MetaFlag) != 0) {		// check META flag
	k$meta.value = vNull.New();	// succeed (null) if set
    } else {
	k$meta.value = null;		// failure value
    }

    if ((x & ShiftFlag) != 0) {		// check SHIFT flag
	k$shift.value = vNull.New();	// succeed (null) if set
    } else {
	k$shift.value = null;		// failure value
    }

    long msec = (y >> 16) & 0xFFF;	// set &interval
    long expo = y >> 28;
    k$interval.self.set(msec << (4 * expo));

    x = (int)(short)x;			// extract signed coordinate values
    y = (int)(short)y;
    //#%#% need to translate x/y to this window's coordinate system

    k$x.self.Assign(vInteger.New(x));	// also sets k$col
    k$y.self.Assign(vInteger.New(y));	// also sets k$row

    return a;				// return event code
}



public void windowOpened(WindowEvent e)		{}
public void windowClosed(WindowEvent e)		{}
public void windowActivated(WindowEvent e)	{}
public void windowDeactivated(WindowEvent e)	{}
public void windowIconified(WindowEvent e)	{}
public void windowDeiconified(WindowEvent e)	{}

public void windowClosing(WindowEvent e) {
    //#%#% window was closed. don't know how to handle; bail out.
    System.err.println("WINDOW CLOSING... EXITING");
    c.f.dispose();
    System.exit(0);
}



public void keyTyped(KeyEvent e) {}
public void keyPressed(KeyEvent e) {}

public void keyReleased(KeyEvent e)	{
    char c = e.getKeyChar();
    if (c != KeyEvent.CHAR_UNDEFINED) {
	enqueue(vString.New((char)c), e);
    } else if (e.isActionKey()) {
	enqueue(vInteger.New(e.getKeyCode()), e);
    }
}



public void mouseClicked(MouseEvent e)	{}

public void mouseEntered(MouseEvent e)	{ xloc = e.getX(); yloc = e.getY(); }
public void mouseMoved(MouseEvent e)	{ xloc = e.getX(); yloc = e.getY(); }
public void mouseExited(MouseEvent e)	{ xloc = yloc = 0; }

public void mousePressed(MouseEvent e) {
    xloc = e.getX();
    yloc = e.getY();
    enqueue(vInteger.New(LPress + mouseMod(e)), e);
}

public void mouseReleased(MouseEvent e) {
    xloc = e.getX();
    yloc = e.getY();
    enqueue(vInteger.New(LRelease + mouseMod(e)), e);
}

public void mouseDragged(MouseEvent e) {
    xloc = e.getX();
    yloc = e.getY();
    enqueue(vInteger.New(LDrag + mouseMod(e)), e);
}

static int mouseMod(MouseEvent e) {	// adjust event code based on modifiers
    if (e.isMetaDown()) {
	return RPress - LPress;
    } else if (e.isAltDown()) {
	return MPress - LPress;
    } else {
	return 0;
    }
}



} // class wEvent

//  wCanvas.java -- an Icon window canvas, including attributes

package rts;

import java.awt.*;
import java.util.*;



class wCanvas extends Canvas {

Frame f;		// enclosing Frame object
Image i;		// backing image used for refreshing visable image

vList evq;		// event queue
wTTY tty;		// file I/O stuff

Vector wlist;		// list of associated vWindows



//  new wCanvas(win, label, w, h) -- create new canvas

wCanvas(vWindow win, String label, int w, int h) {		

    wlist = new Vector();
    wlist.addElement(win);

    this.setSize(w, h);

    f = new Frame(label);
    f.add(this, "North");
    f.pack();
    f.show();		// must precede createImage (?)

    i = this.createImage(w, h);

    evq = iNew.List(0, null);			// create event queue
    tty = new wTTY();				// create TTY instance

    wEvent.register(this);			// register event handlers
}



//  config(win, x, y, w, h) -- reconfigure geometry
//
//  #%#%#%#% for now, x and y are ignored; only size counts
//
//  arguments are *String* values a la WAttrib()
//  any particular argument can be null
//
//  returns failure (false) or success (true).

boolean config(vWindow win, String x, String y, String w, String h) {
    Rectangle r = this.getBounds();

    try {
    	if (x != null) { r.x = Integer.parseInt(x); }
    	if (y != null) { r.y = Integer.parseInt(y); }
    	if (w != null) { r.width = Integer.parseInt(w); }
    	if (h != null) { r.height = Integer.parseInt(h); }
    } catch (Exception e) {
    	return false;
    }
    resize(win, r.width, r.height);
    return true; 
}



//  resize(win, w, h) -- resize canvas

void resize(vWindow win, int w, int h) {

    Dimension d = this.getSize();		// get current size
    if (d.width == w && d.height == h) {
    	return;					// nothing to do
    }
    this.setSize(w, h);				// alter size
    f.pack();					// alter size of enclosing frame

    int iw = i.getWidth(null);
    int ih = i.getHeight(null);

    if (iw >= w && ih >= h) {
    	return;					// image is large enough
    }

    if (iw < w) { iw = w; }
    if (ih < h) { ih = h; }

    Image inew = this.createImage(iw, ih);
    Graphics g = inew.getGraphics();

    // install new graphics context in each associated window
    for (int j = 0; j < wlist.size(); j++) {
        ((vWindow) wlist.elementAt(j)).newgcb(g);
    }

    // g has been cloned for use with each window
    // we can now use it without affecting anything else

    // clear the new image, then copy in the old portion
    g.setColor(win.getBg());
    g.fillRect(0, 0, iw, ih);
    g.drawImage(i, 0, 0, null);

    i = inew;
}



//  paint(g) -- refresh the canvas from the backing image

public void paint(Graphics g) {
    g.drawImage(i,0,0,null);
}



//  enqueue(a, b, c) -- enqueue three event values (synchronized)

synchronized void enqueue(vValue a, vValue b, vValue c) {
   evq.Put(a);
   evq.Put(b);
   evq.Put(c);
}



} // wCanvas

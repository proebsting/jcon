//  wCanvas.java -- an Icon window canvas, including attributes

package rts;

import java.awt.*;



class wCanvas extends Canvas {

Frame f;		// enclosing Frame object
Image i;		// backing image used for refreshing visable image

vList evq;		// event queue



//  new wCanvas(label, w, h) -- create new canvas

wCanvas(String label, int w, int h) {		

    this.setSize(w, h);

    f = new Frame(label);

    f.add(this, "North");
    f.pack();
    f.show();		// must precede createImage (?)

    i = this.createImage(w, h);

    evq = iNew.List(0, null);			// create event queue

    wEvent.register(this);			// register event handlers
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

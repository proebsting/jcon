//  wImage.java -- image operations

package rts;

import java.awt.*;



final class wImage {



//  wImage.load(window, filename) -- load image and wait for completion

static Image load(vWindow win, String fname) {
    Image im = Toolkit.getDefaultToolkit().getImage(fname);
    MediaTracker t = new MediaTracker(win.getCanvas());
    t.addImage(im, 0);
    try {
	t.waitForAll();
    } catch (InterruptedException e) {
	return null;
    }
    if (t.isErrorAny()) {
	return null;
    } else {
	return im;
    }
}



} // class wImage

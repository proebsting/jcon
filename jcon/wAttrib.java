//  wAttrib.java -- code dealing with window attributes

package rts;

import java.awt.*;
import java.util.*;



abstract class wAttrib implements Cloneable {

    String name;			// attribute name
    String val;				// value; null if none specified

abstract vValue set(vWindow win);	// set window according to s
abstract vValue get(vWindow win);	// get current value, set s, return val



//  initialize known attributes

private static Hashtable attlist = new Hashtable();

static {

    // The following canvas attributes are implemented:

    newatt("label", new aLabel());
    newatt("pos", new aPos());
    newatt("posx", new aPosX());
    newatt("posy", new aPosY());

    newatt("resize", new aResize());
    newatt("size", new aSize());
    newatt("height", new aHeight());
    newatt("width", new aWidth());
    newatt("rows", new aRows());
    newatt("columns", new aColumns());
    newatt("image", new aImage());

    newatt("canvas", new aCanvas());		// only "normal" and "hidden"

    newatt("echo", new aEcho());
    newatt("cursor", new aCursor());
    newatt("x", new aX());
    newatt("y", new aY());
    newatt("row", new aRow());
    newatt("col", new aCol());

    newatt("pointer", new aPointer());
    newatt("pointerx",	new aPointerX());		// read-only
    newatt("pointery",	new aPointerY());		// read-only
    newatt("pointerrow", new aPointerRow());		// read-only
    newatt("pointercol", new aPointerCol());		// read-only

    newatt("depth", new aDepth());
    newatt("displayheight", new aDisplayheight());
    newatt("displaywidth", new aDisplaywidth());

    // The following graphics context attributes are implemented:

    newatt("fg", new aFg());
    newatt("bg", new aBg());
    newatt("reverse", new aReverse());
    newatt("drawop", new aDrawop());
    newatt("gamma", new aGamma());

    newatt("font", new aFont());
    newatt("fheight", new aFheight());
    newatt("fwidth", new aFwidth());
    newatt("ascent", new aAscent());
    newatt("descent", new aDescent());
    newatt("leading", new aLeading());

    newatt("clipx", new aClipx());
    newatt("clipy", new aClipy());
    newatt("clipw", new aClipw());
    newatt("cliph", new aCliph());
    newatt("dx", new aDx());
    newatt("dy", new aDy());

    // The following canvas attributes are not implemented.
    // They are recognized as legal, but changes are ignored.

    newatt("display",	new aDummy(vString.New(":0.0")));
    newatt("iconpos",	new aDummy(vString.New("0,0")));
    newatt("iconlabel",	new aDummy(vString.New("")));
    newatt("iconimage",	new aDummy(vString.New("")));

    // The following graphics context attributes are not implemented.
    // They are recognized as legal, but changes are ignored.
    // They will be easier to support under Java 1.2.

    newatt("linewidth",	new aDummy(vInteger.New(1)));
    newatt("linestyle",	new aDummy(vString.New("solid")));
    newatt("fillstyle",	new aDummy(vString.New("solid")));
    newatt("pattern",	new aDummy(vString.New("black")));
}

private static void newatt(String name, wAttrib a) {
    a.name = name;
    attlist.put(name, a);
}



//  parseAtts(args, n) -- parse attribute arglist beginning at position n
//
//  Returns a list of wAttrib objects, each of proper Type and with
//  the "name" field set.  The "val" field is set, always to a String,
//  if "=value" is found in an argument.  (Note that val=null is
//  different from val="").
//
//  Errors are possible:  103 (string expected), 145 (bad name)

static wAttrib[] parseAtts(vDescriptor[] args, int n) {

    wAttrib list[] = new wAttrib[args.length - n];

    for (int i = n; i < args.length; i++) {

	String s = vString.argDescr(args, i).toString();
	String name, val;

	int j = s.indexOf('=');
	if (j < 0) {
	    name = s;
	    val = null;
	    // no value specified
	} else {
	    // value was specified
	    name = s.substring(0, j);
	    val = s.substring(j + 1);
	}

	wAttrib a = (wAttrib) attlist.get(name);
	if (a == null) {
	    iRuntime.error(145, args[i]);
	}
	try {
	    a = (wAttrib) a.clone();	// make new copy and alter that
	} catch (CloneNotSupportedException e)  {
	    iRuntime.bomb(e);
	}
	a.val = val;
	list[i - n] = a;
    }

    return list;
}



//  unsettable(name, value) -- report error 147 (attrib can't be read/written)

static vValue unsettable(String name, String value) {
    iRuntime.error(147, vString.New(name + "=" + value));
    return null;
}



//  parseInt(s) -- parse attribute as integer, allowing "1.5" etc.

static int parseInt(String s) throws NumberFormatException {
    try {
    	return Integer.parseInt(s);
    } catch (Exception e) {
    }
    try {
    	long n = vString.New(s).mkInteger().value;
	if (n >= Integer.MIN_VALUE && n <= Integer.MAX_VALUE) {
	    return (int) n;
        }
    } catch (iError e) {
    }
    throw new NumberFormatException(s);
}



} // class wAttrib



// dummy attribute class: for attributes not implemented

final class aDummy extends wAttrib {
    vValue attval;
    aDummy(vValue v)			{ attval = v; }		// constructor

    vValue set(vWindow win)		{ return attval; }	// ignore set
    vValue get(vWindow win)		{ return attval; }
}



final class aCanvas extends wAttrib {
    vValue get(vWindow win)	{ return win.getCanvas().Canvas(win, null); }
    vValue set(vWindow win)	{ return win.getCanvas().Canvas(win, val); }
}

final class aResize extends wAttrib {
    vValue get(vWindow win)	{ return win.getCanvas().Resize(win, null); }
    vValue set(vWindow win)	{ return win.getCanvas().Resize(win, val); }
}

final class aLabel extends wAttrib {
    vValue get(vWindow win)	{ return win.getCanvas().Label(win, null); }
    vValue set(vWindow win)	{ return win.getCanvas().Label(win, val); }
}

final class aPointer extends wAttrib {
    vValue get(vWindow win)	{ return win.getCanvas().Pointer(win, null); }
    vValue set(vWindow win)	{ return win.getCanvas().Pointer(win, val); }
}



final class aDepth extends wAttrib {
    vValue get(vWindow win)	{ return vInteger.New(
	Toolkit.getDefaultToolkit().getColorModel().getPixelSize()); }
    vValue set(vWindow win)
	{ return wAttrib.unsettable("depth", val); }
}

final class aDisplayheight extends wAttrib {
    vValue get(vWindow win)	{ return vInteger.New(
	Toolkit.getDefaultToolkit().getScreenSize().height); }
    vValue set(vWindow win)
	{ return wAttrib.unsettable("displayheight", val); }
}

final class aDisplaywidth extends wAttrib {
    vValue get(vWindow win)	{ return vInteger.New(
	Toolkit.getDefaultToolkit().getScreenSize().width); }
    vValue set(vWindow win)
	{ return wAttrib.unsettable("displaywidth", val); }
}



final class aDx extends wAttrib {
    vValue get(vWindow win)	{ return vInteger.New(win.dx); }
    vValue set(vWindow win)	{
	try {
	    int dx = wAttrib.parseInt(val);
	    win.Origin(dx, win.dy);
	    return vInteger.New(dx);
	} catch (Exception e) {
	    return null; /*FAIL*/
	}
    }
}

final class aDy extends wAttrib {
    vValue get(vWindow win)	{ return vInteger.New(win.dy); }
    vValue set(vWindow win)	{
	try {
	    int dy = wAttrib.parseInt(val);
	    win.Origin(win.dx, dy);
	    return vInteger.New(dy);
	} catch (Exception e) {
	    return null; /*FAIL*/
	}
    }
}



final class aClipx extends wAttrib {
    vValue get(vWindow win) {
	Rectangle r = win.getClip();
	return (r == null) ? vNull.New() : (vValue)vInteger.New(r.x);
    }
    vValue set(vWindow win) {
	vString s = vString.New(val);
	return (win.Clip(s, null, null, null) == null) ? null : s;
    }
}

final class aClipy extends wAttrib {
    vValue get(vWindow win) {
	Rectangle r = win.getClip();
	return (r == null) ? vNull.New() : (vValue)vInteger.New(r.y);
    }
    vValue set(vWindow win) {
	vString s = vString.New(val);
	return (win.Clip(null, s, null, null) == null) ? null : s;
    }
}

final class aClipw extends wAttrib {
    vValue get(vWindow win) {
	Rectangle r = win.getClip();
	return (r == null) ? vNull.New() : (vValue)vInteger.New(r.width);
    }
    vValue set(vWindow win) {
	vString s = vString.New(val);
	return (win.Clip(null, null, s, null) == null) ? null : s;
    }
}

final class aCliph extends wAttrib {
    vValue get(vWindow win) {
	Rectangle r = win.getClip();
	return (r == null) ? vNull.New() : (vValue)vInteger.New(r.height);
    }
    vValue set(vWindow win) {
	vString s = vString.New(val);
	return (win.Clip(null, null, null, s) == null) ? null : s;
    }
}



final class aGamma extends wAttrib {
    vValue get(vWindow win)	{ return win.Gamma(null); }
    vValue set(vWindow win)	{ return win.Gamma(vString.New(val)); }
}

final class aFg extends wAttrib {
    vValue get(vWindow win)	{ return win.Fg(null); }
    vValue set(vWindow win)	{ return win.Fg(vString.New(val)); }
}

final class aBg extends wAttrib {
    vValue get(vWindow win)	{ return win.Bg(null); }
    vValue set(vWindow win)	{ return win.Bg(vString.New(val)); }
}

final class aDrawop extends wAttrib {
    vValue get(vWindow win)	{ return win.Drawop(null); }
    vValue set(vWindow win)	{ return win.Drawop(vString.New(val)); }
}

final class aReverse extends wAttrib {
    vValue get(vWindow win)	{ return win.Reverse(null); }
    vValue set(vWindow win)	{ return win.Reverse(vString.New(val)); }
}




final class aFont extends wAttrib {
    vValue get(vWindow win)	 { return win.Font(null); }
    vValue set(vWindow win)	 { return win.Font(vString.New(val)); }
}

final class aLeading extends wAttrib {
    vValue get(vWindow win)	 { return vInteger.New(win.Leading()); }
    vValue set(vWindow win)	 {
	try {
	    return vInteger.New(win.Leading(wAttrib.parseInt(val)));
	} catch (Exception e) {
	    return null; /*FAIL*/
	}
    }
}

final class aAscent extends wAttrib {
    vValue get(vWindow win)
	{ return vInteger.New(win.getFontMetrics().getMaxAscent()); }
    vValue set(vWindow win)
	{ return wAttrib.unsettable("ascent", val); }
}

final class aDescent extends wAttrib {
    vValue get(vWindow win)
	{ return vInteger.New(win.getFontMetrics().getMaxDescent()); }
    vValue set(vWindow win)
	{ return wAttrib.unsettable("descent", val); }
}

final class aFheight extends wAttrib {
    vValue get(vWindow win) {
	FontMetrics m = win.getFontMetrics();
	return vInteger.New(m.getMaxAscent() + m.getMaxDescent());
    }
    vValue set(vWindow win)
	{ return wAttrib.unsettable("fheight", val); }
}

final class aFwidth extends wAttrib {
    vValue get(vWindow win) { return vInteger.New(win.Fwidth()); }
    vValue set(vWindow win) { return wAttrib.unsettable("fwidth", val); }
}



final class aEcho extends wAttrib {
    vValue get(vWindow win)	{ return win.getTTY().Echo(null); }
    vValue set(vWindow win)	{ return win.getTTY().Echo(val); }
}

final class aCursor extends wAttrib {
    vValue get(vWindow win)	{ return win.getTTY().Cursor(null); }
    vValue set(vWindow win)	{ return win.getTTY().Cursor(val); }
}

final class aX extends wAttrib {
    vValue get(vWindow win)	{ return win.getTTY().X(null); }
    vValue set(vWindow win)	{ return win.getTTY().X(val); }
}

final class aY extends wAttrib {
    vValue get(vWindow win)	{ return win.getTTY().Y(null); }
    vValue set(vWindow win)	{ return win.getTTY().Y(val); }
}

final class aRow extends wAttrib {
    vValue get(vWindow win)	{ return win.getTTY().Row(win, null); }
    vValue set(vWindow win)	{ return win.getTTY().Row(win, val); }
}

final class aCol extends wAttrib {
    vValue get(vWindow win)	{ return win.getTTY().Col(win, null); }
    vValue set(vWindow win)	{ return win.getTTY().Col(win, val); }
}



final class aPointerX extends wAttrib {
    vValue get(vWindow win)	{ return vInteger.New(win.getCanvas().xloc); }
    vValue set(vWindow win)	{ return get(win); }   // ignore attempt to set
}

final class aPointerY extends wAttrib {
    vValue get(vWindow win)	{ return vInteger.New(win.getCanvas().yloc); }
    vValue set(vWindow win)	{ return get(win); }   // ignore attempt to set
}

final class aPointerRow extends wAttrib {
    vValue get(vWindow win)	{ return win.getTTY().PointerRow(win); }
    vValue set(vWindow win)	{ return get(win); }   // ignore attempt to set
}

final class aPointerCol extends wAttrib {
    vValue get(vWindow win)	{ return win.getTTY().PointerCol(win); }
    vValue set(vWindow win)	{ return get(win); }   // ignore attempt to set
}



final class aWidth extends wAttrib {

    vValue get(vWindow win) {
	return vInteger.New(win.getCanvas().getSize().width);
    }

    vValue set(vWindow win) {
	if (win.getCanvas().config(win, 1, null, null, val, null)) {
	    return get(win);
	} else {
	    return null; /*FAIL*/
	}
    }

}



final class aHeight extends wAttrib {

    vValue get(vWindow win) {
	return vInteger.New(win.getCanvas().getSize().height);
    }

    vValue set(vWindow win) {
	if (win.getCanvas().config(win, 1, null, null, null, val)) {
	    return get(win);
	} else {
	    return null; /*FAIL*/
	}
    }

}



final class aSize extends wAttrib {

    vValue get(vWindow win) {
	Dimension d = win.getCanvas().getSize();
	return vString.New(d.width + "," + d.height);
    }

    vValue set(vWindow win) {
	int j = val.indexOf(',');
	if (j < 0) {
	    return null;
	}
	String w = val.substring(0, j);
	String h = val.substring(j + 1);
	if (win.getCanvas().config(win, 1, null, null, w, h)) {
	    return get(win);
	} else {
	    return null; /*FAIL*/
	}
    }

}



final class aRows extends wAttrib {

    vValue get(vWindow win) {
	int l = win.Leading();
	if (l == 0) {
	    iRuntime.error(204);  // this is what v9 does: real division by 0
	}
	return vInteger.New(win.getCanvas().getSize().height / l);
    }

    vValue set(vWindow win) {
	if (win.getCanvas().config(win, win.Leading(), null, null, null, val)) {
	    return get(win);
	} else {
	    return null; /*FAIL*/
	}
    }

}



final class aColumns extends wAttrib {

    vValue get(vWindow win) {
	return vInteger.New(win.getCanvas().getSize().width / win.Fwidth());
    }

    vValue set(vWindow win) {
	if (win.getCanvas().config(win, win.Fwidth(), null, null, val, null)) {
	    return get(win);
	} else {
	    return null; /*FAIL*/
	}
    }

}



final class aPos extends wAttrib {

    vValue get(vWindow win) {
	Point p = win.getCanvas().f.getLocation();
	return vString.New(p.x + "," + p.y);
    }

    vValue set(vWindow win) {
	int x, y;
	int j = val.indexOf(',');
	if (j < 0) {
	    return null;
	}
	try {
	    x = wAttrib.parseInt(val.substring(0, j));
	    y = wAttrib.parseInt(val.substring(j + 1));
	} catch (Exception e) {
	    return null; /*FAIL*/
	}
	win.getCanvas().f.setLocation(x, y);
	return vString.New(x + "," + y);
    }

}

final class aPosX extends wAttrib {

    vValue get(vWindow win) {
	return vInteger.New(win.getCanvas().f.getLocation().x);
    }

    vValue set(vWindow win) {
	Frame f = win.getCanvas().f;
	Point p = f.getLocation();
	try {
	    p.x = wAttrib.parseInt(val);
	} catch (Exception e) {
	    return null; /*FAIL*/
	}
	f.setLocation(p);
	return vInteger.New(p.x);
    }

}

final class aPosY extends wAttrib {

    vValue get(vWindow win) {
	return vInteger.New(win.getCanvas().f.getLocation().y);
    }

    vValue set(vWindow win) {
	Frame f = win.getCanvas().f;
	Point p = f.getLocation();
	try {
	    p.y = wAttrib.parseInt(val);
	} catch (Exception e) {
	    return null; /*FAIL*/
	}
	f.setLocation(p);
	return vInteger.New(p.y);
    }

}



final class aImage extends wAttrib {

    vValue get(vWindow win) {
	wCanvas c = win.getCanvas();
	return (c.imgfile == null) ? (vValue)vNull.New() : (vValue)c.imgfile;
    }

    vValue set(vWindow win) {
    	Image im = wImage.load(win, val);
	if (im == null) {
	    return null; /*FAIL*/
	}
	wCanvas c = win.getCanvas();
	if (!c.have_set_width && !c.have_set_height) {
	    c.resize(win, im.getWidth(null), im.getHeight(null));
	    c.have_set_width = c.have_set_height = true;
	}
	if (c.defer_image) {
	    c.deferred_image = im;
	} else {
	    win.CopyImage(im, -win.dx, -win.dy);
	    im.flush();
	}
	return c.imgfile = vString.New(val);
    }
}

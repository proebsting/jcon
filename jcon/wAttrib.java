//  wAttrib.java -- code dealing with window attributes

package rts;



class wAttrib {

    boolean rd;
    boolean wt;
    short code;	
    String s;


// canvas attributes have positive codes

static final int LABEL = 1;
static final int POS = 2;
static final int POSX = 3;
static final int POSY = 4;
static final int RESIZE = 5;
static final int SIZE = 6;
static final int HEIGHT = 7;
static final int WIDTH = 8;
static final int ROWS = 9;
static final int COLS = 10;
static final int IMAGE = 11;
static final int CANVAS = 12;
static final int ICONPOS = 13;
static final int ICONLABEL = 14;
static final int ICONIMAGE = 15;
static final int ECHO = 16;
static final int CURSOR = 17;
static final int X = 18;
static final int Y = 19;
static final int ROW = 20;
static final int COL = 21;
static final int POINTER = 22;
static final int POINTERX = 23;
static final int POINTERY = 24;
static final int POINTERROW = 25;
static final int POINTERCOL = 26;
static final int DISPLAY = 27;
static final int DEPTH = 28;
static final int DISPLAYHEIGHT = 29;
static final int DISPLAYWIDTH = 30;

// graphics context attributes have negative codes

static final int FG = -1;
static final int BG = -2;
static final int REVERSE = -3;
static final int DRAWOP = -4;
static final int GAMMA = -5;
static final int FONT = -6;
static final int FHEIGHT = -7;
static final int FWIDTH = -8;
static final int ASCENT = -9;
static final int DESCENT = -10;
static final int LEADING = -11;
static final int LINEWIDTH = -12;
static final int LINESTYLE = -13;
static final int FILLSTYLE = -14;
static final int PATTERN = -15;
static final int CLIPX = -16;
static final int CLIPY = -17;
static final int CLIPW = -18;
static final int CLIPH = -19;
static final int DX = -20;
static final int DY = -21;

} // class wAttrib

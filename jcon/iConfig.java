//  iConfig.java -- configuration constants.

package rts;

class iConfig {



// the following constants are peculiar to the Jcon implementation

static final String Version = "Jcon Version 0.6.1, February 6, 1998";

static final String MonoFont = "DialogInput";		// "mono" font
static final String TypewriterFont = "Monospaced";	// "typewriter" font
static final String SansFont = "SansSerif";		// "sans" font
static final String SerifFont = "Serif";		// "serif" font
static final String FixedFont = "mono,10";		// "fixed" font

static final int PollDelay = 10;		// msec sleep in event polling

static final String FuncPrefix = "rts.f$";	// cls prefix for built-in funcs

static final int MinCachedInt = -10000;		// range of ints to cache
static final int MaxCachedInt = +10000;

static final int MaxTraceback = 100;		// maximum traceback depth



//  the following constants are documented features of the Icon language
//  and should not be changed lightly

static final String FontName = "fixed";		// default font name
static final int FontSize = 14;			// default font size



} // iConfig

//  iConfig.java -- configuration constants.

package rts;

class iConfig {



// the following constants are peculiar to the Jcon implementation

static final String Version = "Jcon Version 0.6.1, December 15, 1997";

static final String MonoFont = "DialogInput";		// "mono" font
static final String TypewriterFont = "Monospaced";	// "typewriter" font
static final String SansFont = "SansSerif";		// "sans" font
static final String SerifFont = "Serif";		// "serif" font
static final String FixedFont = "mono,10";		// "fixed" font

static final int PollDelay = 10;		// msec sleep in event polling

static final String FuncPrefix = "rts.f$";	// cls prefix for built-in funcs

static final int MinPrebuiltInt = -1000;	// range of ints to preallocate
static final int MaxPrebuiltInt = +1000;



//  the following constants are documented features of the Icon language
//  and should not be changed likely

static final String FontName = "fixed";		// default font name
static final int FontSize = 14;			// default font size



} // iConfig

//  iConfig.java -- configuration constants.

package rts;

final class iConfig {



// the following constants are peculiar to the Jcon implementation

static final String Version = "Jcon Version 2.j, August 14, 1998";

static final String PackageName = "rts";	// class prefix
static final String KeywordPrefix = "k$";	// name prefix for keywords
static final String FunctionPrefix = "f$";	// name prefix for builtin funcs

static final int MinCachedInt = -10000;		// range of ints to cache
static final int MaxCachedInt = +10000;

static final int MinCachedIntStr = -10000;	// range to cache in string form
static final int MaxCachedIntStr = +10000;

static final int MaxTraceback = 100;		// maximum traceback depth

static final int PollDelay = 10;		// msec sleep in event polling

static final double Gamma = 1.5;		// default gamma attribute

static final String MonoFont = "DialogInput";		// "mono" font
static final String TypewriterFont = "Monospaced";	// "typewriter" font
static final String SansFont = "SansSerif";		// "sans" font
static final String SerifFont = "Serif";		// "serif" font
static final String FixedFont = "mono,bold,14";		// "fixed" font



//  the following constants are documented features of the Icon language
//  and should not be changed lightly

static final int MaxIntDigits = 30;		// maximum digits in image(i)

static final String FontName = "fixed";		// default font name
static final int FontSize = 14;			// default font size



} // iConfig

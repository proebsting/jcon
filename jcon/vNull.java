package rts;

public final class vNull extends vValue {



final static vNull TheNull = new vNull();    // not private: inhibits inlining

public static vNull New()	{ return TheNull; }	// vNull.New()

private vNull()			{ }



public boolean isnull()		{ return true; }
public vDescriptor IsNull()     { return this; }
public vDescriptor IsntNull()   { return null; /*FAIL*/ }


private static vString emptystring = vString.New();
public vString write()		{ return emptystring; }

private static vString imagestring = vString.New("&null");
public vString image()		{ return imagestring; }

public int hashCode()		{ return 0; }

private static vString typestring = vString.New("null");
public vString Type()		{ return typestring; }

int rank()			{ return 0; }		// nulls sort first
int compareTo(vValue v)		{ return 0; }		// all nulls are equal

public boolean equals(Object o)	{ return (o instanceof vNull); }



} // class vNull

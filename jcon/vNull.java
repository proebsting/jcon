package rts;

public final class vNull extends vValue {



private static vNull TheNull = new vNull();
public static vNull New()	{ return TheNull; }	// vNull.New()

private vNull()			{ }



boolean isnull()		{ return true; }
public vDescriptor IsNull()     { return this; }
public vDescriptor IsntNull()   { return null; /*FAIL*/ }


static vString emptystring = vString.New();
vString write()			{ return emptystring; }

static vString imagestring = vString.New("&null");
vString image()			{ return imagestring; }

public int hashCode()		{ return 0; }

static vString typestring = vString.New("null");
public vString Type()		{ return typestring; }

int rank()			{ return 0; }		// nulls sort first
int compareTo(vValue v)		{ return 0; }		// all nulls are equal

public boolean equals(Object o)	{ return (o instanceof vNull); }



} // class vNull

package rts;

public class vNull extends vValue {



vDescriptor isNull()		{ return this; }
vDescriptor isntNull()		{ return null; /*FAIL*/ }

vString write()			{ return iNew.String(); }

static vString imagestring = iNew.String("&null");
vString image()			{ return imagestring; }

public int hashCode()		{ return 0; }

static vString typestring = iNew.String("null");
vString type()			{ return typestring; }

int rank()			{ return 0; }	// nulls sort first
int compareTo(vValue v)		{ return 0; }	// all nulls are equal

public boolean equals(Object o)	{
    return (o instanceof vNull);
}



} // class vNull

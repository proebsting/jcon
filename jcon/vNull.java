package rts;

public class vNull extends vValue {

    vDescriptor isNull()	{ return this; }
    vDescriptor isntNull()	{ return null; /*FAIL*/ }

    int rank()			{ return 0; }	// nulls sort first
    int compareTo(vValue v)	{ return 0; }	// all nulls are equal

    String write()		{ return ""; }
    String image()		{ return "&null"; }
    String type()		{ return "null"; }

    public int hashCode()	{ return 0; }

    public boolean equals(Object o)	{
	// #%#%  should there be only one instance of Null?
	return (o instanceof vNull);
    }

}

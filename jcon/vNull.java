class vNull extends vValue {

    vDescriptor isNull()	{ return this; }
    vDescriptor isntNull()	{ return null; /*FAIL*/ }

    String write()		{ return ""; }
    String image()		{ return "&null"; }
    String type()		{ return "null"; }

public boolean equals(Object o)	{
	// #%#%  should there be only one instance of Null?
	return (o instanceof vNull);
}

}

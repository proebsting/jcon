class vNull extends vValue {

    vDescriptor isNull()	{ return this; }
    vDescriptor isntNull()	{ return null; /*FAIL*/ }

    String write()		{ return ""; }
    String image()		{ return "&null"; }

}

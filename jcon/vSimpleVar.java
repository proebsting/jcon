class vSimpleVar extends vVariable {

    vValue value;	// value of variable
    
    vSimpleVar(vValue x)	{ value = x; }		// constructor
    vSimpleVar()		{ value = iNew.Null(); }

    vValue deref()		{ return value; }

    vVariable Assign(vValue x)	{ value = x; return this; }


    vDescriptor isNull()	{
    	if (value instanceof vNull) {
	    return this;	// return variable 
	} else {
	    return null;	// fail
	}
    }

    vDescriptor isntNull()	{ 
    	if (value instanceof vNull) {
	    return null;	// fail
	} else {
	    return this;	// return variable 
	}
    }

    vDescriptor Select()	  { return value.deref().SelectVar(this); }
    vDescriptor Bang(iClosure c)  { return value.deref().BangVar(c, this); }
}

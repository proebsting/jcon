//  vStructure -- a subclass of vVariable used for list, set, table, record
//
//  subclasses must maintain a serial number counter
//  and call super(sn) in constructors.

package rts;



abstract public class vStructure extends vValue {

    int snum;		// serial number



abstract vValue Copy();			// must be implemented by subclass



vStructure(int snum) {			// constructor
    this.snum = snum;
}



vInteger Serial()	{ return iNew.Integer(this.snum); }



//  x.image() -- common method for lists, sets, and tables

static vString uscore = iNew.String('_');
static vString lpar = iNew.String('(');
static vString rpar = iNew.String(')');

vString image() {			// type_snum(size)
    return this.type().concat(uscore).concat(iNew.String(snum))
	.concat(lpar).concat(this.Size().mkString()).concat(rpar);
}



//  x.compareTo(y) -- common comparison method for lists, sets, tables
//
//  called only for two sets, two lists, or two tables;
//  for records, overridden by a different method

int compareTo(vValue v) {
    return (int) (this.snum - ((vStructure)v).snum);
}



} // class vStructure

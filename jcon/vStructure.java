//  vStructure -- a subclass of vVariable used for list, set, table, record
//
//  subclasses must maintain a serial number counter
//  and call super(sn) in constructors.

package rts;



abstract public class vStructure extends vValue {

    int snum;		// serial number

abstract vValue Copy();		// must be implemented by subclass



vStructure(int snum) {			// constructor
    this.snum = snum;
}



vInteger Serial()	{ return iNew.Integer(this.snum); }



//  x.image() -- common method for lists, sets, and tables

String image() {
    return this.type() + "_" + this.snum + "(" + this.Size().image() + ")";
}



//  x.compareTo(y) -- common comparison method for lists, sets, tables
//
//  (called only for two sets, two lists, etc.)

int compareTo(vValue v)
{
    return (int) (this.snum - ((vStructure)v).snum);
}



} // class vStructure

//  vStructure -- a subclass of vVariable used for list, set, table, record
//
//  subclasses must maintain a serial number counter
//  and call super(sn) in constructors.

package jcon;



public abstract class vStructure extends vValue {

    int snum;		// serial number



public abstract vValue Copy();		// must be implemented by subclass



vStructure(int snum) {			// constructor
    this.snum = snum;
}



public vInteger Serial()	{ return vInteger.New(this.snum); }



//  x.image() -- common method for lists, sets, and tables

private static vString uscore = vString.New('_');
private static vString lpar = vString.New('(');
private static vString rpar = vString.New(')');

public vString image() {		// type_snum(size)
    return this.Type().concat(uscore).concat(vInteger.New(snum).mkString())
	.concat(lpar).concat(this.Size().mkString()).concat(rpar);
}



//  x.compareTo(y) -- common comparison method for lists, sets, tables
//
//  called only for two sets, two lists, or two tables;
//  for records, overridden by a different method

int compareTo(vValue v) {
    return this.snum - ((vStructure)v).snum;
}



} // class vStructure

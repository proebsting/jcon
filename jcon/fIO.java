//  fIO.java -- functions dealing with I/O

package rts;

import java.io.*;



class fIO {

	// print(f, arglist, newline) -- helper for write(), writes(), stop()

	static vDescriptor print(vFile f, vDescriptor[] args, boolean nl) {
		vDescriptor d = iNew.Null();	// last argument processed
		for (int i = 0; i < args.length; i++) {
			d = args[i];
			if (d instanceof vFile) {	// if file value
				if (i > 0 && nl) {
					f.newline();	// write newline
				}
				f = (vFile) d;		// change files
			} else {
				f.writes(args[i].write());
			}
		}
		if (nl) {
			f.newline();
		}
		return d;
	}
}



class f$open extends iValueClosure {				// open(s1,s2)
	vDescriptor function(vDescriptor[] args) {
		String fname = vString.argVal(args, 0);
		String mode = vString.argVal(args, 1, "r");
		return iNew.File(fname, mode);
	}
}



class f$flush extends iValueClosure {			// flush(f)
	vDescriptor function(vDescriptor[] args) {
		return(vFile.argVal(args, 0).flush());
	}
}



class f$close extends iValueClosure {			// close(f)
	vDescriptor function(vDescriptor[] args) {
		return(vFile.argVal(args, 0).close());
	}
}



class f$read extends iValueClosure {				// read(f)
	vDescriptor function(vDescriptor[] args) {
		return(vFile.argVal(args, 0, k$input.file).read());
	}
}



class f$reads extends iValueClosure {			// reads(f,n)
	vDescriptor function(vDescriptor[] args) {
		vFile f = vFile.argVal(args, 0, k$input.file);
		long n = vInteger.argVal(args, 1, 1);
		return f.reads(n);
	}
}



class f$write extends iValueClosure {			// write(...)
	vDescriptor function(vDescriptor[] args) {
		return fIO.print(k$output.file, args, true);
	}
}



class f$writes extends iValueClosure {			// writes(...)
	vDescriptor function(vDescriptor[] args) {
		return fIO.print(k$output.file, args, false);
	}
}



class f$stop extends iValueClosure {				// stop(...)
	vDescriptor function(vDescriptor[] args) {
		k$output.file.flush();				// flush stdout
		fIO.print(k$errout.file, args, true);		// write msg
		iRuntime.exit(1, parent);			// exit
		return null;	// not reached
	}
}



class f$seek extends iValueClosure {				// seek(f,i)
	vDescriptor function(vDescriptor[] args) {
		vFile f = vFile.argVal(args, 0);
		long n = vInteger.argVal(args, 1);
		return f.seek(n);
	}
}



class f$where extends iValueClosure {			// where(f)
	vDescriptor function(vDescriptor[] args) {
		return(vFile.argVal(args, 0).where());
	}
}

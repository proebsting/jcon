//  fIO.java -- functions dealing with I/O

package rts;

import java.io.*;



class f$read extends iFunctionClosure {				// read()
	static DataInputStream stdin = new DataInputStream(System.in);
	vDescriptor function(vDescriptor[] args) {
		String s = null;
		try {
			s = stdin.readLine();
		} catch (IOException e) {
			iRuntime.error(214);
		}
		if (s == null) {
			return null; /*FAIL*/
		}
		return iNew.String(s);
	}
}



class fOutput {		// common helper for write(), writes(), stop()

	// #%#%#% overly simplified: does not handle file switching

	static vDescriptor print(PrintStream p, vDescriptor[] args) {
		for (int i = 0; i < args.length; i++) {
			p.print(args[i].write());
		}
		if (args.length == 0) {
		    return iNew.Null();
		} else {
		    return args[args.length - 1];
		}
		
	}

}



class f$write extends iFunctionClosure {			// write(...)
	vDescriptor function(vDescriptor[] args) {
		vDescriptor result = fOutput.print(System.out, args);
		System.out.println();
		return result;
	}
}



class f$writes extends iFunctionClosure {			// writes(...)
	vDescriptor function(vDescriptor[] args) {
		return fOutput.print(System.out, args);
	}
}



class f$stop extends iFunctionClosure {				// stop(...)
	vDescriptor function(vDescriptor[] args) {
		System.out.flush();				// flush stdout
		fOutput.print(System.err, args);		// write msg
		System.err.println();
		System.exit(1);					// exit
		return null;	// not reached
	}
}

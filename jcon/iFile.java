package jcon;

public abstract class iFile {


public void link() {}			// follow embedded links
public void unresolved() {}		// announce unresolved references
public void announce_unresolved() {}	// diagnose unresolved references
public void declare() {}		// announce global declarations
public void resolve() {}		// resolve references


} // class iFile

package rts;

public abstract class iFile {
	void link() {}			// follow embedded links 
	void unresolved() {}		// announce unresolved references
	void declare() {}		// announce global declarations
	void resolve() {}		// resolve references
}

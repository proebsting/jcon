package rts;

public abstract class iFile {
    public void link() {}		// follow embedded links 
    public void unresolved() {}	// announce unresolved references
    public void declare() {}	// announce global declarations
    public void resolve() {}	// resolve references
}

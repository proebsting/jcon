package rts;

class Main {

	static final String separator = "--";

	public static void main(String[] args) {
		int loc = -1;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(separator)) {
				loc = i;
				break;
			}
		}
		if (loc < 0) {
			System.err.println();
			System.err.println("Run-time in startup code");
			System.err.println(
				"malformed invocation, missing " + separator);
			iRuntime.exit(1, null);
		} else if (loc + 1 >= args.length) {
			System.err.println();
			System.err.println("Run-time in startup code");
			System.err.println(
				"malformed invocation, missing progname");
			iRuntime.exit(1, null);
		} else {
			String[] files = new String[loc];
			String progname = args[loc+1];
			String[] jargs = new String[args.length-loc-2];
			for (int i = 0; i < files.length; i++) {
				files[i] = args[i];
			}
			for (int i = loc+2; i < args.length; i++) {
				jargs[i-(loc+2)] = args[i];
			}
			iInterface.start(files, jargs, progname);
		}
	}
}
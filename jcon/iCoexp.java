class iCoexp {
	static vCoexp create(iClosure closure) {
		vCoexp coexp = new vCoexp(closure);
		coexp.create();
		return coexp;
	}
}

class iError extends Error {
    int num;
    vDescriptor desc;

iError(int num, vDescriptor desc) {
    this.num = num;
    this.desc = desc;
}

void error() {
    //#%#% if &error is zero, issue message and abort
    //#%#% if &error is not zero, decrement it and set other error keywords
    //#%#% throw exception for handling as failure at proper point

    System.out.flush();
    System.err.println("Run-time error " + num);
    System.err.println(iRunerr.text(num));
    if (desc != null) {
        System.err.println("offending value: " + desc.report());
    }
    System.exit(1);
}

}

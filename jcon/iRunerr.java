//  iRunerr.java -- Icon runtime errors


class iRunerr {



//  text(n) -- return text for error number n

static String text(int n) {		
    if (n > 0 && n < tlist.length && tlist[n] != null) {
	return tlist[n];
    } else {
	return "unknown error code";
    }
}


private static String tlist[];

static {
    tlist = new String[1000];

    tlist[101] = "integer expected or out of range";
    tlist[102] = "numeric expected";
    tlist[103] = "string expected";
    tlist[104] = "cset expected";
    tlist[105] = "file expected";
    tlist[106] = "procedure or integer expected";
    tlist[107] = "record expected";
    tlist[108] = "list expected";
    tlist[109] = "string or file expected";
    tlist[110] = "string or list expected";
    tlist[111] = "variable expected";
    tlist[112] = "invalid type to size operation";
    tlist[113] = "invalid type to random operation";
    tlist[114] = "invalid type to subscript operation";
    tlist[115] = "structure expected";
    tlist[116] = "invalid type to element generator";
    tlist[117] = "missing main procedure";
    tlist[118] = "co-expression expected";
    tlist[119] = "set expected";
    tlist[120] = "two csets or two sets expected";
    tlist[121] = "function not supported";
    tlist[122] = "set or table expected";
    tlist[123] = "invalid type";
    tlist[124] = "table expected";
    tlist[125] = "list, record, or set expected";
    tlist[126] = "list or record expected";

    tlist[140] = "window expected";
    tlist[141] = "program terminated by window manager";
    tlist[142] = "attempt to read/write on closed window";
    tlist[143] = "malformed event queue";
    tlist[144] = "window system error";
    tlist[145] = "bad window attribute";
    tlist[146] = "incorrect number of arguments to drawing function";

    tlist[201] = "division by zero";
    tlist[202] = "remaindering by zero";
    tlist[203] = "integer overflow";
    tlist[204] = "real overflow, underflow, or division by zero";
    tlist[205] = "invalid value";
    tlist[206] = "negative first argument to real exponentiation";
    tlist[207] = "invalid field name";
    tlist[208] = "second and third arguments to map of unequal length";
    tlist[209] = "invalid second argument to open";
    tlist[210] = "non-ascending arguments to detab/entab";
    tlist[211] = "by value equal to zero";
    tlist[212] = "attempt to read file not open for reading";
    tlist[213] = "attempt to write file not open for writing";
    tlist[214] = "input/output error";
    tlist[215] = "attempt to refresh &main";
    tlist[216] = "external function not found";

    tlist[301] = "evaluation stack overflow";
    tlist[302] = "memory violation";
    tlist[303] = "inadequate space for evaluation stack";
    tlist[304] = "inadequate space in qualifier list";
    tlist[305] = "inadequate space for static allocation";
    tlist[306] = "inadequate space in string region";
    tlist[307] = "inadequate space in block region";
    tlist[308] = "system stack overflow in co-expression";

    tlist[401] = "co-expressions not implemented";
    tlist[402] = "program not compiled with debugging option";

    tlist[500] = "program malfunction";

    tlist[600] = "vidget usage error";

    tlist[901] = "implementation incomplete";
    tlist[902] = "implementation malfunction";
    tlist[903] = "unexpected catastrophic event";
};



} // class iRunerr

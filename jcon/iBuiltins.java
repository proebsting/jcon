//  iBuiltins.java -- built-in functions

package rts;



public final class iBuiltins extends iFile {


static iBuiltins self = new iBuiltins();

static void announce() {
    iEnv.declareBuiltin("abs", 1, fNumeric.self);
    iEnv.declareBuiltin("acos", 1, fNumeric.self);
    iEnv.declareBuiltin("any", 4, fScan.self);
    iEnv.declareBuiltin("args", 1, fMisc.self);
    iEnv.declareBuiltin("asin", 1, fNumeric.self);
    iEnv.declareBuiltin("atan", 2, fNumeric.self);
    iEnv.declareBuiltin("bal", 6, fScan.self);
    iEnv.declareBuiltin("center", 3, fString.self);
    iEnv.declareBuiltin("char", 1, fString.self);
    iEnv.declareBuiltin("close", 1, fIO.self);
    iEnv.declareBuiltin("collect", 2, fMisc.self);
    iEnv.declareBuiltin("copy", 1, fStruct.self);
    iEnv.declareBuiltin("cos", 1, fNumeric.self);
    iEnv.declareBuiltin("cset", 1, fConvert.self);
    iEnv.declareBuiltin("delay", 1, fMisc.self);
    iEnv.declareBuiltin("delete", 2, fStruct.self);
    iEnv.declareBuiltin("detab", -2, fTab.self);
    iEnv.declareBuiltin("display", 2, fMisc.self);
    iEnv.declareBuiltin("dtor", 1, fNumeric.self);
    iEnv.declareBuiltin("entab", -2, fTab.self);
    iEnv.declareBuiltin("errorclear", 0, fMisc.self);
    iEnv.declareBuiltin("exit", 1, fMisc.self);
    iEnv.declareBuiltin("exp", 1, fNumeric.self);
    iEnv.declareBuiltin("find", 4, fScan.self);
    iEnv.declareBuiltin("flush", 1, fIO.self);
    iEnv.declareBuiltin("function", 0, fMisc.self);
    iEnv.declareBuiltin("get", 1, fList.self);
    iEnv.declareBuiltin("getenv", 1, fMisc.self);
    iEnv.declareBuiltin("iand", 2, fNumeric.self);
    iEnv.declareBuiltin("icom", 1, fNumeric.self);
    iEnv.declareBuiltin("image", 1, fConvert.self);
    iEnv.declareBuiltin("insert", 3, fStruct.self);
    iEnv.declareBuiltin("integer", 1, fConvert.self);
    iEnv.declareBuiltin("ior", 2, fNumeric.self);
    iEnv.declareBuiltin("ishift", 2, fNumeric.self);
    iEnv.declareBuiltin("ixor", 2, fNumeric.self);
    iEnv.declareBuiltin("key", 1, fStruct.self);
    iEnv.declareBuiltin("left", 3, fString.self);
    iEnv.declareBuiltin("list", 2, fList.self);
    iEnv.declareBuiltin("loadfunc", 2, fLoad.self);
    iEnv.declareBuiltin("log", 2, fNumeric.self);
    iEnv.declareBuiltin("many", 4, fScan.self);
    iEnv.declareBuiltin("map", 3, fString.self);
    iEnv.declareBuiltin("match", 4, fScan.self);
    iEnv.declareBuiltin("member", 2, fStruct.self);
    iEnv.declareBuiltin("move", 1, fScan.self);
    iEnv.declareBuiltin("name", 1, fMisc.self);
    iEnv.declareBuiltin("numeric", 1, fConvert.self);
    iEnv.declareBuiltin("open", -3, fIO.self);
    iEnv.declareBuiltin("ord", 1, fString.self);
    iEnv.declareBuiltin("pop", 1, fList.self);
    iEnv.declareBuiltin("pos", 1, fScan.self);
    iEnv.declareBuiltin("proc", 2, fMisc.self);
    iEnv.declareBuiltin("pull", 1, fList.self);
    iEnv.declareBuiltin("push", -2, fList.self);
    iEnv.declareBuiltin("put", -2, fList.self);
    iEnv.declareBuiltin("read", 1, fIO.self);
    iEnv.declareBuiltin("reads", 2, fIO.self);
    iEnv.declareBuiltin("real", 1, fConvert.self);
    iEnv.declareBuiltin("remove", 1, fIO.self);
    iEnv.declareBuiltin("rename", 2, fIO.self);
    iEnv.declareBuiltin("repl", 2, fString.self);
    iEnv.declareBuiltin("reverse", 1, fString.self);
    iEnv.declareBuiltin("right", 3, fString.self);
    iEnv.declareBuiltin("rtod", 1, fNumeric.self);
    iEnv.declareBuiltin("runerr", 2, fMisc.self);
    iEnv.declareBuiltin("seek", 2, fIO.self);
    iEnv.declareBuiltin("seq", 2, fNumeric.self);
    iEnv.declareBuiltin("serial", 1, fStruct.self);
    iEnv.declareBuiltin("set", 1, fStruct.self);
    iEnv.declareBuiltin("sin", 1, fNumeric.self);
    iEnv.declareBuiltin("sort", 2, fList.self);
    iEnv.declareBuiltin("sortf", 2, fList.self);
    iEnv.declareBuiltin("sqrt", 1, fNumeric.self);
    iEnv.declareBuiltin("stop", -1, fIO.self);
    iEnv.declareBuiltin("string", 1, fConvert.self);
    iEnv.declareBuiltin("system", 1, fMisc.self);
    iEnv.declareBuiltin("tab", 1, fScan.self);
    iEnv.declareBuiltin("table", 1, fStruct.self);
    iEnv.declareBuiltin("tan", 1, fNumeric.self);
    iEnv.declareBuiltin("trim", 2, fString.self);
    iEnv.declareBuiltin("type", 1, fConvert.self);
    iEnv.declareBuiltin("upto", 4, fScan.self);
    iEnv.declareBuiltin("variable", 1, fMisc.self);
    iEnv.declareBuiltin("where", 1, fIO.self);
    iEnv.declareBuiltin("write", -1, fIO.self);
    iEnv.declareBuiltin("writes", -1, fIO.self);
}



} // class iBuiltins

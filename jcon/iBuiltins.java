//  iBuiltins.java -- built-in functions

package rts;



public final class iBuiltins extends iFile {



static void announce() {
    iEnv.declareBuiltin("abs", 1);
    iEnv.declareBuiltin("acos", 1);
    iEnv.declareBuiltin("any", 4);
    iEnv.declareBuiltin("args", 1);
    iEnv.declareBuiltin("asin", 1);
    iEnv.declareBuiltin("atan", 2);
    iEnv.declareBuiltin("bal", 6);
    iEnv.declareBuiltin("center", 3);
    iEnv.declareBuiltin("char", 1);
    iEnv.declareBuiltin("close", 1);
    iEnv.declareBuiltin("collect", 2);
    iEnv.declareBuiltin("copy", 1);
    iEnv.declareBuiltin("cos", 1);
    iEnv.declareBuiltin("cset", 1);
    iEnv.declareBuiltin("delay", 1);
    iEnv.declareBuiltin("delete", 2);
    iEnv.declareBuiltin("detab", -2);
    iEnv.declareBuiltin("display", 2);
    iEnv.declareBuiltin("dtor", 1);
    iEnv.declareBuiltin("entab", -2);
    iEnv.declareBuiltin("errorclear", 0);
    iEnv.declareBuiltin("exit", 1);
    iEnv.declareBuiltin("exp", 1);
    iEnv.declareBuiltin("find", 4);
    iEnv.declareBuiltin("flush", 1);
    iEnv.declareBuiltin("function", 0);
    iEnv.declareBuiltin("get", 1);
    iEnv.declareBuiltin("getenv", 1);
    iEnv.declareBuiltin("iand", 2);
    iEnv.declareBuiltin("icom", 1);
    iEnv.declareBuiltin("image", 1);
    iEnv.declareBuiltin("insert", 3);
    iEnv.declareBuiltin("integer", 1);
    iEnv.declareBuiltin("ior", 2);
    iEnv.declareBuiltin("ishift", 2);
    iEnv.declareBuiltin("ixor", 2);
    iEnv.declareBuiltin("key", 1);
    iEnv.declareBuiltin("left", 3);
    iEnv.declareBuiltin("list", 2);
    iEnv.declareBuiltin("log", 2);
    iEnv.declareBuiltin("many", 4);
    iEnv.declareBuiltin("map", 3);
    iEnv.declareBuiltin("match", 4);
    iEnv.declareBuiltin("member", 2);
    iEnv.declareBuiltin("move", 1);
    iEnv.declareBuiltin("name", 1);
    iEnv.declareBuiltin("numeric", 1);
    iEnv.declareBuiltin("open", -3);
    iEnv.declareBuiltin("ord", 1);
    iEnv.declareBuiltin("pop", 1);
    iEnv.declareBuiltin("pos", 1);
    iEnv.declareBuiltin("proc", 2);
    iEnv.declareBuiltin("pull", 1);
    iEnv.declareBuiltin("push", -2);
    iEnv.declareBuiltin("put", -2);
    iEnv.declareBuiltin("read", 1);
    iEnv.declareBuiltin("reads", 2);
    iEnv.declareBuiltin("real", 1);
    iEnv.declareBuiltin("remove", 1);
    iEnv.declareBuiltin("rename", 2);
    iEnv.declareBuiltin("repl", 2);
    iEnv.declareBuiltin("reverse", 1);
    iEnv.declareBuiltin("right", 3);
    iEnv.declareBuiltin("rtod", 1);
    iEnv.declareBuiltin("runerr", 2);
    iEnv.declareBuiltin("seek", 2);
    iEnv.declareBuiltin("seq", 2);
    iEnv.declareBuiltin("serial", 1);
    iEnv.declareBuiltin("set", 1);
    iEnv.declareBuiltin("sin", 1);
    iEnv.declareBuiltin("sort", 2);
    iEnv.declareBuiltin("sortf", 2);
    iEnv.declareBuiltin("sqrt", 1);
    iEnv.declareBuiltin("stop", -1);
    iEnv.declareBuiltin("string", 1);
    iEnv.declareBuiltin("system", 1);
    iEnv.declareBuiltin("tab", 1);
    iEnv.declareBuiltin("table", 1);
    iEnv.declareBuiltin("tan", 1);
    iEnv.declareBuiltin("trim", 2);
    iEnv.declareBuiltin("type", 1);
    iEnv.declareBuiltin("upto", 4);
    iEnv.declareBuiltin("variable", 1);
    iEnv.declareBuiltin("where", 1);
    iEnv.declareBuiltin("write", -1);
    iEnv.declareBuiltin("writes", -1);
}



} // class iBuiltins

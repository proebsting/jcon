//  iBuiltins.java -- built-in functions

package rts;



public class iBuiltins extends iFile {



void announce() {
    declare("abs", 1);
    declare("acos", 1);
    declare("any", 4);
    declare("args", 1);
    declare("asin", 1);
    declare("atan", 2);
    declare("bal", 6);
    declare("center", 3);
    declare("char", 1);
    declare("close", 1);
    declare("collect", 2);
    declare("copy", 1);
    declare("cos", 1);
    declare("cset", 1);
    declare("delay", 1);
    declare("delete", 2);
    declare("detab", -2);
    declare("display", 2);
    declare("dtor", 1);
    declare("entab", -2);
    declare("errorclear", 0);
    declare("exit", 1);
    declare("exp", 1);
    declare("find", 4);
    declare("flush", 1);
    declare("function", 0);
    declare("get", 1);
    declare("getenv", 1);
    declare("iand", 2);
    declare("icom", 1);
    declare("image", 1);
    declare("insert", 3);
    declare("integer", 1);
    declare("ior", 2);
    declare("ishift", 2);
    declare("ixor", 2);
    declare("key", 1);
    declare("left", 3);
    declare("list", 2);
    declare("log", 2);
    declare("many", 4);
    declare("map", 3);
    declare("match", 4);
    declare("member", 2);
    declare("move", 1);
    declare("name", 1);
    declare("numeric", 1);
    declare("open", -3);
    declare("ord", 1);
    declare("pop", 1);
    declare("pos", 1);
    declare("proc", 2);
    declare("pull", 1);
    declare("push", -2);
    declare("put", -2);
    declare("read", 1);
    declare("reads", 2);
    declare("real", 1);
    declare("remove", 1);
    declare("rename", 2);
    declare("repl", 2);
    declare("reverse", 1);
    declare("right", 3);
    declare("rtod", 1);
    declare("runerr", 2);
    declare("seek", 2);
    declare("seq", 2);
    declare("serial", 1);
    declare("set", 1);
    declare("sin", 1);
    declare("sort", 2);
    declare("sortf", 2);
    declare("sqrt", 1);
    declare("stop", -1);
    declare("string", 1);
    declare("system", 1);
    declare("tab", 1);
    declare("table", 1);
    declare("tan", 1);
    declare("trim", 2);
    declare("type", 1);
    declare("upto", 4);
    declare("variable", 1);
    declare("where", 1);
    declare("write", -1);
    declare("writes", -1);
}


static void declare(String name, int args)
{
    iEnv.declareBuiltin(name, iNew.Proc(
	"function " + name, iConfig.FuncPrefix + name, args));
}



} // class iBuiltins

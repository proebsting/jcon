//  iBuiltins.java -- built-in functions

package rts;



public class iBuiltins extends iFile {



void announce() {
    iEnv.declareBuiltin("abs", new f$abs(), 1);
    iEnv.declareBuiltin("acos", new f$acos(), 1);
    iEnv.declareBuiltin("any", new f$any(), 4);
    iEnv.declareBuiltin("args", new f$args(), 1);
    iEnv.declareBuiltin("asin", new f$asin(), 1);
    iEnv.declareBuiltin("atan", new f$atan(), 2);
    iEnv.declareBuiltin("bal", new f$bal(), 6);
    iEnv.declareBuiltin("center", new f$center(), 3);
    iEnv.declareBuiltin("char", new f$char(), 1);
    iEnv.declareBuiltin("close", new f$close(), 1);
    iEnv.declareBuiltin("collect", new f$collect(), 2);
    iEnv.declareBuiltin("copy", new f$copy(), 1);
    iEnv.declareBuiltin("cos", new f$cos(), 1);
    iEnv.declareBuiltin("cset", new f$cset(), 1);
    iEnv.declareBuiltin("delay", new f$delay(), 1);
    iEnv.declareBuiltin("delete", new f$delete(), 2);
    iEnv.declareBuiltin("detab", new f$detab(), -2);
    iEnv.declareBuiltin("display", new f$display(), 2);
    iEnv.declareBuiltin("dtor", new f$dtor(), 1);
    iEnv.declareBuiltin("entab", new f$entab(), -2);
    iEnv.declareBuiltin("errorclear", new f$errorclear(), 0);
    iEnv.declareBuiltin("exit", new f$exit(), 1);
    iEnv.declareBuiltin("exp", new f$exp(), 1);
    iEnv.declareBuiltin("find", new f$find(), 4);
    iEnv.declareBuiltin("flush", new f$flush(), 1);
    iEnv.declareBuiltin("function", new f$function(), 0);
    iEnv.declareBuiltin("get", new f$get(), 1);
    iEnv.declareBuiltin("getenv", new f$getenv(), 1);
    iEnv.declareBuiltin("iand", new f$iand(), 2);
    iEnv.declareBuiltin("icom", new f$icom(), 1);
    iEnv.declareBuiltin("image", new f$image(), 1);
    iEnv.declareBuiltin("insert", new f$insert(), 3);
    iEnv.declareBuiltin("integer", new f$integer(), 1);
    iEnv.declareBuiltin("ior", new f$ior(), 2);
    iEnv.declareBuiltin("ishift", new f$ishift(), 2);
    iEnv.declareBuiltin("ixor", new f$ixor(), 2);
    iEnv.declareBuiltin("key", new f$key(), 1);
    iEnv.declareBuiltin("left", new f$left(), 3);
    iEnv.declareBuiltin("list", new f$list(), 2);
    iEnv.declareBuiltin("log", new f$log(), 2);
    iEnv.declareBuiltin("many", new f$many(), 4);
    iEnv.declareBuiltin("map", new f$map(), 3);
    iEnv.declareBuiltin("match", new f$match(), 4);
    iEnv.declareBuiltin("member", new f$member(), 2);
    iEnv.declareBuiltin("move", new f$move(), 1);
    iEnv.declareBuiltin("name", new f$name(), 1);
    iEnv.declareBuiltin("numeric", new f$numeric(), 1);
    iEnv.declareBuiltin("open", new f$open(), -3);
    iEnv.declareBuiltin("ord", new f$ord(), 1);
    iEnv.declareBuiltin("pop", new f$pop(), 1);
    iEnv.declareBuiltin("pos", new f$pos(), 1);
    iEnv.declareBuiltin("proc", new f$proc(), 2);
    iEnv.declareBuiltin("pull", new f$pull(), 1);
    iEnv.declareBuiltin("push", new f$push(), -2);
    iEnv.declareBuiltin("put", new f$put(), -2);
    iEnv.declareBuiltin("read", new f$read(), 1);
    iEnv.declareBuiltin("reads", new f$reads(), 2);
    iEnv.declareBuiltin("real", new f$real(), 1);
    iEnv.declareBuiltin("remove", new f$remove(), 1);
    iEnv.declareBuiltin("rename", new f$rename(), 2);
    iEnv.declareBuiltin("repl", new f$repl(), 2);
    iEnv.declareBuiltin("reverse", new f$reverse(), 1);
    iEnv.declareBuiltin("right", new f$right(), 3);
    iEnv.declareBuiltin("rtod", new f$rtod(), 1);
    iEnv.declareBuiltin("runerr", new f$runerr(), 2);
    iEnv.declareBuiltin("seek", new f$seek(), 2);
    iEnv.declareBuiltin("seq", new f$seq(), 2);
    iEnv.declareBuiltin("serial", new f$serial(), 1);
    iEnv.declareBuiltin("set", new f$set(), 1);
    iEnv.declareBuiltin("sin", new f$sin(), 1);
    iEnv.declareBuiltin("sort", new f$sort(), 2);
    iEnv.declareBuiltin("sortf", new f$sortf(), 2);
    iEnv.declareBuiltin("sqrt", new f$sqrt(), 1);
    iEnv.declareBuiltin("stop", new f$stop(), -1);
    iEnv.declareBuiltin("string", new f$string(), 1);
    iEnv.declareBuiltin("system", new f$system(), 1);
    iEnv.declareBuiltin("tab", new f$tab(), 1);
    iEnv.declareBuiltin("table", new f$table(), 1);
    iEnv.declareBuiltin("tan", new f$tan(), 1);
    iEnv.declareBuiltin("trim", new f$trim(), 2);
    iEnv.declareBuiltin("type", new f$type(), 1);
    iEnv.declareBuiltin("upto", new f$upto(), 4);
    iEnv.declareBuiltin("variable", new f$variable(), 1);
    iEnv.declareBuiltin("where", new f$where(), 1);
    iEnv.declareBuiltin("write", new f$write(), -1);
    iEnv.declareBuiltin("writes", new f$writes(), -1);
}



} // class iBuiltins

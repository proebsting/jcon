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
    //			"chdir"				// not possible in Java
    iEnv.declareBuiltin("close", 1, fIO.self);
    iEnv.declareBuiltin("collect", 2, fMisc.self);
    iEnv.declareBuiltin("copy", 1, fStruct.self);
    iEnv.declareBuiltin("cos", 1, fNumeric.self);
    iEnv.declareBuiltin("cset", 1, fConvert.self);
    iEnv.declareBuiltin("delay", 1, fMisc.self);
    iEnv.declareBuiltin("delete", 2, fStruct.self);
    iEnv.declareBuiltin("detab", -2, fTab.self);
    iEnv.declareBuiltin("display", 2, fMisc.self);	// globals only
    iEnv.declareBuiltin("dtor", 1, fNumeric.self);
    iEnv.declareBuiltin("entab", -2, fTab.self);
    iEnv.declareBuiltin("errorclear", 0, fMisc.self);
    iEnv.declareBuiltin("exit", 1, fMisc.self);
    iEnv.declareBuiltin("exp", 1, fNumeric.self);
    iEnv.declareBuiltin("find", 4, fScan.self);
    iEnv.declareBuiltin("flush", 1, fIO.self);
    iEnv.declareBuiltin("function", 0, fMisc.self);
    iEnv.declareBuiltin("get", 1, fList.self);
    //			"getch"				// not implemented
    //			"getche"			// not implemented
    iEnv.declareBuiltin("getenv", 1, fMisc.self);	// uses "env" in $PATH
    iEnv.declareBuiltin("iand", 2, fNumeric.self);
    iEnv.declareBuiltin("icom", 1, fNumeric.self);
    iEnv.declareBuiltin("image", 1, fConvert.self);
    iEnv.declareBuiltin("insert", 3, fStruct.self);
    iEnv.declareBuiltin("integer", 1, fConvert.self);
    iEnv.declareBuiltin("ior", 2, fNumeric.self);
    iEnv.declareBuiltin("ishift", 2, fNumeric.self);
    iEnv.declareBuiltin("ixor", 2, fNumeric.self);
    //			"kbhit"				// not implemented
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
    iEnv.declareBuiltin("seek", 2, fIO.self);		// not for &input etc.
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
    iEnv.declareBuiltin("variable", 1, fMisc.self);	// globals only
    iEnv.declareBuiltin("where", 1, fIO.self);		// not for &input etc.
    iEnv.declareBuiltin("write", -1, fIO.self);
    iEnv.declareBuiltin("writes", -1, fIO.self);

    iEnv.declareBuiltin("Active", 0, fGraphics.self);
    iEnv.declareBuiltin("Alert", 1, fGraphics.self);
    iEnv.declareBuiltin("Bg", 2, fGraphics.self);
    iEnv.declareBuiltin("Clip", 5, fGraphics.self);
    iEnv.declareBuiltin("Clone", -1, fGraphics.self);
    iEnv.declareBuiltin("Color", -1, fGraphics.self);		// always fails
    iEnv.declareBuiltin("ColorValue", 2, fGraphics.self);
    iEnv.declareBuiltin("CopyArea", 8, fGraphics.self);
    //			"Couple"				// not possible?
    iEnv.declareBuiltin("DrawArc", -1, fDrawing.self);
    iEnv.declareBuiltin("DrawCircle", -1, fDrawing.self);
    iEnv.declareBuiltin("DrawCurve", -1, fDrawing.self);
    iEnv.declareBuiltin("DrawImage", 4, fDrawing.self);
    iEnv.declareBuiltin("DrawLine", -1, fDrawing.self);
    iEnv.declareBuiltin("DrawPoint", -1, fDrawing.self);
    iEnv.declareBuiltin("DrawPolygon", -1, fDrawing.self);
    iEnv.declareBuiltin("DrawRectangle", -1, fDrawing.self);
    iEnv.declareBuiltin("DrawSegment", -1, fDrawing.self);
    iEnv.declareBuiltin("DrawString", -1, fDrawing.self);
    iEnv.declareBuiltin("EraseArea", -1, fDrawing.self);
    iEnv.declareBuiltin("Event", 1, fGraphics.self);
    iEnv.declareBuiltin("Fg", 2, fGraphics.self);
    iEnv.declareBuiltin("FillArc", -1, fDrawing.self);
    iEnv.declareBuiltin("FillCircle", -1, fDrawing.self);
    iEnv.declareBuiltin("FillPolygon", -1, fDrawing.self);
    iEnv.declareBuiltin("FillRectangle", -1, fDrawing.self);
    iEnv.declareBuiltin("Font", 2, fGraphics.self);
    iEnv.declareBuiltin("FreeColor", -1, fGraphics.self);
    iEnv.declareBuiltin("GotoRC", 3, fGraphics.self);
    iEnv.declareBuiltin("GotoXY", 3, fGraphics.self);
    iEnv.declareBuiltin("Lower", 1, fGraphics.self);
    iEnv.declareBuiltin("NewColor", 2, fGraphics.self);		// always fails
    iEnv.declareBuiltin("PaletteChars", 2, fGraphics.self);
    iEnv.declareBuiltin("PaletteColor", 3, fGraphics.self);
    iEnv.declareBuiltin("PaletteKey", 3, fGraphics.self);
    iEnv.declareBuiltin("Pattern", 2, fGraphics.self);		// always fails
    iEnv.declareBuiltin("Pending", 1, fGraphics.self);
    iEnv.declareBuiltin("Pixel", 5, fGraphics.self);
    iEnv.declareBuiltin("Raise", 1, fGraphics.self);
    iEnv.declareBuiltin("ReadImage", 4, fGraphics.self);	// no quantizatn
    iEnv.declareBuiltin("TextWidth", 2, fGraphics.self);
    iEnv.declareBuiltin("Uncouple", 1, fGraphics.self);
    iEnv.declareBuiltin("WAttrib", -1, fGraphics.self);
    iEnv.declareBuiltin("WDefault", 3, fGraphics.self);		// always fails
    iEnv.declareBuiltin("WFlush", 1, fGraphics.self);
    iEnv.declareBuiltin("WriteImage", 7, fGraphics.self);
    iEnv.declareBuiltin("WSync", 1, fGraphics.self);
}



} // class iBuiltins

//  wBuiltins.java -- built-in graphics functions

package rts;

import java.awt.*;



public final class wBuiltins extends iFile {


static void announce() {

    // graphics functions that are at least partially implemented:

    iEnv.declareBuiltin("Alert", 1, wFunctions.self);
    iEnv.declareBuiltin("Bg", 2, wFunctions.self);
    iEnv.declareBuiltin("Clip", 5, wFunctions.self);
    iEnv.declareBuiltin("Clone", -1, wFunctions.self);
    iEnv.declareBuiltin("Color", -1, wFunctions.self);		// always fails
    iEnv.declareBuiltin("ColorValue", 2, wFunctions.self);
    iEnv.declareBuiltin("CopyArea", 8, wFunctions.self);
    iEnv.declareBuiltin("DrawArc", -1, wFunctions.self);
    iEnv.declareBuiltin("DrawCircle", -1, wFunctions.self);
    iEnv.declareBuiltin("DrawCurve", -1, wFunctions.self);	// just DrawLine
    iEnv.declareBuiltin("DrawImage", 4, wFunctions.self);	// no bilevels
    iEnv.declareBuiltin("DrawLine", -1, wFunctions.self);
    iEnv.declareBuiltin("DrawPoint", -1, wFunctions.self);
    iEnv.declareBuiltin("DrawPolygon", -1, wFunctions.self);
    iEnv.declareBuiltin("DrawRectangle", -1, wFunctions.self);
    iEnv.declareBuiltin("DrawSegment", -1, wFunctions.self);
    iEnv.declareBuiltin("DrawString", -1, wFunctions.self);
    iEnv.declareBuiltin("EraseArea", -1, wFunctions.self);
    iEnv.declareBuiltin("Event", 1, wFunctions.self);
    iEnv.declareBuiltin("Fg", 2, wFunctions.self);
    iEnv.declareBuiltin("FillArc", -1, wFunctions.self);
    iEnv.declareBuiltin("FillCircle", -1, wFunctions.self);
    iEnv.declareBuiltin("FillRectangle", -1, wFunctions.self);
    iEnv.declareBuiltin("FillPolygon", -1, wFunctions.self);
    iEnv.declareBuiltin("Font", 2, wFunctions.self);
    iEnv.declareBuiltin("FreeColor", -1, wFunctions.self);
    iEnv.declareBuiltin("GotoRC", 3, wFunctions.self);
    iEnv.declareBuiltin("GotoXY", 3, wFunctions.self);
    iEnv.declareBuiltin("Lower", 1, wFunctions.self);
    iEnv.declareBuiltin("NewColor", 2, wFunctions.self);	// always fails
    iEnv.declareBuiltin("PaletteChars", 2, wFunctions.self);
    iEnv.declareBuiltin("PaletteColor", 3, wFunctions.self);
    iEnv.declareBuiltin("PaletteKey", 3, wFunctions.self);
    iEnv.declareBuiltin("Pattern", 2, wFunctions.self);		// always fails
    iEnv.declareBuiltin("Pending", 1, wFunctions.self);
    iEnv.declareBuiltin("Pixel", 5, wFunctions.self);
    iEnv.declareBuiltin("Raise", 1, wFunctions.self);
    iEnv.declareBuiltin("ReadImage", 4, wFunctions.self);	// no quantizatn
    iEnv.declareBuiltin("TextWidth", 2, wFunctions.self);
    iEnv.declareBuiltin("Uncouple", 1, wFunctions.self);
    iEnv.declareBuiltin("WAttrib", -1, wFunctions.self);
    iEnv.declareBuiltin("WDefault", 3, wFunctions.self);	// always fails
    iEnv.declareBuiltin("WFlush", 1, wFunctions.self);
    iEnv.declareBuiltin("WSync", 1, wFunctions.self);

    //#%#% Not implemented:
    // iEnv.declareBuiltin("WriteImage", 6);
    // iEnv.declareBuiltin("Active", 0);
    // iEnv.declareBuiltin("Couple", 2);	// may not be possible
}


} // class wBuiltins

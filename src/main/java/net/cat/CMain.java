package net.cat;

import net.ccbluex.liquidbounce.clickgui.Module.fonts.api.FontManager;
import net.ccbluex.liquidbounce.clickgui.Module.fonts.impl.SimpleFontManager;

public class CMain {
    public static String Name = "Cat of Duty";
    public static String Ver = "B2.7";
    private static CMain CAT;

    public static CMain getInstance() {
        try {
            if (CAT == null) CAT = new CMain();
            return CAT;
        } catch (Throwable t) {
            throw t;
        }
    }
    public static FontManager fontManager = SimpleFontManager.create();
    public static FontManager getFontManager() {
        return fontManager;
    }
}

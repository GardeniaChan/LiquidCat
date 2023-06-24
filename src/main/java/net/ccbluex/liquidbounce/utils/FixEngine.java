package net.ccbluex.liquidbounce.utils;


public class FixEngine {

    public static float fixRightClick() {

        if (ServerUtils.getRemoteIp() == "Singleplayer") {
            return 16.0F;
        } else {
            return 1.0F;
        }
    }
}

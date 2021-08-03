package fr.rader.bob.utils;

import fr.rader.bob.configs.BobConfig;
import fr.rader.bob.Main;
import org.lwjgl.LWJGLUtil;

public class OS {

    public static String getBobFolder() {
        BobConfig settings = Main.getInstance().getBobConfig();

        if(settings == null) {
            return System.getProperty("user.home").replaceAll("\\\\", "/") + "/.bob/";
        }

        return settings.getProperty("workingDirectory");
    }

    public static String getMinecraftFolder() {
        switch(getOS()) {
            case LWJGLUtil.PLATFORM_WINDOWS_NAME:
                return System.getenv("appdata") + "/.minecraft/";
            case LWJGLUtil.PLATFORM_LINUX_NAME:
                return System.getProperty("user.home") + "/.minecraft/";
            case LWJGLUtil.PLATFORM_MACOSX_NAME:
                return System.getProperty("user.home") + "/Library/Application Support/minecraft/";
            default:
                return null;
        }
    }

    public static String getOS() {
        return LWJGLUtil.getPlatformName();
    }
}

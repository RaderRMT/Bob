package fr.rader.bob;

public class OS {

    private static final int PLATFORM;

    private static final int LINUX = 1;
    private static final int MACOSX = 2;
    private static final int WINDOWS = 3;

    public static String getBobFolder() {
        return System.getProperty("user.home") + "/.bob/";
    }

    public static String getOS() {
        switch (PLATFORM) {
            case LINUX:
                return "linux";
            case MACOSX:
                return "macos";
            case WINDOWS:
                return "windows";
            default:
                return null;
        }
    }

    static {
        String osName = System.getProperty("os.name");

        if (osName.startsWith("Windows")) {
            PLATFORM = WINDOWS;
        } else if (!osName.startsWith("Linux") &&
                !osName.startsWith("FreeBSD") &&
                !osName.startsWith("SunOS") &&
                !osName.startsWith("Unix")) {
            if (!osName.startsWith("Mac OS X") &&
                    !osName.startsWith("Darwin")) {
                throw new IllegalStateException("Unknown platform: " + osName);
            }

            PLATFORM = MACOSX;
        } else {
            PLATFORM = LINUX;
        }
    }
}

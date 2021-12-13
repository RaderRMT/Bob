package fr.rader.utils;

public class StringUtils {

    public static int leadingSpaces(String string) {
        int length = 0;

        if (string != null) {
            while (string.charAt(length) == ' ') {
                length++;
            }
        }

        return length;
    }

    public static int length(String string) {
        int length = 0;

        if (string != null) {
            for (char c : string.toCharArray()) {
                if (c < 0x80 || c > 0xbf) {
                    length++;
                }
            }
        }

        return length;
    }

    public static boolean hasUpperCaseLetters(String string) {
        for (char c : string.toCharArray()) {
            if (CharacterUtils.isAlphaUpper(c)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSnakeCase(String string) {
        for (char c : string.toCharArray()) {
            if (c != '_' && !CharacterUtils.isAlphaLower(c)) {
                return false;
            }
        }

        return true;
    }
}

package fr.rader.utils;

public class CharacterUtils {

    /**
     * Look if the character is a letter, so if it's hex value is greater than 'a' and smaller than
     * 'z'. This method only checks for lowercase letters
     *
     * @param c the character to check
     * @return true if it's a lowercase character<br>
     * false otherwise
     */
    public static boolean isAlphaLower(char c) {
        return (c >= 'a' && c <= 'z');
    }

    /**
     * Look if the character is a letter, so if it's hex value is greater than 'A' and smaller than
     * 'Z'. This method only checks for uppercase letters
     *
     * @param c the character to check
     * @return true if it's an uppercase character<br>
     * false otherwise
     */
    public static boolean isAlphaUpper(char c) {
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Look if the character is a letter, so if it's hex value is greater than 'a' and smaller than
     * 'z' or if it's greater than 'A' and smaller than 'Z'
     *
     * @param c the character to check
     * @return true if it's a character<br>
     * false otherwise
     */
    public static boolean isAlpha(char c) {
        return isAlphaLower(c) || isAlphaUpper(c);
    }

    /**
     * Look if the character is a digit, so if it's hex value is greater than '0' and smaller than
     * '9'
     *
     * @param c the character to check
     * @return true if it's a number<br>
     * false otherwise
     */
    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}

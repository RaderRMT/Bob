package fr.rader.utils.errors;

public enum Error {

    /** Unexpected character */
    A01(
            // error message
            "Unexpected character: %",
            // solution(s)
            "Try removing the character"
    ),

    /** Unknown keyword */
    A02(
            // error message
            "Unknown keyword: %",
            // solution(s)
            "Use a valid keyword",
            "Use only lowercase letters for keywords"
    ),

    /** Invalid brace level at the end of file */
    A03(
            // error message
            "Invalid brace level at the end of file",
            // solution(s)
            ""
    ),

    /** Curly bracket resulting in negative brace level */
    A04(
            // error message
            "Curly bracket resulting in negative brace level",
            // solution(s)
            "Remove the curly bracket"
    ),

    /** Missing space after identifier */
    A05(
            // error message
            "Expected space after identifier",
            // solution(s)
            "Add a space at the end of the identifier"
    ),

    /** Names must be in snake_case */
    A06(
            // error message
            "Names must be in snake_case",
            // solution(s)
            "Change all uppercase letters to lowercase",
            "Use underscores for spaces",
            "Make sure the name does not contain numbers"
    ),

    /** Unterminated name */
    A07(
            // error message
            "Unterminated name",
            // solution(s)
            "Add a double quote at the end of the name"
    ),

    /** Unexpected bang */
    A08(
            // error message
            "Unexpected bang",
            // solution(s)
            "Try with \"!=\""
    ),

    /** Incomplete equal form */
    A09(
            // error message
            "Incomplete equal form",
            // solution(s)
            "Try with \"==\" or \"=>\""
    ),

    /** Unexpected slash */
    A10(
            // error message
            "Unexpected slash",
            // solution(s)
            "Try with \"//\" or \"/*\""
    ),

    /** Number too big */
    A11(
            // error message
            "Number too big",
            // solution(s)
            "Make sure that the number isn't above 2,147,483,647"
    ),

    /** Unexpected token */
    A12(
            // error message
            "Unexpected token, got a % instead of a %",
            // solution(s)
            "Try using a % instead"
    ),

    /** Unexpected line break */
    A13(
            // error message
            "Unexpected line break before the %",
            // solution(s)
            "Remove the line break before the %"
    ),

    /** Unknown variable */
    A14(
            // error message
            "Unknown variable: %",
            // solution(s)
            "Try using a variable from the current scope"
    ),

    /** Invalid variable type */
    A15(
            // error message
            "Invalid variable type, got % for variable \"%\"",
            // solution(s)
            "Try using a byte, short, int or varint instead"
    ),

    /** Variable already defined */
    A16(
            // error message
            "Variable already defined: %",
            // solution(s)
            "Try using a different name for the variable"
    ),

    /** Unexpected token after end of packet definition */
    A17(
            // error message
            "Unexpected token after end of packet definition",
            // solution(s)
            "Remove everything after the packet definition"
    ),

    /** Unexpected token v2 */
    A18(
            // error message
            "Unexpected token: %",
            // solution(s)
            "Remove the \"%\" and try again"
    ),;

    private final String errorMessage;
    private final String[] solutions;

    Error(String errorMessage, String... solution) {
        this.errorMessage = errorMessage;
        this.solutions = solution;
    }

    public String getErrorID() {
        return this.name();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String[] getSolutions() {
        return solutions;
    }

    public String formatErrorMessage(String... strings) {
        String toReturn = errorMessage;

        for (String string : strings) {
            toReturn = toReturn.replaceFirst("%", string);
        }

        return toReturn;
    }

    public String[] formatSolutions(String... strings) {
        String[] formattedSolutions = solutions;
        int index = 0;

        for (int i = 0; i < formattedSolutions.length; i++) {
            String solution = formattedSolutions[i];

            while (solution.contains("%")) {
                solution = solution.replaceFirst("%", strings[index++]);
            }

            formattedSolutions[i] = solution;
        }

        return formattedSolutions;
    }
}

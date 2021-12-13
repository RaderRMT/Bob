package fr.rader.utils.errors;

import fr.rader.utils.LineReader;
import fr.rader.utils.StringUtils;
import fr.rader.utils.logger.Logger;

import java.io.File;
import java.io.IOException;

public class ErrorPrinter {

    public static void printError(File file, int lineIndex, int start, int size, String errorID, String message, String solution) throws IOException {
        // print the error
        print(file, lineIndex, start, size, errorID, message);

        // print the solution if there is one
        if (solution != null) {
            printSolution(solution);
        }
    }

    private static void print(File file, int lineIndex, int start, int size, String errorID, String message) throws IOException {
        // get the line and leading spaces
        String line = LineReader.getLine(file, lineIndex);
        int leadingSpaces = StringUtils.leadingSpaces(line);

        // remove the leading spaces from the start index
        start -= leadingSpaces;

        // trim the line because we don't want
        // to show the leading spaces
        line = line.trim();

        // build the underline string
        String errorUnderline = "    ";
        for (int i = 0; i < start + size; i++) {
            errorUnderline += (i < start) ? " " : "-";
        }

        // build the error message
        String errorMessage = "    ";
        for (int i = 0; i < start; i++) {
            errorMessage += " ";
        }

        // append the message to the error
        errorMessage += "> " + message;

        // print the error
        printLines(
                "Error " + errorID + ":",
                "",
                lineIndex + ":  " + line,
                errorUnderline,
                errorMessage
        );
    }

    public static void printSimpleError(String errorID, String message, String solution) {
        printLines(
                "Error " + errorID + ":",
                "",
                "> " + message
        );

        if (solution != null) {
            printSolution(solution);
        }
    }

    public static void printSolution(String solution) {
        // print the solution
        printLines(
                "",
                "Suggestion:",
                "    > " + solution
        );
    }

    private static void printLines(String... lines) {
        for (String line : lines) {
            Logger.error(line);
        }
    }
}

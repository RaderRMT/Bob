package fr.rader.utils.errors;

import fr.rader.utils.LineReader;
import fr.rader.utils.StringUtils;
import fr.rader.utils.logger.Logger;

import java.io.File;

public class ErrorPrinter {

    /**
     * Print a nice error message to the user
     *
     * @param error the error (not formatted)
     */
    public static void newPrintError(Error error) {
        newPrintError(
                null,
                0,
                error
        );
    }

    /**
     * Print a nice error message to the user
     *
     * @param file source file
     * @param line line in the source that has the error
     * @param errorID the error id (please refer to {@link Error})
     * @param errorMessage the error message
     * @param solutions the possible solution(s) to fix the issue
     */
    public static void newPrintError(File file, int line, String errorID, String errorMessage, String[] solutions) {
        newPrintError(
                file,
                line,
                0,
                0,
                errorID,
                errorMessage,
                solutions
        );
    }

    /**
     * Print a nice error message to the user
     *
     * @param file source file
     * @param line line in the source that has the error
     * @param error the error (not formatted)
     */
    public static void newPrintError(File file, int line, Error error) {
        newPrintError(
                file,
                line,
                0,
                0,
                error
        );
    }

    /**
     * Print a nice error message to the user
     *
     * @param file source file
     * @param line line in the source that has the error
     * @param start start index for the underline
     * @param size size of the underline
     * @param error the error (not formatted)
     */
    public static void newPrintError(File file, int line, int start, int size, Error error) {
        newPrintError(
                file,
                line,
                start,
                size,
                error.getErrorID(),
                error.getErrorMessage(),
                error.getSolutions()
        );
    }

    /**
     * Print a nice error message to the user
     *
     * @param file source file
     * @param line line in the source that has the error
     * @param start start index for the underline
     * @param size size of the underline
     * @param errorID the error id (please refer to {@link Error})
     * @param errorMessage the error message
     * @param solutions the possible solution(s) to fix the issue
     */
    public static void newPrintError(File file, int line, int start, int size, String errorID, String errorMessage, String[] solutions) {
        Logger.error("Error " + errorID + ":");
        Logger.error("");

        // if we give a file to this method,
        // we'll use it to print the line
        // that has the error
        if (file != null) {
            // we get the line
            String lineWithError = LineReader.getLine(file, line);
            // we return if we can't get the line
            if (lineWithError == null) {
                return;
            }

            Logger.error(line + ":  " + lineWithError.trim());

            // we check if we have to underline it
            if (start != 0 && size != 0) {
                // we remove the leading spaces from the start index
                start -= StringUtils.leadingSpaces(lineWithError);

                // we build the underline
                StringBuilder underline = new StringBuilder("    ");
                for (int i = 0; i < start + size; i++) {
                    // as long as i is less than start + size,
                    // we add spaces to the underline to put it
                    // right under what we want to underline
                    underline.append((i < start) ? " " : "-");
                }

                // we print the underline
                Logger.error(underline.toString());
            }
        }

        // we build the error message
        StringBuilder error = new StringBuilder("    ");
        for (int i = 0; i < start; i++) {
            error.append(" ");
        }

        // and we print it
        Logger.error(error + "> " + errorMessage);

        // if the solutions array isn't empty,
        // we print the solutions
        if (!solutions[0].isEmpty()) {
            Logger.error("");
            Logger.error("Suggestion" + (solutions.length > 1 ? "s" : "") + ":");

            // print each solutions
            for (String solution : solutions) {
                Logger.error("    > " + solution);
            }
        }
    }
}

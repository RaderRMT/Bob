package fr.rader.utils.logger;

import fr.rader.utils.OS;
import fr.rader.utils.DateUtils;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Logger {

    /** DateTime patterns */
    private static final String DATETIME_LOG_PATTERN = "[yyyy-MM-dd] [HH:mm:ss]";

    private static final String DATETIME_FILE_PATTERN = "yyyy-MM-dd_HH-mm-ss";

    /** Log Labels */
    private static final String INFO_LABEL = "[INFO]";

    private static final String YAY_LABEL = "[YAY]";
    private static final String WARN_LABEL = "[WARN]";
    private static final String ERROR_LABEL = "[ERROR]";
    private static final String KRAKEN_LABEL = "[KRAKEN]";

    /** Path to the main logs folder */
    public static final String LOG_PATH = OS.getBobFolder() + "logs/";

    /** Path to the current log */
    public static final String CURRENT_LOG = LOG_PATH + DateUtils.getFormattedDate(DATETIME_FILE_PATTERN);

    public static void info(String message) {
        info(LoggerChannel.DEFAULT, message);
    }

    public static void info(LoggerChannel channel, String message) {
        write(channel, INFO_LABEL, message);
    }

    public static void yay(String message) {
        yay(LoggerChannel.DEFAULT, message);
    }

    public static void yay(LoggerChannel channel, String message) {
        write(channel, YAY_LABEL, message);
    }

    public static void warn(String message) {
        warn(LoggerChannel.DEFAULT, message);
    }

    public static void warn(LoggerChannel channel, String message) {
        write(channel, WARN_LABEL, message);
    }

    public static void error(String message) {
        error(LoggerChannel.DEFAULT, message);
    }

    public static void error(LoggerChannel channel, String message) {
        write(channel, ERROR_LABEL, message);
    }

    /**
     * The Kraken: The Destroyer of Replays.
     *
     * <p>Jokes aside, the Kraken shouldn't really be an issue. This method is still going to be
     * called in some places, like when rewriting packets, or anything related to Replays in
     * general. It's only going to write a sentence with the {@link Logger#KRAKEN_LABEL} label in
     * the log so that my moderators know if the issue has a fix or not. A Kraken might not appear
     * in the logs, but it's effects could be seen and a fix could be guessed
     */
    public static void kraken() {
        write(LoggerChannel.DEFAULT, KRAKEN_LABEL, "Kraken happened, please be nice to it :(");
    }

    public static void printStackTrace(Exception exception) {
        printStackTrace(LoggerChannel.DEFAULT, exception);
    }

    public static void printStackTrace(LoggerChannel channel, Exception exception) {
        if (channel == null || channel.isClosed()) {
            return;
        }

        PrintWriter channelPrintWriter = new PrintWriter(channel.getWriter());

        exception.printStackTrace(channelPrintWriter);

        channelPrintWriter.flush();
    }

    private static void write(LoggerChannel channel, String label, String message) {
        if (channel == null || channel.isClosed()) {
            return;
        }

        try {
            FileWriter writer = channel.getWriter();

            writer.append(DateUtils.getFormattedDate(DATETIME_LOG_PATTERN));
            writer.append(" ");
            writer.append(label);
            writer.append(" ");
            writer.append(message);
            writer.append('\n');

            // todo: remove this once finished
            System.out.println(DateUtils.getFormattedDate(DATETIME_LOG_PATTERN) + " " + label + " " + message);

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

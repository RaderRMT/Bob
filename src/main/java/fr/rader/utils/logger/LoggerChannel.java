package fr.rader.utils.logger;

import fr.rader.utils.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public enum LoggerChannel {

    /**
     * This channel is used to store every commands used by the user. It's used for debug purposes
     * if you experience issues with Bob. No sensitive data will be stored in the log.
     */
    COMMAND("command.log"),

    /**
     * This channel is used to store basically as much information as possible, so basically
     * anything you do and anything Bob does will be logged to this log file. It's also used for
     * debug purposes if you experience issues with Bob. No sensitive data will be stored in the
     * log.
     */
    DEFAULT("the_log.log");

    /** This writer is used to write the log to the {@link LoggerChannel#log} file */
    private FileWriter writer;

    /** This is used to keep track of the log name */
    private final String log;

    /** This is used to keep track of whether the writer is closed or not */
    private boolean isClosed = false;

    LoggerChannel(String log) {
        this.log = log;

        try {
            // create the log file
            File logFile = FileUtils.makeFile(getLogPath(), true);

            // we throw an IllegalStateException if we
            // cannot create the log file.
            if (logFile == null) {
                throw new IllegalStateException("Log cannot be created");
            }

            // create the writer for this log
            this.writer = new FileWriter(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Flush and close the writer's stream
     *
     * @throws IOException If an I/O error occurs
     */
    public void close() throws IOException {
        writer.flush();
        writer.close();
        isClosed = true;
    }

    /**
     * Small method to know if the writer has been closed because we cannot write to a log if the
     * stream is closed
     *
     * @return true if the writer's stream is closed, false otherwise
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Getter for the FileWriter so that Bob can write to the log file.
     *
     * @return The channel's FileWriter
     */
    public FileWriter getWriter() {
        return writer;
    }

    /**
     * Currently not used. It's there just in case I need the log name for some file stuff
     *
     * @return The log file name
     */
    public String getLogFileName() {
        return log;
    }

    /**
     * Getter for the log path.
     *
     * <p>For this comment, {@code LOG_NAME} will be the date and time at which<br>
     * Bob has been started, matching the {@link Logger#DATETIME_FILE_PATTERN} pattern
     *
     * <p>Windows: {@code %userprofile%/.bob/logs/LOG_NAME/}<br>
     * macOS & Linux: {@code ~/.bob/logs/LOG_NAME/}
     *
     * @return Path to the log
     */
    public String getLogPath() {
        return Logger.CURRENT_LOG + "/" + log;
    }
}

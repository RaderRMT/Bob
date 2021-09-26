package fr.rader.bob.logger;

import fr.rader.bob.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public enum LoggerChannel {

    COMMAND("command.log"),
    DEFAULT("the_log.log");

    private FileWriter writer;

    private final String logName;

    private boolean isClosed = true;

    LoggerChannel(String logName) {
        this.logName = logName;

        try {
            // create the log file
            File log = FileUtils.makeFile(getLogPath(), true);

            // we throw an IllegalStateException if we
            // cannot create the log file.
            if (log == null) {
                throw new IllegalStateException("Log cannot be created");
            }

            // create the writer for this log
            this.writer = new FileWriter(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        writer.flush();
        writer.close();
        isClosed = false;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public FileWriter getWriter() {
        return writer;
    }

    public String getLogName() {
        return logName;
    }

    public String getLogPath() {
        return Logger.CURRENT_LOG + "/" + logName;
    }
}

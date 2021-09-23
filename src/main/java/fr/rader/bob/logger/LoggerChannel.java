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

    LoggerChannel(String logName) {
        this.logName = logName;

        try {
            File log = FileUtils.makeFile(getLogPath(), true);

            if (log == null) {
                throw new IllegalStateException("Log cannot be created");
            }

            this.writer = new FileWriter(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

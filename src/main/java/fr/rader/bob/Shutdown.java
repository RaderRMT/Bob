package fr.rader.bob;

import fr.rader.bob.logger.Logger;
import fr.rader.bob.logger.LoggerChannel;
import fr.rader.bob.utils.FileUtils;
import fr.rader.bob.zip.ZipWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Shutdown extends Thread {

    @Override
    public void run() {
        // flush then close the log channels
        try {
            for (LoggerChannel channel : LoggerChannel.values()) {
                FileWriter writer = channel.getWriter();

                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write the logs to a zip file
        try {
            ZipWriter zipWriter = new ZipWriter(Logger.CURRENT_LOG + ".zip");

            for (LoggerChannel channel : LoggerChannel.values()) {
                zipWriter.addFile(new File(channel.getLogPath()));
            }

            zipWriter.close();
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }

        // delete the current log directory as it's now a zip
        FileUtils.deleteDirectory(Logger.CURRENT_LOG);
    }
}

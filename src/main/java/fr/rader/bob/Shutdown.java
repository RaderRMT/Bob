package fr.rader.bob;

import fr.rader.bob.logger.LoggerChannel;

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
    }
}

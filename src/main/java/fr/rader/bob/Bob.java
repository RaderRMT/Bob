package fr.rader.bob;

import fr.rader.bob.logger.Logger;
import fr.rader.bob.utils.DirectoryUtils;

public class Bob {

    public Bob() {
        // setting up a shutdown hook to close the loggers
        Runtime.getRuntime().addShutdownHook(new Shutdown());
    }

    void start() {
        DirectoryUtils.makeDirectory(OS.getBobFolder(), false);

        Logger.info("Starting Bob.");
    }
}

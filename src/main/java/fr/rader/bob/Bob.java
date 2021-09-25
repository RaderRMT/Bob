package fr.rader.bob;

import fr.rader.bob.logger.Logger;

public class Bob {

    public Bob() {
        // setting up a shutdown hook to close the loggers
        Runtime.getRuntime().addShutdownHook(new Shutdown());
    }

    void start() {
        Logger.info("Starting Bob.");
    }
}

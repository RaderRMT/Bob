package fr.rader.bob;

import fr.rader.utils.OS;
import fr.rader.utils.logger.Logger;
import fr.rader.utils.io.DirectoryUtils;

public class Bob {

    public Bob() {
        // setting up a shutdown hook to close the loggers
        Runtime.getRuntime().addShutdownHook(new Shutdown());
    }

    void start() {
        // this shouldn't be needed but why not keep it?
        DirectoryUtils.makeDirectory(OS.getBobFolder(), false);

        Logger.info("Starting Bob.");
    }
}

package fr.rader.bob;

public class Bob {

    public Bob() {
        // Setting up a shutdown hook to close the loggers
        Runtime.getRuntime().addShutdownHook(new Shutdown());
    }

    void start() {
    }
}

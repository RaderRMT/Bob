package fr.rader.bob;

import fr.rader.bob.configs.BobConfig;
import fr.rader.bob.configs.ProtocolsConfig;
import fr.rader.bob.project.ProjectManager;

import java.io.IOException;

public class Bob {

    private static Bob instance;

    private final ProtocolsConfig protocolsConfig;
    private final BobConfig bobConfig;

    private final ProjectManager projectManager;

    public Bob() throws IOException {
        instance = this;

        bobConfig = new BobConfig();
        protocolsConfig = new ProtocolsConfig();

        projectManager = new ProjectManager();
    }

    public void start() {
    }

    public static Bob getInstance() {
        return instance;
    }

    public ProtocolsConfig getProtocolsConfig() {
        return protocolsConfig;
    }

    public BobConfig getBobConfig() {
        return bobConfig;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }
}

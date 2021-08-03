package fr.rader.bob;

import fr.rader.bob.configs.BobConfig;
import fr.rader.bob.configs.ProtocolsConfig;
import fr.rader.bob.project.ProjectManager;

import java.io.IOException;

public class Main {

    private static Main instance;

    private final ProtocolsConfig protocolsConfig;
    private final BobConfig bobConfig;

    private final ProjectManager projectManager;

    public void start() {
    }

    public Main() throws IOException {
        instance = this;

        bobConfig = new BobConfig();
        protocolsConfig = new ProtocolsConfig();

        projectManager = new ProjectManager();
    }

    public static void main(String[] args) {
        try {
            Main main = new Main();
            main.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() {
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

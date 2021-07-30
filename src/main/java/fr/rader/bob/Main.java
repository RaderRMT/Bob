package fr.rader.bob;

import fr.rader.bob.config.BobConfig;
import fr.rader.bob.config.ProjectsConfig;
import fr.rader.bob.config.ProtocolsConfig;

import java.io.IOException;

public class Main {

    private static Main instance;

    private final ProtocolsConfig protocolsConfig;
    private final ProjectsConfig projectsConfig;
    private final BobConfig bobConfig;

    public void start() {
    }

    public Main() throws IOException {
        instance = this;

        bobConfig = new BobConfig();
        projectsConfig = new ProjectsConfig();
        protocolsConfig = new ProtocolsConfig();
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

    public ProjectsConfig getProjectsConfig() {
        return projectsConfig;
    }

    public BobConfig getBobConfig() {
        return bobConfig;
    }
}

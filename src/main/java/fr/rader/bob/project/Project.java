package fr.rader.bob.project;

import fr.rader.bob.project.configs.ProjectConfig;

import java.io.IOException;

public class Project {

    private final ProjectConfig config;

    public Project(String name) throws IOException {
        this.config = new ProjectConfig(name);
    }

    public ProjectConfig getConfig() {
        return config;
    }
}

package fr.rader.bob.project.configs;

import fr.rader.bob.configs.Config;
import fr.rader.bob.utils.OS;

import java.io.IOException;

public class ProjectConfig extends Config {

    public ProjectConfig(String projectName) throws IOException {
        super(OS.getBobFolder() + "projects/" + projectName, "project_settings.nbt");
    }

    // todo:
    //  to myself: don't forget to finish this class once
    //             you have something to load projects. thanks

    @Override
    public void save() {
        writeToConfig();
    }
}

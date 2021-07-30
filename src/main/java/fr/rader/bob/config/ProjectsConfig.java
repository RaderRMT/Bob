package fr.rader.bob.config;

import fr.rader.bane.nbt.tags.TagList;
import fr.rader.bane.nbt.tags.TagString;
import fr.rader.bob.Main;
import fr.rader.bob.io.file.FileIO;
import fr.rader.bob.utils.OS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectsConfig {

    private final List<String> projects;
    private final Config config;

    public ProjectsConfig() throws IOException {
        this.projects = new ArrayList<>();
        this.config = new Config(OS.getBobFolder() + "projects/", "projects.nbt");
    }

    public void addProject(String project) {
        boolean created = FileIO.createFolder(Main.getInstance().getBobConfig().getProperty("workingDirectory") + "projects/" + project);
        if(!created) {
            System.out.println("Could not create project folder for '" + project + "'");
            return;
        }

        projects.add(project);
    }

    public void removeProject(String project) {
        FileIO.deleteDirectory(Main.getInstance().getBobConfig().getProperty("workingDirectory") + "projects/" + project);

        projects.remove(project);
    }

    public void save() {
        TagList<TagString> projectsList = config.getProperties().get("projects").getAsStringList();

        projectsList.getTags().clear();
        for(String project : projects) {
            projectsList.add(new TagString(project));
        }

        config.save();
    }
}

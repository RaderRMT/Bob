package fr.rader.bob.config;

import fr.rader.bane.nbt.tags.TagList;
import fr.rader.bane.nbt.tags.TagString;
import fr.rader.bob.Main;
import fr.rader.bob.io.file.FileIO;
import fr.rader.bob.utils.OS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectsConfig extends Config {

    private final List<String> projects;

    public ProjectsConfig() throws IOException {
        super(OS.getBobFolder() + "projects/", "projects.nbt");

        this.projects = new ArrayList<>();
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

    @Override
    public void save() {
        TagList<TagString> projectsList = getProperties().get("projects").getAsStringList();

        projectsList.getTags().clear();
        for(String project : projects) {
            projectsList.add(new TagString(project));
        }

        writeToConfig();
    }
}

package fr.rader.bob.project.configs;

import fr.rader.bane.tags.TagList;
import fr.rader.bane.tags.TagString;
import fr.rader.bob.configs.Config;
import fr.rader.bob.utils.OS;

import java.io.IOException;
import java.util.HashSet;

public class ProjectListConfig extends Config {

    private final HashSet<String> projects;

    public ProjectListConfig() throws IOException {
        super(OS.getBobFolder() + "projects/", "projects_list.nbt");

        this.projects = new HashSet<>();
        getProjectsFromConfig();
    }

    private void getProjectsFromConfig() {
        TagList<TagString> projectsList = getProperties().get("projects").getAsStringList();
        for(TagString project : projectsList.getTags()) {
            projects.add(project.getValue());
        }
    }

    public HashSet<String> getProjects() {
        return projects;
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

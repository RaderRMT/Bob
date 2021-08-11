package fr.rader.bob.project;

import fr.rader.bob.Bob;
import fr.rader.bob.io.file.FileIO;
import fr.rader.bob.project.configs.ProjectListConfig;

import java.io.IOException;

public class ProjectManager {

    private final ProjectListConfig projectList;

    public ProjectManager() throws IOException {
        this.projectList = new ProjectListConfig();
    }

    public void addProject(String name) {
        if(!projectList.getProjects().add(name)) {
            System.out.println("Project already exist");
            return;
        }

        boolean projectDirCreated = FileIO.createFolder(
                Bob.getInstance()
                         .getBobConfig()
                         .getProperty("workingDirectory") + "projects/" + name
        );

        if(!projectDirCreated) {
            System.out.println("Could not create project folder for '" + name + "'");
        }
    }

    public void removeProject(String name) {
        if(!projectList.getProjects().remove(name)) {
            System.out.println("Project does not exist");
            return;
        }

        FileIO.deleteDirectory(
                Bob.getInstance()
                    .getBobConfig()
                    .getProperty("workingDirectory") + "projects/" + name
        );
    }

    public void saveProjectList() {
        projectList.save();
    }
}

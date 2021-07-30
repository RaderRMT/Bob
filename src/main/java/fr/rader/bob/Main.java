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
        /*TagCompound root = new TagCompound("");

        // 754 protocol
        TagList<TagShort> protocol754 = new TagList<>(TagShort.class, "754");

        protocol754.add(new TagShort(0x06)); // Statistics
        protocol754.add(new TagShort(0x0C)); // Boss Bar
        protocol754.add(new TagShort(0x0D)); // Server Difficulty
        protocol754.add(new TagShort(0x0E)); // Chat Message
        protocol754.add(new TagShort(0x0F)); // Tab-Complete
        protocol754.add(new TagShort(0x10)); // Declare Commands
        protocol754.add(new TagShort(0x11)); // Window Confirmation
        protocol754.add(new TagShort(0x12)); // Close Window
        protocol754.add(new TagShort(0x13)); // Window Items
        protocol754.add(new TagShort(0x14)); // Window Property
        protocol754.add(new TagShort(0x16)); // Set Cooldown
        protocol754.add(new TagShort(0x18)); // Named Sound Effect
        protocol754.add(new TagShort(0x1E)); // Open Horse Window
        protocol754.add(new TagShort(0x26)); // Trade List
        protocol754.add(new TagShort(0x2C)); // Open Book
        protocol754.add(new TagShort(0x2D)); // Open Window
        protocol754.add(new TagShort(0x2E)); // Open Sign Editor
        protocol754.add(new TagShort(0x2F)); // Craft Recipe Response
        protocol754.add(new TagShort(0x30)); // Player Abilities
        protocol754.add(new TagShort(0x35)); // Unlock Recipes
        protocol754.add(new TagShort(0x3C)); // Select Advancement Tab
        protocol754.add(new TagShort(0x48)); // Set Experience
        protocol754.add(new TagShort(0x49)); // Update Health
        protocol754.add(new TagShort(0x50)); // Entity Sound Effect
        protocol754.add(new TagShort(0x51)); // Sound Effect
        protocol754.add(new TagShort(0x52)); // Stop Sound
        protocol754.add(new TagShort(0x53)); // Player List Header And Footer
        protocol754.add(new TagShort(0x54)); // NBT Query Response
        protocol754.add(new TagShort(0x57)); // Advancements
        protocol754.add(new TagShort(0x5A)); // Declare Recipes

        root.add(protocol754);

        NBTFileIO.write(new File("C:/Users/Rader/IdeaProjects/Bob/resources/config_defaults/useless_packets.nbt"), root);*/
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

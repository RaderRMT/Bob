package fr.rader.bob.config;

import fr.rader.bob.utils.OS;

import java.io.IOException;

public class BobConfig {

    private final Config config;

    public BobConfig() throws IOException {
        this.config = new Config(OS.getBobFolder(),"settings.nbt");

        if(!config.alreadyExist()) {
            config.getProperties().get("workingDirectory").getAsTagString().setValue(OS.getBobFolder());
            config.save();
        }
    }

    public String getProperty(String key) {
        return config.getProperties().get(key).getAsString();
    }

    public void setProperty(String key, String value) {
        config.getProperties().get(key).getAsTagString().setValue(value);
    }

    public void save() {
        config.save();
    }
}

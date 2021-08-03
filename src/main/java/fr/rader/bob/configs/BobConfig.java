package fr.rader.bob.configs;

import fr.rader.bob.utils.OS;

import java.io.IOException;

public class BobConfig extends Config {

    public BobConfig() throws IOException {
        super(OS.getBobFolder(), "settings.nbt");

        if(!alreadyExist()) {
            getProperties().get("workingDirectory").getAsTagString().setValue(OS.getBobFolder());
            save();
        }
    }

    public String getProperty(String key) {
        return getProperties().get(key).getAsString();
    }

    public void setProperty(String key, String value) {
        getProperties().get(key).getAsTagString().setValue(value);
    }

    @Override
    public void save() {
        writeToConfig();
    }
}

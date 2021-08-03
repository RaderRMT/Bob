package fr.rader.bob.configs;

import fr.rader.bane.nbt.tags.TagCompound;
import fr.rader.bob.io.file.FileIO;
import fr.rader.bob.io.file.NBTFileIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class Config {

    private final TagCompound properties;
    private final File configFile;
    private final boolean alreadyExist;

    public Config(String folder, String configName) throws IOException {
        this.configFile = new File(folder + configName);
        this.alreadyExist = configFile.exists();

        // check if the config does not already exist
        if(!alreadyExist) {
            // if it does not, we get the default config from the resources
            InputStream configDefault = getClass().getResourceAsStream("/assets/config_defaults/" + configName);

            // if it's not null (that means that it exists)
            if(configDefault != null) {
                // we create the file (+ parent folders if needed)
                // we write the config and close the stream
                FileIO.createFile(configFile);
                FileIO.write(configFile, configDefault);
                configDefault.close();
            } else {
                // if the config does not exist,
                // we stop the program
                System.out.println("Default for '" + configName + "' not found!");

                System.exit(0);
            }
        }

        // we read the properties from the config file
        this.properties = NBTFileIO.read(configFile);
    }

    public abstract void save();

    protected void writeToConfig() {
        NBTFileIO.write(getConfigFile(), getProperties());
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public TagCompound getProperties() {
        return properties;
    }

    public boolean alreadyExist() {
        return alreadyExist;
    }
}

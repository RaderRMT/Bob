package fr.rader.bob.config;

import fr.rader.bane.nbt.tags.TagCompound;
import fr.rader.bob.io.file.FileIO;
import fr.rader.bob.io.file.NBTFileIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Config {

    private final TagCompound properties;
    private final File file;
    private final boolean alreadyExist;

    public Config(String outputFolder, String defaults) throws IOException {
        this.file = new File(outputFolder + defaults);
        this.alreadyExist = file.exists();

        // check if the config does not already exist
        if(!alreadyExist) {
            // if it does not, we create the file and
            // get the default config from the resources
            FileIO.createFile(file);
            InputStream configDefault = getClass().getResourceAsStream("/assets/config_defaults/" + defaults);

            // if it's not null (that means that it exists)
            if(configDefault != null) {
                // we write the config and close the stream
                FileIO.write(file, configDefault);
                configDefault.close();
            } else {
                // if the config does not exist,
                // we set the properties to null and return
                System.out.println("Default for '" + defaults + "' not found!");

                this.properties = null;
                return;
            }
        }

        // we read the properties from the config file
        this.properties = NBTFileIO.read(file);
    }

    public void save() {
        NBTFileIO.write(getConfigFile(), getProperties());
    }

    public File getConfigFile() {
        return this.file;
    }

    public TagCompound getProperties() {
        return properties;
    }

    public boolean alreadyExist() {
        return alreadyExist;
    }
}

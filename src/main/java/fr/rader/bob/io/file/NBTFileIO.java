package fr.rader.bob.io.file;

import fr.rader.bane.nbt.tags.TagCompound;
import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NBTFileIO {

    public static TagCompound read(File file) {
        try {
            return new DataReader(Files.readAllBytes(file.toPath())).readNBT();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void write(File destination, TagCompound compound) {
        DataWriter writer = new DataWriter();
        compound.write(writer);

        FileIO.write(destination, writer.getInputStream());
    }
}

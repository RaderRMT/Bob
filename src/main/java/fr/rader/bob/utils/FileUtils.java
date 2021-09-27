package fr.rader.bob.utils;

import fr.rader.bob.logger.Logger;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    /**
     * Create a file
     *
     * @param path      The path of the file to create
     * @param overwrite true if we want to overwrite the file, false otherwise
     * @return The file that was created, or null if it cannot be created
     * @throws IOException If an I/O error occurs
     */
    public static File makeFile(String path, boolean overwrite) throws IOException {
        return makeFile(new File(path), overwrite);
    }

    /**
     * Create a file
     *
     * @param file      The file to create
     * @param overwrite true if we want to overwrite the file, false otherwise
     * @return The file that was created, or null if it cannot be created
     * @throws IOException If an I/O error occurs
     */
    public static File makeFile(File file, boolean overwrite) throws IOException {
        Logger.info("Creating file " + file.getAbsolutePath());

        // deleting the file if it already exists
        if (overwrite) {
            if (file.exists()) {
                Logger.info(file.getAbsolutePath() + " already exists, deleting it");

                if (!file.delete()) {
                    Logger.error("Couldn't delete " + file.getAbsolutePath());
                    return null;
                }
            }
        }

        // create the parent directory if it does not exist
        File parent = file.getParentFile();
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                return null;
            }
        }

        // creating the file
        if (!file.createNewFile()) {
            return null;
        }

        // returning the newly-created file
        return file;
    }
}

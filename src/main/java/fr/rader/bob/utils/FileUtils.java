package fr.rader.bob.utils;

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
        // deleting the file if it already exists
        if (overwrite) {
            if (file.exists()) {
                if (!file.delete()) {
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

    /**
     * Delete a directory and it's contents
     *
     * @param path The path to the directory to delete
     * @return true if the directory has been deleted, false otherwise
     */
    public static boolean deleteDirectory(String path) {
        return deleteDirectory(new File(path));
    }

    /**
     * Delete a directory and it's contents
     *
     * @param directory The directory to delete
     * @return true if the directory has been deleted, false otherwise
     */
    public static boolean deleteDirectory(File directory) {
        // get all files in the directory
        File[] directoryFiles = directory.listFiles();

        // if it's null, so if the directory does not exist
        // we return false because we cannot delete a directory
        // that doesn't exist
        if (directoryFiles == null) {
            return false;
        }

        // here, we delete the file, or the directory (by recursion)
        // and keep track of if everything has been deleted
        boolean isEverythingDeleted = true;
        for (File file : directoryFiles) {
            isEverythingDeleted &= (file.isDirectory()) ? deleteDirectory(file) : file.delete();
        }

        return isEverythingDeleted & directory.delete();
    }
}

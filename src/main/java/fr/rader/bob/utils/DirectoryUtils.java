package fr.rader.bob.utils;

import fr.rader.bob.logger.Logger;

import java.io.File;

public class DirectoryUtils {

    /**
     * Create a file
     *
     * @param path The path of the directory to create
     * @param emptyIfExists true if we want to empty the directory if it already exists, false
     *     otherwise
     * @return The directory that was created, or null if it cannot be created
     */
    public static File makeDirectory(String path, boolean emptyIfExists) {
        return makeDirectory(new File(path), emptyIfExists);
    }

    /**
     * Create a file
     *
     * @param directory The directory to create
     * @param emptyIfExists true if we want to empty the directory if it already exists, false
     *     otherwise
     * @return The file that was created, or null if it cannot be created
     */
    public static File makeDirectory(File directory, boolean emptyIfExists) {
        Logger.info("Creating directory " + directory.getAbsolutePath());

        // deleting the file if it already exists
        // & if the emptyIfExists boolean is true
        if (emptyIfExists & directory.exists()) {
            Logger.info(directory.getAbsolutePath() + " already exists, emptying it");

            if (!emptyDirectory(directory)) {
                Logger.warn("Couldn't empty " + directory.getAbsolutePath());
                return null;
            }
        }

        // creating the directory
        if (!directory.mkdirs()) {
            Logger.warn("Couldn't create " + directory.getAbsolutePath() + ", it might already exist");
            return null;
        }

        // returning the newly-created directory
        return directory;
    }

    /**
     * Empties a directory
     *
     * @param path The path to the directory to empty
     * @return true if the directory has been emptied, false otherwise
     */
    public static boolean emptyDirectory(String path) {
        return emptyDirectory(new File(path));
    }

    /**
     * Empties a directory
     *
     * @param directory The directory to empty
     * @return true if the directory has been emptied, false otherwise
     */
    public static boolean emptyDirectory(File directory) {
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

        return isEverythingDeleted;
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
        return emptyDirectory(directory) & directory.delete();
    }
}

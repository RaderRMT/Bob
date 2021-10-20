package fr.rader.utils.zip;

import fr.rader.utils.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipReader {

    /** The ZIP file we're reading */
    private final File file; // this is our zip file

    /** This is required to read entries in the ZIP file */
    private ZipFile zipFile = null;

    /**
     * Allows us to read ZIP files
     *
     * @param path Path to the ZIP file to read
     */
    public ZipReader(String path) {
        this(new File(path));
    }

    /**
     * Allows us to read ZIP files
     *
     * @param file ZIP file to read
     */
    public ZipReader(File file) {
        this.file = file;

        try {
            this.zipFile = new ZipFile(file);
        } catch (ZipException e) {
            Logger.error("ZIP format error!");
            Logger.printStackTrace(e);
        } catch (IOException e) {
            Logger.error("I/O error!");
            Logger.printStackTrace(e);
        }
    }

    /**
     * Look if the ZIP file contains the given {@code entry}
     *
     * @param entry Entry to look for
     * @return {@code true} if the ZIP file contains the provided {@code entry},<br>
     *     {@code false} otherwise
     */
    public boolean hasEntry(String entry) {
        if (zipFile == null) {
            Logger.error(file.getName() + ": ZIP file is null");
            return false;
        }

        return zipFile.getEntry(entry) != null;
    }

    /**
     * Get an {@link InputStream} from a file in the zip
     *
     * @param entry Entry to get
     * @return The file to read, returned as an {@link InputStream}
     * @throws IOException If an I/O error occurs
     */
    public InputStream getEntry(String entry) throws IOException {
        if (zipFile == null) {
            Logger.warn(file.getName() + ": ZIP file is null");
            return null;
        }

        return zipFile.getInputStream(zipFile.getEntry(entry));
    }

    /**
     * Close the streams
     *
     * @throws IOException If an I/O error occurs
     */
    public void close() throws IOException {
        if (zipFile != null) {
            zipFile.close();
        }
    }
}

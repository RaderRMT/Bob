package fr.rader.utils.zip;

import fr.rader.utils.io.FileUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriter {

    /** Buffer size for the {@link ZipOutputStream#write(byte[], int, int)} method */
    private static final int BUFFER_SIZE = 8192;

    /** The ZIP file we're creating */
    private final File zipFile; // this is our zip file

    /** Various OutputStreams to make this work */
    private FileOutputStream zipOutputStream; // this OutputStream is used for the ZipOutputStream

    private ZipOutputStream outputStream; // this is the OutputStream we're using to write ZIP entries

    /**
     * Allows us to write ZIP files
     *
     * @param path Path to the ZIP file to write
     */
    public ZipWriter(String path) throws IOException {
        this(new File(path));
    }

    /**
     * Allows us to write ZIP files
     *
     * @param file ZIP file to write
     */
    public ZipWriter(File file) throws IOException {
        this.zipFile = FileUtils.makeFile(file, true);

        // stop creating the ZipWriter as the zip file wasn't created
        if (zipFile == null) {
            return;
        }

        // open the streams
        zipOutputStream = new FileOutputStream(zipFile);
        outputStream = new ZipOutputStream(zipOutputStream);
    }

    /**
     * Add a new entry to the zip file
     *
     * @param file File to write in the ZIP
     * @throws IOException If an I/O error occurs
     */
    public void addFile(File file) throws IOException {
        // return if the file does not exist,
        // why write a file that doesn't exist?
        if (!file.exists()) {
            return;
        }

        // adding the entry in the zip
        addEntry(file.getName(), new FileInputStream(file));
    }

    /**
     * Add a new entry to the zip file
     *
     * @param name The name of the entry
     * @param stream The stream containing the file's data
     * @throws IOException If an I/O error occurs
     */
    public void addEntry(String name, InputStream stream) throws IOException {
        // return if the zipFile is null, aka if it wasn't created
        // why write in a file that doesn't exist?
        if (zipFile == null) {
            return;
        }

        // creating the entry
        ZipEntry entry = new ZipEntry(name);
        outputStream.putNextEntry(entry);

        // writing the file to the zip
        int length;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((length = stream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // closing the steams
        stream.close();
        outputStream.closeEntry();
    }

    /**
     * Close the streams
     *
     * @throws IOException If an I/O error occurs
     */
    public void close() throws IOException {
        // return if the zipFile is null, aka if it wasn't created
        // why close streams that were not even created?
        if (zipFile == null) {
            return;
        }

        outputStream.close();
        zipOutputStream.close();
    }
}

package fr.rader.bob.zip;

import fr.rader.bob.utils.FileUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriter {

    /**
     * The ZIP file we're creating
     */
    private final File zipFile;                 // this is our zip file

    /**
     * Various OutputStreams to make this work
     */
    private FileOutputStream zipOutputStream;   // this OutputStream is used for the ZipOutputStream
    private ZipOutputStream outputStream;       // this is the OutputStream we're using to write ZIP entries

    /**
     * Allows to write ZIP file
     *
     * @param path Path to the ZIP file to write
     */
    public ZipWriter(String path) throws IOException {
        this(new File(path));
    }

    /**
     * Allows to write ZIP file
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
        // return if the zipFile is null, aka if it wasn't created
        // why write in a file that doesn't exist?
        if (zipFile == null) {
            return;
        }

        // return if the file does not exist,
        // why write a file that doesn't exist?
        if (!file.exists()) {
            return;
        }

        // creating the entry
        ZipEntry entry = new ZipEntry(file.getName());
        outputStream.putNextEntry(entry);

        // opening the stream to read the file
        FileInputStream entryInputStream = new FileInputStream(file);

        // writing the file to the zip
        int length;
        byte[] buffer = new byte[8192];
        while ((length = entryInputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        // closing the steams
        entryInputStream.close();
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

package fr.rader.bob.io;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ReplayZip {

    private final File file;

    private File tempFile;

    private ZipOutputStream outputStream;
    private ZipInputStream inputStream;

    private ZipFile zipFile;

    public ReplayZip(File file) {
        this.file = file;

        try {
            zipFile = new ZipFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getEntry(String entry) {
        if(zipFile == null) {
            return null;
        }

        InputStream out = null;

        try {
            out = zipFile.getInputStream(zipFile.getEntry(entry));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out;
    }

    public boolean hasEntry(String entry) {
        if(zipFile == null) {
            return false;
        }

        return zipFile.getEntry(entry) != null;
    }

    public void open() {
        try {
            zipFile.close();

            this.tempFile = File.createTempFile(file.getName(), null);
            tempFile.delete();
            file.renameTo(tempFile);

            inputStream = new ZipInputStream(new FileInputStream(tempFile));
            outputStream = new ZipOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a file in the ZIP file, and overwrite it if it already exists
     * @param file File to write
     * @param entry Entry name in zip file
     */
    public void addFile(File file, String entry) {
        if(file == null) {
            return;
        }

        try {
            addFile(new FileInputStream(file), entry);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addFile(InputStream entryData, String entry) {
        if(file == null) {
            return;
        }

        try {
            writeEntry(entryData, entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeEntry(InputStream entryData, String entry) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entry);
        outputStream.putNextEntry(zipEntry);

        int length;
        byte[] buffer = new byte[1024];
        while((length = entryData.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.closeEntry();
    }

    /**
     * Close all opened streams
     */
    public void close() {
        if(file == null) {
            return;
        }

        try {
            ZipEntry entry = inputStream.getNextEntry();
            while(entry != null) {
                try {
                    writeEntry(inputStream, entry.getName());
                } catch (IOException ignored) {}

                entry = inputStream.getNextEntry();
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
            zipFile.close();
            tempFile.delete();

            zipFile = new ZipFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package fr.rader.utils;

import fr.rader.utils.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class DataReader {

    private RandomAccessFile randomAccessFile = null;

    public DataReader(String path) {
        this(new File(path));
    }

    public DataReader(File file) {
        try {
            this.randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            Logger.error("Cannot find the specified file: " + file.getAbsolutePath());
            Logger.printStackTrace(e);
        }
    }
}

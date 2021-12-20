package fr.rader.utils;

import fr.rader.utils.logger.Logger;

import java.io.*;

public class LineReader {

    public static String getLine(File file, int line) {
        try {
            // open the file in a reader
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            // loop through the file until we're at the correct line
            int index = 1;
            while (index++ != line) {
                reader.readLine();
            }

            // fetch the line
            String out = reader.readLine();

            // close the streams
            reader.close();
            fileReader.close();

            // return the line
            return out;
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }

        return null;
    }
}

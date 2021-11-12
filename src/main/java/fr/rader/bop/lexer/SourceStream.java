package fr.rader.bop.lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SourceStream extends FileInputStream {

    // data[0] -> previous character
    // data[1] -> current character
    // data[2] -> next character
    private final char[] data = new char[3];

    // the size of our source file
    private final long sourceSize;

    // the offset in the source file
    private long offset = 0;

    public static final int PREVIOUS = 0;
    public static final int CURRENT = 1;
    public static final int NEXT = 2;

    /**
     * The {@code SourceStream} class extends {@link FileInputStream} and add a few useful methods,
     * such as {@link SourceStream#next() and {@link SourceStream#peek()}
     *
     * @param file source file to read
     * @throws IOException if an I/O error occurs
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a
     *     regular file, or for some other reason cannot be opened for reading.
     */
    public SourceStream(File file) throws IOException {
        super(file);

        this.sourceSize = file.length();

        this.initData();
    }

    /**
     * Reads the current character and calls the {@link SourceStream#shiftData()} method
     *
     * @return the character read from the source file<br>
     *     0 if the end of the file has been reached
     * @throws IOException if an I/O error occurs.
     */
    public char next() throws IOException {
        shiftData();

        return peek(CURRENT);
    }

    /**
     * Skip the current character
     *
     * @throws IOException if an I/O error occurs.
     */
    public void skip() throws IOException {
        shiftData();
    }

    public boolean hasNext() {
        return offset != sourceSize;
    }

    /**
     * Look at the {@code index} character in the array, without calling the {@link
     * SourceStream#shiftData()} method.
     *
     * @param index must be [0; 2]
     * @return the character read from the source file<br>
     *     0 if the end of the file has been reached
     */
    public char peek(int index) {
        if (index > NEXT || index < PREVIOUS) {
            throw new IllegalArgumentException("index must be between 0 inclusive and 2 inclusive");
        }

        return data[index];
    }

    /**
     * Initialize the data array with the first 3 characters from the source file
     *
     * @throws IOException if an I/O error occurs.
     */
    private void initData() throws IOException {
        data[PREVIOUS] = 0;
        data[CURRENT] = 0;
        data[NEXT] = fetchChar();
    }

    /**
     * Shifts the data one slot to the left and fetch a character from the source file to the last
     * data slot
     *
     * @throws IOException if an I/O error occurs.
     */
    private void shiftData() throws IOException {
        data[PREVIOUS] = data[CURRENT];
        data[CURRENT] = data[NEXT];
        data[NEXT] = fetchChar();

        offset++;
    }

    /**
     * Fetches one character from the source file
     *
     * @return the character read from the source file<br>
     *     0 if we reached the end of file
     * @throws IOException if an I/O error occurs.
     */
    private char fetchChar() throws IOException {
        int value = read();

        return (value == -1) ? 0 : (char) value;
    }
}

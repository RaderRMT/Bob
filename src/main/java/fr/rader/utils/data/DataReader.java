package fr.rader.utils.data;

import fr.rader.psl.tokens.TokenType;
import fr.rader.types.VarInt;
import fr.rader.types.VarLong;
import fr.rader.utils.logger.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class DataReader {

    private InputStream inputStream;

    public DataReader(File file) {
        if (file == null) {
            Logger.error("File is null!");
            return;
        }

        if (!file.exists()) {
            Logger.error("File does not exist: " + file.getAbsolutePath());
            return;
        }

        if (!file.isFile()) {
            Logger.error("File is a directory: " + file.getAbsolutePath());
            return;
        }

        try {
            this.inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Logger.printStackTrace(e);
        }
    }

    public DataReader(InputStream inputStream) {
        if (inputStream == null) {
            Logger.error("InputStream is null!");
            return;
        }

        this.inputStream = inputStream;
    }

    public boolean readBoolean() throws IOException {
        int value = readByte();

        if (value <= 1) {
            return value == 1;
        }

        throw new IllegalStateException("Boolean value above 1: " + value);
    }

    public int readByte() throws IOException {
        if (inputStream == null) {
            throw new IllegalStateException("InputStream is null!");
        }

        if (inputStream.available() == 0) {
            throw new EOFException("Reached end of file!");
        }

        return inputStream.read() & 0xFF;
    }

    public int readShort() throws IOException {
        return (readByte() << 8 | readByte()) & 0xffff;
    }

    public int readInt() throws IOException {
        return readShort() << 16 | readShort();
    }

    public long readLong() throws IOException {
        return (long) readInt() << 32 | readInt() & 0xffffffffL;
    }

    public float readFloat() throws IOException {
        return ByteBuffer.wrap(readFollowingBytes(4)).order(ByteOrder.BIG_ENDIAN).getFloat();
    }

    public double readDouble() throws IOException {
        return ByteBuffer.wrap(readFollowingBytes(8)).order(ByteOrder.BIG_ENDIAN).getDouble();
    }

    public char readChar() throws IOException {
        return (char) readByte();
    }

    public byte[] readFollowingBytes(int length) throws IOException {
        byte[] out = new byte[length];

        for (int i = 0; i < length; i++) {
            out[i] = (byte) readByte();
        }

        return out;
    }

    public VarInt readVarInt() throws IOException {
        int bytesRead = 0;
        int result = 0;
        int read;

        do {
            read = readByte();
            int value = (read & 0x7f);
            result |= value << (7 * bytesRead);

            bytesRead++;
            if (bytesRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0x80) != 0);

        return new VarInt(result);
    }

    public VarLong readVarLong() throws IOException {
        int numRead = 0;
        long result = 0;
        int read;

        do {
            read = readByte();
            long value = (read & 0x7f);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0x80) != 0);

        return new VarLong(result);
    }

    public String readString(int length) throws IOException {
        StringBuilder out = new StringBuilder();

        while (length-- > 0) {
            out.append(readChar());
        }

        return out.toString();
    }

    public int[] readIntArray(int length) throws IOException {
        int[] out = new int[length];

        for (int i = 0; i < length; i++) {
            out[i] = readInt();
        }

        return out;
    }

    public long[] readLongArray(int length) throws IOException {
        long[] out = new long[length];

        for (int i = 0; i < length; i++) {
            out[i] = readLong();
        }

        return out;
    }

    public UUID readUUID() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(readFollowingBytes(16));
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public Object readFromTokenType(TokenType type) throws IOException {
        switch (type) {
            case BOOLEAN:
                return readBoolean() ? 1 : 0;
            case BYTE:
            case ANGLE:
                return readByte();
            case SHORT:
                return readShort();
            case INT:
                return readInt();
            case LONG:
                return readLong();
            case CHAT:
            case STRING:
                return readString(readVarInt().getValue());
            case FLOAT:
                return readFloat();
            case DOUBLE:
                return readDouble();
            case VARINT:
                return readVarInt();
            case VARLONG:
                return readVarLong();

            /*
             * TODO:
             *  write the Metadata class
             *  and the method to read it
             *
             * case METADATA:
             *     return readMetadata();
             */

            /*
             * TODO:
             *  write a NBT library
             *  and the method to read it
             *
             * case NBT:
             *     return readNBT();
             */

            /*
             * TODO:
             *  write the Position class
             *  and the method to read it
             *
             * case POSITION:
             *     return readPosition();
             */

             case UUID:
                 return readUUID();
        }

        throw new IllegalStateException("Unknown type: " + type.getFriendlyName());
    }

    public int getLength() {
        try {
            return inputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void skip(int i) {
        try {
            inputStream.skip(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Logger.printStackTrace(e);
            }
        }
    }
}

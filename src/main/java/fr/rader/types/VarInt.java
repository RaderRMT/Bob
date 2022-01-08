package fr.rader.types;

public class VarInt {

    private int value;

    public VarInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSize() {
        int bytes = 1;

        int temp = value;
        while ((temp >>>= 7) != 0) {
            bytes++;
        }

        return bytes;
    }

    @Override
    public String toString() {
        return "VarInt{" +
                "value=" + value +
                '}';
    }
}

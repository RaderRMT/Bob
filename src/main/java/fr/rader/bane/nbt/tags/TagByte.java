package fr.rader.bane.nbt.tags;

import fr.rader.bob.utils.DataWriter;

public class TagByte extends TagBase {

    public static final byte TAG_ID = 1;

    private int value;

    public TagByte(int value) {
        setID(TAG_ID);
        setValue(value & 0xff);
    }

    public TagByte(String name, int value) {
        setID(TAG_ID);
        setName(name);
        setValue(value & 0xff);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value & 0xff;
    }

    @Override
    public void write(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(TAG_ID);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeByte(value);
    }
}

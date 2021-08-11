package fr.rader.bane.tags;

import fr.rader.bob.utils.DataWriter;

public class TagShort extends TagBase {

    public static final byte TAG_ID = 2;

    private int value;

    public TagShort(int value) {
        setID(TAG_ID);
        setValue(value & 0xffff);
    }

    public TagShort(String name, int value) {
        setID(TAG_ID);
        setName(name);
        setValue(value & 0xffff);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value & 0xffff;
    }

    @Override
    public void write(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(TAG_ID);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeShort(value);
    }
}

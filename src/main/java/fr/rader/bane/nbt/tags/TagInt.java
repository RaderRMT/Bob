package fr.rader.bane.nbt.tags;

import fr.rader.bob.utils.DataWriter;

public class TagInt extends TagBase {

    public static final byte TAG_ID = 3;

    private int value;

    public TagInt(int value) {
        setID(TAG_ID);
        setValue(value);
    }

    public TagInt(String name, int value) {
        setID(TAG_ID);
        setName(name);
        setValue(value);
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void write(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(TAG_ID);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeInt(value);
    }
}

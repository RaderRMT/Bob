package fr.rader.bane.nbt.tags;

import fr.rader.bob.utils.DataWriter;

public class TagLong extends TagBase {

    public static final byte TAG_ID = 4;

    private long value;

    public TagLong(long value) {
        setID(TAG_ID);
        setValue(value);
    }

    public TagLong(String name, long value) {
        setID(TAG_ID);
        setName(name);
        setValue(value);
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public void write(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(TAG_ID);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeLong(value);
    }
}

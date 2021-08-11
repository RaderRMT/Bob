package fr.rader.bane.tags;

import fr.rader.bob.utils.DataWriter;

public class TagString extends TagBase {

    public static final byte TAG_ID = 8;

    private String value;

    public TagString(String value) {
        setID(TAG_ID);
        setValue(value);
    }

    public TagString(String name, String value) {
        setID(TAG_ID);
        setName(name);
        setValue(value);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void write(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(TAG_ID);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeShort(value.length());
        writer.writeString(value);
    }
}

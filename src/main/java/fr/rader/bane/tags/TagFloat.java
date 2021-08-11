package fr.rader.bane.tags;

import fr.rader.bob.utils.DataWriter;

public class TagFloat extends TagBase {

    public static final byte TAG_ID = 5;

    private float value;

    public TagFloat(float value) {
        setID(TAG_ID);
        setValue(value);
    }

    public TagFloat(String name, float value) {
        setID(TAG_ID);
        setName(name);
        setValue(value);
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void write(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(TAG_ID);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeFloat(value);
    }
}

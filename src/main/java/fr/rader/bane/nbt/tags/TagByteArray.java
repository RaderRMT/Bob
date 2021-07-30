package fr.rader.bane.nbt.tags;

import fr.rader.bob.utils.DataWriter;

import java.util.ArrayList;
import java.util.List;

public class TagByteArray extends TagBase {

    public static final byte TAG_ID = 7;

    private List<Byte> value;

    public TagByteArray(String name) {
        setID(TAG_ID);
        setName(name);

        this.value = new ArrayList<>();
    }

    public TagByteArray(byte[] value) {
        setID(TAG_ID);
        setValue(value);
    }

    public TagByteArray(String name, byte[] value) {
        setID(TAG_ID);
        setName(name);

        setValue(value);
    }

    public int size() {
        return value.size();
    }

    public byte[] getValue() {
        byte[] out = new byte[value.size()];

        for(int i = 0; i < value.size(); i++) {
            out[i] = value.get(i);
        }

        return out;
    }

    public void setValue(byte[] value) {
        if(this.value == null) {
            this.value = new ArrayList<>();
        }

        if(value == null) {
            return;
        }

        for(byte b : value) {
            this.value.add(b);
        }
    }

    public void add(byte value) {
        this.value.add(value);
    }

    public byte get(int index) {
        validateIndex("get(index)", index);

        return this.value.get(index);
    }

    public void remove(int index) {
        validateIndex("remove(index)", index);

        this.value.remove(index);
    }

    public void replace(int index, byte value) {
        validateIndex("replace(index, value)", index);

        this.value.set(index, value);
    }

    private void validateIndex(String method, int index) {
        if(this.value.size() == 0) {
            throw new IndexOutOfBoundsException("[TagByteArray] -> [#" + method + "] cannot replace value in an empty list (index is " + index + ")");
        }

        if(index < 0 || index >= this.value.size()) {
            throw new IndexOutOfBoundsException("[TagByteArray] -> [#" + method + "] index must be " + ((this.value.size() == 1) ? "" : "between 0 and ") + (this.value.size() - 1) + " (index is " + index + ")");
        }
    }

    @Override
    public void write(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(TAG_ID);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeInt(value.size());
        writer.writeByteArray(getValue());
    }
}

package fr.rader.bane.nbt.tags;

import fr.rader.bob.utils.DataWriter;

import java.util.ArrayList;
import java.util.List;

public class TagLongArray extends TagBase {

    public static final byte TAG_ID = 12;

    private List<Long> value;

    public TagLongArray(String name) {
        setID(TAG_ID);
        setName(name);

        this.value = new ArrayList<>();
    }

    public TagLongArray(long[] value) {
        setID(TAG_ID);
        setValue(value);
    }

    public TagLongArray(String name, long[] value) {
        setID(TAG_ID);
        setName(name);

        setValue(value);
    }

    public int size() {
        return value.size();
    }

    public long[] getValue() {
        long[] out = new long[value.size()];

        for(int i = 0; i < value.size(); i++) {
            out[i] = value.get(i);
        }

        return out;
    }

    public void setValue(long[] value) {
        if(this.value == null) {
            this.value = new ArrayList<>();
        }

        if(value == null) {
            return;
        }

        for(long l : value) {
            this.value.add(l);
        }
    }

    public void add(long value) {
        this.value.add(value);
    }

    public long get(int index) {
        validateIndex("get(index)", index);

        return this.value.get(index);
    }

    public void remove(int index) {
        validateIndex("remove(index)", index);

        this.value.remove(index);
    }

    public void replace(int index, long value) {
        validateIndex("replace(index, value)", index);

        this.value.set(index, value);
    }

    private void validateIndex(String method, int index) {
        if(this.value.size() == 0) {
            throw new IndexOutOfBoundsException("[TagLongArray] -> [#" + method + "] cannot get value in an empty list (index is " + index + ")");
        }

        if(index < 0 || index >= this.value.size()) {
            throw new IndexOutOfBoundsException("[TagLongArray] -> [#" + method + "] index must be " + ((this.value.size() == 1) ? "" : "between 0 and ") + (this.value.size() - 1) + " (index is " + index + ")");
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
        writer.writeLongArray(getValue());
    }
}

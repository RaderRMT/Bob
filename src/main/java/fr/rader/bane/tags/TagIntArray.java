package fr.rader.bane.tags;

import fr.rader.bob.utils.DataWriter;

import java.util.ArrayList;
import java.util.List;

public class TagIntArray extends TagBase {

    public static final byte TAG_ID = 11;

    private List<Integer> value;

    public TagIntArray(String name) {
        setID(TAG_ID);
        setName(name);

        this.value = new ArrayList<>();
    }

    public TagIntArray(int[] value) {
        setID(TAG_ID);
        setValue(value);
    }

    public TagIntArray(String name, int[] value) {
        setID(TAG_ID);
        setName(name);

        setValue(value);
    }

    public int size() {
        return value.size();
    }

    public int[] getValue() {
        int[] out = new int[value.size()];

        for(int i = 0; i < value.size(); i++) {
            out[i] = value.get(i);
        }

        return out;
    }

    public void setValue(int[] value) {
        if(this.value == null) {
            this.value = new ArrayList<>();
        }

        if(value == null) {
            return;
        }

        for(int i : value) {
            this.value.add(i);
        }
    }

    public void add(int value) {
        this.value.add(value);
    }

    public int get(int index) {
        validateIndex("get(index)", index);

        return this.value.get(index);
    }

    public void remove(int index) {
        validateIndex("remove(index)", index);

        this.value.remove(index);
    }

    public void replace(int index, int value) {
        validateIndex("replace(index, value)", index);

        this.value.set(index, value);
    }

    private void validateIndex(String method, int index) {
        if(this.value.size() == 0) {
            throw new IndexOutOfBoundsException("[TagIntArray] -> [#" + method + "] cannot replace value in an empty list (index is " + index + ")");
        }

        if(index < 0 || index >= this.value.size()) {
            throw new IndexOutOfBoundsException("[TagIntArray] -> [#" + method + "] index must be " + ((this.value.size() == 1) ? "" : "between 0 and ") + (this.value.size() - 1) + " (index is " + index + ")");
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
        writer.writeIntArray(getValue());
    }
}

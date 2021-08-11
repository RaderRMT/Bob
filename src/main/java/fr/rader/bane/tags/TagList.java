package fr.rader.bane.tags;

import fr.rader.bob.utils.DataReader;
import fr.rader.bob.utils.DataWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class TagList<T extends TagBase> extends TagBase {

    public static final byte TAG_ID = 9;

    private final List<T> tags;

    private byte childrenID;

    public TagList(Class<T> tagsClass) {
        setID(TAG_ID);

        this.childrenID = getIDFromClass(tagsClass);
        this.tags = new ArrayList<>();
    }

    public TagList(Class<T> tagsClass, String name) {
        setID(TAG_ID);
        setName(name);

        this.childrenID = getIDFromClass(tagsClass);
        this.tags = new ArrayList<>();
    }

    public TagList(String name, DataReader reader) {
        setID(TAG_ID);
        setName(name);

        this.tags = new ArrayList<>();

        try {
            this.childrenID = (byte) reader.readByte();
            readList(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte getIDFromClass(Class<T> tagClass) {
        try {
            Field field = tagClass.getField("TAG_ID");
            return field.getByte(field);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Class \"" + tagClass.getSimpleName() + "\" does not contain a \"TAG_ID\" field.");
    }

    private void readList(DataReader reader) throws IOException {
        int length = reader.readInt();
        for(int i = 0; i < length; i++) {
            switch(childrenID) {
                case 1:
                    add((T) new TagByte(reader.readByte()));
                    break;
                case 2:
                    add((T) new TagShort(reader.readShort()));
                    break;
                case 3:
                    add((T) new TagInt(reader.readInt()));
                    break;
                case 4:
                    add((T) new TagLong(reader.readLong()));
                    break;
                case 5:
                    add((T) new TagFloat(reader.readFloat()));
                    break;
                case 6:
                    add((T) new TagDouble(reader.readDouble()));
                    break;
                case 7:
                    add((T) new TagByteArray(reader.readFollowingBytes(reader.readInt())));
                    break;
                case 8:
                    add((T) new TagString(reader.readString(reader.readShort())));
                    break;
                case 9:
                    add((T) new TagList<>(null, reader));
                    break;
                case 10:
                    add((T) new TagCompound(null, reader));
                    break;
                case 11:
                    add((T) new TagIntArray(reader.readIntArray(reader.readInt())));
                    break;
                case 12:
                    add((T) new TagLongArray(reader.readLongArray(reader.readInt())));
                    break;
                default:
                    throw new IllegalStateException("Unexpected tag: " + Integer.toHexString(childrenID));
            }
        }
    }

    @Override
    public void write(DataWriter writer) {
        if(getName() != null) {
            writer.writeByte(TAG_ID);
            writer.writeShort(getName().length());
            writer.writeString(getName());
        }

        writer.writeByte(childrenID);
        writer.writeInt(tags.size());

        for(T tag : tags) {
            tag.write(writer);
        }
    }

    public byte getChildrenID() {
        return childrenID;
    }

    public void add(T nbt) {
        if(nbt.getName() != null) {
            throw new IllegalArgumentException("NBT Tag must not have a name");
        }

        if(nbt.getTagID() != childrenID) {
            throw new IllegalArgumentException("NBT Tag id must be " + childrenID + " (it is " + nbt.getTagID() + " (" + nbt.getClass().getSimpleName() + ") instead)");
        }

        tags.add(nbt);
    }

    public void remove(int index) {
        validateIndex("remove(index)", index);

        tags.remove(index);
    }

    public void remove(T nbt) {
        tags.remove(nbt);
    }

    public void replace(int index, T nbt) {
        if(nbt.getName() != null) {
            throw new IllegalArgumentException("NBT Tag must not have a name");
        }

        validateIndex("replace(index, nbt)", index);

        tags.set(index, nbt);
    }

    public T get(int index) {
        validateIndex("get(index)", index);

        return tags.get(index);
    }

    public boolean isEmpty() {
        return tags.size() == 0;
    }

    /**
     * Look if the list has something at the given index
     * @return true if the list contains something at the given index, false otherwise
     */
    public boolean has(int index) {
        return get(index) != null;
    }

    public int size() {
        return tags.size();
    }

    public List<T> getTags() {
        return tags;
    }

    private void validateIndex(String method, int index) {
        if(this.tags.size() == 0) {
            throw new IndexOutOfBoundsException("[TagList] -> [#" + method + "] cannot replace value in an empty list (index is " + index + ")");
        }

        if(index < 0 || index >= this.tags.size()) {
            throw new IndexOutOfBoundsException("[TagList] -> [#" + method + "] index must be " + ((this.tags.size() == 1) ? "" : "between 0 and ") + (this.tags.size() - 1) + " (index is " + index + ")");
        }
    }
}

package fr.rader.bane.tags;

import fr.rader.bob.utils.DataWriter;

public abstract class TagBase {

    private final String simpleName = this.getClass().getSimpleName();
    private final String type = capitalizeFirst(getTagNameFromClass());

    private String name;
    private byte tagID;

    // todo:
    //  either make this return a byte[], and add them together in a datawriter,
    //  or keep it as it is. if we go with the first route, we could move the datawriter class to the io package,
    //   then make the datawriter package private,
    //   and have a IO class managing reading/writing nbt files (the utils package might have to move to the io package)
    public abstract void write(DataWriter writer);

    public void setID(byte id) {
        this.tagID = id;
    }

    public byte getTagID() {
        return tagID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public TagByte getAsTagByte() {
        if(!(this instanceof TagByte)) {
            throw new ClassCastException("Cannot cast to TagByte, please use #getAs" + simpleName + "() instead.");
        }

        return (TagByte) this;
    }

    public TagShort getAsTagShort() {
        if(!(this instanceof TagShort)) {
            throw new ClassCastException("Cannot cast to TagShort, please use #getAs" + simpleName + "() instead.");
        }

        return (TagShort) this;
    }

    public TagInt getAsTagInt() {
        if(!(this instanceof TagInt)) {
            throw new ClassCastException("Cannot cast to TagInt, please use #getAs" + simpleName + "() instead.");
        }

        return (TagInt) this;
    }

    public TagLong getAsTagLong() {
        if(!(this instanceof TagLong)) {
            throw new ClassCastException("Cannot cast to TagLong, please use #getAs" + simpleName + "() instead.");
        }

        return (TagLong) this;
    }

    public TagFloat getAsTagFloat() {
        if(!(this instanceof TagFloat)) {
            throw new ClassCastException("Cannot cast to TagFloat, please use #getAs" + simpleName + "() instead.");
        }

        return (TagFloat) this;
    }

    public TagDouble getAsTagDouble() {
        if(!(this instanceof TagDouble)) {
            throw new ClassCastException("Cannot cast to TagDouble, please use #getAs" + simpleName + "() instead.");
        }

        return (TagDouble) this;
    }

    public TagByteArray getAsTagByteArray() {
        if(!(this instanceof TagByteArray)) {
            throw new ClassCastException("Cannot cast to TagByteArray, please use #getAs" + simpleName + "() instead.");
        }

        return (TagByteArray) this;
    }

    public TagString getAsTagString() {
        if(!(this instanceof TagString)) {
            throw new ClassCastException("Cannot cast to TagString, please use #getAs" + simpleName + "() instead.");
        }

        return (TagString) this;
    }

    public TagCompound getAsTagCompound() {
        if(!(this instanceof TagCompound)) {
            throw new ClassCastException("Cannot cast to TagCompound, please use #getAs" + simpleName + "() instead.");
        }

        return (TagCompound) this;
    }

    public TagIntArray getAsTagIntArray() {
        if(!(this instanceof TagIntArray)) {
            throw new ClassCastException("Cannot cast to TagIntArray, please use #getAs" + simpleName + "() instead.");
        }

        return (TagIntArray) this;
    }

    public TagLongArray getAsTagLongArray() {
        if(!(this instanceof TagLongArray)) {
            throw new ClassCastException("Cannot cast to TagLongArray, please use #getAs" + simpleName + "() instead.");
        }

        return (TagLongArray) this;
    }

    public int getAsByte() {
        try {
            return getAsTagByte().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as byte, please use #getAs" + type + "() instead.");
        }
    }

    public int getAsShort() {
        try {
            return getAsTagByte().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as short, please use #getAs" + type + "() instead.");
        }
    }

    public int getAsInt() {
        try {
            return getAsTagInt().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as int, please use #getAs" + type + "() instead.");
        }
    }

    public long getAsLong() {
        try {
            return getAsTagLong().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as long, please use #getAs" + type + "() instead.");
        }
    }

    public float getAsFloat() {
        try {
            return getAsTagFloat().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as float, please use #getAs" + type + "() instead.");
        }
    }

    public double getAsDouble() {
        try {
            return getAsTagDouble().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as double, please use #getAs" + type + "() instead.");
        }
    }

    public byte[] getAsByteArray() {
        try {
            return getAsTagByteArray().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as byte array, please use #getAs" + type + "() instead.");
        }
    }

    public String getAsString() {
        try {
            return getAsTagString().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as string, please use #getAs" + type + "() instead.");
        }
    }

    public int[] getAsIntArray() {
        try {
            return getAsTagIntArray().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as int array, please use #getAs" + type + "() instead.");
        }
    }

    public long[] getAsLongArray() {
        try {
            return getAsTagLongArray().getValue();
        } catch (ClassCastException e) {
            throw new ClassCastException("Cannot get as long array, please use #getAs" + type + "() instead.");
        }
    }

    public TagList<TagByte> getAsByteList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagByte> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagByte> list = ((TagList<TagByte>) this);

        if(list.getChildrenID() != TagByte.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagByte> because the childen ID does not match the TagByte ID");
        }

        return list;
    }

    public TagList<TagShort> getAsShortList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagShort> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagShort> list = ((TagList<TagShort>) this);

        if(list.getChildrenID() != TagShort.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagShort> because the childen ID does not match the TagShort ID");
        }

        return list;
    }

    public TagList<TagInt> getAsIntList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagInt> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagInt> list = ((TagList<TagInt>) this);

        if(list.getChildrenID() != TagInt.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagInt> because the childen ID does not match the TagInt ID");
        }

        return list;
    }

    public TagList<TagLong> getAsLongList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagLong> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagLong> list = ((TagList<TagLong>) this);

        if(list.getChildrenID() != TagLong.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagLong> because the childen ID does not match the TagLong ID");
        }

        return list;
    }

    public TagList<TagFloat> getAsFloatList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagFloat> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagFloat> list = ((TagList<TagFloat>) this);

        if(list.getChildrenID() != TagFloat.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagFloat> because the childen ID does not match the TagFloat ID");
        }

        return list;
    }

    public TagList<TagDouble> getAsDoubleList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagDouble> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagDouble> list = ((TagList<TagDouble>) this);

        if(list.getChildrenID() != TagDouble.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagDouble> because the childen ID does not match the TagDouble ID");
        }

        return list;
    }

    public TagList<TagByteArray> getAsByteArrayList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagByteArray> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagByteArray> list = ((TagList<TagByteArray>) this);

        if(list.getChildrenID() != TagByteArray.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagByteArray> because the childen ID does not match the TagByteArray ID");
        }

        return list;
    }

    public TagList<TagString> getAsStringList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagString> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagString> list = ((TagList<TagString>) this);

        if(list.getChildrenID() != TagString.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagString> because the childen ID does not match the TagString ID");
        }

        return list;
    }

    public TagList<TagList<?>> getAsListList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagList<?>> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagList<?>> list = ((TagList<TagList<?>>) this);

        if(list.getChildrenID() != TagList.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagList<?>> because the childen ID does not match the TagList ID");
        }

        return list;
    }

    public TagList<TagCompound> getAsCompoundList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagCompound> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagCompound> list = ((TagList<TagCompound>) this);

        if(list.getChildrenID() != TagCompound.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagCompound> because the childen ID does not match the TagCompound ID");
        }

        return list;
    }

    public TagList<TagIntArray> getAsIntArrayList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagIntArray> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagIntArray> list = ((TagList<TagIntArray>) this);

        if(list.getChildrenID() != TagIntArray.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagIntArray> because the childen ID does not match the TagIntArray ID");
        }

        return list;
    }

    public TagList<TagLongArray> getAsLongArrayList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<TagLongArray> because the class does not match (tag is a " + simpleName + ").");
        }

        @SuppressWarnings("unchecked")
        TagList<TagLongArray> list = ((TagList<TagLongArray>) this);

        if(list.getChildrenID() != TagLongArray.TAG_ID) {
            throw new RuntimeException("Cannot cast to TagList<TagLongArray> because the childen ID does not match the TagLongArray ID");
        }

        return list;
    }

    public TagList<TagBase> getAsUnknownList() {
        if(!(this instanceof TagList)) {
            throw new ClassCastException("Cannot cast to TagList<?> because the class does not match (tag is a " + simpleName + ").");
        }

        return (TagList<TagBase>) this;
    }

    private String capitalizeFirst(String string) {
        char[] charString = string.toCharArray();
        charString[0] = Character.toUpperCase(charString[0]);
        return new String(charString);
    }

    private String uncapitalizeFirst(String string) {
        char[] charString = string.toCharArray();
        charString[0] = Character.toLowerCase(charString[0]);
        return new String(charString);
    }

    public String getTagNameFromClass() {
        return uncapitalizeFirst(getClass().getSimpleName().substring(3));
    }
}

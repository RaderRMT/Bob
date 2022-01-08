package fr.rader.psl.packets.entries;

import java.util.HashMap;
import java.util.List;

public class ArrayEntry extends PacketEntry {

    private final int length;

    private final HashMap<Integer, List<PacketEntry>> entries;

    public ArrayEntry(int length, String name) {
        super(name);

        this.length = length;
        this.entries = new HashMap<>();
    }

    public int getLength() {
        return length;
    }

    public List<PacketEntry> getEntriesForIndex(int index) {
        return entries.get(index);
    }

    public void setEntriesForIndex(int index, List<PacketEntry> entries) {
        this.entries.put(index, entries);
    }

    @Override
    public String toString() {
        return "ArrayEntry{" +
                "length=" + length +
                ", entries=" + entries +
                '}';
    }
}

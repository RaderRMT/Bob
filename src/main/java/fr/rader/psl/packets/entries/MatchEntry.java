package fr.rader.psl.packets.entries;

import java.util.ArrayList;
import java.util.List;

public class MatchEntry extends PacketEntry {

    private final int value;

    private List<PacketEntry> entries;

    public MatchEntry(int value) {
        super(null);

        this.value = value;
        this.entries = new ArrayList<>();
    }

    public int getValue() {
        return value;
    }

    public List<PacketEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<PacketEntry> entries) {
        this.entries = entries;
    }

    @Override
    public String toString() {
        return "MatchEntry{" +
                "value='" + value + '\'' +
                ", entries=" + entries +
                '}';
    }
}

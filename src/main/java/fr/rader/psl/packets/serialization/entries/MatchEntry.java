package fr.rader.psl.packets.serialization.entries;

import java.util.ArrayList;
import java.util.List;

public class MatchEntry extends PacketEntry {

    private List<PacketEntry> entries;

    public MatchEntry() {
        super(null);

        this.entries = new ArrayList<>();
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
                "entries=" + entries +
                '}';
    }
}

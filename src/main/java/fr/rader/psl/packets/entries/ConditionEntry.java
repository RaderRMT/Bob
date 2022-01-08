package fr.rader.psl.packets.entries;

import java.util.ArrayList;
import java.util.List;

public class ConditionEntry extends PacketEntry {

    private List<PacketEntry> entries;

    public ConditionEntry() {
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
        return "ConditionEntry{" +
                "entries=" + entries +
                '}';
    }
}

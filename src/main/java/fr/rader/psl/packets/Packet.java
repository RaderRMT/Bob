package fr.rader.psl.packets;

import fr.rader.psl.packets.entries.PacketEntry;

import java.util.ArrayList;
import java.util.List;

public class Packet {

    private List<PacketEntry> entries;

    public Packet() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(PacketEntry entry) {
        this.entries.add(entry);
    }

    public void setEntry(List<PacketEntry> entry) {
        this.entries = entry;
    }

    public List<PacketEntry> getEntries() {
        return entries;
    }
}

package fr.rader.psl.packets;

import fr.rader.psl.packets.serialization.entries.PacketEntry;

import java.util.ArrayList;
import java.util.List;

public class Packet {

    /**
     * The entries in our packet, this contains everything
     */
    private List<PacketEntry> entries;

    public Packet() {
        this.entries = new ArrayList<>();
    }

    /**
     * Add an entry to our packet, this will be added to the end of the list
     *
     * @param entry the entry to add
     */
    public void addEntry(PacketEntry entry) {
        this.entries.add(entry);
    }

    /**
     * Replace the entire list of packet entries with the one given as a parameter
     *
     * @param entry the new entries
     */
    public void setEntry(List<PacketEntry> entry) {
        this.entries = entry;
    }

    public List<PacketEntry> getEntries() {
        return entries;
    }
}

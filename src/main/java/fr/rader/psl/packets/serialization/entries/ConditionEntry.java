package fr.rader.psl.packets.serialization.entries;

import java.util.ArrayList;
import java.util.List;

public class ConditionEntry extends PacketEntry {

    /** The entries in the condition */
    private List<PacketEntry> entries;

    public ConditionEntry() {
        super(null);

        this.entries = new ArrayList<>();
    }

    /**
     * Get the entries in the condition
     *
     * @return {@link List}<{@link PacketEntry}> - the list of entries in the condition
     */
    public List<PacketEntry> getEntries() {
        return entries;
    }

    /**
     * Set the entries in the condition
     *
     * @param entries {@link List}<{@link PacketEntry}> - the list of entries to add
     */
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

package fr.rader.psl.packets.entries;

import java.util.ArrayList;
import java.util.List;

public class SimpleArrayEntry extends PacketEntry {

    private final List<VariableEntry> variables;

    private final int length;

    public SimpleArrayEntry(int length, String name) {
        super(name);

        this.length = length;
        this.variables = new ArrayList<>();
    }

    public List<VariableEntry> getVariables() {
        return variables;
    }

    public int getLength() {
        return length;
    }

    public void addVariable(VariableEntry value) {
        variables.add(value);
    }

    public void setVariable(int index, Object value) {
        variables.get(index).setValue(value);
    }

    public VariableEntry getVariable(int index) {
        return variables.get(index);
    }

    @Override
    public String toString() {
        return "SimpleArrayEntry{" +
                "variables=" + variables +
                '}';
    }
}

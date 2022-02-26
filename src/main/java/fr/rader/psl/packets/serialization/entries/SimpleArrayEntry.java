package fr.rader.psl.packets.serialization.entries;

import java.util.ArrayList;
import java.util.List;

public class SimpleArrayEntry extends PacketEntry {

    private final List<VariableEntry> variables;

    public SimpleArrayEntry(String name) {
        super(name);

        this.variables = new ArrayList<>();
    }

    public List<VariableEntry> getVariables() {
        return variables;
    }

    public void addVariable(VariableEntry value) {
        variables.add(value);
    }

    public void setVariable(int index, Object value) {
        variables.add(index, new VariableEntry(null, value));
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

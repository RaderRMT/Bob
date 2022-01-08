package fr.rader.psl.packets.entries;

public class VariableEntry extends PacketEntry {

    private Object value;

    public VariableEntry(String name, Object value) {
        super(name);

        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "VariableEntry{" +
                "name=" + getName() +
                ", value=" + value +
                '}';
    }
}

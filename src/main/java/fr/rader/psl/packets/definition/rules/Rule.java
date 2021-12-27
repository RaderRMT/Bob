package fr.rader.psl.packets.definition.rules;

public class Rule {

    /** The rule name */
    private final String name;

    public Rule(String name) {
        // the name is not needed,
        // it can be set to null
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

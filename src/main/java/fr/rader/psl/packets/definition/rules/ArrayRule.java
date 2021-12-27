package fr.rader.psl.packets.definition.rules;

import java.util.ArrayList;
import java.util.List;

public class ArrayRule extends Rule {

    /** The array length (this is the variable's name,
     * we will use it later to get the actual array length as a number) */
    private final String lengthVariable;

    /** The list of rules inside the array */
    private List<Rule> rules = new ArrayList<>();

    public ArrayRule(String lengthVariable, String name) {
        super(name);

        // we keep track of the length variable
        this.lengthVariable = lengthVariable;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public String getLengthVariable() {
        return lengthVariable;
    }

    public List<Rule> getRules() {
        return rules;
    }
}

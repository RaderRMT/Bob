package fr.rader.psl.packets.definition.rules;

import java.util.HashMap;
import java.util.List;

public class MatchRule extends Rule {

    /** The variable to compare */
    private final String variable;

    /** The rules depending on the integer key */
    private final HashMap<Integer, List<Rule>> rules;

    public MatchRule(String variable) {
        super(null);

        // we keep track of the variable to compare
        this.variable = variable;
        // and we create a new empty hashmap
        this.rules = new HashMap<>();
    }

    public void setRulesForValue(int value, List<Rule> rules) {
        this.rules.put(value, rules);
    }

    public List<Rule> getRulesForValue(int value) {
        return rules.get(value);
    }

    public String getVariable() {
        return variable;
    }
}

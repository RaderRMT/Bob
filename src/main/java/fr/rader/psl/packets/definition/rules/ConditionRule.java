package fr.rader.psl.packets.definition.rules;

import fr.rader.psl.tokens.TokenType;
import fr.rader.utils.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class ConditionRule extends Rule {

    /** The variable's name */
    private final String variable;
    /** The comparison sign */
    private final TokenType comparison;
    /** The value to compare to */
    private final int value;

    /** The list of rules to execute if
     * the variable's value equals the value */
    private List<Rule> branchRules = new ArrayList<>();

    public ConditionRule(String variable, TokenType comparison, int value) {
        super(null);

        // we keep track of the variable,
        // the sign and the value to compare to
        this.variable = variable;
        this.comparison = comparison;
        this.value = value;
    }

    /**
     * Check if branch is taken depending on the given value
     *
     * @param valueToCheck the variable's value
     * @return true if the branch is taken, false otherwise
     */
    public boolean isBranchTaken(int valueToCheck) {
        switch (comparison) {
            case LESS_SIGN:
                return valueToCheck < value;
            case LESS_EQUAL_SIGN:
                return valueToCheck <= value;
            case GREATER_SIGN:
                return valueToCheck > value;
            case GREATER_EQUAL_SIGN:
                return valueToCheck >= value;
            case EQUAL_EQUAL_SIGN:
                return value == valueToCheck;
            case BANG_EQUAL_SIGN:
                return value != valueToCheck;

            default:
                Logger.error("Unexpected comparison");
                return false;
        }
    }

    public int getValue() {
        return value;
    }

    public List<Rule> getBranchRules() {
        return branchRules;
    }

    public String getVariable() {
        return variable;
    }

    public void setRules(List<Rule> branchRules) {
        this.branchRules = branchRules;
    }
}

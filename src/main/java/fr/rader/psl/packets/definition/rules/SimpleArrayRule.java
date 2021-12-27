package fr.rader.psl.packets.definition.rules;

import fr.rader.psl.tokens.TokenType;

public class SimpleArrayRule extends Rule {

    /** The type of data to read */
    private final TokenType type;

    /** The array length (this is the variable's name,
     * we will use it later to get the actual array length as a number) */
    private final String lengthVariable;

    public SimpleArrayRule(TokenType type, String lengthVariable, String name) {
        super(name);

        // we keep the type and the length variable
        // in final variables
        this.type = type;
        this.lengthVariable = lengthVariable;
    }

    public TokenType getType() {
        return type;
    }

    public String getLengthVariable() {
        return lengthVariable;
    }
}

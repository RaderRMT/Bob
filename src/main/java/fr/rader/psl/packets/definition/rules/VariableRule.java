package fr.rader.psl.packets.definition.rules;

import fr.rader.psl.tokens.TokenType;

public class VariableRule extends Rule {

    /** The type of our variable */
    private final TokenType type;

    public VariableRule(TokenType type, String name) {
        super(name);

        // we keep the type in a variable
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }
}
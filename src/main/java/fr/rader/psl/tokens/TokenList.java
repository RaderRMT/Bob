package fr.rader.psl.tokens;

import java.util.ArrayList;
import java.util.List;

public class TokenList {

    /** Index of the token we're reading */
    private int index = 0;

    /** The list of token to read */
    private final List<Token> tokens;

    public TokenList() {
        this.tokens = new ArrayList<>();
    }

    /**
     * Add a token to the list
     *
     * @param token Token to add
     */
    public void add(Token token) {
        tokens.add(token);
    }

    /**
     * Get a token from the list at {@code index}
     *
     * @return token or null if out of bound
     */
    public Token get() {
        if (index == tokens.size()) {
            return null;
        }

        return tokens.get(index++);
    }

    /**
     * Skip the current token
     */
    public void skip() {
        index++;
    }

    /**
     * Peek the next token
     *
     * @return the next token or null if out of bound
     */
    public Token peek() {
        if (index == tokens.size()) {
            return null;
        }

        return tokens.get(index);
    }

    /**
     * Get the list of tokens
     *
     * @return list of Token
     */
    public List<Token> getTokens() {
        return tokens;
    }
}

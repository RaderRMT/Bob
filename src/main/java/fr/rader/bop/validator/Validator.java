package fr.rader.bop.validator;

import fr.rader.bop.tokens.Token;
import fr.rader.bop.tokens.TokenList;
import fr.rader.bop.tokens.TokenType;
import fr.rader.utils.logger.Logger;

import static fr.rader.bop.tokens.TokenType.*;

public class Validator {

    /** The list of token to validate */
    private final TokenList tokens;

    /** Keep track of the validity of the PSL source */
    private boolean isSourceValid = true;

    public Validator(TokenList tokens) {
        this.tokens = tokens;
    }

    /**
     * Get whether the PSL source is valid or not
     *
     * @return true if the source is valid, false otherwise.
     */
    public boolean isValid() {
        return isSourceValid;
    }

    /**
     * Validates the PSL source file.
     * It stops validating once it found an error.
     */
    public void validate() {
        while (tokens.peek().getType() != EOF && isSourceValid) {
            expect(PACKET, NAME, OPEN_CURLY_BRACKET);
            validateBlock();
        }
    }

    private void validateBlock() {
        if (!isSourceValid) {
            return;
        }

        TokenType type;
        while ((type = tokens.get().getType()) != CLOSE_CURLY_BRACKET && isSourceValid) {
            // variable
            if (type.isType()) {
                // arrays
                if (tokens.peek().getType().equals(OPEN_SQUARE_BRACKET)) {
                    expect(OPEN_SQUARE_BRACKET, NAME, CLOSE_SQUARE_BRACKET, NAME, SEMICOLON);
                    continue;
                }

                expect(NAME, SEMICOLON);
                continue;
            }

            // arrays
            if (type.equals(ARRAY)) {
                expect(OPEN_PAREN, NAME, COMMA, NAME, CLOSE_PAREN, OPEN_CURLY_BRACKET);

                validateBlock();
                continue;
            }

            // conditions
            if (type.equals(IF)) {
                expect(NAME);
                isSourceValid &= tokens.get().getType().isComparator();
                expect(NUMBER, OPEN_CURLY_BRACKET);

                validateBlock();
                continue;
            }

            // match
            if (type.equals(MATCH)) {
                expect(NAME, OPEN_CURLY_BRACKET);

                validateMatch();
                continue;
            }

            isSourceValid = false;
        }
    }

    /**
     * Validates a match, as it has a different syntax.
     */
    private void validateMatch() {
        if (!isSourceValid) {
            return;
        }

        while (tokens.peek().getType() != CLOSE_CURLY_BRACKET && isSourceValid) {
            expect(NUMBER, MATCH_ARROW, OPEN_CURLY_BRACKET);
            validateBlock();
        }

        tokens.skip();
    }

    /**
     * Reads the given types and compare them to the tokens from the TokenList.
     * This method will change the {@code isSourceValid} value to false if the tokens
     * do not match.
     *
     * @param types Types to compare
     */
    private void expect(TokenType... types) {
        for (TokenType type : types) {
            Token token = tokens.get();

            if (!type.equals(token.getType())) {
                Logger.warn("Expected " + type.name() + ", got " + token.getType().name() + " instead.");
                isSourceValid = false;
                return;
            }
        }
    }
}

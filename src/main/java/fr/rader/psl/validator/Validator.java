package fr.rader.psl.validator;

import fr.rader.psl.tokens.Token;
import fr.rader.psl.tokens.TokenList;
import fr.rader.psl.tokens.TokenType;
import fr.rader.utils.errors.ErrorPrinter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static fr.rader.psl.tokens.TokenType.*;

public class Validator {

    /** The list of tokens to validate */
    private final TokenList tokens;

    /** The PSL source */
    private final File source;

    /** Used to tell the validator is the source is valid.
     * If this is false, it's going to stop validating */
    private boolean isSourceValid = true;

    /** This holds all the variables
     * The String key is the variable's name, and the
     * TokenType value is the variable's type */
    private final Map<String, TokenType> variables = new HashMap<>();

    public Validator(TokenList tokens, File source) {
        this.tokens = tokens;
        this.source = source;
    }

    public void validate() {
        // quick summary of what this is doing:
        //
        // check for `packet`
        // check for name
        // check for {
        // validate everything until matching }
        // check if we're at the end of file

        int line = tokens.peek().getLine();
        expect(line, PACKET);
        expect(line, NAME);
        expect(line, OPEN_CURLY_BRACKET);

        // validate the packet block
        validateBlock();

        // check if we're at the end of file.
        // only one packet declaration per PSL file
        // is allowed. if there's a packet that isn't EOF,
        // we then tell the validator that the source isn't valid
        if (isSourceValid && !tokens.peek().getType().equals(EOF)) {
            // we throw an error
            ErrorPrinter.printError(
                    source,
                    tokens.peek().getLine(),
                    0,
                    0,
                    "A17",
                    "Unexpected token after end of packet definition",
                    "Remove everything after the packet definition"
            );

            // and then tell the validator that
            // the source is invalid
            isSourceValid = false;
        }
    }

    private void validateBlock() {
        while (isSourceValid) {
            Token token = tokens.get();
            TokenType type = token.getType();

            if (type.equals(CLOSE_CURLY_BRACKET)) {
                break;
            }

            int line = token.getLine();

            // check for variable
            if (type.isType()) {
                validateVariable(line, type);

                continue;
            }

            if (type.equals(ARRAY)) {
                validateArray(line);

                continue;
            }

            if (type.equals(IF)) {
                validateCondition(line);

                continue;
            }

            if (type.equals(MATCH)) {
                validateMatch(line);

                continue;
            }

            ErrorPrinter.printError(
                    source,
                    line,
                    0,
                    0,
                    "A12",
                    "Unexpected token: " + type.getFriendlyName(),
                    "Remove the \"" + type.getFriendlyName() + "\" and try again"
            );

            isSourceValid = false;
        }
    }

    private void validateVariable(int line, TokenType type) {
        // quick summary of what this is doing:
        //
        // peek for [
        // if [ (
        //      check for [
        //      check for name
        //      check if name is a key in variables.
        //          if it is, we check its type
        //      check for ]
        // )
        //
        // check for name
        // check for semicolon
        // add variable to variables map

        // check if we're declaring an array
        if (tokens.peek().getType().equals(OPEN_SQUARE_BRACKET)) {
            // if we are, we check for an open square bracket
            expect(line, OPEN_SQUARE_BRACKET);

            // then we peek the next token. it should be a
            // name. we keep it in a variable because
            // TokenList doesn't have a method to get the current token
            Token arrayLengthVariable = tokens.peek();
            // then we check if the token is a name.
            // this name is the array's length
            expect(line, NAME);
            // then we check if the array length variable is a number
            checkVariableIsNumber(arrayLengthVariable);

            // then we check for a close square bracket
            expect(line, CLOSE_SQUARE_BRACKET);

            // if the source is not valid at this point,
            // we then leave this method
            if (!isSourceValid) {
                return;
            }

            // if the source is still valid,
            // we change the type to be an array
            // because that's what the variable is now.
            type = ARRAY;
        }

        // then we peek the next token. it should be a
        // name. we keep it in a variable because
        // TokenList doesn't have a method to get the current token
        Token variableName = tokens.peek();
        // then we check if the token is a name.
        // this name is the variable's name
        expect(line, NAME);
        // then we check if the variable already exists
        if (isSourceValid && variableExists(variableName)) {
            // if it already exists, we throw an error
            ErrorPrinter.printError(
                    source,
                    line,
                    0,
                    0,
                    "A16",
                    "Variable already defined: " + variableName.getValue(),
                    "Try using a different name for the variable"
            );

            // then we tell the validator that the source isn't valid
            isSourceValid = false;
            // and we finally leave this method
            return;
        }

        // if everything is still valid,
        // we check if the next token is a semicolon
        expect(line, SEMICOLON);

        // we check if the source is valid
        // if it isn't, we don't want to add the
        // variable to the map because we don't even know
        // if it's the correct type or not
        if (isSourceValid) {
            // we add the variable to variables map if everything is ok
            variables.put(
                    (String) variableName.getValue(),   // the variable name
                    type                                // the variable type
            );
        }
    }

    private void validateArray(int line) {
        // quick summary of what this is doing:
        //
        // check for (
        // check for name
        // check for ,
        // check for name
        // check for )
        // check for {
        // validate everything until matching }

        // we check for a (
        expect(line, OPEN_PAREN);

        // then we peek the next token. it should be a
        // name. we keep it in a variable because
        // TokenList doesn't have a method to get the current token
        Token arrayLengthVariable = tokens.peek();
        // then we check if the token is a name.
        // this name is the array's length
        expect(line, NAME);
        // then we check if the array length variable is a number
        checkVariableIsNumber(arrayLengthVariable);

        // we then check for a comma
        expect(line, COMMA);

        // then we peek the next token. it should be a
        // name. we keep it in a variable because
        // TokenList doesn't have a method to get the current token
        Token arrayName = tokens.peek();
        // then we check if the token is a name.
        // this name is the variable's name
        expect(line, NAME);
        // then we check if the variable already exists
        if (isSourceValid && variableExists(arrayName)) {
            // if it already exists, we throw an error
            ErrorPrinter.printError(
                    source,
                    line,
                    0,
                    0,
                    "A16",
                    "Variable already defined: " + arrayName.getValue(),
                    "Try using a different name for the variable"
            );

            // then we tell the validator that the source isn't valid
            isSourceValid = false;
            // and we finally leave this method
            return;
        }

        // we then check for a )
        expect(line, CLOSE_PAREN);
        // and finally we check for a {
        expect(line, OPEN_CURLY_BRACKET);

        // we check if the source is valid
        // if it isn't, we don't want to add the
        // variable to the map
        if (isSourceValid) {
            // we add the variable to variables map if everything is ok
            variables.put(
                    (String) arrayName.getValue(),  // the variable name
                    ARRAY                           // the variable type
            );

            // then we validate the array block
            validateBlock();
        }
    }

    private void validateCondition(int line) {
        // quick summary of what this is doing:
        //
        // check for name
        // check if name is a boolean
        // if not boolean (
        //      check for comparator
        //      check for number
        // ) else (
        //      check if name is a number
        // )
        //
        // check for {
        // validate everything until matching }

        // we peek the next token. it should be a
        // name. we keep it in a variable because
        // TokenList doesn't have a method to get the current token
        Token variableToCompare = tokens.peek();
        // then we check if the token is a name.
        // this name is the variable's name
        expect(line, NAME);
        // then we check if the variable doesn't exist
        if (isSourceValid && !variableExists(variableToCompare)) {
            // if it doesn't exist, we print an error
            ErrorPrinter.printError(
                    source,
                    variableToCompare.getLine(),
                    0,
                    0,
                    "A14",
                    "Unknown variable: " + variableToCompare.getValue(),
                    "Try using a variable you already created"
            );

            // then we tell the validator that the source isn't valid
            isSourceValid = false;
            // and we finally leave this method
            return;
        }

        // we leave this method if the source
        // is not valid at this point.
        // this avoids a ClassCastException in the getVariableType method
        if (!isSourceValid) {
            return;
        }

        // if everything is valid up to here, we
        // get the variable's type
        TokenType type = getVariableType(variableToCompare);
        // and we check if the variable is a number
        if (!type.isNumber() && !type.equals(BOOLEAN)) {
            // if it isn't, we throw an error
            ErrorPrinter.printError(
                    source,
                    line,
                    0,
                    0,
                    "A15",
                    "Invalid variable type, got " + type.getFriendlyName() + " for variable \"" + variableToCompare.getValue() + '"',
                    "Try using a variable that can hold numbers"
            );

            // then we tell the validator that the source isn't valid
            isSourceValid = false;
            // and we finally leave this method
            return;
        }

        // we check if the variable is a number,
        // because the way we write the condition
        // depends on the type
        if (type.isNumber()) {
            // then we get the next token,
            // it should be a comparator
            Token comparatorToken = tokens.get();
            // and we check if the token's type is a comparator
            if (!comparatorToken.getType().isComparator()) {
                // if it isn't, we throw an error
                ErrorPrinter.printError(
                        source,
                        line,
                        0,
                        0,
                        "A12",
                        "Unexpected token: " + comparatorToken.getType().getFriendlyName(),
                        "Try using a comparator instead"
                );

                // then we tell the validator that the source isn't valid
                isSourceValid = false;
                // and we finally leave this method
                return;
            }

            // we then check for a number
            expect(line, NUMBER);
        }

        // then we check for a {
        expect(line, OPEN_CURLY_BRACKET);
        // and finally, we validate the block
        validateBlock();
    }

    private void validateMatch(int line) {
        // quick summary of what this is doing:
        //
        // check for name
        // check that name is a number
        // check for {
        // validate everything until matching }
        // validating the match block will have to be done
        // in a separate method as it's not written
        // the same way as a normal block

        // then we peek the next token. it should be a
        // name. we keep it in a variable because
        // TokenList doesn't have a method to get the current token
        Token matchVariable = tokens.peek();
        // then we check if the token is a name
        expect(line, NAME);
        // then we check if the variable is a number
        checkVariableIsNumber(matchVariable);

        // check for a {
        expect(line, OPEN_CURLY_BRACKET);

        // we validate the match block until the source isn't valid,
        // or until the corresponding close curly bracket has been read
        while (isSourceValid && !tokens.peek().getType().equals(CLOSE_CURLY_BRACKET)) {
            // we get the line from the token, because everything has
            // to be on the same line to be valid
            line = tokens.peek().getLine();
            // we then expect a number
            expect(line, NUMBER);
            // we also expect the '=>' arrow
            expect(line, MATCH_ARROW);
            // and finally, we expect the match block to open
            expect(line, OPEN_CURLY_BRACKET);
            // after that, we just validate the entire match
            validateBlock();
        }

        // we skip the }
        tokens.skip();
    }

    private void checkVariableIsNumber(Token token) {
        // we don't check the token if the
        // source is not valid
        if (!isSourceValid) {
            return;
        }

        // we get the token type from the variables map
        // using the token's value as the map's key
        TokenType variable = getVariableType(token);
        // if the variable type is null, that means
        // the variable does not exist
        if (variable == null) {
            // we can then throw an error
            ErrorPrinter.printError(
                    source,
                    token.getLine(),
                    0,
                    0,
                    "A14",
                    "Unknown variable: " + token.getValue(),
                    "Try using a variable you already created"
            );

            // we can tell the validator that
            // the source is not valid
            isSourceValid = false;
            // and we leave the method
            return;
        }

        // we check if the variable isn't a number
        if (!variable.isNumber()) {
            // if it isn't a number, we throw an error
            ErrorPrinter.printError(
                    source,
                    token.getLine(),
                    0,
                    0,
                    "A15",
                    "Invalid variable type, got " + variable.getFriendlyName() + " for variable \"" + token.getValue() + '"',
                    "Try using a variable that can hold numbers"
            );

            // we can tell the validator that
            // the source is not valid
            isSourceValid = false;
        }
    }

    /**
     * Check if a variable has been declared
     *
     * @param token The variable's {@code NAME} token
     * @return {@code true} if the variable has been defined previously<br>
     * {@code false} otherwise
     */
    private boolean variableExists(Token token) {
        return variables.containsKey((String) token.getValue());
    }

    /**
     * Return the variable's type
     *
     * @param token The variable's {@code NAME} token
     * @return {@link TokenType} if the variable has been declared<br>
     * {@code null} otherwise
     */
    private TokenType getVariableType(Token token) {
        return variables.get((String) token.getValue());
    }

    private void expect(int line, TokenType type) {
        // we don't check the token if the
        // source is not valid
        if (!isSourceValid) {
            return;
        }

        // we get the token, so we can compare its type
        // with the type given as a parameter
        Token token = tokens.get();
        // we check if they're not the same
        if (!token.getType().equals(type)) {
            // if they're not the same, we throw an error
            ErrorPrinter.printError(
                    source,
                    token.getLine(),
                    0,
                    0,
                    "A12",
                    "Unexpected token, got a " + token.getType().getFriendlyName() + " instead of a " + type.getFriendlyName(),
                    "Try using a " + type.getFriendlyName() + " instead of a " + token.getType().getFriendlyName()
            );

            // we tell the validator that the source isn't valid
            isSourceValid = false;
            // and we return because we don't have to check
            // if the token is in the correct line
            return;
        }

        // then we check if both tokens are on the same line
        if (token.getLine() != line) {
            // if they aren't, we throw an error
            ErrorPrinter.printError(
                    source,
                    token.getLine(),
                    0,
                    0,
                    "A13",
                    "Unexpected line break before the " + token.getType().getFriendlyName(),
                    "Remove the line break before the " + token.getType().getFriendlyName()
            );

            // and we tell the validator that the source isn't valid
            isSourceValid = false;
        }
    }

    public boolean isValid() {
        return isSourceValid;
    }
}

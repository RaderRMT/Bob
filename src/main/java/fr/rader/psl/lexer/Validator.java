package fr.rader.psl.lexer;

import fr.rader.psl.tokens.Token;
import fr.rader.psl.tokens.TokenList;
import fr.rader.psl.tokens.TokenType;
import fr.rader.utils.errors.Error;
import fr.rader.utils.errors.ErrorPrinter;

import java.io.File;
import java.util.HashMap;
import java.util.Stack;

import static fr.rader.psl.tokens.TokenType.*;

public class Validator {

    /** The list of tokens to validate */
    private final TokenList tokens;

    /** The PSL source */
    private final File source;

    /** This holds the variables in the current scope.
     * Once we leave a code block, we pop the stack.
     * The String is the variable's name.
     * The TokenType is the variable's type. */
    private final Stack<HashMap<String, TokenType>> variables;

    /** Used to tell the validator is the source is valid.
     * If this is false, it's going to stop validating */
    private boolean isSourceValid = true;

    public Validator(TokenList tokens, File source) {
        this.tokens = tokens;
        this.source = source;

        this.variables = new Stack<>();
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
        // is allowed. If there is a token that isn't EOF,
        // we then tell the validator that the source isn't valid
        if (isSourceValid && !tokens.peek().getType().equals(EOF)) {
            ErrorPrinter.newPrintError(
                    source,
                    tokens.peek().getLine(),
                    Error.A17
            );

            // and then tell the validator that
            // the source is invalid
            isSourceValid = false;
        }
    }

    private void validateBlock() {
        // add a new variable map to the variables stack
        HashMap<String, TokenType> scopeVariables = variables.push(new HashMap<>());

        // we loop through the code until the source
        // isn't invalid, or if we found the corresponding }
        while (isSourceValid) {
            // we get the token to check
            Token token = tokens.get();
            // we get the token type
            TokenType type = token.getType();

            // we leave the method is we find a }
            if (type.equals(CLOSE_CURLY_BRACKET)) {
                break;
            }

            // we get the token's line,
            // as everything has to be on
            // the same line
            int line = token.getLine();

            // we check if the token is a type
            if (type.isType()) {
                // if the token is a type,
                // we validate the variable
                validateVariable(line, type, scopeVariables);

                // and we continue
                continue;
            }

            // we check if the token is an array
            if (type.equals(ARRAY)) {
                // if the token is an array,
                // we validate the array
                validateArray(line, scopeVariables);

                // and we continue
                continue;
            }

            // we check if the token is an if
            if (type.equals(IF)) {
                // if it is, we validate the condition
                validateCondition(line);

                // and we continue
                continue;
            }

            // we check if the token is a match
            if (type.equals(MATCH)) {
                // if it is, we validate the match
                validateMatch(line);

                // and we continue
                continue;
            }

            // if the token we're reading isn't expected,
            // we throw an error
            Error error = Error.A18;
            ErrorPrinter.newPrintError(
                    source,
                    line,
                    error.getErrorID(),
                    error.formatErrorMessage(type.getFriendlyName()),
                    error.formatSolutions(type.getFriendlyName())
            );

            // then we tell the validator that the source isn't valid
            isSourceValid = false;
        }

        // then we pop our scope variables
        // because we're leaving the scope
        variables.pop();
    }

    private void validateVariable(int line, TokenType type, HashMap<String, TokenType> scopeVariables) {
        // quick summary of what this is doing:
        //
        // peek for [
        // if [ (
        //     check for [
        //     check for name
        //     check if name is in the variables stack.
        //         if it is, we check its type
        //     check for ]
        // )
        //
        // check for name
        // check for semicolon
        // add variable to variables stack

        // check if we're declaring an array
        if (tokens.peek().getType().equals(OPEN_SQUARE_BRACKET)) {
            // if we are declaring an array,
            // we check for an open square bracket
            expect(line, OPEN_SQUARE_BRACKET);

            // then we peek the next token. it must be a name.
            // we keep it in a variable because TokenList doesn't
            // have a method to get the current token
            Token arrayLengthVariable = tokens.peek();
            // then we check if the token is a name.
            // this is the array's length
            expect(line, NAME);
            // then we check if the array length variable is a number
            checkIfVariableIsNumber(arrayLengthVariable);

            // then we check for a close square bracket
            expect(line, CLOSE_SQUARE_BRACKET);

            // if the source is not valid at this point,
            // we leave this method
            if (!isSourceValid) {
                return;
            }

            // if the source is still valid,
            // we change the type to an array
            // because that's what the variable is now
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
            // if the variable already exists,
            // we throw an error
            Error error = Error.A16;
            ErrorPrinter.newPrintError(
                    source,
                    line,
                    error.getErrorID(),
                    error.formatErrorMessage((String) variableName.getValue()),
                    error.getSolutions()
            );

            // then we tell the validator that the source isn't valid
            isSourceValid = false;
            // and we finally leave this method
            return;
        }

        // is everything is still valid,
        // we check if the next token is a semicolon
        expect(line, SEMICOLON);

        // we check if the source is valid
        // if it isn't, we don't want to add the
        // variable to the stack because we don't even know
        // if it's the correct type or not
        if (isSourceValid) {
            // we add the variable to the
            // variables stack if everything is ok
            scopeVariables.put(
                    (String) variableName.getValue(),
                    type
            );
        }
    }

    private void validateArray(int line, HashMap<String, TokenType> scopeVariables) {
        // quick summary of what this is doing:
        //
        // check for (
        // check for name
        // check for ,
        // check for name
        // check for )
        // check for {
        // add variable to variables stack
        // validate everything until the matching }

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
        checkIfVariableIsNumber(arrayLengthVariable);

        // we then check for a comma
        expect(line, COMMA);

        // then we peek the next token. it should be a
        // name. we keep it in a variable because
        // TokenList doesn't have a method to get the current token
        Token arrayName = tokens.peek();
        // then we check if the token is a name.
        // this name is the array's name
        expect(line, NAME);
        // then we check if the variable already exists.
        // we skip the check if the source is invalid
        if (isSourceValid && variableExists(arrayName)) {
            // if the variable already exists,
            // we throw an error
            Error error = Error.A16;
            ErrorPrinter.newPrintError(
                    source,
                    line,
                    error.getErrorID(),
                    error.formatErrorMessage((String) arrayName.getValue()),
                    error.getSolutions()
            );

            // then we tell the validator that the source isn't valid
            isSourceValid = false;
            // and we finally leave this method
            return;
        }

        // if it's still valid,
        // we check for a )
        expect(line, CLOSE_PAREN);
        // and finally, we check for a {
        expect(line, OPEN_CURLY_BRACKET);

        // we then check if the source is valid.
        // if it isn't, we don't want to add
        // the variable to the variables stack
        if (isSourceValid) {
            // if the source is still valid,
            // we add the variable to the variables stack
            scopeVariables.put(
                    (String) arrayName.getValue(),
                    ARRAY
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
            Error error = Error.A14;
            ErrorPrinter.newPrintError(
                    source,
                    variableToCompare.getLine(),
                    error.getErrorID(),
                    error.formatErrorMessage((String) variableToCompare.getValue()),
                    error.getSolutions()
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
            Error error = Error.A15;
            ErrorPrinter.newPrintError(
                    source,
                    line,
                    error.getErrorID(),
                    error.formatErrorMessage(type.getFriendlyName(), (String) variableToCompare.getValue()),
                    error.getSolutions()
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
                Error error = Error.A12;
                ErrorPrinter.newPrintError(
                        source,
                        line,
                        error.getErrorID(),
                        error.formatErrorMessage(comparatorToken.getType().getFriendlyName()),
                        error.getSolutions()
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
        // in a different way as it's not written
        // the same way as a normal block

        // then we peek the next token. it should be a
        // name. we keep it in a variable because
        // TokenList doesn't have a method to get the current token
        Token matchVariable = tokens.peek();
        // then we check if the token is a name
        expect(line, NAME);
        // then we check if the variable is a number
        checkIfVariableIsNumber(matchVariable);

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

    private void checkIfVariableIsNumber(Token token) {
        // we don't check the token
        // if the source isn't valid
        if (!isSourceValid) {
            return;
        }

        // we get the token type from the variables stack
        // using the token's value as the key
        TokenType variable = getVariableType(token);
        // if the variable type is null, that means
        // the variable doesn't exist
        if (variable == null) {
            // if the variable doesn't exist,
            // we throw an error
            Error error = Error.A14;
            ErrorPrinter.newPrintError(
                    source,
                    token.getLine(),
                    error.getErrorID(),
                    error.formatErrorMessage((String) token.getValue()),
                    error.getSolutions()
            );

            // we can tell the validator that
            // the source is not valid
            isSourceValid = false;
            // and we leave the method
            return;
        }

        // we check if the variable isn't a number
        if (!variable.isNumber()) {
            // if the variable isn't a number,
            // we throw an error
            Error error = Error.A15;
            ErrorPrinter.newPrintError(
                    source,
                    token.getLine(),
                    error.getErrorID(),
                    error.formatErrorMessage(variable.getFriendlyName(), (String) token.getValue()),
                    error.getSolutions()
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
        return getVariableType(token) != null;
    }

    /**
     * Return the variable's type
     *
     * @param token The variable's {@code NAME} token
     * @return {@link TokenType} if the variable has been declared<br>
     * {@code null} otherwise
     */
    private TokenType getVariableType(Token token) {
        String key = (String) token.getValue();

        // loop through all the scopes
        for (HashMap<String, TokenType> scopeVariables : variables) {
            // if the scope contains the variable
            if (scopeVariables.containsKey(key)) {
                // we return the type
                return scopeVariables.get(key);
            }
        }

        // if the variable doesn't exist,
        // we return null
        return null;
    }

    private void expect(int line, TokenType type) {
        // we don't check the token if
        // the source is not valid
        if (!isSourceValid) {
            return;
        }

        // we get the token, so we can compare its type
        // with the type given as a parameter
        Token token = tokens.get();
        // we check if they're not the same
        if (!token.getType().equals(type)) {
            // if they're not the same, we throw an error
            Error error = Error.A12;
            ErrorPrinter.newPrintError(
                    source,
                    token.getLine(),
                    error.getErrorID(),
                    error.formatErrorMessage(token.getType().getFriendlyName(), type.getFriendlyName()),
                    error.formatSolutions(type.getFriendlyName())
            );

            // we tell the validator that the source isn't valid
            isSourceValid = false;
            // and we return because we don't have to check
            // if the token is in the correct line
            return;
        }

        // then we check if the token is on the correct line
        if (token.getLine() != line) {
            // if it isn't on the correct line,
            // we throw an error
            Error error = Error.A13;
            ErrorPrinter.newPrintError(
                    source,
                    token.getLine(),
                    error.getErrorID(),
                    error.formatErrorMessage(token.getType().getFriendlyName()),
                    error.formatSolutions(token.getType().getFriendlyName())
            );

            // and we tell the validator that the source isn't valid
            isSourceValid = false;
        }
    }

    public boolean isValid() {
        return isSourceValid;
    }
}

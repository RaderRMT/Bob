package fr.rader.psl.lexer;

import fr.rader.utils.errors.ErrorPrinter;
import fr.rader.utils.io.SourceStream;
import fr.rader.psl.tokens.Token;
import fr.rader.psl.tokens.TokenList;
import fr.rader.psl.tokens.TokenType;
import fr.rader.utils.CharacterUtils;
import fr.rader.utils.StringUtils;

import java.io.File;
import java.io.IOException;

import static fr.rader.psl.tokens.TokenType.*;

public class Lexer {

    /** The PSL file */
    private final File file;

    /** The PSL source code */
    private final SourceStream source;

    /** The list of tokens (filled when scanning the tokens) */
    private final TokenList tokens;

    /** Holds whether an error occurred */
    private boolean hasError = false;

    /** Keeps track of the character position in the line */
    private int characterPositionInLine = 0;

    /** Keeps track of the line we're lexing */
    private int lineCounter = 1;

    /** Keeps track of the brace level.
     * '{' adds one and '}' removes one */
    private int braceLevel = 0;

    public Lexer(File file) throws IOException {
        this.file = file;

        this.source = new SourceStream(file);
        this.tokens = new TokenList();
    }

    public TokenList scanTokens() throws IOException {
        // we read the source until we're at the end,
        // or until an error is thrown
        while (!hasError && source.hasNext()) {
            scanToken();
        }

        // check if we don't have any errors and the brace
        // level isn't at 0
        if (!hasError && braceLevel != 0) {
            // if the brace level isn't at 0, then we print an error
            ErrorPrinter.printSimpleError(
                    "A03",
                    "Invalid brace level at the end of file",
                    null
            );

            // we then tell the lexer that an error occurred
            hasError = true;
        }

        // we add the end of file packet
        addToken(EOF);

        // then we return the tokens we read
        return tokens;
    }

    public void scanToken() throws IOException {
        // fetch the next character
        char c = source.next();

        // fancy switch case for our character
        switch (c) {
            // skip spaces, carriage returns and tabs
            case ' ':
            case '\r':
            case '\t':
                break;

            // count lines
            case '\n':
                // increment the line counter
                lineCounter++;
                // setting the character position to 0
                characterPositionInLine = 0;
                // return because we don't want to
                // increment the character position.
                return;

            // add the parenthesis in the tokens list
            case '(':
                addToken(OPEN_PAREN);
                break;
            case ')':
                addToken(CLOSE_PAREN);
                break;

            // add the square bracket in the tokens list
            case '[':
                addToken(OPEN_SQUARE_BRACKET);
                break;
            case ']':
                addToken(CLOSE_SQUARE_BRACKET);
                break;

            // add commas to the tokens list
            case ',':
                addToken(COMMA);
                break;

            // add semicolons to the tokens list
            case ';':
                addToken(SEMICOLON);
                break;

            // check for <, <=, > and >=
            case '<':
                addToken(nextCharIs('=') ? LESS_EQUAL_SIGN : LESS_SIGN);
                break;
            case '>':
                addToken(nextCharIs('=') ? GREATER_EQUAL_SIGN : GREATER_SIGN);
                break;

            // check for bang equal
            case '!':
                // check if the next char is an equal sign to form a bang equal
                if (nextCharIs('=')) {
                    // if it is, we add the bang equal to the tokens list.
                    // nextCharIs() will consume the equal sign
                    addToken(BANG_EQUAL_SIGN);
                } else {
                    // if it isn't, we print an error
                    ErrorPrinter.printError(
                            file,
                            lineCounter,
                            characterPositionInLine,
                            1,
                            "A08",
                            "Unexpected bang",
                            "Try with \"!=\""
                    );

                    // we then tell the lexer that an error occurred
                    hasError = true;
                    // we return because we don't need to
                    // do anything else
                    return;
                }

                break;

            // check for equal_equal and match arrow
            case '=':
                // check if the next char is an equal sign
                // to form an equal_equal token
                if (nextCharIs('=')) {
                    // if it is, we add the equal_equal token in the tokens list
                    addToken(EQUAL_EQUAL_SIGN);
                } else if (nextCharIs('>')) {
                    // check if the next char is a greater sign
                    // to form an equal_equal token
                    // if it is, we add the match arrow token in the tokens list
                    addToken(MATCH_ARROW);
                } else {
                    // if it's not a valid character, we print an error
                    ErrorPrinter.printError(
                            file,
                            lineCounter,
                            characterPositionInLine,
                            1,
                            "A09",
                            "Incomplete equal form",
                            "Try with \"==\" or \"=>\""
                    );

                    // we then tell the lexer that an error occurred
                    hasError = true;
                    // we return because we don't need to
                    // do anything else
                    return;
                }

                break;

            // count brace levels
            case '{':
                // we increment the brace level
                braceLevel++;
                // and we add the curly bracket in the tokens list
                addToken(OPEN_CURLY_BRACKET);
                break;
            case '}':
                // if the brace level is at 0, and we're trying to
                // reduce it even more, then we throw an error
                if (braceLevel == 0) {
                    ErrorPrinter.printError(
                            file,
                            lineCounter,
                            characterPositionInLine,
                            1,
                            "A04",
                            "Curly bracket resulting in negative brace level",
                            "Remove the curly bracket"
                    );

                    // we then tell the lexer that an error occurred
                    hasError = true;
                    // we return because we don't need to
                    // do anything else
                    return;
                }

                // if everything is fine, we just decrease the brace level
                braceLevel--;
                // and we then add the curly bracket in the tokens list
                addToken(CLOSE_CURLY_BRACKET);
                break;

            case '"':
                // we read a name,
                // it's basically a string delimited by double quotes
                readName();
                break;

            // removing the comments.
            // comments are either:
            //      - one line comments (using // at the start of the comment)
            //      - multiple lines comments (using /* at the start and */ at the end of the
            //              comment)
            case '/':
                if (nextCharIs('/')) {
                    // as this is a single line comment, we can read
                    // up to the next like, or to the end of the file
                    // if someone decides to put a comment at the end
                    while (source.hasNext() && !nextCharIs('\n')) {
                        source.skip();
                    }
                } else if (nextCharIs('*')) {
                    // as this is a multiple lines comment, we skip
                    // the characters until we find */ (the end of our comment)
                    while (source.hasNext()) {
                        if (nextCharIs('*') && nextCharIs('/')) {
                            // we don't have to do anything here because nextCharIs will
                            // consume the character if it's the same as the one passed as the
                            // argument, so we just break out of the while loop
                            break;
                        }

                        source.skip();
                    }
                } else {
                    ErrorPrinter.printError(
                            file,
                            lineCounter,
                            characterPositionInLine,
                            1,
                            "A10",
                            "Unexpected slash",
                            "Try with \"//\" or \"/*\""
                    );

                    // we then tell the lexer that an error occurred
                    hasError = true;
                    // we return because we don't need to
                    // do anything else
                    return;
                }

                break;

            default:
                // if the character is a lowercase letter
                if (CharacterUtils.isAlphaLower(c)) {
                    // we then read an identifier
                    readIdentifier();

                    // we check if the identifier is followed by a space,
                    // a square bracket or a parenthesis
                    if (source.peek(SourceStream.NEXT) != ' ' &&
                            source.peek(SourceStream.NEXT) != '[' &&
                            source.peek(SourceStream.NEXT) != '(') {
                        // if the identifier isn't followed by
                        // one of those characters, we print an error
                        ErrorPrinter.printError(
                                file,
                                lineCounter,
                                characterPositionInLine,
                                1,
                                "A05",
                                "Expected space after identifier",
                                "Add a space at the end of the identifier"
                        );

                        // we then tell the lexer that an error occurred
                        hasError = true;
                        // we return because we don't need to
                        // do anything else
                        return;
                    }
                } else if (CharacterUtils.isDigit(c)) {
                    // if the character is a digit,
                    // we read the number from the source
                    readNumber();
                } else {
                    // if the character isn't a lowercase letter or a digit,
                    // then we print an error
                    ErrorPrinter.printError(
                            file,
                            lineCounter,
                            characterPositionInLine,
                            1,
                            "A01",
                            "Unexpected character: \"" + c + '"',
                            "Try removing the character"
                    );

                    // we then tell the lexer that an error occurred
                    hasError = true;
                    // we return because we don't need to
                    // do anything else
                    return;
                }
        }

        // increment the character position.
        // this now points to the next character.
        // if we call scanToken() again, it'll point
        // to source.next()
        characterPositionInLine++;
    }

    private void readIdentifier() throws IOException {
        // the first character of the identifier is the current character from the source
        String identifier = String.valueOf(source.peek(SourceStream.CURRENT));

        // while the source has characters
        while (source.hasNext()) {
            // we check if it's a letter (ignoring the case)
            if (CharacterUtils.isAlpha(source.peek(SourceStream.NEXT))) {
                // if it is a letter, we append it to the identifier
                identifier += source.next();
            } else {
                // if it isn't a letter, we break from the loop
                break;
            }
        }

        // we get the token type from the keywords list
        TokenType type = Keywords.get(identifier);
        // if the identifier doesn't exist, then we print an error
        if (type == null) {
            ErrorPrinter.printError(
                    file,
                    lineCounter,
                    characterPositionInLine,
                    identifier.length(),
                    "A02",
                    "Unknown keyword: " + identifier,
                    "Use a valid keyword"
            );

            // as keywords can't have uppercase letters,
            // we show a solution telling the user to remove uppercase letters
            if (StringUtils.hasUpperCaseLetters(identifier)) {
                ErrorPrinter.printSolution("Use lowercase letters for keywords");
            }

            // we then tell the lexer that an error occurred
            hasError = true;
            // then we return from the function
            return;
        }

        // if everything is fine, we then
        // increment the character position
        characterPositionInLine += identifier.length();

        // if everything is fine, we add the token type
        // to the tokens list
        addToken(type);
    }

    private void readNumber() throws IOException {
        // the first character of the number is the current character from the source
        String number = String.valueOf(source.peek(SourceStream.CURRENT));

        // while the source has characters
        while (source.hasNext()) {
            // we check if it's a digit
            if (CharacterUtils.isDigit(source.peek(SourceStream.NEXT))) {
                // if it is a letter, we append it to the number
                number += source.next();
            } else {
                // if it isn't a digit, we break from the loop
                break;
            }
        }

        try {
            // we add the number token to the tokens list
            addToken(NUMBER, Integer.parseInt(number));
        } catch (NumberFormatException e) {
            // if the integer is too big, we print an error
            ErrorPrinter.printError(
                    file,
                    lineCounter,
                    characterPositionInLine,
                    number.length(),
                    "A11",
                    "Number too big",
                    "Make sure that the number isn't above 2,147,483,647"
            );

            // we then tell the lexer that an error occurred
            hasError = true;
            // then we return from the function
            return;
        }

        // we increment the character position
        // by the length of our number string
        characterPositionInLine += number.length();
    }

    private void readName() throws IOException {
        // creating a string to hold the name
        String name = "";

        // iterate though the source while it still has characters
        while (source.hasNext()) {
            // we store the next character in a variable
            // I don't want to do a call to the same method with the same
            // parameters twice
            char next = source.peek(SourceStream.NEXT);

            // we check if the name is unterminated
            if (next == '\n') {
                // if the name is unterminated,
                // we print an error
                ErrorPrinter.printError(
                        file,
                        lineCounter,
                        characterPositionInLine,
                        0,
                        "A07",
                        "Unterminated name",
                        "Add a double quote at the end of the name"
                );

                // we then tell the lexer that an error occurred
                hasError = true;
                // then we return from the function
                return;
            }

            // if we're still reading the string,
            // we append the character until we reach a double quote
            if (source.peek(SourceStream.NEXT) != '"') {
                name += source.next();
            } else {
                // break out of the while loop if we reached a double quote
                break;
            }
        }

        // consuming the closing double quote
        source.skip();

        // then we check if the name is in snake_case
        // the name has to match this regex: [a-z_]+
        if (!StringUtils.isSnakeCase(name)) {
            // if the name isn't in snake case,
            // we print an error
            ErrorPrinter.printError(
                    file,
                    lineCounter,
                    characterPositionInLine,
                    name.length(),
                    "A06",
                    "Names must be in snake_case",
                    "Change all uppercase letters to lowercase"
            );

            ErrorPrinter.printSolution("Use underscores for spaces");
            ErrorPrinter.printSolution("Make sure the name does not contain numbers");

            // we then tell the lexer that an error occurred
            hasError = true;
            // then we return from the function
            return;
        }

        // if everything is fine, we then
        // increment the character position
        // we add one because it's the double quote we consumed above
        characterPositionInLine += name.length() + 1;

        // and finally we add the name to the tokens list
        addToken(NAME, name);
    }

    /**
     * Look at the next character to see if it matches the one we're passing in as the parameter
     *
     * @param nextCharacter character to look for
     * @return true if both characters match<br>
     * false otherwise
     * @throws IOException if an I/O error occurs.
     */
    private boolean nextCharIs(char nextCharacter) throws IOException {
        if (source.peek(SourceStream.NEXT) != nextCharacter) {
            return false;
        }

        source.skip();
        characterPositionInLine++;
        return true;
    }

    /**
     * Create and add a new token to {@link Lexer#tokens}, value will be null
     *
     * @param tokenType the type of the token to add
     */
    private void addToken(TokenType tokenType) {
        addToken(tokenType, null);
    }

    /**
     * Create and add a new token to {@link Lexer#tokens}
     *
     * @param tokenType the type of the token to add
     * @param value     the value of the token
     */
    private void addToken(TokenType tokenType, Object value) {
        this.tokens.add(new Token(tokenType, value, lineCounter));
    }

    /**
     * Get the state of the PSL file.
     *
     * @return true if the file contains an error, false otherwise.
     */
    public boolean hasError() {
        return hasError;
    }
}

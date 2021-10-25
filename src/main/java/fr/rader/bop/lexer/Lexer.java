package fr.rader.bop.lexer;

import fr.rader.bop.tokens.Token;
import fr.rader.bop.tokens.TokenType;
import fr.rader.utils.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.rader.bop.tokens.TokenType.*;

public class Lexer {

    private final SourceStream source;

    private final List<Token> tokens;

    private int line = 1;

    public Lexer(File file) throws IOException {
        this.source = new SourceStream(file);
        this.tokens = new ArrayList<>();
    }

    public List<Token> scanTokens() throws IOException {
        while (source.hasNext()) {
            scanToken();
        }

        addToken(EOF);
        return tokens;
    }

    private void scanToken() throws IOException {
        char c = source.next();

        switch (c) {
                // skip spaces, tabs and carriage returns
            case ' ':
            case '\r':
            case '\t':
                break;

                // count lines
            case '\n':
                line++;
                break;

                // checking for parenthesis, curly and square brackets
                // and adding them as tokens in the token list
            case '(':
                addToken(OPEN_PAREN);
                break;
            case ')':
                addToken(CLOSE_PAREN);
                break;
            case '{':
                addToken(OPEN_CURLY_BRACKET);
                break;
            case '}':
                addToken(CLOSE_CURLY_BRACKET);
                break;
            case '[':
                addToken(OPEN_SQUARE_BRACKET);
                break;
            case ']':
                addToken(CLOSE_SQUARE_BRACKET);
                break;

                // same for commas and semicolons
            case ',':
                addToken(COMMA);
                break;
            case ';':
                addToken(SEMICOLON);
                break;

            case '!':
                addToken(nextCharIs('=') ? BANG_EQUAL : BANG);
                break;

            case '=':
                if (nextCharIs('=')) {
                    addToken(EQUAL_EQUAL);
                } else if (nextCharIs('>')) {
                    addToken(MATCH_ARROW);
                } else {
                    Logger.error(
                            "Missing character on line "
                                    + line
                                    + ": '=' must be followed by either '=' or '>'.");
                }
                break;

            case '<':
                addToken(nextCharIs('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(nextCharIs('=') ? GREATER_EQUAL : GREATER);
                break;

                // removing the comments.
                // comments are either:
                //      - one line comments (using // at the start of the comment)
                //      - multiple lines comments (using /* at the start and */ at the end of the
                // comment)
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
                            // argument
                            // so we just break out of the while loop
                            break;
                        }

                        source.skip();
                    }
                }
                break;

                // variable names
            case '"':
                name();
                break;

            default:
                if (isAlpha(c)) {
                    identifier();
                } else if (isDigit(c)) {
                    number();
                } else {
                    Logger.error("Unexpected character on line " + line + ": '" + c + "'");
                }
        }
    }

    /**
     * Reads an number from the source file
     * @throws IOException if an I/O error occurs.
     */
    private void number() throws IOException {
        String number = String.valueOf(source.peek(SourceStream.CURRENT));
        while (source.hasNext() && isDigit(source.peek(SourceStream.NEXT))) {
            number += source.next();
        }

        addToken(NUMBER, Integer.parseInt(number));
    }

    /**
     * Reads an name from the source file
     * @throws IOException if an I/O error occurs.
     */
    private void name() throws IOException {
        String name = "";
        while (source.hasNext() && source.peek(SourceStream.NEXT) != '"') {
            name += source.next();
        }

        // consume the closing double quote
        source.skip();

        addToken(NAME, name);
    }

    /**
     * Reads an identifier from the source file
     * @throws IOException if an I/O error occurs.
     */
    private void identifier() throws IOException {
        String identifier = String.valueOf(source.peek(SourceStream.CURRENT));
        while (source.hasNext() && isAlpha(source.peek(SourceStream.NEXT))) {
            identifier += source.next();
        }

        TokenType type = Keywords.get(identifier);
        if (type == null) {
            type = IDENTIFIER;
        }

        addToken(type);
    }

    /**
     * Look if the character is a letter, so if it's hex value is greater than 'a' and smaller than
     * 'z' or if it's greater than 'A' and smaller than 'Z'
     *
     * @param c the character to check
     * @return true if it's a number<br>
     *     false otherwise
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * Look if the character is a digit, so if it's hex value is greater than '0' and smaller than
     * '9'
     *
     * @param c the character to check
     * @return true if it's a number<br>
     *     false otherwise
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Look at the next character to see if it matches the one we're passing in as the parameter
     *
     * @param nextCharacter character to look for
     * @return true if both characters match<br>
     *     false otherwise
     * @throws IOException if an I/O error occurs.
     */
    private boolean nextCharIs(char nextCharacter) throws IOException {
        if (source.peek(SourceStream.NEXT) != nextCharacter) {
            return false;
        }

        source.skip();

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
     * @param value the value of the token
     */
    private void addToken(TokenType tokenType, Object value) {
        this.tokens.add(new Token(tokenType, value));
    }
}

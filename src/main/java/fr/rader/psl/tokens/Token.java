package fr.rader.psl.tokens;

public class Token {

    private final TokenType type;
    private final Object value;

    private final int line;

    public Token(TokenType type, int line) {
        this.value = null;
        this.type = type;
        this.line = line;
    }

    public Token(TokenType type, Object value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }
}

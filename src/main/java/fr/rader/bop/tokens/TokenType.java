package fr.rader.bop.tokens;

public enum TokenType {

    OPEN_PAREN(0),              // '('
    CLOSE_PAREN(0),             // ')'
    OPEN_CURLY_BRACKET(0),      // '{'
    CLOSE_CURLY_BRACKET(0),     // '}'
    OPEN_SQUARE_BRACKET(0),     // '['
    CLOSE_SQUARE_BRACKET(0),    // ']'

    COMMA(0),                   // ','
    SEMICOLON(0),               // ';'

    BANG_EQUAL(2),              // '!='
    EQUAL_EQUAL(2),             // '=='
    GREATER(2),                 // '>'
    GREATER_EQUAL(2),           // '>='
    LESS(2),                    // '<'
    LESS_EQUAL(2),              // '<='

    // types:
    BOOLEAN(1),                 // 1 byte
    BYTE(1),                    // 1 byte
    SHORT(1),                   // 2 bytes
    INT(1),                     // 4 bytes
    LONG(1),                    // 8 bytes
    STRING(1),                  // Depends on previous data
    FLOAT(1),                   // 4 bytes
    DOUBLE(1),                  // 8 bytes
    VARINT(1),                  // 1 - 5 bytes
    VARLONG(1),                 // 1 - 10 bytes

    METADATA(1),                // Size depends on previous data
    CHAT(1),                    // Size depends on previous data
    IDENTIFIER(1),              // Size depends on previous data
    NBT(1),                     // Size depends on previous data
    POSITION(1),                // 8 bytes
    ANGLE(1),                   // 1 byte
    UUID(1),                    // 16 bytes

    ARRAY(0),                   // Size depends on previous data

    NUMBER(0),

    PACKET(0),                  // used to start a new packet, e.g. `packet "packet_name" { ... }`
    NAME(0),                    // name of the "variable" we're going to read , e.g. `int "test";`
                                //   (here, NAME would be "test")

    MATCH(0),
    MATCH_ARROW(0),             // =>

    IF(0),
    FALSE(0),
    TRUE(0),

    EOF(0);

    private static final byte TYPE = 1;
    private static final byte COMPARATOR = 2;

    private final int tokenType;

    TokenType(int tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isType() {
        return this.tokenType == TYPE;
    }

    public boolean isComparator() {
        return this.tokenType == COMPARATOR;
    }
}

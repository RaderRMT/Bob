package fr.rader.psl.tokens;

import static fr.rader.psl.tokens.TokenKind.*;

public enum TokenType {

    OPEN_PAREN              (KIND_NONE),                // '('
    CLOSE_PAREN             (KIND_NONE),                // ')'
    OPEN_CURLY_BRACKET      (KIND_NONE),                // '{'
    CLOSE_CURLY_BRACKET     (KIND_NONE),                // '}'
    OPEN_SQUARE_BRACKET     (KIND_NONE),                // '['
    CLOSE_SQUARE_BRACKET    (KIND_NONE),                // ']'

    COMMA                   (KIND_NONE),                // ','
    SEMICOLON               (KIND_NONE),                // ';'

    BANG_EQUAL_SIGN         (KIND_COMPARATOR),          // '!='
    EQUAL_EQUAL_SIGN        (KIND_COMPARATOR),          // '=='
    GREATER_SIGN            (KIND_COMPARATOR),          // '>'
    GREATER_EQUAL_SIGN      (KIND_COMPARATOR),          // '>='
    LESS_SIGN               (KIND_COMPARATOR),          // '<'
    LESS_EQUAL_SIGN         (KIND_COMPARATOR),          // '<='

    // types:
    BOOLEAN                 (KIND_TYPE),                // 1 byte
    BYTE                    (KIND_TYPE | KIND_ARRAY_INDEX),  // 1 byte
    SHORT                   (KIND_TYPE | KIND_ARRAY_INDEX),  // 2 bytes
    INT                     (KIND_TYPE | KIND_ARRAY_INDEX),  // 4 bytes
    LONG                    (KIND_TYPE),                // 8 bytes
    STRING                  (KIND_TYPE),                // Depends on previous data
    FLOAT                   (KIND_TYPE),                // 4 bytes
    DOUBLE                  (KIND_TYPE),                // 8 bytes
    VARINT                  (KIND_TYPE | KIND_ARRAY_INDEX),  // 1 - 5 bytes
    VARLONG                 (KIND_TYPE),                // 1 - 10 bytes

    METADATA                (KIND_TYPE),                // Size depends on previous data
    CHAT                    (KIND_TYPE),                // Size depends on previous data
    NBT                     (KIND_TYPE),                // Size depends on previous data
    POSITION                (KIND_TYPE),                // 8 bytes
    ANGLE                   (KIND_TYPE),  // 1 byte
    UUID                    (KIND_TYPE),                // 16 bytes

    ARRAY                   (KIND_NONE),                // Size depends on previous data

    NUMBER                  (KIND_NONE),

    PACKET                  (KIND_NONE),                // used to start a new packet, e.g. `packet "packet_name" { ... }`
    NAME                    (KIND_NONE),                // name of the "variable" we're going to read , e.g. `int "test";`
                                                        //   (here, NAME would be "test")

    MATCH                   (KIND_NONE),
    MATCH_ARROW             (KIND_NONE),                // =>

    IF                      (KIND_NONE),
    FALSE                   (KIND_NONE),
    TRUE                    (KIND_NONE),

    EOF                     (KIND_NONE);

    private final int tokenType;

    TokenType(int tokenType) {
        this.tokenType = tokenType;
    }

    public String getFriendlyName() {
        return this.name().toLowerCase().replace('_', ' ');
    }

    public boolean isType() {
        return (this.tokenType & KIND_TYPE) != 0;
    }

    public boolean isNumber() {
        return (this.tokenType & KIND_ARRAY_INDEX) != 0;
    }

    public boolean isComparator() {
        return (this.tokenType & KIND_COMPARATOR) != 0;
    }
}

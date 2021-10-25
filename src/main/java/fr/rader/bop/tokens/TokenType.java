package fr.rader.bop.tokens;

public enum TokenType {

    OPEN_PAREN,             // '('
    CLOSE_PAREN,            // ')'
    OPEN_CURLY_BRACKET,     // '{'
    CLOSE_CURLY_BRACKET,    // '}'
    OPEN_SQUARE_BRACKET,    // '['
    CLOSE_SQUARE_BRACKET,   // ']'

    COMMA,                  // ','
    SEMICOLON,              // ';'

    BANG,                   // '!'
    BANG_EQUAL,             // '!='
    EQUAL_EQUAL,            // '=='
    GREATER,                // '>'
    GREATER_EQUAL,          // '>='
    LESS,                   // '<'
    LESS_EQUAL,             // '<='

    // types:
    BOOLEAN,                // 1 byte
    BYTE,                   // 1 byte
    SHORT,                  // 2 bytes
    INT,                    // 4 bytes
    LONG,                   // 8 bytes
    STRING,                 // Depends on previous data
    FLOAT,                  // 4 bytes
    DOUBLE,                 // 8 bytes
    VARINT,                 // 1 - 5 bytes
    VARLONG,                // 1 - 10 bytes

    METADATA,               // Size depends on previous data
    CHAT,                   // Size depends on previous data
    IDENTIFIER,             // Size depends on previous data
    NBT,                    // Size depends on previous data
    ARRAY,                  // Size depends on previous data
    POSITION,               // 8 bytes
    ANGLE,                  // 1 byte
    UUID,                   // 16 bytes

    NUMBER,

    PACKET,                 // used to start a new packet, e.g. `packet 70 { ... }`
    NAME,                   // name of the "variable" we're going to read , e.g. `int "test";`
                            //   (here, NAME would be "test")

    MATCH,
    MATCH_ARROW, // =>

    IF,
    FALSE,
    TRUE,

    EOF
}

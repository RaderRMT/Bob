package fr.rader.psl.lexer;

import fr.rader.psl.tokens.TokenType;

import java.util.HashMap;
import java.util.Map;

import static fr.rader.psl.tokens.TokenType.*;

public class Keywords {

    // a map containing the keyword and it's associated token
    private static final Map<String, TokenType> keywords = new HashMap<>();

    public static TokenType get(String keyword) {
        return keywords.get(keyword);
    }

    static {
        keywords.put("boolean", BOOLEAN);
        keywords.put("byte", BYTE);
        keywords.put("short", SHORT);
        keywords.put("int", INT);
        keywords.put("long", LONG);
        keywords.put("string", STRING);
        keywords.put("float", FLOAT);
        keywords.put("double", DOUBLE);
        keywords.put("varint", VARINT);
        keywords.put("varlong", VARLONG);
        keywords.put("position", POSITION);
        keywords.put("metadata", METADATA);
        keywords.put("chat", CHAT);
        keywords.put("nbt", NBT);
        keywords.put("angle", ANGLE);
        keywords.put("array", ARRAY);
        keywords.put("uuid", UUID);
        keywords.put("packet", PACKET);
        keywords.put("match", MATCH);
        keywords.put("if", IF);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
    }
}

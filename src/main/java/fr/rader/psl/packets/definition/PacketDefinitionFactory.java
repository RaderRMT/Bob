package fr.rader.psl.packets.definition;

import fr.rader.psl.lexer.Lexer;
import fr.rader.psl.packets.definition.rules.*;
import fr.rader.psl.tokens.Token;
import fr.rader.psl.tokens.TokenList;
import fr.rader.psl.validator.Validator;
import fr.rader.utils.OS;
import fr.rader.utils.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.rader.psl.tokens.TokenType.*;

public class PacketDefinitionFactory {

    public static PacketDefinition createPacketDefinition(int protocol, int packetID) throws IOException {
        Logger.info("Using the new and improved lexer for '" + packetID + ".psl' from protocol " + protocol);

        // get the source file depending on
        // the protocol and the packet id
        File source = OS.getPSL(protocol, packetID);
        // we create an instance of our lexer
        // to scan our source file
        Lexer lexer = new Lexer(source);
        // we then scan the source file.
        // the lexer has some error checks, but
        // we'll feed the tokens in a validator
        // to further check for errors
        TokenList tokens = lexer.scanTokens();

        // check if the lexer found no errors
        if (!lexer.hasError()) {
            // if no errors were found, we feed the
            // tokens to the validator for a more complete check
            Logger.info("Validating PSL using the new and improved validator...");
            // we create an instance of our Validator,
            // and we give it the tokens from the lexer
            // and the source file to print errors
            Validator validator = new Validator(tokens, source);
            // we then validate the tokens
            validator.validate();

            // here, we check if the tokens are valid
            // if the tokens are valid, that means the
            // psl source is valid, and we can create
            // the packet definition
            if (validator.isValid()) {
                // if everything is valid, we reset
                // the tokens index to the start of the TokenList,
                Logger.info("PSL is valid, generating Packet Definition");
                tokens.reset();

                // and we return a new instance of a PacketDefinition
                return new PacketDefinition(generateRules(tokens), packetID);
            }
        }

        // if the source file isn't valid
        // (so if the lexer found an error
        // or if the validator found an error)
        // we return null
        Logger.error("Invalid PSL");
        return null;
    }

    private static List<Rule> generateRules(TokenList tokenList) {
        // we create a list of rules for the brace level we're on
        List<Rule> rules = new ArrayList<>();

        // we loop through the tokens list until we find the matching '}'
        while (!tokenList.peek().getType().equals(CLOSE_CURLY_BRACKET)) {
            // we get the token, as we were peeking
            // the next token in our while condition
            Token token = tokenList.get();

            // we put the type in a switch statement
            switch (token.getType()) {
                // check the token
                case ARRAY:
                    // we generate an array rule,
                    // and we add it to the rules list
                    rules.add(generateArrayRule(tokenList));
                    break;

                // check the token is an if
                case IF:
                    // we generate a condition rule,
                    // and we add it to the rules list
                    rules.add(generateConditionRule(tokenList));
                    break;

                // check the token is a match
                case MATCH:
                    // we generate a match rule,
                    // and we add it to the rules list
                    rules.add(generateMatchRule(tokenList));
                    break;

                // if the token is neither an array,
                // an if nor a match, we know it's a variable
                default:
                    // we check if the variable is an array
                    // by peeking the next token and checking
                    // if it's a '['
                    if (tokenList.peek().getType().equals(OPEN_SQUARE_BRACKET)) {
                        // if it's an array, we generate a simple array rule,
                        // and we add it to the rules list
                        rules.add(generateSimpleArrayRule(tokenList, token));
                    } else {
                        // if it's not an array, we generate a variable rule,
                        // and we add it to the rules list
                        rules.add(generateVariableRule(tokenList, token));
                    }

                    break;
            }
        }

        // we skip the closing curly bracket
        tokenList.skip();

        // and we return the rules
        return rules;
    }

    private static ArrayRule generateArrayRule(TokenList tokenList) {
        // arrays have a special syntax:
        //
        //      array(<arrayLength>, <arrayName>) {
        //          // structure
        //      }
        //
        // we have to skip the '(' to know the array length
        // we then have to skip the comma to know the array name
        // and finally, we skip ')' and the '{'
        tokenList.skip(); // skip '('
        // we get the array length
        String arrayLength = (String) tokenList.get().getValue();
        tokenList.skip(); // skip ','
        // we get the array name
        String arrayName = (String) tokenList.get().getValue();

        // we create a new instance of an array rule
        ArrayRule array = new ArrayRule(
                arrayLength,    // array length
                arrayName       // array name
        );

        // here, we skip the final ')' and '{'
        tokenList.skip();
        tokenList.skip();

        // we then generate the rules for the array contents
        array.setRules(generateRules(tokenList));

        // and we finally return the array rule
        return array;
    }

    private static ConditionRule generateConditionRule(TokenList tokenList) {
        // condition use the same syntax as Rust:
        //
        //      if <variable> <sign> <value> {
        //          // structure
        //      }
        //
        // they also have this syntax:
        //
        //      if <variable> {
        //          // structure
        //      }
        //
        // we can get the variable just fine,
        // but the end of the condition depends
        // on what token is after the variable

        // we read the variable
        String variable = (String) tokenList.get().getValue();

        // we declare the condition rule variable
        ConditionRule condition;
        // we check if the condition is for a boolean
        if (tokenList.peek().getType().equals(OPEN_CURLY_BRACKET)) {
            // if it is, we create a new instance of a condition rule,
            // and we give it the variable. as it's a boolean, we check
            // if it's value equals one
            condition = new ConditionRule(
                    variable,           // the boolean variable
                    EQUAL_EQUAL_SIGN,   // "==", because we want to check if it's true
                    1                   // 1, because that's considered "true" for a boolean
            );
        } else {
            // if it is not, we create a new instance of a condition rule,
            // and we give it the variable, the sign and value
            condition = new ConditionRule(
                    variable,                               // the variable to compare
                    tokenList.get().getType(),              // the sign
                    (Integer) tokenList.get().getValue()    // the value to compare the variable to
            );
        }

        // we skip the '{'
        tokenList.skip();

        // we then generate the rules for the condition contents
        condition.setRules(generateRules(tokenList));

        // and we finally return the condition rule
        return condition;
    }

    private static MatchRule generateMatchRule(TokenList tokenList) {
        // matches use the same syntax as Rust:
        //
        //      match <variable> {
        //          <number> => {
        //              // structure
        //          }
        //
        //          ...
        //      }
        //
        // we can get the variable just fine.
        // before reading the rules for each
        // entry in the match, we first have to skip
        // the '{'. after that, we enter a while
        // loop that reads the number, skip '=>' and '{'
        // and generate the rules for that number

        // we create an instance of a match rule with the variable
        MatchRule match = new MatchRule((String) tokenList.get().getValue());

        // we skip the '{'
        tokenList.skip();
        // we loop through the token list until we find the matching '}'
        while (!tokenList.peek().getType().equals(CLOSE_CURLY_BRACKET)) {
            // we get the number (<number> in the syntax written above)
            int number = (Integer) tokenList.get().getValue();

            // we skip the '=>' and '{'
            tokenList.skip();
            tokenList.skip();

            // and we read and add the rules for our value
            match.setRulesForValue(
                    number,                     // the number to match
                    generateRules(tokenList)    // the list of rules
            );
        }

        // we skip the '}'
        tokenList.skip();

        // and we return the match rule
        return match;
    }

    private static SimpleArrayRule generateSimpleArrayRule(TokenList tokenList, Token type) {
        // a simple array doesn't have a special syntax, it looks like this:
        //
        //      <type>[<length>] <name>;
        //
        // as there's no way to get the type,
        // we have to get it from the method's parameter
        // to get the length, we have to first skip the '[',
        // and to get to the name, we have to first skip the ']'.
        // after that, we can skip the semicolon

        // we skip the '['
        tokenList.skip();

        // we get the array length
        String simpleArrayLength = (String) tokenList.get().getValue();
        // we skip the ']'
        tokenList.skip();
        // we get the array name
        String simpleArrayName = (String) tokenList.get().getValue();

        // we create an instance of a simple array rule,
        // and we feed it the type, the length and the name
        SimpleArrayRule simpleArrayRule = new SimpleArrayRule(
                type.getType(),     // the array type
                simpleArrayLength,  // the array length
                simpleArrayName     // the array name
        );

        // we then skip the semicolon
        tokenList.skip();

        // and we return the simple array rule
        return simpleArrayRule;
    }

    private static VariableRule generateVariableRule(TokenList tokenList, Token type) {
        // a variable doesn't have a special syntax, it looks like this:
        //
        //      <type> <name>;
        //
        // there are no challenges here,
        // so this method is pretty short

        // we create an instance of a variable rule,
        // and we feed it the type and the name of our variable
        VariableRule variableRule = new VariableRule(
                type.getType(),                     // the variable type
                (String) tokenList.get().getValue() // the variable name
        );

        // we skip the semicolon
        tokenList.skip();

        // and we return the variable rule
        return variableRule;
    }
}

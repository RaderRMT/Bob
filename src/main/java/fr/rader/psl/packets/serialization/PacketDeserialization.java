package fr.rader.psl.packets.serialization;

import fr.rader.psl.packets.Packet;
import fr.rader.psl.packets.definition.PacketDefinition;
import fr.rader.psl.packets.definition.rules.*;
import fr.rader.psl.packets.serialization.entries.*;
import fr.rader.types.VarInt;
import fr.rader.utils.data.DataReader;

import java.io.IOException;
import java.util.*;

public class PacketDeserialization {

    private static final Stack<HashMap<String, Object>> variablesStack = new Stack<>();

    /**
     * Deserialize raw bytes from the {@link DataReader} to a {@link Packet} object based on the {@link PacketDefinition} object.
     *
     * @param definition the {@link PacketDefinition}
     * @param reader the {@link DataReader} that holds the data
     * @return the deserialized {@link Packet}
     * @throws IOException if an I/O error occurs.
     */
    public static Packet deserialize(PacketDefinition definition, DataReader reader) throws IOException {
        Packet packet = new Packet();

        // deserialize the packet from
        // the rules in the definition
        packet.setEntry(deserializeCodeBlockFromRules(
                // the rules
                definition.getRules(),
                // the reader holding the data
                reader
        ));

        return packet;
    }

    /**
     * Deserialize a code block from the list of {@link Rule} given as a parameter
     *
     * @param rules the set of rules used to tell how to deserialize the code block
     * @param reader the {@link DataReader} holding the data
     * @return a list of {@link PacketEntry} deserialized from the reader
     * @throws IOException if an I/O error occurs.
     */
    private static List<PacketEntry> deserializeCodeBlockFromRules(List<Rule> rules, DataReader reader) throws IOException {
        // the entries in our code block
        List<PacketEntry> entries = new ArrayList<>();

        // this map holds the variables in our code block,
        // so we know which variables haven't been declared
        Map<String, Object> variablesInCodeBlock = variablesStack.push(new HashMap<>());

        // we loop through our rules and
        // deserialize them based on their class
        for (Rule rule : rules) {
            // check if our rule declares a variable definition
            if (rule instanceof VariableRule) {
                // if it does, we deserialize the variable
                deserializeVariable(rule, reader, entries, variablesInCodeBlock);
            }

            // check if our rule declares an array definition
            if (rule instanceof ArrayRule) {
                // if it does, we deserialize the array
                deserializeArray(rule, reader, entries);
            }

            // check if our rule declares a simple array definition
            if (rule instanceof SimpleArrayRule) {
                // if it does, we deserialize the simple array
                deserializeSimpleArray(rule, reader, entries);
            }

            // check if our rule declares a condition definition
            if (rule instanceof ConditionRule) {
                // if it does, we deserialize the condition
                deserializeCondition(rule, reader, entries);
            }

            // check if our rule declares a match definition
            if (rule instanceof MatchRule) {
                // if it does, we deserialize the match
                deserializeMatch(rule, reader, entries);
            }
        }

        // we pop the stack because we're done
        // reading the code block
        variablesStack.pop();

        return entries;
    }

    /**
     * Deserialize a match from the {@link Rule}'s data
     *
     * @param rule the variable's rule containing the variable name and type
     * @param reader the {@link DataReader} holding the data
     * @param entries the list of entries in our packet
     * @throws IOException if an I/O error occurs.
     */
    private static void deserializeMatch(Rule rule, DataReader reader, List<PacketEntry> entries) throws IOException {
        // we cast our rule back to a MatchRule
        MatchRule matchRule = (MatchRule) rule;

        // get value from the variable
        int value = getValueFromVariable(matchRule.getVariable());

        // we create an instance of our match entry
        MatchEntry matchEntry = new MatchEntry();

        // we get the entries based on the variable value
        List<Rule> matchPacketEntries = matchRule.getRulesForValue(value);
        if (matchPacketEntries != null) {
            // if the value has been defined in the match,
            // we deserialize it and store it in the match entry
            matchEntry.setEntries(
                    deserializeCodeBlockFromRules(
                            matchPacketEntries,
                            reader
                    )
            );
        }

        // finally, we add the array entry to the entries
        entries.add(matchEntry);
    }

    /**
     * Deserialize a variable from the {@link Rule}'s data
     *
     * @param rule the variable's rule containing the variable name and type
     * @param reader the {@link DataReader} holding the data
     * @param entries the list of entries in our packet
     * @param variablesInCodeBlock the variables in the code block
     * @throws IOException if an I/O error occurs.
     */
    private static void deserializeVariable(Rule rule, DataReader reader, List<PacketEntry> entries, Map<String, Object> variablesInCodeBlock) throws IOException {
        // we cast our rule back to a VariableRule
        VariableRule variableRule = (VariableRule) rule;

        // we create our VariableEntry
        VariableEntry entry = new VariableEntry(
                // the variable's name from the rule
                variableRule.getName(),
                // we read the data based on the token type
                reader.readFromTokenType(variableRule.getType())
        );

        // we add the VariableEntry to the entries list
        entries.add(entry);

        // we add the variable to the variables in code block list,
        // so we know it has been declared
        variablesInCodeBlock.put(
                // the variable's name
                entry.getName(),
                // the variable's value
                entry.getValue()
        );
    }

    /**
     * Deserialize an array from the {@link Rule}'s data
     *
     * @param rule the variable's rule containing the variable name and type
     * @param reader the {@link DataReader} holding the data
     * @param entries the list of entries in our packet
     * @throws IOException if an I/O error occurs.
     */
    private static void deserializeArray(Rule rule, DataReader reader, List<PacketEntry> entries) throws IOException {
        // we cast our rule back to an ArrayRule
        ArrayRule arrayRule = (ArrayRule) rule;

        // get value from the variable
        int value = getValueFromVariable(arrayRule.getLengthVariable());

        // we create an instance of our array entry
        // it'll hold every entry defined in it's rules
        ArrayEntry arrayEntry = new ArrayEntry(arrayRule.getName());
        // then we loop for the
        // total size of our array.
        // the length is defined in the value variable
        for (int i = 0; i < value; i++) {
            arrayEntry.setEntriesForIndex(
                    // this is the array entry index
                    i,
                    // this is the set of entry for the index
                    deserializeCodeBlockFromRules(
                            arrayRule.getRules(),
                            reader
                    )
            );
        }

        // finally, we add the array entry to the entries
        entries.add(arrayEntry);
    }

    /**
     * Deserialize a simple array from the {@link Rule}'s data
     *
     * @param rule the variable's rule containing the variable name and type
     * @param reader the {@link DataReader} holding the data
     * @param entries the list of entries in our packet
     * @throws IOException if an I/O error occurs.
     */
    private static void deserializeSimpleArray(Rule rule, DataReader reader, List<PacketEntry> entries) throws IOException {
        // we cast our rule back to a SimpleArrayRule
        SimpleArrayRule simpleArrayRule = (SimpleArrayRule) rule;

        // get value from the variable
        int value = getValueFromVariable(simpleArrayRule.getLengthVariable());

        // we create an instance of our simple array entry
        SimpleArrayEntry simpleArrayEntry = new SimpleArrayEntry(simpleArrayRule.getName());
        // then we loop for the
        // total size of our array.
        // the length is defined in the value variable
        for (int i = 0; i < value; i++) {
            simpleArrayEntry.setVariable(
                    // this is the array entry index
                    i,
                    // this is the set of entry for the index
                    reader.readFromTokenType(simpleArrayRule.getType())
            );
        }

        // finally, we add the array entry to the entries
        entries.add(simpleArrayEntry);
    }

    /**
     * Deserialize a condition from the {@link Rule}'s data
     *
     * @param rule the variable's rule containing the variable name and type
     * @param reader the {@link DataReader} holding the data
     * @param entries the list of entries in our packet
     * @throws IOException if an I/O error occurs.
     */
    private static void deserializeCondition(Rule rule, DataReader reader, List<PacketEntry> entries) throws IOException {
        // we cast our rule back to a ConditionRule
        ConditionRule conditionRule = (ConditionRule) rule;

        // get value from the variable
        int value = getValueFromVariable(conditionRule.getVariable());

        // we create an instance of the condition entry
        // it'll hold a list of entry if the branch is taken.
        // is the branch isn't taken, the list will be empty
        ConditionEntry conditionEntry = new ConditionEntry();
        if (conditionRule.isBranchTaken(value)) {
            // if the branch is taken, we deserialize
            // the branch rules from the condition rule
            conditionEntry.setEntries(
                    deserializeCodeBlockFromRules(
                            conditionRule.getBranchRules(),
                            reader
                    )
            );
        }

        // finally, we add the array entry to the entries
        entries.add(conditionEntry);
    }

    private static int getValueFromVariable(String variable) {
        // getting the variable from the variables stack
        Object variableValue = getVariableValue(variable);
        // if the variable doesn't exist, we throw an exception.
        // this should never happen, but it's still nice to have nonetheless
        if (variableValue == null) {
            throw new IllegalStateException("Undefined variable: " + variable);
        }

        // this is the variable's value as an int.
        int value;
        if (variableValue instanceof VarInt) {
            // we get the value if it's from a VarInt
            value = ((VarInt) variableValue).getValue();
        } else {
            // we get the value from the object
            value = Integer.parseInt(variableValue.toString());
        }

        return value;
    }

    private static Object getVariableValue(String variable) {
        for (HashMap<String, Object> blockVariables : variablesStack) {
            if (blockVariables.containsKey(variable)) {
                return blockVariables.get(variable);
            }
        }

        return null;
    }
}

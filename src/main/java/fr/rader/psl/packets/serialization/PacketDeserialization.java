package fr.rader.psl.packets.serialization;

import fr.rader.psl.packets.Packet;
import fr.rader.psl.packets.definition.PacketDefinition;
import fr.rader.psl.packets.definition.rules.*;
import fr.rader.psl.packets.entries.*;
import fr.rader.types.VarInt;
import fr.rader.utils.data.DataReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class PacketDeserialization {

    private static final Stack<HashMap<String, Object>> variables = new Stack<>();

    public static Packet deserialize(PacketDefinition definition, DataReader reader) throws IOException {
        Packet packet = new Packet();

        packet.setEntry(deserializeEntry(definition.getRules(), reader));

        return packet;
    }

    private static List<PacketEntry> deserializeEntry(List<Rule> rules, DataReader reader) throws IOException {
        List<PacketEntry> entries = new ArrayList<>();

        HashMap<String, Object> blockVariables = variables.push(new HashMap<>());

        for (Rule rule : rules) {
            if (rule instanceof VariableRule) {
                deserializeVariable(rule, reader, entries, blockVariables);
            }

            if (rule instanceof ArrayRule) {
                deserializeArray(rule, reader, entries);
            }

            if (rule instanceof ConditionRule) {
                deserializeCondition(rule, reader, entries);
            }

            if (rule instanceof SimpleArrayRule) {
                deserializeSimpleArray(rule, reader, entries);
            }

            if (rule instanceof MatchRule) {
                deserializeMatch(rule, reader, entries);
            }
        }

        variables.pop();

        return entries;
    }

    private static void deserializeArray(Rule rule, DataReader reader, List<PacketEntry> entries) throws IOException {
        ArrayRule arrayRule = (ArrayRule) rule;

        String lengthVariable = arrayRule.getLengthVariable();

        Object variableValue = getVariableValue(lengthVariable);
        if (variableValue == null) {
            throw new IllegalStateException("Undefined variable: " + lengthVariable);
        }

        int length = ((VarInt) variableValue).getValue();

        ArrayEntry arrayEntry = new ArrayEntry(length, arrayRule.getName());

        for (int i = 0; i < length; i++) {
            arrayEntry.setEntriesForIndex(i, deserializeEntry(arrayRule.getRules(), reader));
        }

        entries.add(arrayEntry);
    }

    private static void deserializeCondition(Rule rule, DataReader reader, List<PacketEntry> entries) throws IOException {
        ConditionRule conditionRule = (ConditionRule) rule;

        String variable = conditionRule.getVariable();
        Object variableValue = getVariableValue(variable);
        if (variableValue == null) {
            throw new IllegalStateException("Undefined variable: " + variable);
        }

        int value = (int) variableValue;

        ConditionEntry conditionEntry = new ConditionEntry();

        if (conditionRule.isBranchTaken(value)) {
            conditionEntry.setEntries(deserializeEntry(conditionRule.getBranchRules(), reader));
        }

        entries.add(conditionEntry);
    }

    private static void deserializeSimpleArray(Rule rule, DataReader reader, List<PacketEntry> entries) throws IOException {
        SimpleArrayRule simpleArrayRule = (SimpleArrayRule) rule;

        String lengthVariable = simpleArrayRule.getLengthVariable();

        Object variableValue = getVariableValue(lengthVariable);
        if (variableValue == null) {
            throw new IllegalStateException("Undefined variable: " + lengthVariable);
        }

        int length = (int) variableValue;

        SimpleArrayEntry simpleArrayEntry = new SimpleArrayEntry(length, simpleArrayRule.getName());

        for (int i = 0; i < length; i++) {
            simpleArrayEntry.addVariable(new VariableEntry(
                    null,
                    reader.readFromTokenType(simpleArrayRule.getType())
            ));
        }

        entries.add(simpleArrayEntry);
    }

    private static void deserializeMatch(Rule rule, DataReader reader, List<PacketEntry> entries) throws IOException {
        MatchRule matchRule = (MatchRule) rule;

        String variable = matchRule.getVariable();

        Object variableValue = getVariableValue(variable);
        if (variableValue == null) {
            throw new IllegalStateException("Undefined variable: " + variable);
        }

        int value = ((VarInt) variableValue).getValue();

        MatchEntry matchEntry = new MatchEntry(value);

        List<Rule> matchPacketEntries = matchRule.getRulesForValue(value);
        if (matchPacketEntries != null) {
            matchEntry.setEntries(deserializeEntry(matchPacketEntries, reader));
        }

        entries.add(matchEntry);

    }

    private static void deserializeVariable(Rule rule, DataReader reader, List<PacketEntry> entries, HashMap<String, Object> blockVariables) throws IOException {
        VariableRule variableRule = (VariableRule) rule;

        VariableEntry entry = new VariableEntry(
                variableRule.getName(),
                reader.readFromTokenType(variableRule.getType())
        );

        entries.add(entry);

        blockVariables.put(entry.getName(), entry.getValue());
    }

    private static Object getVariableValue(String variable) {
        for (HashMap<String, Object> blockVariables : variables) {
            if (blockVariables.containsKey(variable)) {
                return blockVariables.get(variable);
            }
        }

        return null;
    }
}

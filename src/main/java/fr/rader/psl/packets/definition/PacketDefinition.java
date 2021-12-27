package fr.rader.psl.packets.definition;

import fr.rader.psl.packets.definition.rules.Rule;

import java.util.List;

public class PacketDefinition {

    /** The list of rules that describes
     * the format of our packet */
    private final List<Rule> rules;

    /** The name of the packet */
    private final String packetName;
    /** The ID of the packet
     * this PacketDefinition defines */
    private final int packetID;

    PacketDefinition(List<Rule> rules, int packetID) {
        // the first rule is always a Variable Rule,
        // with a type of PACKET, and it's name is the packet name,
        // so we get the name from the first rule
        this.packetName = rules.get(0).getName();
        // and we then remove it from the rules
        rules.remove(0);

        // we then keep the rules and the packet id
        this.rules = rules;
        this.packetID = packetID;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public String getPacketName() {
        return packetName;
    }

    public int getPacketID() {
        return packetID;
    }
}

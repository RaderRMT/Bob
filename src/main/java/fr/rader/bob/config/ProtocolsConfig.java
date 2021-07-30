package fr.rader.bob.config;

import fr.rader.bob.utils.OS;

import java.io.IOException;

public class ProtocolsConfig {

    private final Config config;

    public ProtocolsConfig() throws IOException {
        this.config = new Config(OS.getBobFolder() + "resources/assets/protocols/", "protocols.nbt");
    }

    public void addUselessPacket(int packetID) {
        // todo: once i made a class that holds project-related information
        //  like the current protocol, etc...
    }

    public void removeUselessPacket(int packetID) {
        // todo: once i made a class that holds project-related information
        //  like the current protocol, etc...
    }

    public boolean isPacketUseless(int packetID) {
        // todo: once i made a class that holds project-related information
        //  like the current protocol, etc...
        //  this might require a HashMap<String, List<Integer>> that will hold
        //  a list of integers (packet ids) per protocol (string key in hashmap).
        //  that might require a function to turn the protocols.nbt into the
        //  hashmap and vice-versa.
        return false;
    }

    public void save() {
        config.save();
    }
}

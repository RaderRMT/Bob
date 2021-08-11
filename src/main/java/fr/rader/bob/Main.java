package fr.rader.bob;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Bob bob = new Bob();
            bob.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

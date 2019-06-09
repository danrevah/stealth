package com.openu.security.stealth.app.Sockets;

import java.util.ArrayList;

public class Database {

    private static ArrayList<ServerDetails> relays = new ArrayList<>();

    public static synchronized void addRelay(ServerDetails relay) {
        relays.add(relay);
    }

    public static synchronized ArrayList<ServerDetails> getRelays() {
        return relays;
    }

    public static synchronized void resetRelays() {
        relays = new ArrayList<>();
    }
}

package com.openu.security.tor.app.Sockets;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static ArrayList<ServerDetails> relays = new ArrayList<>();

    public static synchronized void addRelay(ServerDetails relay) {
        relays.add(relay);
    }

    public static synchronized ArrayList<ServerDetails> getRelays() {
        return relays;
    }
}

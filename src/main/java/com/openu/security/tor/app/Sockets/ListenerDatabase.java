package com.openu.security.tor.app.Sockets;

import java.util.ArrayList;
import java.util.List;

public class ListenerDatabase {

    private static List<ServerDetails> relays = new ArrayList<>();

    public static synchronized void addRelay(ServerDetails relay) {
        relays.add(relay);
    }

    public static synchronized List<ServerDetails> getRelays() {
        return relays;
    }
}

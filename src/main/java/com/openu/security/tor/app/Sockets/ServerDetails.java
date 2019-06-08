package com.openu.security.tor.app.Sockets;

import java.security.PublicKey;

public class ServerDetails {
    private int port;
    private PublicKey publicKey;
    private String host;

    public ServerDetails(String host, int port, PublicKey publicKey) {
        this.host = host;
        this.port = port;
        this.publicKey = publicKey;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}

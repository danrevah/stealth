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

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}

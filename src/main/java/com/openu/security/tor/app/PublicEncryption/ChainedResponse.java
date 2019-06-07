package com.openu.security.tor.app.PublicEncryption;

import com.openu.security.tor.app.Sockets.ServerDetails;

public class ChainedResponse {
    private final String encryptedPacket;
    private final ServerDetails firstRelay;

    public ChainedResponse(String encryptedPacket, ServerDetails firstRelay) {
        this.encryptedPacket = encryptedPacket;
        this.firstRelay = firstRelay;
    }

    public String getEncryptedPacket() {
        return encryptedPacket;
    }

    public ServerDetails getFirstRelay() {
        return firstRelay;
    }
}
package com.openu.security.tor.app.Services.TrustedServer;

import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Encryption.KeyHelper;
import com.openu.security.tor.app.Services.Config;
import com.openu.security.tor.app.Services.Service;
import com.openu.security.tor.app.Sockets.ListenerSocket;

/**
 * TrustedServer
 *
 * TrustedServer application
 *  - 1. Listening to port for incoming connections
 *  - 2. Decrypting packet with TrustedServer privateKey.
 *  - 3. IF (Packet = 'ADD_RELAY')
 *  - 3.1. This means it's a new relay which will be added to the list of relays
 *  - 4. IF (Packet = 'GET_RELAYS [AMOUNT]')
 *  - 4.1. This means it's a client, returning [AMOUNT] relays back.
 *  - 5. TrustedServer also act's as a relay, which mean it can be used with 'ROUTE'
 *       commands to route message, this can be used when a user don't want to expose
 *       it's IP to the initial relay.
 *       (even tho the relay can't tell if it's the client, or another relay which
 *       is sending this packet).
 */
public class TrustedServer implements Service {

    private ListenerSocket server;

    public TrustedServer() throws Exception {
        this.server = new ListenerSocket("0.0.0.0", Config.TRUSTED_SERVER_PORT, KeyHelper.base64ToPrivateKey(Keys.PRIVATE_KEY));
        Logger.info("TrustedServer listening for connections...");
    }

    public void execute() throws Exception {
        this.server.listen();
    }
}
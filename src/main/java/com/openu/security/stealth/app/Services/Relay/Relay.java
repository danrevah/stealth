package com.openu.security.stealth.app.Services.Relay;

import com.openu.security.stealth.app.Encryption.KeyHelper;
import com.openu.security.stealth.app.Encryption.KeyPairs;
import com.openu.security.stealth.app.Services.Service;
import com.openu.security.stealth.app.Sockets.ClientSocket;
import com.openu.security.stealth.app.Services.Config;
import com.openu.security.stealth.app.Sockets.ListenerSocket;

/**
 * Relay
 *
 * Relay application
 *  - 1. Listening to port for incoming connections
 *  - 2. Connecting and adding itself to the TrustedServer relay's list
 *  - 3. Sending the port number that the Relay have started listening to (to the TrustedServer)
 *  - 4. Waiting for incoming connections (from client OR other relays).
 *  - 5. Incoming connection
 *  - 6. EncryptedPacket is being decrypted by relays private key
 *  - 6.1. IF (EncryptedPacket = 'ROUTE [IP] [PORT] [ENC_MESSAGE]')
 *  - 6.1.1. Sending the [ENC_MESSAGE] to [IP]:[PORT] (next relay in line)
 *  - 6.2. IF (EncryptedPacket = 'HTTP_GET_REQUEST [URL] [CLIENT_PUBLIC_KEY]')
 *  - 6.2.1. Sending HTTP Get request to [URL]
 *  - 6.2.2. Encrypted response with [CLIENT_PUBLIC_KEY]
 *  - 6.2.3. Returning encrypted response to previous connection (relay).
 */
public class Relay implements Service {

    private ClientSocket clientSocket;
    private ListenerSocket serverSocket;
    private KeyPairs keyPairs;

    public Relay() throws Exception {
        this.keyPairs = new KeyPairs();
        this.keyPairs.generateKeyPair();

        this.clientSocket = new ClientSocket(Config.TRUSTED_SERVER_HOST, Config.TRUSTED_SERVER_PORT, this.keyPairs,
                KeyHelper.base64ToPublicKey(Config.TRUSTED_SERVER_PUBLIC_KEY));

        this.serverSocket = new ListenerSocket("0.0.0.0", 0, this.keyPairs.getPrivateKey());
    }

    public void execute() throws Exception {
        this.clientSocket.addRelay(this.serverSocket.getPort(), this.keyPairs.getPublicKey());
        this.serverSocket.listen();
    }
}

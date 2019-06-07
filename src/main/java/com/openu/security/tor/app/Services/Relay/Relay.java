package com.openu.security.tor.app.Services.Relay;

import com.openu.security.tor.app.Logger.LogLevel;
import com.openu.security.tor.app.PublicEncryption.KeyPairs;
import com.openu.security.tor.app.Services.Config;
import com.openu.security.tor.app.Services.Service;
import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Sockets.ClientSocket;
import com.openu.security.tor.app.Sockets.ListenerSocket;

import java.net.ServerSocket;

public class Relay implements Service {

    private ClientSocket clientSocket;
    private ListenerSocket serverSocket;
    private KeyPairs keyPairs;

    public Relay() throws Exception {

        this.keyPairs = new KeyPairs();
        this.keyPairs.generateKeyPair();

        // @TODO: Move converting functions to static or a different class
        KeyPairs publicOnlyKeyPair = new KeyPairs();
        publicOnlyKeyPair.setPublicKeyFromBase64(Config.TRUSTED_SERVER_PUBLIC_KEY);

        this.clientSocket = new ClientSocket(Config.TRUSTED_SERVER_HOST, Config.TRUSTED_SERVER_PORT, this.keyPairs, publicOnlyKeyPair.getPublicKey());
        this.serverSocket = new ListenerSocket("127.0.0.1", 0, this.keyPairs.getPrivateKey());
    }

    public void execute() throws Exception {
        this.clientSocket.addRelay(this.serverSocket.getPort(), this.keyPairs.getPublicKey());

        this.serverSocket.listen();
    }
}
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

    private int instanceAmount;
    private ClientSocket clientSocket;
    private ListenerSocket serverSocket;
    private KeyPairs keyPairs;

    public Relay(int instanceAmount) throws Exception {
        this.instanceAmount = instanceAmount;

        this.keyPairs = new KeyPairs();
        this.keyPairs.generateKeyPair();

        this.clientSocket = new ClientSocket(Config.TRUSTED_SERVER_HOST, Config.TRUSTED_SERVER_PORT, this.keyPairs);
        this.serverSocket = new ListenerSocket("127.0.0.1", 0, this.keyPairs.getPrivateKey());
    }

    public void execute() throws Exception {
        if (instanceAmount < 1 || instanceAmount > 10) {
            Logger.log(LogLevel.Error, "Relay instances in parallel must be in range of 1-10.");
            return;
        }

        Logger.log(LogLevel.Info, "Launching " + instanceAmount + " relays..");


        this.clientSocket.addRelay(this.serverSocket.getPort(), this.keyPairs.getPublicKey());
        // @TODO: Start from client!!! has many things in common!

        // @TODO: 2. Use threads to run this function multiple times.. with different ports (randomized)!
        // @TODO: 3. Listen to connections in-case of `HTTP_GET_REQUEST` decrypt
        //           If no more encrypted messages -> make GET Request to server
        //              + encrypt response with PUBLIC_KEY of client
        //
        //           If there is -> take next IP:PORT in line and send 'HTTP_GET_REQUEST' to him
        //              + wait for response in order to return to the connection
        //              + no need to encrypt response here
    }
}
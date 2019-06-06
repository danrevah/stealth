package com.openu.security.tor.app.Services.Client;

import com.openu.security.tor.app.Logger.LogLevel;
import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Services.Config;
import com.openu.security.tor.app.Services.Service;
import com.openu.security.tor.app.Sockets.ClientSocket;

import java.util.Scanner;

public class Client implements Service {

    private int chainLength;
    private int instanceAmount;
    private ClientSocket clientSocket;
    private Scanner scanner;

    public Client(int instanceAmount, int chainLength) throws Exception {
        this.chainLength = chainLength;
        this.instanceAmount = instanceAmount;

        this.clientSocket = new ClientSocket(Config.TRUSTED_SERVER_HOST, Config.TRUSTED_SERVER_PORT);
        this.scanner = new Scanner(System.in);

        Logger.log(LogLevel.Info,
            "<Client> Connected to Trusted Server (" + Config.TRUSTED_SERVER_HOST + ":" + Config.TRUSTED_SERVER_PORT + ")"
        );
    }

    public void execute() throws Exception {
        if (instanceAmount != 1) {
            throw new Exception("Client service cannot be executed in parallel mode.");
        }

        String input;

        while (true) {
            System.out.print("URL for HTTP Get request (CTRL+C to stop): ");
            input = scanner.nextLine();

            // @TODO: validate HTTP and not HTTPS?
            // @TODO: validate URL?

            // Send get Relays
            this.clientSocket.getRelays();
            // @TODO: 1. Get relays to list!
            // @TODO: 2. Write a method that builds a chain of encrypted messages and sends to Relay! with `Protocol.HTTP_GET_REQUEST`
            // @TODO: 3. Send encrypted message to relay
        }

        // @TODO: Next: TrustedServer, since relay is very depdended on it also..
    }
}
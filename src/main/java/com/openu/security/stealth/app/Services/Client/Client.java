package com.openu.security.stealth.app.Services.Client;

import com.openu.security.stealth.app.Encryption.ChainedResponse;
import com.openu.security.stealth.app.Encryption.KeyHelper;
import com.openu.security.stealth.app.Logger.Logger;
import com.openu.security.stealth.app.Validator.Validator;
import com.openu.security.stealth.app.Encryption.ChainedEncryption;
import com.openu.security.stealth.app.Encryption.KeyPairs;
import com.openu.security.stealth.app.Services.Config;
import com.openu.security.stealth.app.Services.Service;
import com.openu.security.stealth.app.Sockets.ClientSocket;
import com.openu.security.stealth.app.Sockets.ServerDetails;

import java.util.Scanner;

/**
 * Client
 *
 * Client application
 *  - 1. Receives URL from user
 *  - 2. Getting a list of Relay's from the TrustedServer
 *  - 3. Randomly building a relay chain
 *  - 4. Encrypting GET Request, so that only the exit relay could read.
 *  - 5. Each relay is adding an additional encryption layer,
 *       which means a relay only knows the next relay in chain,
 *       but is unaware of the length of the route, 
 *       or the message itself (unless it's the exit relay).
 *  - 6. Send chained encrypted packet to first relay
 *  - 7. Waiting for response and decrypting with the private key.
 */
public class Client implements Service {

    private int chainLength;
    private ClientSocket clientSocket;
    private Scanner scanner;
    private KeyPairs keyPairs;

    public Client(int chainLength) throws Exception {
        this.chainLength = chainLength;
        this.keyPairs = new KeyPairs();
        this.clientSocket = new ClientSocket(Config.TRUSTED_SERVER_HOST, Config.TRUSTED_SERVER_PORT, this.keyPairs,
                KeyHelper.base64ToPublicKey(Config.TRUSTED_SERVER_PUBLIC_KEY));
        this.scanner = new Scanner(System.in);

        Logger.info(
            "<Client> Connected to Trusted Server (" + Config.TRUSTED_SERVER_HOST + ":" + Config.TRUSTED_SERVER_PORT + ")"
        );
    }

    public void execute() throws Exception {
        String input;

        while (true) {
            System.out.print("URL for HTTP Get request (CTRL+C to stop): ");
            input = scanner.nextLine();

            if (!Validator.isUrlValid(input)) {
                Logger.error("Invalid URL!");
                continue;
            }

            // Regenerate key-pair each request (protection against spoofing)
            this.keyPairs.generateKeyPair();

            // Send "Get Relays" request to TrustedServer
            this.clientSocket.getRelays(chainLength, keyPairs.getPublicKey());

            // Building a chain of encryption's
            ChainedResponse chain = ChainedEncryption.chain(chainLength, "HTTP_GET_REQUEST " + input + " "
                    + KeyHelper.publicKeyToBase64(keyPairs.getPublicKey()));

            ServerDetails relayDetails = chain.getFirstRelay();
            Logger.info("<Get Request> Sending to initial relay " + relayDetails.getHost() + ":" + relayDetails.getPort());
            ClientSocket relay = new ClientSocket(relayDetails.getHost(), relayDetails.getPort(), this.keyPairs, relayDetails.getPublicKey());

            String response = relay.send(chain.getEncryptedPacket(), true, true);

            System.out.println("Response: " + response);
        }
    }
}

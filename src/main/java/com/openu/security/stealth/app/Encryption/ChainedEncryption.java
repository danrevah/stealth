package com.openu.security.stealth.app.Encryption;

import com.openu.security.stealth.app.Logger.Logger;
import com.openu.security.stealth.app.Sockets.Database;
import com.openu.security.stealth.app.Sockets.ServerDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Protocol concept behind the chained encryption
 *  1. HTTP_GET_REQUEST [HTTP_URL] [PUBLIC_KEY]
 *  2. ROUTE [IP] [PORT] [ENC_MESSAGE of 1]
 *  3. ROUTE [IP] [PORT] [ENC_MESSAGE of 2]
 *  ...
 *  N. ROUTE [IP] [PORT] [ENC_MESSAGE of N-1]
 *
 * @TODO: Exit relay should digitally sign the response making the client sure it hasn't been spoofed
 *
 * Scenarios:
 *  1. Relay receives a ROUTE request and sends the ENC_MESSAGE next to the specified IP and PORT.
 *     Waiting for response and transferring it back to previous relay.
 *
 *  2. Relay receives an HTTP_GET_REQUEST request and sends a GET request to the specified HTTP_URL,
 *     Encrypting back the data using the PUBLIC_KEY and return back.
 */
public class ChainedEncryption {

    public static ChainedResponse chain(int chainLength, String packet) throws Exception {
        ArrayList<ServerDetails> relays = Database.getRelays();

        if (relays.size() < 2) {
            throw new Exception("Chain encryption must incl. at least 2 relays");
        }

        // @TODO: Extract relays randomly
        List<ServerDetails> middleRelays = relays;
        String encryptedPacket = packet;

        Logger.debug("<Chain Encryption> Chaining " + chainLength + " relays..");

        int lastIndex = middleRelays.size() - 1;
        ServerDetails firstRelay = middleRelays.get(lastIndex);
        middleRelays.remove(lastIndex);

        for (ServerDetails relay : middleRelays) {
            encryptedPacket = "ROUTE " + relay.getHost() + " " + relay.getPort() + " "
                    + PublicEncryption.encrypt(encryptedPacket, relay.getPublicKey());
        }

        return new ChainedResponse(encryptedPacket, firstRelay);
    }
}

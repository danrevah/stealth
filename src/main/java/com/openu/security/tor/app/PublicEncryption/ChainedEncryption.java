package com.openu.security.tor.app.PublicEncryption;

import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Sockets.Database;
import com.openu.security.tor.app.Sockets.ServerDetails;

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

    public static String chain(int chainLength, String packet) throws Exception {
        ArrayList<ServerDetails> relays = Database.getRelays();

        if (relays.size() < 2) {
            throw new Exception("Chain encryption must incl. at least 2 relays");
        }

        // @TODO: Change this
        // An exit relay should NOT be taken randomly from the relay list!
        // There should be a list of predefined relays which are well known, same as the TrustedServer!
        // This is IMPORTANT in order to response spoofing.
        ServerDetails exitRelay = relays.get(0);

        // @TODO: Extract relays randomly
        List<ServerDetails> middleRelays = new ArrayList<>(relays.subList(1, chainLength - 1));
        String encryptedPacket = PublicEncryption.encryptChunks(packet, exitRelay.getPublicKey());

        Logger.info("<Chain Encryption> of: " + packet);

        for (ServerDetails relay : middleRelays) {
            String wrappedPacket = "ROUTE " + relay.getHost() + " " + relay.getPort() + " " + encryptedPacket;
            Logger.info("<Chain Encryption> of: " + wrappedPacket);
            encryptedPacket = PublicEncryption.encryptChunks(wrappedPacket, relay.getPublicKey());
        }

        return encryptedPacket;
    }
}

package com.openu.security.stealth.app.Sockets;

import com.openu.security.stealth.app.Encryption.KeyHelper;
import com.openu.security.stealth.app.Encryption.KeyPairs;
import com.openu.security.stealth.app.Encryption.PublicEncryption;
import com.openu.security.stealth.app.Logger.Logger;
import com.openu.security.stealth.app.Protocol.ProtocolHeader;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

/**
 * ClientSocket
 *
 * Used both by Relay & Client applications,
 * interacting with other relays or the TrustedServer.
 */
public class ClientSocket {

    private Socket socket;
    private PublicKey hostPublicKey;
    private KeyPairs keyPairs;

    public ClientSocket(String host, int port, KeyPairs keyPairs, PublicKey hostPublicKey) throws Exception {
        this.keyPairs = keyPairs;
        this.socket = new Socket(InetAddress.getByName(host), port);
        this.hostPublicKey = hostPublicKey;
    }

    public void getRelays(int chainLength, PublicKey publicKey) throws Exception {
        String packet = buildPacket(ProtocolHeader.GET_RELAYS, Arrays.asList(
            "" + chainLength,
            DatatypeConverter.printBase64Binary(publicKey.getEncoded())
        ));

        String relaysPacket = this.send(packet, true, true);

        // clear old relays from list..
        Database.resetRelays();

        for (String row : relaysPacket.split("\n")) {
            String[] splitted = row.split(" ");

            if (splitted.length != 4) {
                continue;
            }

            Database.addRelay(new ServerDetails(splitted[1], Integer.valueOf(splitted[2]), KeyHelper.base64ToPublicKey(splitted[3])));
        }
    }

    public void addRelay(int port, PublicKey publicKey) throws Exception {
        String packet = buildPacket(ProtocolHeader.ADD_RELAY, Arrays.asList(
            String.valueOf(port),
            DatatypeConverter.printBase64Binary(publicKey.getEncoded())
        ));

        this.send(packet, false, true);
    }

    public String send(String packet, boolean recieveBack, boolean encryption) throws Exception {
        String modifiedPacket = packet;

        if (encryption) {
            Logger.info("<Encrypting>");
            modifiedPacket = PublicEncryption.encrypt(packet, hostPublicKey);
            Logger.debug("<Encrypted Packet> " + modifiedPacket);
        }

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);

        Logger.info("<Sending packet ...>");
        out.println(modifiedPacket);
        out.flush();

        if (recieveBack) {
            String message = br.readLine();
            Logger.info("<Received back>");
            Logger.debug(message);

            if (encryption) {
                String decrypted = PublicEncryption.decrypt(message, keyPairs.getPrivateKey());
                Logger.info("<Decrypted>");
                Logger.debug(decrypted);
                return decrypted;
            }

            return message;
        }

        return "";
    }

    // -- Private
    private String buildPacket(ProtocolHeader header, List<String> data) throws Exception {
        String packet = header.getName() + (data.size() > 0 ? (" " + String.join(" ", data)) : "");
        Logger.info("<Packet> " + header.getName());
        Logger.debug(packet);
        return packet;
    }
}

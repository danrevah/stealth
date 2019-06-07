package com.openu.security.tor.app.Sockets;

import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Protocol.ProtocolHeader;
import com.openu.security.tor.app.PublicEncryption.KeyPairs;
import com.openu.security.tor.app.PublicEncryption.PublicEncryption;
import com.openu.security.tor.app.Services.Config;

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

        for (String row : relaysPacket.split("\n")) {
            String[] splitted = row.split(" ");

            if (splitted.length != 4) {
                continue;
            }

            // @TODO: Instance creeation can be avoided..
            KeyPairs keyPairs = new KeyPairs();
            keyPairs.setPublicKeyFromBase64(splitted[3]);
            Database.addRelay(new ServerDetails(splitted[1], Integer.valueOf(splitted[2]), keyPairs.getPublicKey()));
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
            modifiedPacket = PublicEncryption.encryptChunks(packet, hostPublicKey);
            Logger.info("<Encrypted Packet> " + modifiedPacket);
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
            Logger.info("<Received back>: " + message);

            if (encryption) {
                String decrypted = PublicEncryption.decryptChunks(message, keyPairs.getPrivateKey());
                Logger.info("<Decrypted>: " + decrypted);
                return decrypted;
            }

            return message;
        }

        return "";
    }

    // -- Private
    private String buildPacket(ProtocolHeader header, List<String> data) throws Exception {
        String packet = header.getName() + (data.size() > 0 ? (" " + String.join(" ", data)) : "");
        Logger.info("<Packet> " + packet);
        return packet;
    }
}

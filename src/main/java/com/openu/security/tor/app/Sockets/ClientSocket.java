package com.openu.security.tor.app.Sockets;

import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Protocol.ProtocolHeader;
import com.openu.security.tor.app.PublicEncryption.KeyPairs;
import com.openu.security.tor.app.PublicEncryption.PublicEncryption;
import com.openu.security.tor.app.Services.Config;
import com.google.common.base.Joiner;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientSocket {

    private Socket socket;
    private PublicKey hostPublicKey;

    public ClientSocket(String host, int port) throws Exception {
        this.socket = new Socket(InetAddress.getByName(host), port);

        // Instance creation used to convert from base64..
        // @TODO: Move converting functions to static or a different class
        KeyPairs publicOnlyKeyPair = new KeyPairs();
        publicOnlyKeyPair.setPublicKeyFromBase64(Config.TRUSTED_SERVER_PUBLIC_KEY);

        hostPublicKey = publicOnlyKeyPair.getPublicKey();
    }

    public void getRelays() throws Exception {
        String packet = buildPacket(ProtocolHeader.GET_RELAYS, new ArrayList<String>());
        this.send(packet, true);
    }

    public void addRelay(int port, PublicKey publicKey) throws Exception {
        String packet = buildPacket(ProtocolHeader.ADD_RELAY, Arrays.asList(
            InetAddress.getLocalHost().getCanonicalHostName(),
            String.valueOf(port),
            DatatypeConverter.printBase64Binary(publicKey.getEncoded())
        ));

        this.send(packet, false);
    }

    // -- Private
    private String buildPacket(ProtocolHeader header, List<String> data) throws Exception {
        String packet = header.getName() + (data.size() > 0 ? (" " + String.join(" ", data)) : "");
        Logger.info("<Packet> " + packet);
        return packet;
    }

    private void send(String packet, boolean recieveBack) throws Exception {
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
        Logger.info("<Encrypting Packet>");

        byte[] fullBytes = packet.getBytes();

        ArrayList<String> encryptedChunks = new ArrayList<>();

        // Chunk size 200 since RSA with 2048 bits can only encrypt up to: 2048/11-8 bytes.
        final int CHUNK_SIZE = 200;

        for (int i = 0; i < fullBytes.length; i += CHUNK_SIZE) {
            encryptedChunks.add(
                DatatypeConverter.printBase64Binary(
                    PublicEncryption.encrypt(
                        new String(Arrays.copyOfRange(fullBytes, i, i + CHUNK_SIZE < fullBytes.length ? i + CHUNK_SIZE : fullBytes.length)),
                        hostPublicKey
                    )
                )
            );
        }

        String encryptedConcat = Joiner.on("@").join(encryptedChunks);

        Logger.info("<Encrypted packet being sent ...>");
        out.println(encryptedConcat);
        out.flush();

        if (recieveBack) {
            String message = br.readLine();
            Logger.info("<Received back>: " + message);
        }
    }
}

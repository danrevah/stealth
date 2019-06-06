package com.openu.security.tor.app.Sockets;

import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Protocol.ProtocolHeader;
import com.openu.security.tor.app.PublicEncryption.KeyPairs;
import com.openu.security.tor.app.PublicEncryption.PublicEncryption;
import com.openu.security.tor.app.Services.Config;

import javax.xml.bind.DatatypeConverter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
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
        this.send(packet);
    }

    // -- Private
    private String buildPacket(ProtocolHeader header, List<String> data) throws Exception {
        String packet = header.getName() + (data.size() > 0 ? (" " + String.join(" ", data)) : "");
        Logger.info("<Packet> " + packet);
        return packet;
    }

    private void send(String packet) throws Exception {
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
        Logger.info("<Encrypting Packet>");

        byte[] encryptedBytes = PublicEncryption.encrypt(packet, hostPublicKey);
        String encryptedPacket = DatatypeConverter.printBase64Binary(encryptedBytes);

        Logger.info("<Encrypted packet being sent ...>");
        out.println(encryptedPacket);
        out.flush();
    }
}

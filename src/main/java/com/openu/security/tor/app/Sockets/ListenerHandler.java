package com.openu.security.tor.app.Sockets;

import com.openu.security.tor.app.Http.HttpRequest;
import com.openu.security.tor.app.Logger.LogLevel;
import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Protocol.ProtocolHeader;
import com.openu.security.tor.app.Encryption.KeyHelper;
import com.openu.security.tor.app.Encryption.KeyPairs;
import com.openu.security.tor.app.Encryption.PublicEncryption;
import com.google.common.base.Joiner;
import com.openu.security.tor.app.Services.Config;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class ListenerHandler implements Runnable {

    private Socket clientSocket;
    private String host;
    private int port;
    private PrivateKey privateKey;

    public ListenerHandler(Socket clientSocket, PrivateKey privateKey, String host, int port) {
        this.clientSocket = clientSocket;
        this.privateKey = privateKey;
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            String clientAddress = clientSocket.getInetAddress().getHostAddress();

            Logger.info("<Port " + port + ">  Incoming connection from " + clientAddress);

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String data;
            while ((data = in.readLine()) != null) {
                Logger.info("<Received Message>");
                Logger.debug(data);
                Logger.info("<Decrypting>");

                String decrypted = PublicEncryption.decrypt(data, privateKey);
                Logger.debug(decrypted);

                handleRequests(decrypted);
            }
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter= new PrintWriter(writer);
            e.printStackTrace(printWriter);
            Logger.log(LogLevel.Error, "Exception in String is :: " + writer.toString());
        }
    }

    private void handleRequests(String packet) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
        OutputStreamWriter os = new OutputStreamWriter(bos, "US-ASCII");

        String[] chunks = packet.split(" ");
        String header = chunks[0].replace(" ", "");

        if (header.equals(ProtocolHeader.ADD_RELAY.getName())) {
            String host = clientSocket.getInetAddress().getHostAddress();
            int port = Integer.valueOf(chunks[1]);
            Database.addRelay(new ServerDetails(host, port, KeyHelper.base64ToPublicKey(chunks[2])));
        }

        if (header.equals(ProtocolHeader.ROUTE.getName())) {
            String host = chunks[1];
            int port = Integer.valueOf(chunks[2]);
            String encrypted = chunks[3];
            ClientSocket relay = new ClientSocket(host, port, new KeyPairs(), KeyHelper.base64ToPublicKey(Config.TRUSTED_SERVER_PUBLIC_KEY));
            Logger.info("<Route> Passing to next route " + host + ":" + port);
            String response = relay.send(encrypted, true, false);
            Logger.info("<Received Route Response>");
            Logger.debug(response);
            os.write(response + "\n");
            os.flush();
        }

        if (header.equals(ProtocolHeader.HTTP_GET_REQUEST.getName())) {
            String url = chunks[1];
            String pubKey = chunks[2];

            String response = HttpRequest.get(url);

            String encryptedResponse = PublicEncryption.encrypt(response, KeyHelper.base64ToPublicKey(pubKey));

            os.write(encryptedResponse + "\n");
            os.flush();
        }

        if (header.equals(ProtocolHeader.GET_RELAYS.getName())) {
            Logger.info("Returned relays!");
            // @TODO: Use relay amount and randomly return ACTIVE relays
            int relayAmount = Integer.valueOf(chunks[1]);
            String clientPubKey = chunks[2];

            List<ServerDetails> relaysArr = Database.getRelays();
            ArrayList<String> relays = new ArrayList<>();
            Logger.debug("Total Relays: " + relaysArr.size());

            for (ServerDetails details : relaysArr) {
                relays.add(details.getHost() + " " + details.getPort() + " " + DatatypeConverter.printBase64Binary(details.getPublicKey().getEncoded()));
            }

            PublicKey clientPublicKey = KeyHelper.base64ToPublicKey(clientPubKey);

            if (relaysArr.size() > 0) {
                String returnedPacket = "RELAY " + Joiner.on("\nRELAY ").join(relays);
                String encryptedPacket = PublicEncryption.encrypt(returnedPacket, clientPublicKey);
                os.write(encryptedPacket + "\n");
            } else {
                String encryptedPacket = PublicEncryption.encrypt("NO_RELAYS", clientPublicKey);
                os.write(encryptedPacket + "\n");
            }

            os.flush();
        }
    }
}

package com.openu.security.tor.app.Sockets;

import com.openu.security.tor.app.Logger.LogLevel;
import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Protocol.ProtocolHeader;
import com.openu.security.tor.app.PublicEncryption.KeyPairs;
import com.openu.security.tor.app.PublicEncryption.PublicEncryption;
import com.google.common.base.Joiner;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class ListenerHandler implements Runnable {

    private Socket clientSocket;
    private String host;
    private int port;
    private PrivateKey privateKey;

    public ListenerHandler(Socket clientSocket, PrivateKey privateKey) {
        this.clientSocket = clientSocket;
        this.privateKey = privateKey;
    }

    public void run() {
        try {
            String clientAddress = clientSocket.getInetAddress().getHostAddress();

            Logger.info("<Port " + port + ">  Incoming connection from " + clientAddress);

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String data;
            while ((data = in.readLine()) != null) {
                Logger.info("<Received Message>");
                Logger.info(data);
                Logger.info("<Decrypting>");

                String decrypted = "";
                String[] chunks = data.split("@");

                for (String chunk : chunks) {
                    decrypted += PublicEncryption.decrypt(DatatypeConverter.parseBase64Binary(chunk), privateKey);
                }

                Logger.info("Message: " + decrypted);
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
        String[] chunks = packet.split(" ");
        String header = chunks[0].replace(" ", "");

        if (header.equals(ProtocolHeader.ADD_RELAY.getName())) {
            String host = chunks[1];

            int port = Integer.valueOf(chunks[2]);
            KeyPairs keyPairPub = new KeyPairs();
            keyPairPub.setPublicKeyFromBase64(chunks[3]);

            ListenerDatabase.addRelay(new ServerDetails(host, port, keyPairPub.getPublicKey()));
        }

        if (header.equals(ProtocolHeader.GET_RELAYS.getName())) {
            Logger.info("Returned relays!");
            BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
            OutputStreamWriter os = new OutputStreamWriter(bos, "US-ASCII");

            ArrayList<String> relays = new ArrayList<>();
            Logger.info("Total Relays: " + relays.size());
            for (ServerDetails details : ListenerDatabase.getRelays()) {
                relays.add(details.getHost() + ":" + details.getPort() + ":" + DatatypeConverter.printBase64Binary(details.getPublicKey().getEncoded()));
            }

            os.write(Joiner.on("@").join(relays) + "\n");
            os.flush();
        }
    }
}

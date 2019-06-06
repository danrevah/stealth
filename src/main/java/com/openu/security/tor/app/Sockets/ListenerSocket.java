package com.openu.security.tor.app.Sockets;

import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.PublicEncryption.PublicEncryption;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;

public class ListenerSocket {

    private ServerSocket socket;
    private String host;
    private int port;
    private PrivateKey privateKey;

    public ListenerSocket(String host, int port, PrivateKey privateKey) throws Exception {
        this.port = port;
        this.host = host;
        this.privateKey = privateKey;
        this.socket = new ServerSocket(port, 1, InetAddress.getByName(host));
    }

    public void listen() throws Exception {
        Socket client = this.socket.accept();
        String clientAddress = client.getInetAddress().getHostAddress();

        Logger.info("<Port " + port + ">  Incoming connection from " + clientAddress);

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String data;
        while ((data = in.readLine()) != null) {
            Logger.info("<Received Message> - <Decrypting>");
            String decrypted = PublicEncryption.decrypt(DatatypeConverter.parseBase64Binary(data), privateKey);
            Logger.info("Message: " + decrypted);
        }
    }
}

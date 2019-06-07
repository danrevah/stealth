package com.openu.security.tor.app.Sockets;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class ListenerSocket {

    private ServerSocket socket;
    private Socket clientSocket;
    private String host;
    private int port;
    private PrivateKey privateKey;

    private List<ServerDetails> relays;

    public ListenerSocket(String host, int port, PrivateKey privateKey) throws Exception {
        this.host = host;
        this.privateKey = privateKey;
        this.socket = new ServerSocket(port, 1, InetAddress.getByName(host));
        this.port = this.socket.getLocalPort();
        this.relays = new ArrayList<>();
    }

    public void listen() throws Exception {
        while (true) {
            this.clientSocket = this.socket.accept();

            ListenerHandler connection = new ListenerHandler(clientSocket, privateKey, host, port);
            new Thread(connection).start();
        }
    }

    public int getPort() {
        return port;
    }
}

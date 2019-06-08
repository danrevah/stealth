package com.openu.security.tor.app.Sockets;

import com.openu.security.tor.app.Logger.Logger;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class ListenerSocket {

    private ServerSocket socket;
    private String host;
    private int port;
    private PrivateKey privateKey;

    public ListenerSocket(String host, int port, PrivateKey privateKey) throws Exception {
        this.host = host;
        this.privateKey = privateKey;
        this.socket = new ServerSocket(port, 10, InetAddress.getByName(host));
        this.port = this.socket.getLocalPort();
    }

    public void listen() throws Exception {
        while (true) {
            Socket clientSocket = this.socket.accept();
            ListenerHandler connection = new ListenerHandler(clientSocket, privateKey, host, port);
            Logger.info("<ListenerSocket> Listening to new connections..");

            new Thread(connection).start();
        }
    }

    public int getPort() {
        return port;
    }
}

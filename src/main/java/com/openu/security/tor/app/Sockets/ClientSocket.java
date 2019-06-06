package com.openu.security.tor.app.Sockets;

import com.openu.security.tor.app.Protocol.ProtocolHeader;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientSocket {

    private Socket socket;

    private ClientSocket(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }

    public void getRelays() throws Exception {
        String packet = buildPacket(ProtocolHeader.GET_RELAYS, new ArrayList<String>());
        // @TODO: Encrypt!
        this.send(packet);
    }

    public void getRelays() throws Exception {
        String packet = buildPacket(ProtocolHeader.GET_RELAYS, new ArrayList<String>());
        this.send(packet);
    }

    // -- Private
    private String buildPacket(ProtocolHeader header, List<String> data) throws Exception {
        return header.getName() + " " + String.join(" ", data);
    }

    private void send(String packet) throws Exception {
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
        out.println(packet);
        out.flush();
    }
}

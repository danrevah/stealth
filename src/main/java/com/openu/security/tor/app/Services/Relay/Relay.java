package com.openu.security.tor.app;

public class Relay implements Service {

    public void execute(int instanceAmount, boolean verbose) {
        if (instanceAmount < 1 || instanceAmount > 10) {
            System.out.println("Relay instances in parallel must be in range of 1-10.");
            return;
        }

        if (verbose) {
            System.out.println("Launching " + instanceAmount + " relays..");
        }

        // @TODO: Start from client!!! has many things in common!

        // @TODO: 1. On startup send 'Protocol.ADD_RELAY' to the trusted server
        // @TODO: 2. Use threads to run this function multiple times.. with different ports (randomized)!
        // @TODO: 3. Listen to connections in-case of `HTTP_GET_REQUEST` decrypt
        //           If no more encrypted messages -> make GET Request to server
        //              + encrypt response with PUBLIC_KEY of client
        //
        //           If there is -> take next IP:PORT in line and send 'HTTP_GET_REQUEST' to him
        //              + wait for response in order to return to the connection
        //              + no need to encrypt response here
    }
}
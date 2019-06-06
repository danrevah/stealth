package com.openu.security.tor.app.Services.Relay;

import com.openu.security.tor.app.Logger.LogLevel;
import com.openu.security.tor.app.Services.Service;
import com.openu.security.tor.app.Logger.Logger;

public class Relay implements Service {

    private int instanceAmount;

    public Relay(int instanceAmount) throws Exception {
        this.instanceAmount = instanceAmount;
    }

    public void execute() throws Exception {
        if (instanceAmount < 1 || instanceAmount > 10) {
            Logger.log(LogLevel.Error, "Relay instances in parallel must be in range of 1-10.");
            return;
        }

        Logger.log(LogLevel.Info, "Launching " + instanceAmount + " relays..");

        // @TODO: Start from client!!! has many things in common!

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
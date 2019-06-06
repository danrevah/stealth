package com.openu.security.tor.app.Services.TrustedServer;

import com.openu.security.tor.app.Services.Service;

public class TrustedServer implements Service {

    public void execute(int instanceAmount, boolean verbose) {
        if (instanceAmount != 1) {
            System.out.println("TrustedServer service cannot be executed in parallel mode.");
            return;
        }

        // @TODO: Start from client!!! has many things in common!

        // @TODO: Implement server: https://www.pegaxchange.com/2017/12/07/simple-tcp-ip-server-client-java/
        // @TODO: 1. Create method to handle trusted server requests
        // @TODO: 2. Recieve `Protocol.ADD_RELAY` with `IP:PORT` + `PUBLIC KEY` and add to a list of relays.
        // @TODO: 3. Public-Key + IP/PORT of TrustedServer should be pre-configured in Relays & Client in order to prevent
        //           MITM attacks. (document this in the API!)
        // @TODO: 4. Recieve `Protocol.GET_RELAYS` with `chainLength`
        // @TODO: 5. Return back `Protocol.RELAY` encrypted with client public key
    }
}
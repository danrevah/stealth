package com.openu.security.tor.app.Services.Client;

import com.openu.security.tor.app.Services.Service;

public class Client implements Service {

    public void execute(int instanceAmount, boolean verbose) {
        if (instanceAmount != 1) {
            System.out.println("Client service cannot be executed in parallel mode.");
            return;
        }

        // @TODO: Start from public encryption with tests!! use mvn-test there's already a lib there nad it works!

        // @TODO: 1. Create method to handle client requests
        // @TODO: 2. Create socket helper library to make connectios
        // @TODO: 3. IP/PORT of TrustedServer should be pre-configured in Relays & Client in order to prevent MITM
        //           attacks. (document this in the API!) -> Maven Configuration?
        // @TODO: 4. get chain length from command line! as optional parameter - only in Client (constructor..) < `relay amount`!
        // @TODO: 5. Send `Protocol.GET_RELAYS` with `chainLength` to TrustedServer. (encrypted ofc).
        // @TODO: 6. Recieve back `Protocol.RELAY`
        // @TODO: 7. Document in code & README -> @TODO: TrustedServer should ping relays every X seconds to validate is alive.
        // @TODO: 8. Write a method that builds a chain of encrypted messages and sends to Relay! with `Protocol.HTTP_GET_REQUEST`
        // @TODO: 9. Send back response to client

        // @TOOD: Next: TrustedServer, since relay is very depdended on it also..
    }
}
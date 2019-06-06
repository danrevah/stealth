package com.openu.security.tor.app.Services.TrustedServer;

import com.openu.security.tor.app.Logger.LogLevel;
import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.PublicEncryption.KeyPairs;
import com.openu.security.tor.app.Services.Config;
import com.openu.security.tor.app.Services.Service;
import com.openu.security.tor.app.Sockets.ListenerSocket;

public class TrustedServer implements Service {

    private int instanceAmount;
    private ListenerSocket server;

    public TrustedServer(int instanceAmount) throws Exception {
        this.instanceAmount = instanceAmount;

        // @TODO: Instance creation is redundant, move to static!
        KeyPairs keyPairs = new KeyPairs();
        keyPairs.setPrivateKeyFromBase64(Keys.PRIVATE_KEY);

        this.server = new ListenerSocket("127.0.0.1", Config.TRUSTED_SERVER_PORT, keyPairs.getPrivateKey());
        Logger.info("TrustedServer listening for connections...");
    }

    public void execute() throws Exception {
        if (instanceAmount != 1) {
            Logger.log(LogLevel.Error, "TrustedServer service cannot be executed in parallel mode.");
            return;
        }

        this.server.listen();
    }
}
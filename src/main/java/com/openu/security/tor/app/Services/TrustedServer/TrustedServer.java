package com.openu.security.tor.app.Services.TrustedServer;

import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Encryption.KeyHelper;
import com.openu.security.tor.app.Services.Config;
import com.openu.security.tor.app.Services.Service;
import com.openu.security.tor.app.Sockets.ListenerSocket;

public class TrustedServer implements Service {

    private ListenerSocket server;

    public TrustedServer() throws Exception {
        this.server = new ListenerSocket("0.0.0.0", Config.TRUSTED_SERVER_PORT, KeyHelper.base64ToPrivateKey(Keys.PRIVATE_KEY));
        Logger.info("TrustedServer listening for connections...");
    }

    public void execute() throws Exception {
        this.server.listen();
    }
}
package com.openu.security.tor.app;

public class TrustedServer implements Service {

    public void execute(int instanceAmount, boolean verbose) {
        if (instanceAmount != 1) {
            System.out.println("TrustedServer service cannot be executed in parallel mode.");
            return;
        }
    }
}
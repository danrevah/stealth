package com.openu.security.tor.app;

public class Client implements Service {

    public void execute(int instanceAmount, boolean verbose) {
        if (instanceAmount != 1) {
            System.out.println("Client service cannot be executed in parallel mode.");
            return;
        }

    }
}
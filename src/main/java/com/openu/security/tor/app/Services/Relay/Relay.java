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
    }
}
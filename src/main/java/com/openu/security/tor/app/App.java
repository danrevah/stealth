package com.openu.security.tor.app;

import com.openu.security.tor.app.Logger.LogLevel;
import com.openu.security.tor.app.Logger.Logger;
import com.openu.security.tor.app.Services.Client.Client;
import com.openu.security.tor.app.Services.Relay.Relay;
import com.openu.security.tor.app.Services.TrustedServer.TrustedServer;
import picocli.CommandLine;
import picocli.CommandLine.*;
import com.openu.security.tor.app.Services.*;

import java.io.PrintWriter;
import java.io.StringWriter;

enum ServiceNames { Relay, TrustedServer, Client }

@Command(name = "proxy", mixinStandardHelpOptions = true, version = "Encrypted proxy 1.0")
public class App implements Runnable {
    @Option(
        names = { "-v", "--verbose" },
        description = "Verbose mode. Helpful for troubleshooting. "
    )
    private boolean verbose = false;

    @Parameters(arity = "1", paramLabel = "SERVICE", description = "Service types: ${COMPLETION-CANDIDATES}.")
    private ServiceNames serviceName = null;

    @Option(
            names = { "-p", "--parallel" },
            description = "Amount of instances to run in parallel."
    )
    private int instanceAmount = 1;

    @Option(
            names = { "-c", "--chain" },
            description = "Chain length (number of relay's in chain)."
    )
    private int chainLength = 3;

    public void run() {
        Logger.setMode(verbose);

        if (chainLength < 3) {
            Logger.log(LogLevel.Error, "Chain length must be at least 3!");
            return;
        }

        try {
            Service service;

            switch (serviceName) {
                case Relay:
                    service = new Relay(instanceAmount);
                    break;
                case Client:
                    service = new Client(instanceAmount, chainLength);
                    break;
                case TrustedServer:
                    service = new TrustedServer(instanceAmount);
                    break;

                default:
                    System.out.println("ERROR: No such service!");
                    return;
            }

            service.execute();
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter= new PrintWriter(writer);
            e.printStackTrace(printWriter);
            Logger.log(LogLevel.Error, "Exception in String is :: " + writer.toString());
        }
    }

    public static void main(String[] args) {
        CommandLine.run(new App(), args);
    }
}
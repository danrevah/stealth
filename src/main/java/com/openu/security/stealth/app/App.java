package com.openu.security.stealth.app;

import com.openu.security.stealth.app.Logger.Logger;
import com.openu.security.stealth.app.Services.Client.Client;
import com.openu.security.stealth.app.Services.Service;
import com.openu.security.stealth.app.Services.TrustedServer.TrustedServer;
import com.openu.security.stealth.app.Logger.LogLevel;
import com.openu.security.stealth.app.Services.Relay.Relay;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.PrintWriter;
import java.io.StringWriter;

enum ServiceNames { Relay, TrustedServer, Client }

/**
 * Stealth
 *
 * Encrypted chained proxy, depended on relays and a trusted server (implements similar concepts to TOR).
 * This is my final project in the Cyber-Security course at Open University of Israel.
 *
 * @author Dan Revah (danrevah89@gmail.com)
 * @version v0.1
 */
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
                    service = new Relay();
                    break;
                case Client:
                    service = new Client(chainLength);
                    break;
                case TrustedServer:
                    service = new TrustedServer();
                    break;

                default:
                    Logger.error("No such service!");
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
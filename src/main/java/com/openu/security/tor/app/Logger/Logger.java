package com.openu.security.tor.app.Logger;

public class Logger {
    private static boolean verbose = false;

    public static void setMode(boolean verbose) {
        Logger.verbose = verbose;
    }

    public static void error(String log) {
        log(LogLevel.Error, log);
    }

    public static void info(String log) {
        log(LogLevel.Info, log);
    }

    // @TODO: Handle colors more nicely with enum in a private method.
    public static void log(LogLevel level, String log) {
        if (level == LogLevel.Error) {
            System.out.print((char)27 + "[31m" + "ERROR:" + (char)27 + "[0m ");
            System.out.println(log);
            return;
        }

        if (level == LogLevel.Info && verbose) {
            System.out.print((char)27 + "[33m" + "INFO:" + (char)27 + "[0m ");
            System.out.println(log);
            return;
        }
    }
}

package com.openu.security.tor.app.Logger;

/**
 * Logger
 *
 * Levels:
 *  - ERROR
 *  - INFO
 *  - DEBUG (only seen in verbose mode)
 */
public class Logger {

    private static final String BEING_COLOR_PREFIX = (char)27 + "[";
    private static final String BEING_COLOR_POSTFIX = "m";
    private static final String END_COLOR = (char)27 + "[0m ";

    private static final int RED_COLOR = 31;
    private static final int YELLOW_COLOR = 33;
    private static final int BLUE_COLOR = 34;

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

    public static void debug(String log) {
        log(LogLevel.Debug, log);
    }

    public static void log(LogLevel level, String log) {
        if (level == LogLevel.Error) {
            logLabel(RED_COLOR, "ERROR", log);
        }
        else if (level == LogLevel.Info) {
            logLabel(YELLOW_COLOR, "INFO", log);
        }
        else if (level == LogLevel.Debug && verbose) {
            logLabel(BLUE_COLOR, "DEBUG", log);
        }
    }

    private static void logLabel(int color, String label, String log) {
        System.out.print(BEING_COLOR_PREFIX + color + BEING_COLOR_POSTFIX + label + ":" + END_COLOR);
        System.out.println(log);
    }
}

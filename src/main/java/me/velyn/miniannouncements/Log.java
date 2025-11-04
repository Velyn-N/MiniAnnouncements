package me.velyn.miniannouncements;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private final Logger logger;
    private boolean isDebug = false;

    public Log(Logger logger) {
        this.logger = logger;
    }

    public void setDebug(boolean debug) {
        this.isDebug = debug;
    }

    public void infoF(String message, Object... args) {
        logger.log(Level.INFO, String.format(message, args));
    }

    public void warnF(String message, Object... args) {
        logger.log(Level.WARNING, String.format(message, args));
    }

    public void errorF(String message, Object... args) {
        logger.log(Level.SEVERE, String.format(message, args));
    }

    public void debugF(String message, Object... args) {
        if (isDebug) {
            logger.log(Level.INFO, "[DEBUG] " + String.format(message, args));
        }
    }
}

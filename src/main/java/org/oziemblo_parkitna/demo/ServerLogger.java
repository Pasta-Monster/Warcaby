package org.oziemblo_parkitna.demo;

import org.apache.log4j.Logger;
/**
 * Klasa ServerLogger dostarcza metody do logowania informacji i błędów na serwerze.
 */
public class ServerLogger {
    private static final Logger logger = Logger.getLogger(ServerLogger.class);

    /**
     * Loguje informację.
     *
     * @param message wiadomość do zalogowania
     */
    public static void logInfo(String message) {
        logger.info(message);
    }

    /**
     * Loguje błąd.
     *
     * @param message wiadomość o błędzie
     * @param t       wyjątek do zalogowania
     */
    public static void logError(String message, Throwable t) {
        logger.error(message, t);
    }
}
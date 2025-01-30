package org.oziemblo_parkitna.demo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Klasa Client uruchamia aplikację kliencką.
 */
public class Client {
    private static final Logger logger = Logger.getLogger(Client.class);

    public static void main(String[] args) {
        BasicConfigurator.configure(); // Konfiguracja log4j
        logger.info("Client started");
        ClientUI.launch(ClientUI.class, args);  // Uruchomienie interfejsu JavaFX
    }
}
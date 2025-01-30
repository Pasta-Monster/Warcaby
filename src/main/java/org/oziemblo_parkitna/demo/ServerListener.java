package org.oziemblo_parkitna.demo;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Klasa ServerListener nasłuchuje wiadomości z serwera.
 */
public class ServerListener implements Runnable {
    private BufferedReader in;

    /**
     * Konstruktor tworzący nową instancję ServerListener.
     *
     * @param in BufferedReader do nasłuchiwania wiadomości
     */
    public ServerListener(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        String response;
        try {
            while ((response = in.readLine()) != null) {
                System.out.println("Server: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
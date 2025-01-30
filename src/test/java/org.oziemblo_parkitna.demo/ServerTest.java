package org.oziemblo_parkitna.demo;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

/**
 * Klasa ServerTest testuje klasę Server.
 */
public class ServerTest {

    /**
     * Testuje, czy serwer się uruchamia.
     */
    @Test
    public void testServerStarts() {
        // Mockowanie Loggera
        try (MockedStatic<Logger> mockLogger = Mockito.mockStatic(Logger.class)) {
            Logger mockLoggerInstance = mock(Logger.class);
            mockLogger.when(() -> Logger.getLogger(Server.class)).thenReturn(mockLoggerInstance);

            // Tworzenie mocka dla ServerSocket, aby uniknąć otwierania prawdziwego portu
            try (ServerSocket mockServerSocket = mock(ServerSocket.class)) {
                when(mockServerSocket.accept()).thenReturn(new Socket());

                // Sprawdzanie, czy serwer nie rzuca wyjątkiem przy uruchomieniu
                Server server = new Server();
                server.main(new String[0]);

                // Weryfikacja: upewnianie się, że serwer nasłuchuje
                verify(mockServerSocket, times(1)).accept();

                // Weryfikacja: Sprawdzanie, czy serwer loguje uruchomienie
                verify(mockLoggerInstance, times(1)).info("Server started on port 12345");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
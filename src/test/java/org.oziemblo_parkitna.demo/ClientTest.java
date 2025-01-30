package org.oziemblo_parkitna.demo;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * Klasa ClientTest testuje klasę Client.
 */
public class ClientTest {

    /**
     * Testuje metodę main.
     */
    @Test
    public void testMainMethod() {
        // Mockowanie logera
        Logger mockLogger = mock(Logger.class);
        Client client = new Client();

        // Sprawdzenie, czy zostało zalogowane "Client started"
        client.main(new String[]{});
        verify(mockLogger, times(1)).info("Client started");
    }
}
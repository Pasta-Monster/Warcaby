package org.oziemblo_parkitna.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Klasa ClientUITest testuje klasę ClientUI.
 */
class ClientUITest {

    @Mock
    private Socket mockSocket;

    @Mock
    private PrintWriter mockPrintWriter;

    @Mock
    private BufferedReader mockBufferedReader;

    @Mock
    private CheckersGame mockCheckersGame;

    private ClientUI clientUI;

    /**
     * Ustawia początkowy stan przed każdym testem.
     */
    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);  // Inicjalizowanie mocków

        // Przygotowanie mocka dla InputStream i BufferedReader
        mockBufferedReader = mock(BufferedReader.class);
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));
        when(mockSocket.getOutputStream()).thenReturn(System.out);

        // Tworzymy instancję ClientUI
        clientUI = new ClientUI();
        clientUI.setSocket(mockSocket);  // Ustawiamy mockowany socket
    }

    /**
     * Testuje metodę connectToServer.
     */
    @Test
    void testConnectToServer() throws IOException {
        // Przygotowanie mocków, aby symulować odpowiedzi serwera
        when(mockBufferedReader.readLine()).thenReturn("{\"type\":\"player_id\", \"player_id\":\"12345\"}");

        // Testujemy metodę connectToServer
        clientUI.connectToServerTest();

        // Sprawdzamy, czy socket został połączony
        verify(mockSocket, times(1)).connect(any());
        // Sprawdzamy, czy odpowiedź została przeczytana
        verify(mockBufferedReader, times(1)).readLine();
    }

    /**
     * Testuje metodę setRole.
     */
    @Test
    void testSetRole() {
        // Testowanie metody setRole
        String role = "dog";
        clientUI.setRole(role);

        // Weryfikacja, że rola została prawidłowo ustawiona i odpowiednia wiadomość została wysłana do serwera
        verify(mockPrintWriter, times(1)).println(ArgumentMatchers.contains(role));
    }

    /**
     * Testuje metodę sendMove.
     */
    @Test
    void testSendMove() {
        // Testujemy metodę sendMove
        int fromX = 1, fromY = 2, toX = 3, toY = 4;
        clientUI.sendMove(fromX, fromY, toX, toY);

        // Weryfikacja, że sendMove wysyła odpowiednie dane do serwera
        verify(mockPrintWriter, times(1)).println(ArgumentMatchers.contains("move"));
    }

    @Test
    void testSendSaveRequest() {
        // Testujemy metodę sendSaveRequest
        String gameStateJson = "{\"state\":\"some_state\"}";
        String role = "dog";

        clientUI.sendSaveRequest(gameStateJson, role);

        // Weryfikacja, że sendSaveRequest wysyła żądanie zapisu do serwera
        verify(mockPrintWriter, times(1)).println(ArgumentMatchers.contains("save"));
    }

    @Test
    void testLoadGameStateFromServer() {
        // Testowanie metody loadGameStateFromServer
        String role = "dog";
        clientUI.loadGameStateFromServer(role);

        // Weryfikacja, że loadGameStateFromServer wysyła poprawną wiadomość ładowania do serwera
        verify(mockPrintWriter, times(1)).println(ArgumentMatchers.contains("load"));
    }

    @Test
    void testDisconnect() throws IOException {
        // Testujemy metodę disconnect
        clientUI.disconnectForTest();

        // Weryfikacja, że socket został zamknięty po wywołaniu disconnect
        verify(mockSocket, times(1)).close();
    }

}
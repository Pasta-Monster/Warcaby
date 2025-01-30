package org.oziemblo_parkitna.demo;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Klasa ClientHandlerTest testuje klasę ClientHandler.
 */
class ClientHandlerTest {

    private Socket mockSocket;
    private PrintWriter mockWriter;
    private BufferedReader mockReader;
    private CopyOnWriteArrayList<ClientHandler> clients;
    private ClientHandler clientHandler;

    /**
     * Ustawia początkowy stan przed każdym testem.
     */
    @BeforeEach
    void setUp() throws IOException {
        // Mockowanie obiektów
        mockSocket = mock(Socket.class);
        mockWriter = mock(PrintWriter.class);
        mockReader = mock(BufferedReader.class);
        clients = new CopyOnWriteArrayList<>();

        // Mockowanie metod socketu
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));

        // Tworzenie obiektu ClientHandler
        clientHandler = new ClientHandler(mockSocket, clients);
        clients.add(clientHandler);

        // Mockowanie PrintWriter i BufferedReader
        doReturn(mockWriter).when(mockSocket).getOutputStream();
        doReturn(mockReader).when(mockSocket).getInputStream();
    }

    /**
     * Wywołuje prywatną metodę.
     *
     * @param obj        obiekt, na którym wywoływana jest metoda
     * @param methodName nazwa metody
     * @param paramTypes typy parametrów metody
     * @param params     parametry metody
     * @throws Exception jeśli wystąpi błąd podczas wywoływania metody
     */
    private void invokePrivateMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] params) throws Exception {
        Method method = obj.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        method.invoke(obj, params);
    }

    /**
     * Pobiera wartość prywatnego pola.
     *
     * @param obj       obiekt, z którego pobierane jest pole
     * @param fieldName nazwa pola
     * @return wartość pola
     * @throws Exception jeśli wystąpi błąd podczas pobierania pola
     */
    private Object getPrivateField(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * Testuje metodę handleMove dla prawidłowego ruchu.
     */
    @Test
    void testHandleMove_ValidMove() throws Exception {
        // Przygotowanie symulacji ruchu
        JSONObject moveMessage = new JSONObject();
        moveMessage.put("type", "move");
        moveMessage.put("piece_type", "dog");
        moveMessage.put("fromX", 1);
        moveMessage.put("fromY", 1);
        moveMessage.put("toX", 2);
        moveMessage.put("toY", 2);

        // Dodanie klienta, aby symulować przekazywanie wiadomości
        clients.add(clientHandler);

        // Wywołanie metody prywatnej handleMove
        invokePrivateMethod(clientHandler, "handleMove", new Class[]{JSONObject.class}, new Object[]{moveMessage});

        // Weryfikacja, że wiadomość została wysłana do wszystkich klientów
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockWriter, times(1)).println(captor.capture());
        assertTrue(captor.getValue().contains("move"));
    }

    /**
     * Testuje metodę handleMove dla nieprawidłowego ruchu.
     */
    @Test
    void testHandleMove_InvalidMove() throws Exception {
        // Przygotowanie symulacji niepoprawnego ruchu
        JSONObject moveMessage = new JSONObject();
        moveMessage.put("type", "move");
        moveMessage.put("piece_type", "cat"); // Rola 'dog' nie pasuje do 'cat'
        moveMessage.put("fromX", 1);
        moveMessage.put("fromY", 1);
        moveMessage.put("toX", 2);
        moveMessage.put("toY", 2);

        // Wywołanie metody prywatnej handleMove
        invokePrivateMethod(clientHandler, "handleMove", new Class[]{JSONObject.class}, new Object[]{moveMessage});

        // Weryfikacja, że klient otrzymał odpowiedź o niepoprawnym ruchu
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockWriter, times(1)).println(captor.capture());
        assertTrue(captor.getValue().contains("invalid_move"));
    }

    /**
     * Testuje metodę setRole.
     */
    @Test
    void testSetRole() throws Exception {
        // Ustawienie roli klienta
        invokePrivateMethod(clientHandler, "setRole", new Class[]{String.class}, new Object[]{"dog"});

        // Weryfikacja, że rola została przypisana
        assertEquals("dog", getPrivateField(clientHandler, "role"));
    }

    /**
     * Testuje metodę handleSaveGame dla udanego zapisu.
     */
    @Test
    void testHandleSaveGame_Success() throws Exception {
        // Przygotowanie danych do zapisu
        JSONObject saveMessage = new JSONObject();
        saveMessage.put("type", "save");
        saveMessage.put("state", new JSONObject().put("some_key", "some_value"));

        // Wywołanie metody prywatnej handleSaveGame
        invokePrivateMethod(clientHandler, "handleSaveGame", new Class[]{JSONObject.class}, new Object[]{saveMessage});

        // Weryfikacja, że odpowiednia wiadomość o udanym zapisie została wysłana
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockWriter, times(1)).println(captor.capture());
        assertTrue(captor.getValue().contains("save_ack"));
    }

    /**
     * Testuje metodę handleSaveGame dla nieudanego zapisu, gdy gra została już zapisana.
     */
    @Test
    void testHandleSaveGame_Fail_GameAlreadySaved() throws Exception {
        // Ustawienie flagi, że gra już została zapisana
        invokePrivateMethod(clientHandler, "setPrivateField", new Class[]{String.class}, new Object[]{"gameSaved", true});

        // Przygotowanie danych do zapisu
        JSONObject saveMessage = new JSONObject();
        saveMessage.put("type", "save");
        saveMessage.put("state", new JSONObject().put("some_key", "some_value"));

        // Wywołanie metody prywatnej handleSaveGame
        invokePrivateMethod(clientHandler, "handleSaveGame", new Class[]{JSONObject.class}, new Object[]{saveMessage});

        // Weryfikacja, że odpowiednia wiadomość o błędzie zapisu została wysłana
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockWriter, times(1)).println(captor.capture());
        assertTrue(captor.getValue().contains("save_denied"));
    }

    /**
     * Testuje metodę handleLoadGame dla udanego załadowania gry.
     */
    @Test
    void testHandleLoadGame_Success() throws Exception {
        // Przygotowanie danych do załadowania stanu gry
        JSONObject loadMessage = new JSONObject();
        loadMessage.put("type", "load");
        loadMessage.put("role", "dog");

        // Stworzenie pliku z przykładowym stanem gry
        String gameStateJson = "{\"state\": {\"some_key\": \"some_value\"}}";
        try (FileWriter writer = new FileWriter("dog_game_state.json")) {
            writer.write(gameStateJson);
        }

        // Wywołanie metody do załadowania gry
        invokePrivateMethod(clientHandler, "handleLoadGame", new Class[]{JSONObject.class}, new Object[]{loadMessage});

        // Weryfikacja, że odpowiednia wiadomość została wysłana
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockWriter, times(1)).println(captor.capture());
        assertTrue(captor.getValue().contains("load"));
    }

    /**
     * Testuje metodę handleLoadGame dla nieudanego załadowania gry.
     */
    @Test
    void testHandleLoadGame_Fail() throws Exception {
        // Przygotowanie danych do załadowania stanu gry
        JSONObject loadMessage = new JSONObject();
        loadMessage.put("type", "load");
        loadMessage.put("role", "dog");

        // Usuwamy plik stanu gry, aby test zakończył się niepowodzeniem
        File file = new File("dog_game_state.json");
        if (file.exists()) {
            file.delete();
        }

        // Wywołanie metody do załadowania gry
        invokePrivateMethod(clientHandler, "handleLoadGame", new Class[]{JSONObject.class}, new Object[]{loadMessage});

        // Weryfikacja, że odpowiednia wiadomość o błędzie została wysłana
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockWriter, times(1)).println(captor.capture());
        assertTrue(captor.getValue().contains("load_ack"));
    }
}
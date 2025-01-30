package org.oziemblo_parkitna.demo;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Klasa ClientHandler obsługuje komunikację z klientem.
 */
public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class);
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private CopyOnWriteArrayList<ClientHandler> clients;
    private String playerId;
    private String role;
    private static PieceType currentPlayer = PieceType.DOG; // Pies jest poczatkowym graczem
    private static boolean gameSaved = false;

    /**
     * Konstruktor tworzący nową instancję ClientHandler.
     *
     * @param socket  gniazdo klienta
     * @param clients lista klientów
     */
    public ClientHandler(Socket socket, CopyOnWriteArrayList<ClientHandler> clients) {
        this.clientSocket = socket;
        this.clients = clients;
        this.playerId = UUID.randomUUID().toString(); // Generowanie unikalnego ID dla każdego klienta
    }

    /**
     * Zwraca ID gracza.
     *
     * @return ID gracza
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Ustawia rolę gracza.
     *
     * @param role rola gracza (DOG lub CAT)
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Wysyła ID gracza do klienta
            JSONObject playerIdMessage = new JSONObject();
            playerIdMessage.put("type", "player_id");
            playerIdMessage.put("player_id", playerId);
            out.println(playerIdMessage.toString());

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JSONObject jsonMessage = new JSONObject(inputLine);
                String messageType = jsonMessage.getString("type");

                switch (messageType) {
                    case "move":
                        handleMove(jsonMessage);
                        break;
                    case "set_role":
                        setRole(jsonMessage.getString("role"));
                        break;
                    case "save":
                        handleSaveGame(jsonMessage);
                        break;
                    case "load":
                        handleLoadGame(jsonMessage);
                        break;
                    case "turn_update":
                        updateCurrentPlayer(jsonMessage.getString("current_player"));
                        break;
                }
            }
        } catch (IOException e) {
            if (!clientSocket.isClosed()) {
                logger.error("Error handling client: ", e);
            }
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("Error closing socket: ", e);
            }
        }
    }

    /**
     * Obsługuje ruch pionka.
     *
     * @param moveMessage wiadomość JSON zawierająca informacje o ruchu
     */
    private void handleMove(JSONObject moveMessage) {
        String pieceType = moveMessage.getString("piece_type");
        if (role != null && role.equalsIgnoreCase(pieceType) && role.equalsIgnoreCase(currentPlayer.name())) {
            // Przesyla ruch do wszystkich klientów
            broadcastMessage(moveMessage.toString());
            switchTurn();

            // Po ruchu sprawdza WinCondition
            PieceType winner = checkWinCondition();
            if (winner != null) {
                for (ClientHandler client : clients) {
                    JSONObject winMessage = new JSONObject();
                    winMessage.put("type", "win");
                    winMessage.put("winner", winner.name());
                    winMessage.put("is_winner", client.role.equalsIgnoreCase(winner.name()));
                    client.out.println(winMessage.toString());
                }
            }
        } else {
            // Wysyla wiadomość o nieprawidlowym ruchu
            JSONObject invalidMoveMessage = new JSONObject();
            invalidMoveMessage.put("type", "invalid_move");
            out.println(invalidMoveMessage.toString());
        }
    }

    /**
     * Sprawdza warunki zwycięstwa na serwerze.
     *
     * @return typ zwycięskiego gracza lub null, jeśli nikt jeszcze nie wygrał
     */
    private PieceType checkWinCondition() {
        return null;
    }


    /**
     * Zmienia turę na następnego gracza.
     */
    private void switchTurn() {
        currentPlayer = (currentPlayer == PieceType.DOG) ? PieceType.CAT : PieceType.DOG;
        JSONObject turnUpdateMessage = new JSONObject();
        turnUpdateMessage.put("type", "turn_update");
        turnUpdateMessage.put("current_player", currentPlayer.name());
        broadcastMessage(turnUpdateMessage.toString());
    }

    /**
     * Aktualizuje obecnego gracza.
     *
     * @param currentPlayer obecny gracz
     */
    private void updateCurrentPlayer(String currentPlayer) {
        ClientHandler.currentPlayer = PieceType.valueOf(currentPlayer);
        broadcastMessage(new JSONObject()
                .put("type", "turn_update")
                .put("current_player", currentPlayer).toString());
    }

    /**
     * Wysyła wiadomość do wszystkich klientów.
     *
     * @param message wiadomość do wysłania
     */
    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.out.println(message);
        }
    }

    /**
     * Obsługuje zapis gry.
     *
     * @param jsonMessage wiadomość JSON zawierająca stan gry
     */
    private void handleSaveGame(JSONObject jsonMessage) {
        if (gameSaved) {
            JSONObject saveDeniedMessage = new JSONObject();
            saveDeniedMessage.put("type", "save_denied");
            saveDeniedMessage.put("message", "Game has already been saved.");
            out.println(saveDeniedMessage.toString());
            return;
        }

        try {
            saveGameState(jsonMessage, "dog");
            saveGameState(jsonMessage, "cat");
            gameSaved = true;
            out.println(new JSONObject().put("type", "save_ack").put("status", "success").toString());
        } catch (IOException e) {
            logger.error("Error saving game state: ", e);
            out.println(new JSONObject().put("type", "save_ack").put("status", "error").toString());
        }
    }

    /**
     * Zapisuje stan gry do pliku.
     *
     * @param jsonMessage wiadomość JSON zawierająca stan gry
     * @param role        rola gracza (DOG lub CAT)
     * @throws IOException jeśli wystąpi błąd podczas zapisywania pliku
     */
    private void saveGameState(JSONObject jsonMessage, String role) throws IOException {
        String filename = role + "_game_state.json";
        try (FileWriter file = new FileWriter(filename)) {
            JSONObject gameState = jsonMessage.getJSONObject("state");
            file.write(gameState.toString());
        }
    }

    /**
     * Obsługuje ładowanie gry.
     *
     * @param jsonMessage wiadomość JSON zawierająca informacje o roli gracza
     */
    private void handleLoadGame(JSONObject jsonMessage) {
        String role = jsonMessage.getString("role");
        String filename = role + "_game_state.json";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            JSONObject response = new JSONObject();
            response.put("type", "load");
            response.put("state", new JSONObject(content.toString()));
            out.println(response.toString());
            gameSaved = false;
        } catch (IOException e) {
            logger.error("Error loading game state: ", e);
            out.println(new JSONObject().put("type", "load_ack").put("status", "error").toString());
        }
    }
}
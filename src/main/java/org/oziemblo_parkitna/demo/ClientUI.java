package org.oziemblo_parkitna.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa ClientUI obsługuje interfejs użytkownika aplikacji klienckiej.
 */
public class ClientUI extends Application {
    private PrintWriter out;
    private Socket socket;
    private CheckersGame checkersGame;
    private String playerId;
    private String role;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Checkers Game");

        // Inicjalizacja głównego menu
        MainMenu mainMenu = new MainMenu(primaryStage, this);

        Scene scene = new Scene(mainMenu.createMainMenuLayout(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            disconnect();
            Platform.exit();
            System.exit(0);
        });

        new Thread(this::connectToServer).start();
    }

    /**
     * Łączy się z serwerem.
     */
    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String response;
            while ((response = in.readLine()) != null) {
                JSONObject jsonResponse = new JSONObject(response);
                String messageType = jsonResponse.getString("type");

                switch (messageType) {
                    case "player_id":
                        playerId = jsonResponse.getString("player_id");
                        System.out.println("Assigned Player ID: " + playerId);
                        break;
                    case "move":
                        int fromX = jsonResponse.getInt("fromX");
                        int fromY = jsonResponse.getInt("fromY");
                        int toX = jsonResponse.getInt("toX");
                        int toY = jsonResponse.getInt("toY");
                        if (checkersGame != null) {
                            Platform.runLater(() -> checkersGame.movePiece(fromX, fromY, toX, toY));
                        }
                        break;
                    case "turn_update":
                        String currentPlayer = jsonResponse.getString("current_player");
                        if (checkersGame != null) {
                            Platform.runLater(() -> checkersGame.updateTurn(currentPlayer));
                        }
                        break;
                    case "save_ack":
                        String saveStatus = jsonResponse.getString("status");
                        System.out.println("Save status: " + saveStatus);
                        break;
                    case "save_denied":
                        String message = jsonResponse.getString("message");
                        Platform.runLater(() -> {
                            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                            alert.setTitle("Save Denied");
                            alert.setHeaderText(null);
                            alert.setContentText(message);
                            alert.showAndWait();
                        });
                        break;
                    case "load":
                        JSONObject state = jsonResponse.getJSONObject("state");
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        GameState gameState = mapper.readValue(state.toString(), GameState.class);
                        if (checkersGame != null) {
                            Platform.runLater(() -> checkersGame.loadGameState(gameState));
                        }
                        break;
                    case "invalid_move":
                        Platform.runLater(() -> {
                            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                            alert.setTitle("Invalid Move");
                            alert.setHeaderText(null);
                            alert.setContentText("Invalid move attempted. Please try again.");
                            alert.showAndWait();
                        });
                        break;
                    case "win":
                        String winner = jsonResponse.getString("winner");
                        Platform.runLater(() -> {
                            boolean isWin = winner.equals(role);
                            WinLoseWindow.display(primaryStage, isWin, this);
                        });
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Ustawia gniazdo połączenia.
     *
     * @param socket gniazdo połączenia
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Publiczna wersja metody connectToServer do testów.
     *
     * @throws IOException jeśli wystąpi błąd podczas połączenia z serwerem
     */    public void connectToServerTest() throws IOException {
        connectToServer();
    }

    /**
     * Metoda do rozłączenia się z serwerem dla celów testowych.
     */
    public void disconnectForTest() {
        disconnect();
    }

    /**
     * Rozłącza się z serwerem.
     */
    private void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ustawia instancję CheckersGame.
     *
     * @param checkersGame instancja CheckersGame
     */
    public void setCheckersGame(CheckersGame checkersGame) {
        this.checkersGame = checkersGame;
    }

    /**
     * Ustawia rolę gracza.
     *
     * @param role rola gracza (DOG lub CAT)
     */
    public void setRole(String role) {
        this.role = role;
        sendRoleToServer(role);
    }

    /**
     * Wysyła rolę gracza do serwera.
     *
     * @param role rola gracza (DOG lub CAT)
     */
    private void sendRoleToServer(String role) {
        if (out != null) {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("type", "set_role");
            jsonMessage.put("role", role);
            out.println(jsonMessage.toString());
        }
    }

    /**
     * Wysyła ruch pionka do serwera.
     *
     * @param fromX współrzędna x pozycji źródłowej
     * @param fromY współrzędna y pozycji źródłowej
     * @param toX   współrzędna x pozycji docelowej
     * @param toY   współrzędna y pozycji docelowej
     */
    public void sendMove(int fromX, int fromY, int toX, int toY) {
        if (out != null && playerId != null && role != null) {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("type", "move");
            jsonMessage.put("player_id", playerId);
            jsonMessage.put("piece_type", role);
            jsonMessage.put("fromX", fromX);
            jsonMessage.put("fromY", fromY);
            jsonMessage.put("toX", toX);
            jsonMessage.put("toY", toY);
            out.println(jsonMessage.toString());
        }
    }

    /**
     * Wysyła żądanie zapisu stanu gry do serwera.
     *
     * @param gameStateJson JSON reprezentujący stan gry
     * @param role          rola gracza (DOG lub CAT)
     */
    public void sendSaveRequest(String gameStateJson, String role) {
        if (out != null) {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("type", "save");
            jsonMessage.put("state", new JSONObject(gameStateJson));
            jsonMessage.put("role", role);
            out.println(jsonMessage.toString());
        }
    }

    /**
     * Ładuje stan gry z serwera.
     *
     * @param role rola gracza (DOG lub CAT)
     */
    public void loadGameStateFromServer(String role) {
        if (out != null) {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("type", "load");
            jsonMessage.put("role", role);
            out.println(jsonMessage.toString());
        }
    }

    /**
     * Aktualizuje turę na serwerze.
     *
     * @param currentPlayer obecny gracz
     */
    public void updateTurn(String currentPlayer) {
        if (out != null) {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("type", "turn_update");
            jsonMessage.put("current_player", currentPlayer);
            out.println(jsonMessage.toString());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
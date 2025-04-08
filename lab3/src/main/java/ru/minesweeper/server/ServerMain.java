/*package ru.minesweeper.server;

import ru.minesweeper.model.GameModel;

public class ServerMain {
    public static void main(String[] args) {
        int port = 5555;
        GameModel gameModel = new GameModel();
        RecordsManager recordsManager = new RecordsManager();

        GameServer server = new GameServer(port, gameModel, recordsManager);
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Server failed to start: " + e.getMessage());
        }
    }
}*/
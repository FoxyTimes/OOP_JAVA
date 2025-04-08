/*package ru.minesweeper.server;

import ru.minesweeper.controller.RecordsManager;
import ru.minesweeper.model.GameModel;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.model.entities.datatransfer.DataTransferObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer implements Observer {
    private final int port;
    private final GameModel gameModel;
    private final RecordsManager recordsManager;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;
    private boolean isRunning;

    public GameServer(int port, GameModel gameModel, RecordsManager recordsManager) {
        this.port = port;
        this.gameModel = gameModel;
        this.recordsManager = recordsManager;
        this.gameModel.addObserver(this);
    }

    public void start() throws IOException {
        isRunning = true;
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (isRunning) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket, this);
            clients.add(clientHandler);
            executor.execute(clientHandler);
        }
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
        executor.shutdown();
    }

    public synchronized void broadcast(DataTransferObject data, Notifications notification) {
        clients.forEach(client -> client.sendNotification(notification, data));
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    @Override
    public void notify(Notifications notification, DataTransferObject data) {
        broadcast(data, notification);
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public RecordsManager getRecordsManager() {
        return recordsManager;
    }
}*/
package ru.minesweeper.server;

import ru.minesweeper.server.controller.NetworkController;
import ru.minesweeper.server.controller.NetworkSender;
import ru.minesweeper.client.RecordsManager;
import ru.minesweeper.server.model.GameModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    final int port;
    final GameModel gameModel = new GameModel();
    final List<ClientHandler> clients = new ArrayList<>();
    final ExecutorService executor = Executors.newCachedThreadPool();
    final NetworkController networkController = new NetworkController(gameModel,this);
    final NetworkSender networkSender = new NetworkSender(this);

    int numberOfClients = 0;

    ServerSocket serverSocket;
    boolean isRunning;

    public GameServer(int port) {
        this.port = port;
        gameModel.addObserver(networkSender);
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public NetworkController getNetworkController() {
        return networkController;
    }

    public void start() throws IOException {
        isRunning = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            System.out.println("Server started on port " + port);

            while (isRunning) {
                if (clients.size() <=2) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(clientSocket, this, numberOfClients);
                        numberOfClients++;
                        clients.add(clientHandler);
                        executor.execute(clientHandler);
                    } catch (IOException e) {
                        if (isRunning) {
                            System.err.println("Accept error: " + e.getMessage());
                        }
                        // При нормальной остановке исключение игнорируется
                    }
                }
            }
        }
    }

    public int getCountOfClients() {
        return clients.size();
    }

    public void stop() {
        if (!isRunning) return;

        gameModel.stopAllTimers();

        isRunning = false;
        try {
            // Закрываем ServerSocket - это прервет accept()
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }

        // Закрываем все клиентские соединения
        synchronized (clients) {
            for (ClientHandler client : new ArrayList<>(clients)) {
                client.closeConnection();
            }
            clients.clear();
        }

        // Останавливаем ExecutorService
        executor.shutdown();
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }

}
package server;


import idgenerator.IdGenerator;
import logger.ChatLogger;
import server.model.ChatModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
    private boolean isRunning = false;
    private final int PORT = 8080;
    private final ArrayList<ClientHandler> clients = new ArrayList<>();
    private final ExecutorService clientsThreadPool = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;

    private final NetworkSender networkSender = new NetworkSender(this);
    private final ChatModel model = new ChatModel(networkSender);
    public void run() {
        try {
            isRunning = true;
            serverSocket = new ServerSocket(PORT);
            while (isRunning) {
                try {
                    Socket socket = serverSocket.accept(); // не реагирует на interrupt
                    ClientHandler client = new ClientHandler(socket, IdGenerator.newId(), model, this);
                    clientsThreadPool.execute(client);
                    clients.add(client);
                } catch (IOException | RejectedExecutionException e ) {
                    if (isRunning) {
                        ChatLogger.logger.error("Could not accept client");
                    }
                    break;
                }
            }
        }
        catch (IOException e) {
            if (isRunning) {
                ChatLogger.logger.error("Troubles with server socket");
                isRunning = false;
            }
            else {
                ChatLogger.logger.info("Stopping server");
            }
        }
    }


    synchronized public void stop() {
        clientsThreadPool.shutdown();
        for (ClientHandler client : clients) {
            if (client != null) {
                client.closeConnection();
            }
        }
        clients.clear();
        ChatLogger.logger.info("Stopping server");
        try {
            if (!clientsThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                List<Runnable> unfinishedTasks = clientsThreadPool.shutdownNow();
                ChatLogger.logger.warn("Завершено принудительно: " + unfinishedTasks.size() + " задач");
            }
        } catch (InterruptedException e) {
            clientsThreadPool.shutdownNow();
        }
        isRunning = false;
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            ChatLogger.logger.error("Could not close server");
        }
    }

    synchronized public ArrayList<ClientHandler> getClients() {
        return clients;
    }
}

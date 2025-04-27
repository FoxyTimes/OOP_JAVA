package ru.minesweeper.client;

import ru.minesweeper.server.GameServer;
import java.io.IOException;

public class HostManager {
    private GameServer server;
    private Thread serverThread;

    public HostManager(int port) {
        server = new GameServer(port);
    }

    public void hostStart() throws IOException {
        // Создаем отдельный поток для сервера
        serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                System.err.println("Ошибка сервера: " + e.getMessage());
                e.printStackTrace();
            }
        });

        serverThread.setName("GameServer Thread");
        serverThread.start();
    }

    public void stopHost() {
        if (server != null) {
            server.stop();
            serverThread.interrupt();
        }
        if (serverThread != null) {
            serverThread.interrupt();
        }
    }
}
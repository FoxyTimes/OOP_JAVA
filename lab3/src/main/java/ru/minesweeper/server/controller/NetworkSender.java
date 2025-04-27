package ru.minesweeper.server.controller;

import ru.minesweeper.server.GameServer;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observer;

import java.io.IOException;

public class NetworkSender implements Observer {
    GameServer server;

    public NetworkSender(GameServer server) {
        this.server = server;
    }

    @Override
    public void notify(Notifications notification, DataTransferObject data) {
        switch (notification) {
            case HOST_STARTED:
                for (int i=0; i<server.getClients().size(); i++) {
                    try {
                        server.getClients().get(i).sendInitialState(data);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                for (int i=0; i<server.getClients().size(); i++) {
                    server.getClients().get(i).sendResponse(notification, data);
                }
                break;
        }
        //server.getClients().forEach(client -> client.sendNotification(notification, data));
    }
}

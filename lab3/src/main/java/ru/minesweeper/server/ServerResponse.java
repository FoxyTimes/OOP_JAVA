package ru.minesweeper.server;

import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;
import java.io.Serializable;

public class ServerResponse implements Serializable {

    private final Notifications notification;
    private final DataTransferObject data;

    public ServerResponse(Notifications notification, DataTransferObject data) {
        this.notification = notification;
        this.data = data;
    }

    public Notifications getNotification() {
        return notification;
    }

    public DataTransferObject getData() {
        return data;
    }
}
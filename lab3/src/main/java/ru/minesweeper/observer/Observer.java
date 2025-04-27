package ru.minesweeper.observer;

import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;


public interface Observer {
    void notify(Notifications notification, DataTransferObject data);
}


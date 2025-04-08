package ru.minesweeper.observer;

import ru.minesweeper.model.entities.datatransfer.DataTransferObject;


public interface Observer {
    void notify(Notifications notification, DataTransferObject data);
}


package ru.minesweeper.observer;

import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Notifications notification, DataTransferObject data);
}

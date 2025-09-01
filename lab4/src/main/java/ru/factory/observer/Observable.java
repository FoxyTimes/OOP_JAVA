package ru.factory.observer;

import ru.factory.model.transfer.DataObject;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Notifications notification, DataObject data);
}
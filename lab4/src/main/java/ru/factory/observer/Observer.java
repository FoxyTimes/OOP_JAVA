package ru.factory.observer;

import ru.factory.model.transfer.DataObject;

public interface Observer {
    void sendNotify(Notifications notification, DataObject data);
}

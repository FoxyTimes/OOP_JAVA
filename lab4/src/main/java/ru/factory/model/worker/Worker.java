package ru.factory.model.worker;

import ru.factory.model.entities.Car;
import ru.factory.model.entities.CorePart;
import ru.factory.model.storage.Storage;
import ru.factory.model.transfer.DataObject;
import ru.factory.observer.Notifications;
import ru.factory.observer.Observable;
import ru.factory.observer.Observer;

import java.util.ArrayList;

public class Worker implements Runnable, Observable {
    private int creationTime;
    private final ArrayList<Storage<? extends CorePart>> storages;
    private final Storage<Car> carStorage;
    private final ArrayList<Observer> observers = new ArrayList<>();

    public Worker(int creationTime, ArrayList<Storage<? extends CorePart>> storages, Storage<Car> carStorage) {
        this.creationTime = creationTime;
        this.storages = storages;
        this.carStorage = carStorage;
    }

    public void setCreationTime(int creationTime) {
        this.creationTime = creationTime;
    }

    public Car produce() {
        ArrayList<CorePart> coreParts = new ArrayList<>();
        for (Storage<? extends CorePart> storage : storages) {
            coreParts.add(storage.poll());
        }
        return new Car(coreParts);
    }

    @Override
    public void run() {
        try {
            Car car = produce();
            Thread.sleep(creationTime);
            carStorage.add(car);
            notifyObservers(Notifications.CAR_CREATED, new DataObject.Builder()
                    .setWorker(this)
                    .build());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Notifications notification, DataObject data) {
        for (Observer observer : observers) {
            observer.sendNotify(notification, data);
        }
    }
}

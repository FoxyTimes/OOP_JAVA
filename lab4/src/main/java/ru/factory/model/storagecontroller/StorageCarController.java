package ru.factory.model.storagecontroller;

import ru.factory.model.Jobs;
import ru.factory.model.entities.Car;
import ru.factory.model.storage.Storage;
import ru.factory.model.transfer.DataObject;
import ru.factory.model.worker.WorkerManager;
import ru.factory.observer.Notifications;
import ru.factory.observer.Observer;

public class StorageCarController extends Thread implements Observer {
    Storage<Car> storage;
    WorkerManager workerManager;
    int limit;

    private final Object WAIT_LOCK = new Object();
    public StorageCarController(Storage<Car> storage, WorkerManager workerManager, int limit) {
        this.storage = storage;
        this.workerManager = workerManager;
        this.limit = limit;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (WAIT_LOCK) {
                if ((storage.getSize() + workerManager.getRemainingProduction() + workerManager.getWorkersBusy()) < limit) {
                    workerManager.addToProduce(limit-storage.getSize()-workerManager.getRemainingProduction()-workerManager.getWorkersBusy());
                }
                try {
                    WAIT_LOCK.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void sendNotify(Notifications notification, DataObject data) {
        synchronized (WAIT_LOCK) {
            switch (notification) {
                case OBJECT_REMOVED: {
                    WAIT_LOCK.notify();
                    break;
                }
            }
        }
    }
}

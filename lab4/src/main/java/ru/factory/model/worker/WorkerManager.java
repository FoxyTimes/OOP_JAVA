package ru.factory.model.worker;

import ru.factory.model.entities.Car;
import ru.factory.model.entities.CorePart;
import ru.factory.model.storage.Storage;
import ru.factory.model.threadpool.WorkersThreadPool;
import ru.factory.model.transfer.DataObject;
import ru.factory.observer.Notifications;
import ru.factory.observer.Observable;
import ru.factory.observer.Observer;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkerManager extends Thread implements Observer, Observable {
    ArrayList<Observer> observers = new ArrayList<>();

    AtomicInteger carsToProduce = new AtomicInteger(0);;
    BlockingQueue<Worker> workerQueue = new LinkedBlockingQueue<Worker>();
    ArrayList<Worker> allWorkers = new ArrayList<>();
    WorkersThreadPool workerPool;
    AtomicInteger workersBusy = new AtomicInteger(0);
    public WorkerManager(int numberOfThread, int numberOfWorkers, int timeToCreation, ArrayList<Storage<? extends CorePart>> partStorages, Storage<Car> carStorage) {
        createWorkers(numberOfWorkers, timeToCreation, partStorages, carStorage);
        workerPool = new WorkersThreadPool(numberOfThread);
    }


    synchronized private void createWorkers(int numberOfWorkers, int timeToCreation, ArrayList<Storage<? extends CorePart>> partStorages, Storage<Car> carStorage) {
        for (int i = 0; i < numberOfWorkers; i++) {
            Worker worker = new Worker(timeToCreation, partStorages, carStorage);
            worker.addObserver(this);
            workerQueue.add(worker);
            allWorkers.add(worker);
        }
    }

    synchronized public ArrayList<Worker> getWorkers() {
        return allWorkers;
    }

    public void addToProduce(int number) {
        carsToProduce.addAndGet(number);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            if (carsToProduce.get() > 0 && !workerQueue.isEmpty()) {
                Worker worker = workerQueue.poll();
                carsToProduce.decrementAndGet();
                notifyObservers(Notifications.PRODUCE_CAR_UPDATED, new DataObject.Builder()
                        .setToProduce(carsToProduce.get()+workersBusy.getAndIncrement()).build());
                workerPool.execute(worker);
            }
        }
    }

    public int getWorkersBusy() {
        return workersBusy.get();
    }

    public void addBusy() {
        workersBusy.incrementAndGet();
    }

    public void removeBusy() {
        workersBusy.decrementAndGet();
    }

    public void shutdown() {
        interrupt();
        workerPool.shutdown();
    }

    synchronized public int getRemainingProduction() {
        return carsToProduce.get();
    }

    @Override
    public void sendNotify(Notifications notification, DataObject data) {
        switch (notification) {
            case CAR_CREATED: {
                workerQueue.add(data.getWorker());
                workersBusy.decrementAndGet();
                break;
            }
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

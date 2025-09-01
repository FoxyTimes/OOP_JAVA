package ru.factory.model;

import ru.factory.model.dealer.Dealer;
import ru.factory.model.entities.*;
import ru.factory.model.storage.Storage;
import ru.factory.model.storagecontroller.StorageCarController;
import ru.factory.model.supplier.Supplier;
import ru.factory.model.transfer.DataObject;
import ru.factory.model.worker.WorkerManager;
import ru.factory.observer.Notifications;
import ru.factory.observer.Observable;
import ru.factory.observer.Observer;

import java.util.ArrayList;

public class Model implements Observable, Observer {
    ArrayList<Observer> observers = new ArrayList<>();

    //хранилища запчастей
    Storage<Body> bodyStorage;
    Storage<Motor> motorStorage;
    Storage<Accessory> accessoryStorage;
    Storage<Car> carStorage;

    ArrayList<Storage<? extends CorePart>> partStorages = new ArrayList<>();
    ArrayList<Storage<? extends CorePart>> allStorages = new ArrayList<>();

    //поставщики
    ArrayList<Supplier<Body>> bodySuppliers = new ArrayList<>();
    ArrayList<Supplier<Motor>> motorSuppliers = new ArrayList<>();
    ArrayList<Supplier<Accessory>> accessorySuppliers = new ArrayList<>();

    //времена
    ArrayList<Integer> timesToCreation;

    //работники
    WorkerManager workerManager;

    //дилеры
    ArrayList<Dealer> dealers;

    //контроллер склада
    StorageCarController storageCarController;

    //массив количества разных рабочих
    ArrayList<Integer> countOfDifferentJobs;
    public Model(
         ArrayList<Integer> storagesSizes,
         ArrayList<Integer> countOfSuppliers,
         ArrayList<Integer> timeToCreation,
         int numberOfThreads,
         int numberOfWorkers,
         int numberOfDealers,
         int storageControllerLimit,
         boolean logSale
    ) {

        timesToCreation = timeToCreation;

        countOfDifferentJobs = new ArrayList<>(countOfSuppliers);
        countOfDifferentJobs.add(numberOfWorkers);
        countOfDifferentJobs.add(numberOfDealers);

        createStorages(storagesSizes);
        createSuppliers(countOfSuppliers, timeToCreation);

        workerManager = new WorkerManager(numberOfThreads, numberOfWorkers, timeToCreation.get(Jobs.WORKER.getValue()), partStorages, carStorage);
        workerManager.addObserver(this);
        storageCarController = new StorageCarController(carStorage, workerManager, storageControllerLimit);

        carStorage.addObserver(storageCarController);

        workerManager.start();
        storageCarController.start();

        createDealers(numberOfDealers, carStorage, timeToCreation.get(Jobs.DEALER.getValue()), logSale);
        startDeal();

        startSupply();

    }

    public void shutDown() {
        shutDownSuppliers();
        workerManager.shutdown();
        storageCarController.interrupt();
        shutDownDealers();

    }

    private void shutDownDealers() {
        for (Dealer dealer : dealers) {
            dealer.interrupt();
        }
    }

    private void shutDownSuppliers() {
        for (Supplier<Body> supplier : bodySuppliers) {
            supplier.interrupt();
        }
        for (Supplier<Motor> supplier : motorSuppliers) {
            supplier.interrupt();
        }
        for (Supplier<Accessory> supplier : accessorySuppliers) {
            supplier.interrupt();
        }
    }

    private void startDeal() {
        for (Dealer dealer : dealers) {
            dealer.start();
        }
    }

    public void loadData() {
        notifyObservers(Notifications.LOAD_INFO, new DataObject.Builder()
                .setCountOfDifferentJobs(countOfDifferentJobs)
                .setTimesToCreation(timesToCreation)
                .build());
    }

    private void createDealers(int numberOfDealers, Storage<Car> carStorage, int sellTime, boolean logSale) {
        ArrayList<Dealer> dealers = new ArrayList<>();
        for (int i = 0; i < numberOfDealers; i++) {
            dealers.add(new Dealer(carStorage, sellTime, logSale, i));
        }
        this.dealers = dealers;
    }




    private void startSupply() {
        for (Supplier<Body> supplier : bodySuppliers) {
            supplier.start();
        }
        for (Supplier<Motor> motorSupplier : motorSuppliers) {
            motorSupplier.start();
        }
        for (Supplier<Accessory> accessorySupplier : accessorySuppliers) {
            accessorySupplier.start();
        }
    }


    private void createStorages(ArrayList<Integer> storagesSizes) {
        bodyStorage = new Storage<>(storagesSizes.get(Jobs.BODY.getValue()));
        motorStorage = new Storage<>(storagesSizes.get(Jobs.MOTORS.getValue()));
        accessoryStorage = new Storage<>(storagesSizes.get(Jobs.ACCESSORY.getValue()));
        carStorage = new Storage<>(storagesSizes.get(Jobs.CAR.getValue()));

        bodyStorage.addObserver(this);
        motorStorage.addObserver(this);
        accessoryStorage.addObserver(this);
        carStorage.addObserver(this);

        partStorages.add(bodyStorage);
        partStorages.add(motorStorage);
        partStorages.add(accessoryStorage);

        allStorages.add(bodyStorage);
        allStorages.add(motorStorage);
        allStorages.add(accessoryStorage);
        allStorages.add(carStorage);
    }

    private void createSuppliers(ArrayList<Integer> countOfSuppliers, ArrayList<Integer> timesToCreation) {
        for (int i = 0; i < countOfSuppliers.get(Jobs.BODY.getValue()); i++) {
            bodySuppliers.add(new Supplier<>(bodyStorage, timesToCreation.get(Jobs.BODY.getValue()), Body.class));
        }
        for (int i = 0; i < countOfSuppliers.get(Jobs.MOTORS.getValue()); i++) {
            motorSuppliers.add(new Supplier<>(motorStorage, timesToCreation.get(Jobs.MOTORS.getValue()), Motor.class));
        }
        for (int i = 0; i < countOfSuppliers.get(Jobs.ACCESSORY.getValue()); i++) {
            accessorySuppliers.add(new Supplier<>(accessoryStorage, timesToCreation.get(Jobs.ACCESSORY.getValue()), Accessory.class));
        }
    }

    public ArrayList<Integer> getStoragesSizes() {
        ArrayList<Integer> storagesSizes = new ArrayList<>();
        for (Storage<? extends CorePart> storage : allStorages) {
            storagesSizes.add(storage.getSize());
        }
        return storagesSizes;
    }

    public ArrayList<Integer> getMaxStoragesSizes() {
        ArrayList<Integer> maxStoragesSizes = new ArrayList<>();
        for (Storage<? extends CorePart> storage : allStorages) {
            maxStoragesSizes.add(storage.getMaxSize());
        }
        return maxStoragesSizes;
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

    @Override
    synchronized public void sendNotify(Notifications notification, DataObject data) {
        switch (notification) {
            case PROGRESS_UPDATED: {
                DataObject dataObject = new DataObject.Builder()
                        .setFullness(getStoragesSizes())
                        .setMaxSize(getMaxStoragesSizes())
                        .setTotalCars(carStorage.getTotalParts())
                        .build();
                notifyObservers(notification, dataObject);
                break;
            }
            case PRODUCE_CAR_UPDATED: {
                notifyObservers(Notifications.PRODUCE_CAR_UPDATED, data);
            }
        }
    }

    public void valueWasChanged(int id, int value, Jobs job) {
        switch (job) {
            case BODY: {
                bodySuppliers.get(id).setCreationTime(value);
                break;
            }
            case MOTORS: {
                motorSuppliers.get(id).setCreationTime(value);
                break;
            }
            case ACCESSORY: {
                accessorySuppliers.get(id).setCreationTime(value);
                break;
            }
            case WORKER: {
                workerManager.getWorkers().get(id).setCreationTime(value);
                break;
            }
            case DEALER: {
                dealers.get(id).setSellTime(value);
                break;
            }
        }
    }
}

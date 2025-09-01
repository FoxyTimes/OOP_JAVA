package ru.factory.controller;

import ru.factory.logger.FactoryLogger;
import ru.factory.model.Jobs;
import ru.factory.model.Model;

import java.util.ArrayList;

public class FactoryController {
    Config config = new Config();

    Model model;
    public FactoryController() {}
    public void start(String configFile) {
        FactoryLogger.init();

        try {
            config.load(configFile);
        }
        catch (Exception e) {
            FactoryLogger.logger.error(e);
            return;
        }
        //размеры складов
        ArrayList<Integer> storagesSizes = new ArrayList<>();
        storagesSizes.add(config.getValue("StorageBodySize", Integer.class));
        storagesSizes.add(config.getValue("StorageMotorSize", Integer.class));
        storagesSizes.add(config.getValue("StorageAccessorySize", Integer.class));
        storagesSizes.add(config.getValue("StorageCarSize", Integer.class));
        //количества поставщиков
        ArrayList<Integer> countOfSuppliers = new ArrayList<>();
        countOfSuppliers.add(config.getValue("BodySuppliers", Integer.class));
        countOfSuppliers.add(config.getValue("MotorSuppliers", Integer.class));
        countOfSuppliers.add(config.getValue("AccessorySuppliers", Integer.class));
        //время задержки
        ArrayList<Integer> timesCreations = new ArrayList<>();
        timesCreations.add(config.getValue("BodyTimeCreation", Integer.class));
        timesCreations.add(config.getValue("MotorTimeCreation", Integer.class));
        timesCreations.add(config.getValue("AccessoryTimeCreation", Integer.class));
        timesCreations.add(config.getValue("CarTimeCreation", Integer.class));
        timesCreations.add(config.getValue("SellTime", Integer.class));

        int numberOfThreads = config.getValue("WorkPlaces", Integer.class);
        int numberOfWorkers = config.getValue("Workers", Integer.class);
        int numberOfDealers = config.getValue("Dealers", Integer.class);
        int storageControllerLimit = config.getValue("StorageControllerLimit", Integer.class);

        boolean logSale = config.getValue("LogSale", Boolean.class);

        this.model = new Model(
                storagesSizes,
                countOfSuppliers,
                timesCreations,
                numberOfThreads,
                numberOfWorkers,
                numberOfDealers,
                storageControllerLimit,
                logSale
        );
    }

    public void loadData() {
        model.loadData();
    }

    public Model getModel() {
        return model;
    }

    public void valueWasChanged(int id, int value, Jobs job) {
        model.valueWasChanged(id, value, job);
    }

    public void shutDown() {
        model.shutDown();
    }
}

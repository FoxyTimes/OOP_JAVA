package ru.factory.model.dealer;

import ru.factory.controller.FactoryController;
import ru.factory.logger.FactoryLogger;
import ru.factory.model.entities.Car;
import ru.factory.model.storage.Storage;

public class Dealer extends Thread {
    Storage<Car> storage;
    int sellTime;
    boolean logSale;
    int id;
    public Dealer(Storage<Car> storage, int sellTime, boolean logSale, int id) {
        this.storage = storage;
        this.sellTime = sellTime;
        this.logSale = logSale;
        this.id = id;
    }

     public void sell() {
        Car car = storage.poll();
        try {
            if (logSale) {
                FactoryLogger.logger.info("Dealer<{}>: Sold CarID={} (Body={}, Motor={}, Accessory={})",
                        id,
                        car.getId(),
                        car.getBody().getId(),
                        car.getMotor().getId(),
                        car.getAccessory().getId());
            }
        }
        catch (Exception e) {
            return;
        }
    }

    public void setSellTime(int sellTime) {
        this.sellTime = sellTime;
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                sell();
                Thread.sleep(sellTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

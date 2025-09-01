package ru.factory.model.transfer;

import ru.factory.model.worker.Worker;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DataObject {
    private final ArrayList<Integer> fullnessOfStorage;
    private final ArrayList<Integer> maxSizeOfStorage;
    private final Worker worker;
    private final ArrayList<Integer> countOfDifferentJobs;
    private final ArrayList<Integer> timesToCreation;
    private final int totalCars;
    private final int toProduce;

    private DataObject(Builder builder) {
        this.fullnessOfStorage = builder.fullnessOfStorage;
        this.maxSizeOfStorage = builder.maxSizeOfStorage;
        this.worker = builder.worker;
        this.countOfDifferentJobs = builder.countOfDifferentJobs;
        this.timesToCreation = builder.timesToCreation;
        this.totalCars = builder.totalCars;
        this.toProduce = builder.toProduce;
    }


    public static class Builder {
        private ArrayList<Integer> fullnessOfStorage;
        private ArrayList<Integer> maxSizeOfStorage;
        private Worker worker;
        private ArrayList<Integer> countOfDifferentJobs;
        private ArrayList<Integer> timesToCreation;
        private int totalCars;
        private int toProduce;

        public Builder setFullness(ArrayList<Integer> fullness) {
            this.fullnessOfStorage = new ArrayList<>(fullness);
            return this;
        }

        public Builder setMaxSize(ArrayList<Integer> maxSize) {
            this.maxSizeOfStorage = new ArrayList<>(maxSize);
            return this;
        }

        public Builder setWorker(Worker worker) {
            this.worker = worker;
            return this;
        }

        public Builder setToProduce(int toProduce) {
            this.toProduce = toProduce;
            return this;
        }

        public Builder setCountOfDifferentJobs(ArrayList<Integer> countOfDifferentJobs) {
            this.countOfDifferentJobs = new ArrayList<>(countOfDifferentJobs);
            return this;
        }

        public Builder setTimesToCreation(ArrayList<Integer> timesToCreation) {
            this.timesToCreation = new ArrayList<>(timesToCreation);
            return this;
        }

        public Builder setTotalCars(AtomicInteger totalCars) {
            this.totalCars = totalCars.get();
            return this;
        }

        public DataObject build() {
            return new DataObject(this);
        }

    }

    public ArrayList<Integer> getFullnessOfStorage() {
        return fullnessOfStorage;
    }

    public ArrayList<Integer> getMaxSizeOfStorage() {
        return maxSizeOfStorage;
    }

    public Worker getWorker() {
        return worker;
    }

    public ArrayList<Integer> getCountOfDifferentJobs() {
        return countOfDifferentJobs;
    }

    public ArrayList<Integer> getTimesToCreation() {
        return timesToCreation;
    }

    public int getTotalCars() {
        return totalCars;
    }

    public int getToProduce() {
        return toProduce;
    }

}

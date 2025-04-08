package ru.minesweeper.model;

import ru.minesweeper.Difficulties;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.model.entities.Field;
import ru.minesweeper.observer.Observable;
import ru.minesweeper.observer.Observer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameModel implements Observable {
    LinkedList<Observer> observers = new LinkedList<>();
    Difficulties difficulty = Difficulties.NONE;

    Field field;

    final int EASY_WIDTH = 9, EASY_HEIGHT = 9, EASY_MIN_RADIUS = 2, EASY_MAX_RADIUS = 3, EASY_COUNT_BOMBS = 10;

    final int NORMAL_WIDTH = 18, NORMAL_HEIGHT = 18, NORMAL_MIN_RADIUS = 3, NORMAL_MAX_RADIUS = 4, NORMAL_COUNT_BOMBS = 40;

    final int HARD_WIDTH = 27, HARD_HEIGHT = 27, HARD_MIN_RADIUS = 4, HARD_MAX_RADIUS = 5, HARD_COUNT_BOMBS = 90;

    int time=0;

    private TimerThread timerThread;
    private volatile boolean isTimerRunning = false;

    public GameModel() {
    }

    public void createFillField(int startX, int startY) {
        switch (difficulty) {
            case EASY:
                field.generateField(startX, startY, EASY_MIN_RADIUS, EASY_MAX_RADIUS, EASY_COUNT_BOMBS);
                notifyObservers(Notifications.GAME_CHANGED, createDataTransferObject(field, time));
                break;
            case NORMAL:
                field.generateField(startX, startY, NORMAL_MIN_RADIUS, NORMAL_MAX_RADIUS, NORMAL_COUNT_BOMBS);
                notifyObservers(Notifications.GAME_CHANGED, createDataTransferObject(field, time));
                break;
            case HARD:
                field.generateField(startX, startY, HARD_MIN_RADIUS, HARD_MAX_RADIUS, HARD_COUNT_BOMBS);
                notifyObservers(Notifications.GAME_CHANGED, createDataTransferObject(field, time));
                break;
        }

        time = 0;
        startTimer();
    }

    private void startTimer() {
        stopTimer(); // Останавливаем предыдущий таймер, если был

        isTimerRunning = true;
        timerThread = new TimerThread();
        timerThread.start();
    }

    public void stopTimer() {
        isTimerRunning = false;
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
    }

    private class TimerThread extends Thread {
        @Override
        public void run() {
            try {
                while (isTimerRunning && !isInterrupted()) {
                    Thread.sleep(1000); // Ждем 1 секунду
                    time++;
                    notifyObservers(Notifications.TIME_UPDATED, createDataTransferObject(field, time));
                }
            } catch (InterruptedException e) {
                // Поток был прерван - это нормально при остановке таймера
                Thread.currentThread().interrupt();
            }
        }
    }

    public void createEmptyField() {
        switch(difficulty) {
            case EASY:
                field = new Field(EASY_HEIGHT, EASY_WIDTH);
                notifyObservers(Notifications.GAME_STARTED, createDataTransferObject(field, time));
                break;
            case NORMAL:
                field = new Field(NORMAL_HEIGHT, NORMAL_WIDTH);
                notifyObservers(Notifications.GAME_STARTED, createDataTransferObject(field, time));
                break;
            case HARD:
                field = new Field(HARD_HEIGHT, HARD_WIDTH);
                notifyObservers(Notifications.GAME_STARTED, createDataTransferObject(field, time));
                break;
            case NONE:
                notifyObservers(Notifications.GAME_EXECUTED, createDataTransferObject(field, time));
                break;
        }
    }

    public void openCell(int x, int y) {
        notifyObservers(field.openCell(x, y), createDataTransferObject(field, time));
    }

    public DataTransferObject createDataTransferObject(Field field, int time) {
        return new DataTransferObject(field, time);
    }
    public DataTransferObject createDataTransferObject(Map<String, List<Integer>> records)  {
        return new DataTransferObject(records);
    }

    public void setDifficulty(Difficulties difficulty) {
        this.difficulty = difficulty;
    }

    public void updateRecords(Map<String, List<Integer>> records) {
        notifyObservers(Notifications.RECORDS_UPDATED, createDataTransferObject(records));
    };

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Notifications notification, DataTransferObject data) {
        for (Observer observer : observers) {
            observer.notify(notification, data);
        }
    }

    public Difficulties getDifficulty() {
        return difficulty;
    }
}

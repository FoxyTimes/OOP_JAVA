package ru.minesweeper.server.model;

import ru.minesweeper.observer.Notifications;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.server.model.entities.Field;
import ru.minesweeper.observer.Observable;
import ru.minesweeper.observer.Observer;

import java.util.ArrayList;
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

    int timePlayer1=0;
    int timePlayer2=0;

    int countTurns=0;

    boolean isBombed=false;

    private TimerThreadPlayer1 timerThreadPlayer1;
    private TimerThreadPlayer2 timerThreadPlayer2;
    private volatile boolean isTimerRunningPlayer1 = false;
    private volatile boolean isTimerRunningPlayer2 = false;

    ArrayList<String> playerNames = new ArrayList<>();

    public GameModel() {
    }

    public void createFillField(int startX, int startY, boolean isHostTurn) {
        switch (difficulty) {
            case EASY:
                field.generateField(startX, startY, EASY_MIN_RADIUS, EASY_MAX_RADIUS, EASY_COUNT_BOMBS);
                notifyObservers(Notifications.GAME_CHANGED, createDataTransferObject(field, isHostTurn, playerNames));
                break;
            case NORMAL:
                field.generateField(startX, startY, NORMAL_MIN_RADIUS, NORMAL_MAX_RADIUS, NORMAL_COUNT_BOMBS);
                notifyObservers(Notifications.GAME_CHANGED, createDataTransferObject(field, isHostTurn, playerNames));
                break;
            case HARD:
                field.generateField(startX, startY, HARD_MIN_RADIUS, HARD_MAX_RADIUS, HARD_COUNT_BOMBS);
                notifyObservers(Notifications.GAME_CHANGED, createDataTransferObject(field, isHostTurn, playerNames));
                break;
        }

        timePlayer1=0;
        timePlayer2=0;
        startTimerPlayer1();
    }

    public void startTimerPlayer1() {
        stopTimerPlayer1(); // Останавливаем предыдущий таймер, если был

        isTimerRunningPlayer1 = true;
        timerThreadPlayer1 = new TimerThreadPlayer1();
        timerThreadPlayer1.start();
    }

    public void stopTimerPlayer1() {
        isTimerRunningPlayer1 = false;
        if (timerThreadPlayer1 != null) {
            timerThreadPlayer1.interrupt();
            timerThreadPlayer1 = null;
        }
    }

    public void startTimerPlayer2() {
        stopTimerPlayer2(); // Останавливаем предыдущий таймер, если был

        isTimerRunningPlayer2 = true;
        timerThreadPlayer2 = new TimerThreadPlayer2();
        timerThreadPlayer2.start();
    }

    public void stopTimerPlayer2() {
        isTimerRunningPlayer2 = false;
        if (timerThreadPlayer2 != null) {
            timerThreadPlayer2.interrupt();
            timerThreadPlayer2 = null;
        }
    }


    public void addPlayer(String nickname) {
        playerNames.add(nickname);
        notifyObservers(Notifications.LOBBY_NICKNAMES_UPDATED, createDataTransferObject(playerNames));
    }

    public void removePlayer(String nickname) {
        playerNames.remove(nickname);
        notifyObservers(Notifications.LOBBY_NICKNAMES_UPDATED, createDataTransferObject(playerNames));
    }

    public void stopAllTimers() {
        stopTimerPlayer1();
        stopTimerPlayer2();
    }


    private class TimerThreadPlayer1 extends Thread {
        @Override
        public void run() {
            try {
                while (isTimerRunningPlayer1 && !isInterrupted()) {
                    Thread.sleep(1000); // Ждем 1 секунду
                    timePlayer1++;
                    notifyObservers(Notifications.TIME_UPDATED, createDataTransferObject(timePlayer1, timePlayer2, false));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private class TimerThreadPlayer2 extends Thread {
        @Override
        public void run() {
            try {
                while (isTimerRunningPlayer2 && !isInterrupted()) {
                    Thread.sleep(1000); // Ждем 1 секунду
                    timePlayer2++;
                    notifyObservers(Notifications.TIME_UPDATED, createDataTransferObject(timePlayer1, timePlayer2, false));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void createEmptyField(boolean isHostTurn) {
        switch(difficulty) {
            case EASY:
                field = new Field(EASY_HEIGHT, EASY_WIDTH);
                notifyObservers(Notifications.GAME_STARTED, createDataTransferObject(field, isHostTurn, playerNames));
                break;
            case NORMAL:
                field = new Field(NORMAL_HEIGHT, NORMAL_WIDTH);
                notifyObservers(Notifications.GAME_STARTED, createDataTransferObject(field, isHostTurn, playerNames));
                break;
            case HARD:
                field = new Field(HARD_HEIGHT, HARD_WIDTH);
                notifyObservers(Notifications.GAME_STARTED, createDataTransferObject(field, isHostTurn, playerNames));
                break;
            case NONE:
                notifyObservers(Notifications.HOST_STARTED, createDataTransferObject(field, isHostTurn, playerNames));
                break;
        }
    }

    public void openCell(int x, int y, boolean isHostTurn) {
        countTurns++;
        Notifications notification = field.openCell(x, y);
        if (notification==Notifications.GAME_RUINED) {
            isBombed = true;
        }
        notifyObservers(notification, createDataTransferObject(field, isHostTurn, playerNames));
    }

    public String whoWin(boolean isHostTurn) {
        if (playerNames.size()>=2) {
            if (isBombed) {
                if (isHostTurn) {
                    return playerNames.get(1);
                }
                return playerNames.get(0);
            }
            if (timePlayer1 <= timePlayer2) {
                return playerNames.get(0);
            }
            return playerNames.get(1);
        }
        return null;
    }

    public DataTransferObject createDataTransferObject(Field field, boolean isHostTurn, ArrayList<String> playerNames) {
        if (countTurns==0) {
            return new DataTransferObject(field, isHostTurn, playerNames, whoWin(isHostTurn));
        }
        return new DataTransferObject(field, !isHostTurn, playerNames, whoWin(isHostTurn));
    }

    public DataTransferObject createDataTransferObject(ArrayList<String> nickNames) {
        return new DataTransferObject(nickNames);
    }

    public DataTransferObject createDataTransferObject(int timePlayer1, int timePlayer2, boolean isSingle) {
        return new DataTransferObject(timePlayer1, timePlayer2, isSingle);
    }

    public void setDifficulty(Difficulties difficulty) {
        this.difficulty = difficulty;
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
    public void notifyObservers(Notifications notification, DataTransferObject data) {
        for (Observer observer : observers) {
            observer.notify(notification, data);
        }
    }

    //NETWORK CODE

    public void sendInitialState() {
        notifyObservers(Notifications.HOST_STARTED, createDataTransferObject(field, true, null));
    }
}

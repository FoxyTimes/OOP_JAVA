package ru.minesweeper.client;

import ru.minesweeper.server.model.Difficulties;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observable;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.server.CommandType;
import ru.minesweeper.server.ServerResponse;
import ru.minesweeper.client.view.gui.GameView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientController implements Observable {
    Socket socket;
    ObjectOutputStream output;
    ObjectInputStream input;
    final List<Observer> observers = new ArrayList<>();
    boolean isConnected;

    boolean isIHost=false;

    Difficulties difficulty;

    RecordsManager recordsManager;
    HostManager hostManager;


    public ClientController() {
        recordsManager = new RecordsManager();
    }

    public void executeConnection(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
            this.isConnected = true;
            startListeningThread();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void madeHost(String adress, int port) {
        hostManager = new HostManager(port);
        try {
            hostManager.hostStart();
            System.out.println("Host started");
            isIHost=true;
            int attempts = 0;
            while (attempts < 10) {
                try {
                    executeConnection(adress, port);
                    break;
                } catch (Exception e) {
                    attempts++;
                    System.err.println("Attempt " + attempts + ": " + e.getMessage());
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Host start or connection failed: " + e.getMessage());
        }
    }

    private void startListeningThread() {
        new Thread(() -> {
            try {
                while (isConnected) {
                    ServerResponse response = (ServerResponse) input.readObject();
                    if (response.getNotification()==Notifications.GAME_CHANGED&&isIHost) {
                        notifyObservers(Notifications.GAME_CHANGED_I_HOST, response.getData());
                    }
                    else if (response.getNotification()==Notifications.GAME_CHANGED&&!isIHost) {
                        notifyObservers(Notifications.GAME_CHANGED_I_CLIENT, response.getData());
                    }
                    if (response.getNotification()==Notifications.GAME_WINED&&isIHost) {
                        notifyObservers(Notifications.GAME_WINED_I_HOST, response.getData());
                    }
                    else if (response.getNotification()==Notifications.GAME_WINED&&!isIHost) {
                        notifyObservers(Notifications.GAME_WINED_I_CLIENT, response.getData());
                    }
                    if (response.getNotification()==Notifications.GAME_RUINED&&isIHost) {
                        notifyObservers(Notifications.GAME_RUINED_I_HOST, response.getData());
                    }
                    else if (response.getNotification()==Notifications.GAME_RUINED&&!isIHost) {
                        notifyObservers(Notifications.GAME_RUINED_I_CLIENT, response.getData());
                    }
                    else {
                        notifyObservers(response.getNotification(), response.getData());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                disconnect();
                notifyObservers(Notifications.HOST_DISCONNECTED, new DataTransferObject());
            }
        }).start();
    }


    public void createEmptyField(Difficulties difficulty) {
        this.difficulty=difficulty;
        sendCommand(new ClientCommand(CommandType.START_GAME, difficulty));
    }

    public void openCell(int x, int y) {
        sendCommand(new ClientCommand(CommandType.OPEN_CELL, x, y));
    }

    public void createFillField(int startX, int startY) {
        sendCommand(new ClientCommand(CommandType.GENERATE_FIELD, startX, startY));
    }

    public void GameOver() {
        sendCommand(new ClientCommand(CommandType.GAME_OVER));
    }


    public void connectToLobby(String nickname) {
        if (Objects.equals(nickname, "")) {
            sendCommand(new ClientCommand(CommandType.CONNECT_TO_LOBBY, "nickname"));
        }
        else {
            sendCommand(new ClientCommand(CommandType.CONNECT_TO_LOBBY, nickname));
        }
    }

    public void disconnectFromLobby(String nickname) {
        if (Objects.equals(nickname, "")) {
            sendCommand(new ClientCommand(CommandType.DISCONNECT_FROM_LOBBY, "nickname"));
        }
        else {
            sendCommand(new ClientCommand(CommandType.DISCONNECT_FROM_LOBBY, nickname));
        }
    }

    private void sendCommand(ClientCommand command) {
        try {
            output.writeObject(command);
            output.flush();
        } catch (IOException e) {
            disconnect();
        }
    }

    public void notifyObservers(Notifications notification, DataTransferObject data) {
        for (Observer observer : observers) {
            observer.notify(notification, data);
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }

    public void stopHost() {
        if (hostManager != null) {
            hostManager.stopHost();
        }
        isIHost=false;
    }

    public void sendRecord(int time) {
        recordsManager.sendRecord(time, difficulty);
        updateRecords();
    }


    public void updateRecords() {
        notifyObservers(Notifications.RECORDS_UPDATED, new DataTransferObject(recordsManager.readRecords()));
    }
}
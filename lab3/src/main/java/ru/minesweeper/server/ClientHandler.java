package ru.minesweeper.server;

import ru.minesweeper.client.ClientCommand;
import ru.minesweeper.server.controller.NetworkController;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    final Socket clientSocket;
    final GameServer server;
    ObjectInputStream input;
    ObjectOutputStream output;
    boolean isConnected;

    int clientID;
    public ClientHandler(Socket socket, GameServer server, int clientID) {
        this.clientSocket = socket;
        this.server = server;
        this.isConnected = true;
        this.clientID = clientID;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());

            server.getNetworkController().sendInitialState();

            while (isConnected) {
                ClientCommand received = (ClientCommand) input.readObject();
                processCommand(received);
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        }
        closeConnection();
        server.removeClient(this);
    }

    public void sendInitialState(DataTransferObject data) throws IOException {
        output.writeObject(new ServerResponse(Notifications.HOST_STARTED, data));
        output.reset();
        output.flush();
    }

    private void processCommand(ClientCommand command) {
        NetworkController controller = server.getNetworkController();
        switch (command.getType()) {
            case START_GAME:
                controller.createEmptyField(command.getData().getDifficulty(), clientID);
                break;
            case DISCONNECT_FROM_LOBBY:
                controller.removePlayer(command.getData().getNickname());
                break;
            case CONNECT_TO_LOBBY:
                controller.addPlayer(command.getData().getNickname());
                break;
            case OPEN_CELL:
                controller.openCell(command.getData().getX(), command.getData().getY(), clientID);
                break;
            case GENERATE_FIELD:
                controller.createFillField(command.getData().getX(), command.getData().getY(), clientID);
                break;
            case GAME_OVER:
                controller.gameOver();
                break;
        }
    }


    public synchronized void sendResponse(Notifications notification, DataTransferObject data) {

        try {
            ServerResponse response = new ServerResponse(notification, data);
            output.writeObject(response);
            output.reset();
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending to client: " + e.getMessage());
            closeConnection();
        }
    }

    synchronized void closeConnection() {
        isConnected = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
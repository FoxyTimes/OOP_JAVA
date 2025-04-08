/*package ru.minesweeper.server;

import ru.minesweeper.Difficulties;
import ru.minesweeper.controller.GameController;
import ru.minesweeper.model.GameModel;
import ru.minesweeper.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final GameServer server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean isConnected;

    public ClientHandler(Socket socket, GameServer server) {
        this.clientSocket = socket;
        this.server = server;
        this.isConnected = true;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());

            sendInitialState();

            while (isConnected) {
                Object received = input.readObject();
                if (received instanceof ClientCommand) {
                    processCommand((ClientCommand) received);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        } finally {
            closeConnection();
            server.removeClient(this);
        }
    }

    private void sendInitialState() throws IOException {
        GameModel model = server.getGameModel();
        output.writeObject(new ServerResponse(
                Notifications.GAME_STARTED,
                model.createDataTransferObject(model.getField(), model.getTime())
        ));
    }

    private void processCommand(ClientCommand command) {
        GameController controller = new GameController(server.getGameModel());

        switch (command.getType()) {
            case START_GAME:
                controller.createEmptyField((Difficulties) command.getData());
                break;
            case OPEN_CELL:
                int[] coords = (int[]) command.getData();
                controller.openCell(coords[0], coords[1]);
                break;
            case GENERATE_FIELD:
                int[] startCoords = (int[]) command.getData();
                controller.createFillField(startCoords[0], startCoords[1]);
                break;
            case SEND_RECORD:
                controller.sendRecord((Integer) command.getData());
                break;
            case REQUEST_RECORDS:
                controller.updateRecords();
                break;
        }
    }

    public void sendNotification(Notifications notification, DataTransferObject data) {
        try {
            output.writeObject(new ServerResponse(notification, data));
        } catch (IOException e) {
            System.err.println("Error sending to client: " + e.getMessage());
            closeConnection();
        }
    }

    private void closeConnection() {
        isConnected = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}*/
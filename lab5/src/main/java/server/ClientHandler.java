package server;

import logger.ChatLogger;
import protocolparser.ProtocolParser;
import protocolparser.Serial.command.Command;
import protocolparser.Serial.response.Response;
import server.model.ChatModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final long clientID;
    private final ChatModel chatModel;
    private final Server server;
    private boolean isConnected=true;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientHandler(Socket clientSocket, long clientID, ChatModel model, Server server) {
        this.clientSocket = clientSocket;
        this.clientID = clientID;
        this.chatModel = model;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());


            while(!Thread.currentThread().isInterrupted()&&isConnected) {
                String received = (String) input.readObject();
                processCommand(received);
            }


        } catch (IOException e) {
            closeAndRemove();
            chatModel.logout(clientID);
            ChatLogger.logger.error("Client " + clientID + " is disconnected");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void processCommand(String received) {
        Command command = ProtocolParser.unpackXMLClientMessage(received);
        switch (Objects.requireNonNull(command).getCommandName()) {
            case LOGIN:
                chatModel.registerUser(clientID, command.getName());
                break;
            case LIST:
                chatModel.requestUsersList(clientID);
                break;
            case MESSAGE:
                chatModel.addMessage(clientID, command.getMessage());
                break;
            case PING:
                chatModel.ping(clientID);
                break;
            case LOGOUT:
                closeAndRemove();
                chatModel.logout(clientID);
                break;
            case REQUEST_CHAT:
                chatModel.sendRequestedChat(clientID, command.getMinMessageId());
                break;
            default:
                break;
        }
    }

    public synchronized void sendResponse(Response response) {
        String responseMessage = ProtocolParser.createXMLResponseMessage(response);

        try {
            output.writeObject(responseMessage);
            output.reset();
            output.flush();
        } catch (IOException e) {
            ChatLogger.logger.error("Client " + clientID + "not connected");
            chatModel.logout(clientID);
            closeAndRemove();
        }
    }

    public void closeAndRemove() {
        closeConnection();
        server.getClients().remove(this);
    }

    public void closeConnection() {
        isConnected = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            ChatLogger.logger.error("Client " + clientID + " disconnected");
        }
    }

    public long getClientID() {
        return clientID;
    }
}

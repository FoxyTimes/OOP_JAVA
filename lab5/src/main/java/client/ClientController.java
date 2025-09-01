package client;

import logger.ChatLogger;
import observer.Observable;
import observer.Observer;
import protocolparser.*;
import protocolparser.Serial.command.Command;
import protocolparser.Serial.response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientController implements Observable {
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    boolean isConnected;

    int TIMEOUT = 5000;
    boolean pongReceived;

    private Thread pingThread;
    private Thread listenThread;

    ArrayList<Observer> observers = new ArrayList<>();

    public void setupConnection(String ip, int port, Command command) {
        if (socket != null) {
            if (!socket.isConnected()||socket.isClosed()) {
                closeConnection();
            }
        }
        if (!isConnected) {
            try  {
                socket = new Socket(ip, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                isConnected = true;
                sendCommand(command);
                startPingingThread();
                startListeningThread();
            }
            catch (Exception e) {
                closeConnection();
                ChatLogger.logger.error("Error in setting up connection");
            }
        }
        else {
            ChatLogger.logger.error("Connection already set");
        }
    }

    public void closeConnection() {
        ChatLogger.logger.info("Connection is closing");

        try {
            sendCommand(new Command.Builder()
                    .setCommandName(ClientCommand.LOGOUT)
                    .build());
        } catch (Exception e) {
            ChatLogger.logger.error("error in sending the exit command to the server");
        }


        isConnected = false;


        if (pingThread != null) {
            pingThread.interrupt();
        }
        if (listenThread != null) {
            listenThread.interrupt();
        }

        try {
            if (socket != null) {
                socket.close();
                ChatLogger.logger.info("Socket closed");
            }
        } catch (IOException e) {
            ChatLogger.logger.error("Error closing socket", e);
        }

        notifyObservers(new Response.Builder()
                .setResponseName(ServerResponse.DISCONNECT)
                .build());

    }

    private void startPingingThread() {
        pingThread = new Thread(() -> {
            while (isConnected) {
                try {
                    Thread.sleep(TIMEOUT);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                pongReceived = false;
                sendCommand(new Command.Builder().setCommandName(ClientCommand.PING).build());

                try {
                    // ждём ответ 1 секунду
                    int waited = 0;
                    while (waited < TIMEOUT && !pongReceived) {
                        Thread.sleep(100);
                        waited += 100;
                    }

                    if (!pongReceived) {
                        ChatLogger.logger.error("No pong received from server, closing connection...");
                        closeConnection();
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        pingThread.start();
    }

    private void startListeningThread() {
        listenThread = new Thread(() -> {
            try {
                while (isConnected) {
                    String responseString = (String) in.readObject();
                    ChatLogger.logger.info("Received response: " + responseString);
                    Response response = ProtocolParser.unpackXMLResponseMessage(responseString);
                    assert response != null;
                    if (response.getStatus()==Status.OK) {
                        if (response.getResponseName()==ServerResponse.PONG) {
                            pongReceived = true;
                            ChatLogger.logger.info("Pong received from server");
                        }
                        else {
                            notifyObservers(response);
                        }
                    }
                    else {
                        ChatLogger.logger.error("status is error");
                    }


                }
            } catch (IOException | ClassNotFoundException e) {
                ChatLogger.logger.error("closing ListeningThread");
            }
        });
        listenThread.start();
    }

    public void sendCommand(Command command) {
        if (out == null) {
            ChatLogger.logger.error("Output stream is not initialized");
            return;
        }

        if (isConnected) {
            String message = ProtocolParser.createXMLClientMessage(command);
            try {
                out.writeObject(message);
                out.reset();
                out.flush();
            }
            catch (IOException e) {
                ChatLogger.logger.error("Error sending command");
            }
        }
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
    public void notifyObservers(Response response) {
        for (Observer observer : observers) {
            observer.sendNotify(response);
        }
    }

}

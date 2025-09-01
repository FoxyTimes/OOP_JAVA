package server.model;

import logger.ChatLogger;
import observer.Observable;
import observer.Observer;
import protocolparser.Serial.response.Event;
import protocolparser.Serial.response.Response;
import protocolparser.ServerResponse;
import protocolparser.Status;
import server.SystemMessage;
import server.UsersContext;
import server.NetworkSender;

import java.util.ArrayList;

import static server.Constans.SYSTEM_ID;

public class ChatModel implements Observable {
    private final ChatContext chatContext = new ChatContext();
    private final UsersContext usersContext = new UsersContext();

    private final ArrayList<Observer> observers = new ArrayList<>();

    NetworkSender sender;

    public ChatModel(NetworkSender sender) {
        this.sender = sender;
        usersContext.addClient(-1, "system");
        observers.add(sender);
    }

    synchronized public void addMessage(long clientID, String message) {
        chatContext.addMessage(message, usersContext.getClientInfo(clientID));
        notifyObservers(new Response.Builder()
                .setStatus(Status.OK)
                .setResponseName(ServerResponse.CHAT)
                .setEvent(new Event.Builder()
                        .setMessage(new ChatContext(chatContext, 1))
                        .build())
                .build());
    }

    synchronized public void registerUser(long clientID, String username) {
        usersContext.addClient(clientID, username);
        addSystemMessage(clientID, SystemMessage.JOINED);
        notifyObservers(new Response.Builder()
                .setStatus(Status.OK)
                .setResponseName(ServerResponse.REGISTRATION)
                .setClientID(clientID)
                .build());
        notifyObservers(new Response.Builder()
                .setStatus(Status.OK)
                .setResponseName(ServerResponse.CHAT)
                .setEvent(new Event.Builder()
                        .setMessage(new ChatContext(chatContext, 11))
                        .build())
                .build());
    }

    synchronized public void ping(long clientID) {
        ChatLogger.logger.info("Pinging chat session");
        notifyObservers(new Response.Builder()
                .setResponseName(ServerResponse.PONG)
                .setStatus(Status.OK)
                .setClientID(clientID)
                .build());
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
    synchronized public void notifyObservers(Response response) {
        for (Observer observer : observers) {
            observer.sendNotify(response);
        }
    }

    synchronized public void logout(long clientID) {
        addSystemMessage(clientID, SystemMessage.DISCONNECTED);
        usersContext.removeClient(clientID);
    }

    synchronized public void addSystemMessage(long ClientId, SystemMessage message) {
        switch (message) {
            case JOINED:
                chatContext.addMessage("[user " + usersContext.getClientInfo(ClientId).getName() + " is joined to chat]", usersContext.getClientInfo(SYSTEM_ID));
                break;
            case DISCONNECTED:
                chatContext.addMessage("[user " + usersContext.getClientInfo(ClientId).getName() + " is disconnected from chat]", usersContext.getClientInfo(SYSTEM_ID));
                break;
        }
        notifyObservers(new Response.Builder()
                .setStatus(Status.OK)
                .setResponseName(ServerResponse.CHAT)
                .setEvent(new Event.Builder()
                        .setMessage(new ChatContext(chatContext, 3))
                        .build())
                .build());
    }

    public void sendRequestedChat(long clientID, int minMessageId) {
        notifyObservers(new Response.Builder()
                .setStatus(Status.OK)
                .setResponseName(ServerResponse.REQUESTED_CHAT)
                .setEvent(new Event.Builder()
                        .setMessage(new ChatContext(chatContext, minMessageId-10, minMessageId))
                        .build())
                .setClientID(clientID)
                .build());
    }


    public void requestUsersList(long clientID) {
        notifyObservers(new Response.Builder()
                .setStatus(Status.OK)
                .setResponseName(ServerResponse.REQUESTED_LIST)
                .setClientID(clientID)
                .setEvent(new Event.Builder()
                        .setUsers(usersContext)
                        .build())
                .build());
    }
}

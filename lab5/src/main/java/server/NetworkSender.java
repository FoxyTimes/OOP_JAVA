package server;

import logger.ChatLogger;
import observer.Observer;
import protocolparser.Serial.response.Event;
import protocolparser.Serial.response.Response;
import protocolparser.ServerResponse;
import protocolparser.Status;
import server.model.ChatContext;

public class NetworkSender implements Observer {
    Server server;

    public NetworkSender(Server server) {
        this.server = server;
    }

    @Override
    public void sendNotify(Response response) {
        assert response != null;
        switch (response.getResponseName()) {
            case REGISTRATION, PONG, REQUESTED_CHAT, REQUESTED_LIST:
                for (ClientHandler client : server.getClients()) {
                    if (client.getClientID()==response.getClientID()) {
                        client.sendResponse(response);
                    }
                }
                break;
            case CHAT:
                server.getClients().forEach(client -> client.sendResponse(response));
                break;
        }
    }
}

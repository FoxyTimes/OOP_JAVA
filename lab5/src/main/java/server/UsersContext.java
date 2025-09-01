package server;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import java.util.HashMap;

@XmlAccessorType(XmlAccessType.FIELD)
public class UsersContext {

    private final HashMap<Long, UserInfo> clientsInfo = new HashMap<>();

    public void addClient(long clientID, String name) {
        clientsInfo.put(clientID, new UserInfo(clientID, name));
    }

    public void removeClient(long clientID) {
        clientsInfo.remove(clientID);
    }

    public HashMap<Long, UserInfo> getClients() {
        return clientsInfo;
    }

    public UserInfo getClientInfo(long clientID) {
        return clientsInfo.get(clientID);
    }

}

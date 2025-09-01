package server;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfo {
    private long clientID;
    private String name;

    public UserInfo() {

    }

    public UserInfo(long clientID, String name) {
        this.clientID = clientID;
        this.name = name;
    }

    public long getClientID() {
        return clientID;
    }

    public String getName() {
        return name;
    }

}

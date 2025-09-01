package server.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import server.UserInfo;

import java.util.Date;


@XmlAccessorType(XmlAccessType.FIELD)
public class ChatMessage {
    private UserInfo sender;
    private String messageText;
    private Date date;
    private int id;
    @SuppressWarnings("unused")
    public ChatMessage() {

    }

    public ChatMessage(String messageText, UserInfo sender, int id) {
        this.sender = sender;
        this.messageText = messageText;
        this.date = new Date();
        this.id = id;
    }


    public String getMessageText() {
        return messageText;
    }

    public UserInfo getSender() {
        return sender;
    }

    public Date getDate() {
        return date;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getId() {
        return id;
    }
}

package server.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import logger.ChatLogger;
import server.SystemMessage;
import server.UserInfo;

import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
public class ChatContext {
    @XmlElement(name = "message")
    private final ArrayList<ChatMessage> messagesList = new ArrayList<>();

    synchronized public void addMessage(String message, UserInfo userInfo) {
        messagesList.add(new ChatMessage(message, userInfo, messagesList.size()));
    }

    synchronized public ArrayList<ChatMessage> getMessagesList() {
        return messagesList;
    }

    public ChatContext() {

    }

    public ChatContext(ChatContext chatContext, int count) {
        if (chatContext == null) {
            throw new IllegalArgumentException("Source ChatContext cannot be null");
        }

        ArrayList<ChatMessage> sourceMessages = chatContext.getMessagesList();
        int startIndex = Math.max(0, sourceMessages.size() - count);

        this.messagesList.addAll(sourceMessages.subList(startIndex, sourceMessages.size()));
    }

    public ChatContext(ChatContext chatContext, int minId, int maxId) {
        ArrayList<ChatMessage> sourceMessages = chatContext.getMessagesList();


        for (ChatMessage msg : sourceMessages) {
            int id = msg.getId();
            if (id >= minId && id < maxId) {
                messagesList.add(msg);
            }
        }

    }
}

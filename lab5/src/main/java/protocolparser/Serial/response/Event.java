package protocolparser.Serial.response;

import jakarta.xml.bind.annotation.*;
import server.UsersContext;
import server.model.ChatContext;

@XmlAccessorType(XmlAccessType.FIELD)
public class Event {
    @XmlElement(name = "chat")
    private ChatContext chatContext;
    @XmlElement(name = "users")
    private UsersContext usersContext;
    @SuppressWarnings("unused")
    public Event() {}

    private Event(Builder builder) {
        this.chatContext = builder.chatContext;
        this.usersContext = builder.usersContext;
    }

    public static class Builder {
        private ChatContext chatContext;
        private UsersContext usersContext;



        public Builder setMessage(ChatContext chatContext) {
            this.chatContext = chatContext;
            return this;
        }

        public Builder setUsers(UsersContext usersContext) {
            this.usersContext = usersContext;
            return this;
        }


        public Event build() {
            return new Event(this);
        }
    }


    public ChatContext getChatContext() {
        return chatContext;
    }

    public UsersContext getUsersContext() {
        return usersContext;
    }
}
package protocolparser.Serial.command;

import jakarta.xml.bind.annotation.*;
import protocolparser.ClientCommand;


@XmlRootElement(name = "command")
@XmlAccessorType(XmlAccessType.FIELD)
public class Command {
    @XmlAttribute(name = "name")
    private ClientCommand commandName;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "type")
    private String type;

    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "minMessageId")
    private int minMessageId;

    @SuppressWarnings("unused")
    public Command() {}

    private Command(Builder builder) {
        this.commandName = builder.commandName;
        this.name = builder.name;
        this.type = builder.type;
        this.message = builder.message;
        this.minMessageId = builder.minMessageId;
    }

    public static class Builder {
        ClientCommand commandName;
        String name;
        String type;
        boolean ping;
        String message;
        int minMessageId;

        public Builder setCommandName(ClientCommand commandName) {
            this.commandName = commandName;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setPing(boolean ping) {
            this.ping = ping;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMinMessageId(int minMessageId) {
            this.minMessageId = minMessageId;
            return this;
        }

        public Command build() {
            return new Command(this);
        }

    }

    public ClientCommand getCommandName() {
        return commandName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getMinMessageId() {
        return minMessageId;
    }

    public String getMessage() {
        return message;
    }
}

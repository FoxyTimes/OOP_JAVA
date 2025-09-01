package protocolparser.Serial.response;

import jakarta.xml.bind.annotation.*;
import protocolparser.ServerResponse;
import protocolparser.Status;
import server.model.ChatContext;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {

    @XmlAttribute(name = "name")
    private ServerResponse responseName;

    private Status status;
    private long clientID;
    private Event event;

    public Response() {}

    private Response(Builder builder) {
        this.status = builder.status;
        this.event = builder.event;
        this.responseName = builder.responseName;
        this.clientID = builder.clientID;
    }

    public static class Builder {
        private Status status;
        private Event event;
        private ServerResponse responseName;
        private long clientID;

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder setEvent(Event event) {
            this.event = event;
            return this;
        }

        public Builder setClientID(long clientID) {
            this.clientID = clientID;
            return this;
        }

        public Builder setResponseName(ServerResponse responseName) {
            this.responseName = responseName;
            return this;
        }


        public Response build() {
            return new Response(this);
        }

    }

    public Status getStatus() {
        return status;
    }

    public Event getEvent() {
        return event;
    }


    public ServerResponse getResponseName() {
        return responseName;
    }

    public long getClientID() {
        return clientID;
    }


}
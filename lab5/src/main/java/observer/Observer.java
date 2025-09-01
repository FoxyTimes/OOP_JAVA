package observer;

import protocolparser.Serial.response.Response;

public interface Observer {
    void sendNotify(Response response);
}

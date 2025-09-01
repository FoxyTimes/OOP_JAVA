package observer;


import protocolparser.Serial.response.Response;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Response response);
}
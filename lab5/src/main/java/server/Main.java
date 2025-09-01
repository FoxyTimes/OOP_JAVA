package server;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        Thread serverThread = new Thread(server);
        serverThread.start();

        Thread.sleep(5000);
        server.stop();
        serverThread.join();
    }
}
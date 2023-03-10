package org.example.server;


public class ServerMain {

    public static void main(String[] args) {
        Server server = new Server(5555);
        server.startServer("localhost", 6379);
    }
}
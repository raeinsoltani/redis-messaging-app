package org.example.client;

public class Main {
    public static void main(String[] args) {
        ClientManager client = new ClientManager("127.0.0.1", 5555);
        client.start();
    }
}

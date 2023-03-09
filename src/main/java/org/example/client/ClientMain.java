package org.example.client;

import redis.clients.jedis.Jedis;

public class ClientMain {

    public static void main(String[] args) {
        ClientManager client = new ClientManager("127.0.0.1", 5555);
        client.start();
    }
}

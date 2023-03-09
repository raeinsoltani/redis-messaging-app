package org.example.server;


import redis.clients.jedis.Jedis;

public class ServerMain {

    public static void main(String[] args) {
        Server server = new Server(5555);
        server.startServer();
    }
}
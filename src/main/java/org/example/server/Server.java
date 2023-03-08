package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {
    private final int port;
    private static ConcurrentLinkedDeque<UserThread> clients = new ConcurrentLinkedDeque<>();

    public Server(int port){
        this.port = port;
    }

    public static ConcurrentLinkedDeque<UserThread> getClients() {
        return clients;
    }

    public static void setClients(ConcurrentLinkedDeque<UserThread> clients) {
        Server.clients = clients;
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("server is running on port :" + port);
            while (true) {
                Socket socket = serverSocket.accept();
                UserThread userThread = new UserThread(socket);
                Thread thread = new Thread(userThread);
                thread.start();
                clients.add(userThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package org.example.server;

import org.example.common.Packet;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {
    private final int port;
    private static ConcurrentLinkedDeque<UserThread> clients = new ConcurrentLinkedDeque<>();
    private static Jedis jedis;

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

        jedis = new Jedis();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("server is running on port :" + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("new client connected");
                UserThread userThread = new UserThread(socket);
                Thread thread = new Thread(userThread);
                thread.start();
                clients.add(userThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendReceivedMessageToServer(Packet packet){
        for (UserThread client : clients){
            if (client.getUsername().equals(packet.getTo())){
                client.sendReceivedMessageToClientApplication(packet);
                System.out.println("\nfRelaying a Message\nform: " + packet.getFrom()
                        + "\nto: " + packet.getTo() + "\n\n" + packet.getBody() + "\n");
                break;
            }
        }
    }

    public static void createGroup(Packet packet){
        HashMap<String, String> group = new HashMap<>();

        group.put("creator", packet.getFrom());
        group.put("created_at", packet.getDateTime().toString());
        group.put("description", packet.getDescription());
        group.put("members", String.join(" ", packet.getMembers()));
        group.put("messages", "");

        jedis.hset(packet.getTo(), group);
        System.out.println("Group " + packet.getTo() + "created by: " + packet.getFrom());
    }

    public static void sendGroupMsg(Packet packet){
        HashMap<String, String> group = (HashMap<String, String>) jedis.hgetAll(packet.getTo());

        String messages = group.get("messages");
        messages = messages + "!!!!!" + packet.getDateTime().toString() + "@@@@@" + packet.getFrom() + "@@@@@" + packet.getBody();
        System.out.println(messages);
        group.replace("messages", messages);


    }

    public static void broadcast(String members, Packet packet){
        String[] membersArray = members.split(" ");
        for (String member : membersArray){
            for (UserThread client : clients){
                if (client.getUsername().equals(member)){
                    packet.setDescription("DisplayGroupMsg");
                    client.sendReceivedMessageToClientApplication(packet);
                }
            }
        }
    }
}

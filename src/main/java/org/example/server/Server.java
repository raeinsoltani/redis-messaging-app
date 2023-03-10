package org.example.server;

import org.example.common.Packet;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {
    private final int port;
    private static ConcurrentLinkedDeque<UserThread> clients = new ConcurrentLinkedDeque<>();
    private static JedisPooled jedis;

    public Server(int port){
        this.port = port;
    }

    public static ConcurrentLinkedDeque<UserThread> getClients() {
        return clients;
    }

    public static void setClients(ConcurrentLinkedDeque<UserThread> clients) {
        Server.clients = clients;
    }

    public void startServer(String dbURL, int dbPort) {

        jedis = new JedisPooled(dbURL, dbPort);

//        JedisPool pool = new JedisPool(dbURL, dbPort);
//        try {
//            Jedis jedis = pool.getResource();
//            // Is connected
//            System.out.println("db is connected");
//        } catch (JedisConnectionException e) {
//            // Not connected
//            System.out.println("db is not connected");
//        }

        System.out.println("DB is ready on port:" + dbPort);
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
                String messages;
                if (jedis.get(packet.getTo()) != null){
                    messages = jedis.get(packet.getTo());
                    messages = messages + "!!!!!" + packet.getDateTime().toString() + "@@@@@" + packet.getFrom() + "@@@@@" + packet.getBody();
                }
                else {
                    messages = "!!!!!" + packet.getDateTime().toString() + "@@@@@" + packet.getFrom() + "@@@@@" + packet.getBody();
                }
                jedis.set(packet.getTo(), messages);
                client.server2ClientPacketSender(packet);
//                System.out.println("\nDirect Message\nform: " + packet.getFrom()
//                        + "\n\n" + packet.getBody() + "\n");
                System.out.println("Message uploaded to database");
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
        System.out.println("Group " + packet.getTo() + " created by " + packet.getFrom());
    }

    public static void sendGroupMsg(Packet packet){
        HashMap<String, String> group = (HashMap<String, String>) jedis.hgetAll(packet.getTo());
        System.out.println("retrieved group information form database");

        System.out.println(packet.getTo());
        String messages = group.get("messages");
        messages = messages + "!!!!!" + packet.getDateTime().toString() + "@@@@@" + packet.getFrom() + "@@@@@" + packet.getBody();
        System.out.println(messages);
        group.replace("messages", messages);
        jedis.hset(packet.getTo(), group);
        System.out.println("uploaded new group messages to database");

//        System.out.println(group.get("members"));
//        System.out.println(group.get("description"));
        broadcast(group.get("members"), packet);
    }

    public static void broadcast(String members, Packet packet){
        String[] membersArray = members.split(" ");
        for (String member : membersArray){
            for (UserThread client : clients){
                if (client.getUsername().equals(member) && !client.getUsername().equals(packet.getFrom())){
                    client.server2ClientPacketSender(packet);
                }
            }
        }
    }

    public static void printDirectMsgHistory(Packet packet){
        String msg = jedis.get(packet.getTo());
        for (UserThread client : clients){
            if (client.getUsername().equals(packet.getFrom())){
                Packet newPacket = new Packet();
                newPacket.setRequestType("PrintMsgHistory");
                newPacket.setBody(msg);
                client.server2ClientPacketSender(newPacket);
            }
        }
    }

    public static void printGroupMsgHistory(Packet packet){
        HashMap<String, String> hashMap = (HashMap<String, String>) jedis.hgetAll(packet.getTo());
        String msg = hashMap.get("messages");
        for (UserThread client : clients){
            if (client.getUsername().equals(packet.getFrom())){
                Packet newPacket = new Packet();
                newPacket.setRequestType("PrintMsgHistory");
                newPacket.setBody(msg);
                client.server2ClientPacketSender(newPacket);
            }
        }
    }
}

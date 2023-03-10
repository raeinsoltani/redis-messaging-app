package org.example.client;

import org.example.common.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientManager {
    private String ip;
    private int port;
    private String username;

    public ClientManager(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    Scanner scanner;
    ObjectOutputStream outputStream;
    Socket socket;

    public void start(){
        try {
            socket = new Socket(ip,port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            Listener listener = new Listener(new ObjectInputStream(socket.getInputStream()));
            Thread listenerThread = new Thread(listener);
            listenerThread.start();
            scanner = new Scanner(System.in);

            login();

            while(true){
                System.out.println("To:\n1)Send Direct Message\n2)Send Group Message\n3)Create Group");
                switch (scanner.nextLine()){
                    case "1" -> {
                        sendDirectMsg();
                    }

                    case "2" -> {
                        sendMessageToGroup();
                    }

                    case "3" -> {
                        createGroup();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void login(){
        System.out.println("Please enter your username:");
        username = scanner.nextLine();
        Packet packet = new Packet();
        packet.setRequestType("SetUsername");
        packet.setFrom(username);
        try {
            outputStream.writeObject(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nUsername set\nWelcome" + username + "\n");
    }

    public void createGroup(){
        Packet packet = new Packet();

        packet.setRequestType("CreateGroup");
        System.out.println("What is the groups name? ");
        packet.setTo(scanner.nextLine());
        System.out.println("What is the groups description? (write it in one line)");
        packet.setDescription(scanner.nextLine());
        System.out.println("Who are the members of group? (write ! in an empty line to finish)");
        ArrayList<String> members = new ArrayList<>();
        while (true){
            String member = scanner.nextLine();
            if (member.equals("!")){
                break;
            }
            members.add(member);
        }
        packet.setMembers(members);
        packet.setFrom(username);

        try {
            outputStream.writeObject(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nGroup Creation Request Sent\n");
    }

    public void sendMessageToGroup(){
        Packet packet = new Packet();

        packet.setRequestType("SendGroupMsg");
        packet.setFrom(username);

        System.out.println("enter name of the group you what to send message to: ");
        packet.setTo(scanner.nextLine());

        System.out.println("Please enter your Message: ");
        packet.setBody(scanner.nextLine());

        try {
            outputStream.writeObject(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nMessage sent\n");
    }

    public void sendDirectMsg(){
        scanner = new Scanner(System.in);
        Packet packet = new Packet();
        System.out.println("To(username)");
        String toUsername = scanner.nextLine();
        System.out.println("Write your message:");
        String body = scanner.nextLine();
        packet.setRequestType("DirectMessage");
        packet.setFrom(username);
        packet.setTo(toUsername);
        packet.setBody(body);
        try {
            outputStream.writeObject(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Message Sent");
    }
}

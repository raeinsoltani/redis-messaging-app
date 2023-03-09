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

            while (true){
                Packet packet1 = new Packet();
                System.out.println("To(username)");
                String toUsername = scanner.nextLine();
                System.out.println("Write your message:");
                String body = scanner.nextLine();
                packet1.setRequestType("DirectMessage");
                packet1.setFromUsername(username);
                packet1.setToUsername(toUsername);
                packet1.setBody(body);
                outputStream.writeObject(packet1);
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
        packet.setFromUsername(username);
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
        packet.setToUsername(scanner.nextLine());
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

        try {
            outputStream.writeObject(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nGroup Creation Request Sent\n");
    }
}

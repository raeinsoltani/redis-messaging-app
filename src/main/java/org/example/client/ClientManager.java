package org.example.client;

import org.example.common.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientManager {
    private String ip;
    private int port;
    private String username;

    public ClientManager(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start(){
        try {
            Socket socket = new Socket(ip,port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            Listener listener = new Listener(new ObjectInputStream(socket.getInputStream()));
            Thread listenerThread = new Thread(listener);
            listenerThread.start();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter username:");
            username = scanner.nextLine();
            Packet packet = new Packet();
            packet.setRequestType("SetUsername");
            packet.setFromUsername(username);
            outputStream.writeObject(packet);
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

}

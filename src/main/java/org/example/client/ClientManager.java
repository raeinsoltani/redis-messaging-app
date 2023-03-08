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

    public ClientManager(String ip, int port ,String username) {
        this.ip = ip;
        this.port = port;
        this.username = username;
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
            Packet packet = new Packet();
            packet.setRequestType("SetUsername");
            packet.setFromUsername(scanner.nextLine());
            outputStream.writeObject(packet);
            while (true){
                System.out.println("To(username)");
                String toUsername = scanner.nextLine();
                System.out.println("Write your message:");
                String body = scanner.nextLine();
                packet.setRequestType("DirectMessage");
                packet.setFromUsername(username);
                packet.setToUsername(toUsername);
                packet.setBody(body);
                outputStream.writeObject(packet);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

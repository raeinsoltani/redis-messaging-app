package org.example.server;

import org.example.common.Packet;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class UserThread extends Thread{
    private Socket socket;
    private ObjectOutput objectOutput;
    private ObjectInput objectInput;
    private String username;



    public UserThread(Socket socket){
        try{
            this.socket = socket;
            this.username = "";
            this.objectInput = new ObjectInputStream(socket.getInputStream());
            this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void server2ClientPacketSender(Packet packet){
        try {
            objectOutput.writeObject(packet);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Thread receiver = new Thread(() -> {
            try {
                while (true){
                    Packet packet = (Packet) objectInput.readObject();
                    switch (packet.getRequestType()) {
                        case "SetUsername" -> {
                            setUsername(packet.getFrom());
                            System.out.println("client username set as: " + packet.getFrom());
                        }

                        case "DirectMessage" -> Server.sendReceivedMessageToServer(packet);

                        case "CreateGroup" -> Server.createGroup(packet);

                        case "SendGroupMsg" -> Server.sendGroupMsg(packet);

                        case "PrintMsgHistoryUser" -> Server.printDirectMsgHistory(packet);

                        case "PrintMsgHistoryGroup" -> Server.printGroupMsgHistory(packet);
                    }
                }

            } catch (SocketException e){
                for(UserThread client : Server.getClients()){
                    if (client.getUsername().equals(username)){
                        Server.getClients().remove(client);
                        break;
                    }
                }
            } catch (ClassNotFoundException | IOException e){
                e.printStackTrace();
            }
        });
        receiver.start();
    }
}


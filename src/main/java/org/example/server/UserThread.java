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

    @Override
    public void run() {
        Thread receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Packet packet = (Packet) objectInput.readObject();
                        switch (packet.getRequestType()){
                            case "SetUsername":
                                setUsername(packet.getFromUsername());
                                break;

                            case "DirectMessage":

                        }
                    }

                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                catch (SocketException e){
                    for(UserThread client : Server.getClients()){
                        if (client.getUsername().equals(username)){
                            Server.getClients().remove(client);
                            break;
                        }
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}


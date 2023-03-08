package org.example;

import javax.imageio.IIOException;
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

    @Override
    public void run() {
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        Packet packet = (Packet) objectInput.readObject();
                        //write code to analatzie the packet
                    }

                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                catch (SocketException e){
                    //remove clientHandler from ClientHandlers
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}


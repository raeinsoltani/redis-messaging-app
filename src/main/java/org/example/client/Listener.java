package org.example.client;

import org.example.common.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Listener implements Runnable{
    ObjectInputStream objectInputStream;

    public Listener(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while(true){
            try{
                Packet packet = (Packet) objectInputStream.readObject();
                switch (packet.getRequestType()){

                    case "DirectMessage" -> {
                        System.out.println("Relaying a Message\nform: " + packet.getFrom()
                                + "\nto: " + packet.getTo() + "\n\n" + packet.getBody() + "\n");

                    }

                    case "DisplayGroupMsg" ->{
                        System.out.println("Group: " + packet.getTo() + "    From: "+ packet.getFrom());
                        System.out.println(packet.getBody() + "\n");;
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }
}

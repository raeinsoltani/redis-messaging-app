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

                    case "SendGroupMsg" ->{
                        System.out.println("Group: " + packet.getTo() + "    From: "+ packet.getFrom());
                        System.out.println(packet.getBody() + "\n");;
                    }

                    case "PrintMsgHistory" -> {
                        String[] messages = packet.getBody().split("!!!!!");
                        boolean condition = true;
                        for (String msg : messages){
                            if (condition){
                                condition = false;
                                continue;
                            }
                            System.out.println("at: " + msg.split("@@@@@")[0]);
                            System.out.println("from: " + msg.split("@@@@@")[1] + "\n");
                            System.out.println(msg.split("@@@@@")[2] +  "\n");
                        }
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

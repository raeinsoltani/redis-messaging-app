package org.example.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Packet implements Serializable {

    private String requestType;

    private String from;

    private String to;

    private String body;

    private LocalDateTime dateTime;

    private ArrayList<String> members;

    private String description;



    public Packet(String requestType) {
        this.requestType = requestType;
    }

    public Packet(){
        this.dateTime = LocalDateTime.now();
    };

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

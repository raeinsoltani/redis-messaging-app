package org.example.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Packet implements Serializable {
    private String requestType;

    private String fromUsername;

    private String toUsername;

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

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
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

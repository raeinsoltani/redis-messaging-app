package org.example.common;

import java.io.Serializable;

public class Packet implements Serializable {
    private String requestType;

    private String fromUsername;

    private String toUsername;

    private String body;

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
}

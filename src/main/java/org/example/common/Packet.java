package org.example.common;

import java.io.Serializable;

public class Packet implements Serializable {
    private String requestType;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}

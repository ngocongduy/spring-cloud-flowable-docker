package com.example.messagingrabbitmq;

import java.io.Serializable;

public class Message implements Serializable {
    private String message;
    private String processInstanceId;

    public Message() {
    }

    public Message(String message, String processInstanceId) {
        this.message = message;
        this.processInstanceId = processInstanceId;
    }

    public String getMessage() {
        return message;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
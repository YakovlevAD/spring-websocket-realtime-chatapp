package com.arisusantolie.springwebsocketrealtimechatapp.dto;

public class Sender {
    public String senderId;
    public String displayName;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

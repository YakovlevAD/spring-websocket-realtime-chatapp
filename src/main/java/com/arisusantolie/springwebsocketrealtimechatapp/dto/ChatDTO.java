package com.arisusantolie.springwebsocketrealtimechatapp.dto;

import java.util.Arrays;

public class ChatDTO {
    public String chatId;
    public String[] subscribers;

    @Override
    public String toString() {
        return "ChatDTO{" +
                "chatId='" + chatId + '\'' +
                ", subscribers=" + Arrays.toString(subscribers) +
                '}';
    }
}

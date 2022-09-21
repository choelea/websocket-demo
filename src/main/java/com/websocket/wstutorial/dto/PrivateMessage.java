package com.websocket.wstutorial.dto;

import lombok.Data;

@Data
public class PrivateMessage{
    private String toUser;
    private String content;
}

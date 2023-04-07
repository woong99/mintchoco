package com.woong.mintchoco.global.common;

public enum MessageType {
    msgBack("msg+back"),
    msgUrl("msg+url");

    private final String message;

    MessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
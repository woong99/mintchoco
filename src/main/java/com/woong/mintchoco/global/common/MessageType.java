package com.woong.mintchoco.global.common;

public enum MessageType {

    MSG_BACK("msg+back"),
    MSG_URL("msg+url");

    private final String message;

    MessageType(String message) {
        this.message = message;
    }

    public String messageType() {
        return message;
    }
}

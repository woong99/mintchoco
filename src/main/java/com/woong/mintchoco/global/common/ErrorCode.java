package com.woong.mintchoco.global.common;

public enum ErrorCode {

    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    DUPLICATE_PASSWORD("이미 사용중인 비밀번호입니다.");

    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }

    public String Message() {
        return message;
    }
}

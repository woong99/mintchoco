package com.woong.mintchoco.global.common;

public enum ErrorCode {

    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    DUPLICATE_PASSWORD("이미 사용중인 비밀번호입니다."),
    MENU_NOT_FOUND("메뉴가 존재하지 않습니다."),
    MENU_GROUP_NOT_FOUND("메뉴 그룹이 존재하지 않습니다."),
    MENU_OPTION_GROUP_NOT_FOUND("메뉴 옵션 그룹이 존재하지 않습니다."),
    UPLOAD_FILE_NOT_FOUND("파일이 존재하지 않습니다."),
    UPLOAD_FILE_ONLY_ONE("하나의 파일만 업로드 가능합니다."),
    UPLOAD_FILE_ERROR("파일의 용량 및 확장자를 확인해주세요.");

    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }

    public String Message() {
        return message;
    }
}

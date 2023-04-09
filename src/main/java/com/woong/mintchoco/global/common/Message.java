package com.woong.mintchoco.global.common;

public enum Message {

    SUCCESS_INSERT("추가가 완료되었습니다."),
    SUCCESS_UPDATE("수정이 완료되었습니다."),
    SUCCESS_SAVE("저장이 완료되었습니다."),
    SUCCESS_DELETE("삭제가 완료되었습니다."),
    SUCCESS_REGISTRATION("등록이 완료되었습니다."),
    FAIL_UNKNOWN_ERROR("알 수 없는 오류가 발생했습니다. 다시 시도해주세요.");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}

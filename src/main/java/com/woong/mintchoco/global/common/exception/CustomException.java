package com.woong.mintchoco.global.common.exception;

import com.woong.mintchoco.global.common.ErrorCode;
import java.io.Serializable;

public class CustomException extends RuntimeException implements Serializable {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

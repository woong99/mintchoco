package com.woong.mintchoco.global.common.exception;

import com.woong.mintchoco.global.common.ErrorCode;
import java.io.Serial;

public class CustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2882378805622790012L;

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.Message());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

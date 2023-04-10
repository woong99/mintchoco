package com.woong.mintchoco.global.file.exception;

import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.common.exception.CustomException;
import java.io.Serial;

public class UploadFileOnlyOneException extends CustomException {
    @Serial
    private static final long serialVersionUID = -430248377869666392L;

    public UploadFileOnlyOneException(ErrorCode errorCode) {
        super(errorCode);
    }
}

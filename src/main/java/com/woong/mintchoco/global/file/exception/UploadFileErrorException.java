package com.woong.mintchoco.global.file.exception;

import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.common.exception.CustomException;
import java.io.Serial;

public class UploadFileErrorException extends CustomException {
    @Serial
    private static final long serialVersionUID = 2880091075736774272L;

    public UploadFileErrorException(ErrorCode errorCode) {
        super(errorCode);
    }
}

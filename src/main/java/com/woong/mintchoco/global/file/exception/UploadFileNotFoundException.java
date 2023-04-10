package com.woong.mintchoco.global.file.exception;

import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.common.exception.CustomException;
import java.io.Serial;

public class UploadFileNotFoundException extends CustomException {

    @Serial
    private static final long serialVersionUID = -2202482407045120287L;

    public UploadFileNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

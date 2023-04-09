package com.woong.mintchoco.global.auth.exception.password;

import com.woong.mintchoco.global.common.exception.CustomException;
import com.woong.mintchoco.global.common.ErrorCode;
import java.io.Serializable;

public class InvalidPasswordException extends CustomException implements Serializable {

    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}

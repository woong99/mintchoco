package com.woong.mintchoco.exception.password;

import com.woong.mintchoco.exception.CustomException;
import com.woong.mintchoco.exception.ErrorCode;
import java.io.Serializable;

public class InvalidPasswordException extends CustomException implements Serializable {

    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}

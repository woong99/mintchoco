package com.woong.mintchoco.exception.password;

import com.woong.mintchoco.exception.CustomException;
import com.woong.mintchoco.exception.ErrorCode;
import java.io.Serializable;

public class DuplicatePasswordException extends CustomException implements Serializable {

    public DuplicatePasswordException(ErrorCode errorCode) {
        super(errorCode);
    }

}

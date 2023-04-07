package com.woong.mintchoco.global.auth.exception.password;

import com.woong.mintchoco.global.common.exception.CustomException;
import com.woong.mintchoco.global.common.ErrorCode;
import java.io.Serializable;

public class DuplicatePasswordException extends CustomException implements Serializable {

    public DuplicatePasswordException(ErrorCode errorCode) {
        super(errorCode);
    }

}

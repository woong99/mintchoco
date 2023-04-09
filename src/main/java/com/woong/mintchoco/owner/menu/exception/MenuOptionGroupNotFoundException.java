package com.woong.mintchoco.owner.menu.exception;

import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.common.exception.CustomException;
import java.io.Serial;

public class MenuOptionGroupNotFoundException extends CustomException {

    @Serial
    private static final long serialVersionUID = -7255284751206280406L;

    public MenuOptionGroupNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

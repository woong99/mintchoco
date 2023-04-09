package com.woong.mintchoco.owner.menu.exception;

import com.woong.mintchoco.global.common.ErrorCode;
import com.woong.mintchoco.global.common.exception.CustomException;
import java.io.Serial;

public class MenuNotFoundException extends CustomException {

    @Serial
    private static final long serialVersionUID = -4824766429819711490L;

    public MenuNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
